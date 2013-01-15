package com.jeex.userconfig.impl;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.jeex.userconfig.ConfigService;
import com.jeex.userconfig.dao.GroupDao;
import com.jeex.userconfig.dao.ParameterDao;

public class ConfigServiceImpl implements ConfigService {
	private static final Log log = LogFactory.getLog(ConfigServiceImpl.class);
	
	@Autowired
	private GroupDao groupDao;
	
	@Autowired
	private ParameterDao parameterDao;
	
	public void configure(Object obj) {
		ConfigInfo info = ConfigInfo.contructConfigInfo(obj.getClass());
		configure(obj.getClass(), obj, info);
	}
	
	public void save(Object obj) {
		ConfigInfo info = ConfigInfo.contructConfigInfo(obj.getClass());
		save(obj.getClass(), obj, info);
	}
	
	public void configureStatic(Class<?> clazz) {
		ConfigInfo info = ConfigInfo.contructConfigInfo(clazz);
		configure(clazz, null, info);
	}

	public void saveStatic(Class<?> clazz) {
		ConfigInfo info = ConfigInfo.contructConfigInfo(clazz);
		save(clazz, null, info);		
	}

	void configure(Class<?> clazz, Object obj, ConfigInfo info) {
		String groupId = info.getGroup().getGroupId();
		Group dbGroup = groupDao.getGroup(groupId);
		List<Parameter> dbRps = null;
		if (dbGroup == null) {
			parameterDao.deleteByGroupId(groupId);			
			groupDao.insertGroup(info.getGroup());
		} else {
			updateGroupDescriptionInDbIfNeeded(info.getGroup(), dbGroup);
			dbRps = parameterDao.getParametersByGroupId(groupId);
		}
		
		for (Parameter rp: info.getParameters()) {
			if (rp.getDirection() == Direction.TODB) {
				continue;
			}
			Parameter dbRp = (dbRps != null)? 
								ConfigInfo.findParameter(dbRps, rp.getName()): 
								null;
			if (dbRp == null) {
				// Initialize the value to the default value in DB
				rp.setValue(rp.getDefaultValue());			
				parameterDao.insertParameter(rp);
				
				// Inject default value defined in the annotation
				injectValue(clazz, obj, rp, null); 
			} else {				
				updateParamDescriptionInDbIfNeeded(rp, dbRp);				
				injectValue(clazz, obj, rp, dbRp.getValue()); 
			}
		}		
	}

	void save(Class<?> clazz, Object obj, ConfigInfo info) {
		for (Parameter p: info.getParameters()) {
			if (p.getDirection() == Direction.FROMDB) {
				continue;
			}

			String methodName = "get" 
						+ p.getName().substring(0, 1).toUpperCase() 
						+ p.getName().substring(1);
			try {
				Method m = clazz.getMethod(methodName);
				String newValue = m.invoke(obj).toString();
				updateParamValueInDb(info.getGroup().getGroupId(), p, newValue);
			} catch (Exception e) {
				log.warn("Update parameter failed, param=" + p.getFullName(), e);
			}
		}
	}

	private void injectValue(Class<?> clazz, Object obj, Parameter rp, String value) {
		String methodName = "set" 
					+ rp.getName().substring(0, 1).toUpperCase() 
					+ rp.getName().substring(1);
		try {		
			Method m = clazz.getMethod(methodName, rp.getType().getTypeClass());
			Object arg = ParamType.parseValue(rp, value);
			if (arg != null) {
				m.invoke(obj, arg);
			}
		} catch (Exception e) {
			log.warn("Inject parameter failed, param=" + rp.getFullName(), e);
		}
	}

	private void updateGroupDescriptionInDbIfNeeded(Group group, Group dbGroup) {
		if (!group.getName().equals(dbGroup.getName()) 
				|| !group.getDescription().equals(dbGroup.getDescription())) {
			dbGroup.setName(group.getName());
			dbGroup.setDescription(group.getDescription());
			groupDao.updateGroup(dbGroup);
		}
	}
	
	private void updateParamDescriptionInDbIfNeeded(Parameter rp,
			Parameter dbRp) {
		if (!rp.getDescriptiveName().equals(dbRp.getDescriptiveName())
				|| !rp.getDescription().equals(dbRp.getDescription())
				|| !rp.getDefaultValue().equals(dbRp.getDefaultValue())
				|| !rp.getReadonly().equals(dbRp.getReadonly())) {
			dbRp.setDescriptiveName(rp.getDescriptiveName());
			dbRp.setDescription(rp.getDescription());
			dbRp.setDefaultValue(rp.getDefaultValue());
			dbRp.setReadonly(rp.getReadonly());
			parameterDao.updateParameter(dbRp);
		}
	}
		
	private void updateParamValueInDb(String groupId,
			Parameter p, String newValue) {
		if (p.getLastValue() == null || !p.getLastValue().equals(newValue)) { 
			Parameter dto = new Parameter();
			dto.setGroupId(groupId);
			dto.setName(p.getName());
			dto.setValue(newValue);
			parameterDao.updateValue(groupId, p.getName(), newValue);	
			p.setLastValue(newValue);
		}
	}
}
