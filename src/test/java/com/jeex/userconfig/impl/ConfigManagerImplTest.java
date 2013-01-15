package com.jeex.userconfig.impl;

import java.util.concurrent.TimeUnit;

import org.jmock.lib.concurrent.DeterministicScheduler;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-context.xml")
public class ConfigManagerImplTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	@Autowired
	private ConfigServiceImpl configService;
	
	@Autowired
	private ConfigManagerImpl configManager;
	
	@Autowired
	private DeterministicScheduler scheduledExecutorService;
		
	@Test
	public void testSave() throws InterruptedException {
		TestRule1 tr1 = new TestRule1();
		configManager.register(tr1);
		
		configManager.setConfigurePeroidInSeconds(20);
		configManager.setSavePeroidInSeconds(5);
		configManager.restart();
			
		tr1.setCount(100);
		
		scheduledExecutorService.tick(6, TimeUnit.SECONDS); // call save
		
		tr1.setCount(10);
	
		configService.configure(tr1);
		Assert.assertEquals(100, tr1.getCount());
		configManager.unregister(tr1);
		
	}
	
	@Test
	public void testConfigure() throws InterruptedException {
		TestRule1 tr1 = new TestRule1();
		configManager.register(tr1);
		
		configManager.setConfigurePeroidInSeconds(5);
		configManager.setSavePeroidInSeconds(20);
		configManager.restart();
			
		tr1.setCount(100);
		configService.save(tr1);
		
		tr1.setCount(10);
		scheduledExecutorService.tick(6, TimeUnit.SECONDS); // call configure
		
		Assert.assertEquals(100, tr1.getCount());
		configManager.unregister(tr1);			
	}
	
}
