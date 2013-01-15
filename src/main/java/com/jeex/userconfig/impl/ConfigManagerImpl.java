package com.jeex.userconfig.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.jeex.userconfig.ConfigManager;

public class ConfigManagerImpl implements ConfigManager, InitializingBean {
	private static final Log log = LogFactory.getLog(ConfigManagerImpl.class);
	
	private volatile int configurePeroidInSeconds = 10 * 60;
	
	private volatile int savePeroidInSeconds = 10 * 60;
	
	@Autowired
	private ConfigServiceImpl configService;
	
	@Autowired
	private ScheduledExecutorService scheduledExecutorService;	
	
	private volatile ScheduledFuture<?> configureTask;
	
	private volatile ScheduledFuture<?> saveTask;
	
	private static class ObjectConfigInfo {
		Class<?> clazz;
		Object obj;
		ConfigInfo info;
	}
	
	private List<ObjectConfigInfo> registeredList 
			= Collections.synchronizedList(new ArrayList<ObjectConfigInfo>());

	public void register(Object obj) {				
		if (getRegistedObj(obj) != null) {
			log.warn("Object already registed.");
			return;
		}
		
		ObjectConfigInfo oci = new ObjectConfigInfo();
		oci.clazz = obj.getClass();
    	oci.obj = obj;
		oci.info = ConfigInfo.contructConfigInfo(obj.getClass());
    	registeredList.add(oci);   	
    	
    	configService.configure(obj.getClass(), oci.obj, oci.info);
	}
	
	private ObjectConfigInfo getRegistedObj(Object obj) {
		for (ObjectConfigInfo oci: registeredList) {
			if (obj == oci.obj) {
				return oci;
			}
		}
		return null;
	}
	
	void restart() {
		if (configureTask != null) {
			configureTask.cancel(false);
		}
		if (saveTask != null) {
			saveTask.cancel(false);
		}
		start();		
	}
	
	void start() {

		configureTask = scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
			public void run() {
				configureAll();
			}
		}, configurePeroidInSeconds, configurePeroidInSeconds, TimeUnit.SECONDS);

		saveTask = scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
			public void run() {
				saveAll();
			}
		}, savePeroidInSeconds, savePeroidInSeconds, TimeUnit.SECONDS);			
	}

	public void registerStatic(Class<?> clazz) {
		if (getRegistedClass(clazz) != null) {
			log.warn("Class already registed.");
			return;
		}
		
		ObjectConfigInfo oci = new ObjectConfigInfo();
    	oci.clazz = clazz;
		oci.info = ConfigInfo.contructConfigInfo(clazz);
    	registeredList.add(oci);
    	
    	configService.configure(clazz, oci.obj, oci.info);		
	}
	
	private ObjectConfigInfo getRegistedClass(Class<?> clazz) {
		for (ObjectConfigInfo oci: registeredList) {
			if (clazz == oci.clazz) {
				return oci;
			}
		}
		return null;
	}
	
	private void configureAll() {
		if (log.isDebugEnabled()) {
			log.debug("Auto configure running ...");
		}
		
		for (ObjectConfigInfo oci: registeredList) {
			configService.configure(oci.clazz, oci.obj, oci.info);
		}		
	}
	
	void unregister(Object obj) {
		ObjectConfigInfo oci = getRegistedObj(obj);
		registeredList.remove(oci);		
	}

	void unregisterStatic(Class<?> clazz) {
		ObjectConfigInfo oci = getRegistedObj(clazz);
		registeredList.remove(oci);		
	}
		
	private void saveAll() {
		if (log.isDebugEnabled()) {
			log.debug("Auto save running ...");			
		}		
		
		for (ObjectConfigInfo oci: registeredList) {
			configService.save(oci.clazz, oci.obj, oci.info);	
		}
	}

	public void afterPropertiesSet() throws Exception {
		start();
	}

	public int getConfigurePeroidInSeconds() {
		return configurePeroidInSeconds;
	}

	public void setConfigurePeroidInSeconds(int configurePeroidInSeconds) {
		this.configurePeroidInSeconds = configurePeroidInSeconds;
	}

	public int getSavePeroidInSeconds() {
		return savePeroidInSeconds;
	}

	public void setSavePeroidInSeconds(int savePeroidInSeconds) {
		this.savePeroidInSeconds = savePeroidInSeconds;
	}
}
