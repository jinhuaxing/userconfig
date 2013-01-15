package com.jeex.userconfig.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jeex.userconfig.ConfigService;
import com.jeex.userconfig.dao.GroupDao;
import com.jeex.userconfig.dao.ParameterDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-context.xml")
public class ConfigServiceImplTest extends AbstractTransactionalJUnit4SpringContextTests {
	@Autowired
	ConfigService configService;
	
	@Autowired
	GroupDao groupDao;
	
	@Autowired
	ParameterDao parameterDao;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Test
	public void testConfigure() {
		TestRule1 tr1 = new TestRule1();
		configService.configure(tr1);
				
		Group g = groupDao.getGroup("rule1");
		Assert.assertNotNull(g);
		
		Parameter p = null;
		p = parameterDao.getParameter(g.getGroupId(), "count");
		Assert.assertNotNull(p);
		p = parameterDao.getParameter(g.getGroupId(), "feedback");
		Assert.assertNotNull(p);
		p = parameterDao.getParameter(g.getGroupId(), "active");
		Assert.assertNotNull(p);		
	}
	
	@Test
	public void testSave() {
		TestRule1 tr1 = new TestRule1();
		configService.configure(tr1);		
		
		//test default value injection
		Assert.assertEquals(5, tr1.getCount());
		Assert.assertEquals(12345L, tr1.getLongCount());
		Assert.assertEquals("达到最大次数", tr1.getFeedback());
		Assert.assertEquals(true, tr1.getActive());		
		Assert.assertEquals(10, TestRule1.getStaticCount());
		
		tr1.setCount(100);
		tr1.setLongCount(54321L);
		tr1.setActive(false);
		tr1.setFeedback("到达最小次数");
		TestRule1.setStaticCount(20);
		
		configService.save(tr1);
		
		tr1.setCount(50);
		tr1.setLongCount(231L);
		tr1.setActive(true);
		tr1.setFeedback("nonsense");		
		TestRule1.setStaticCount(10);
		
		configService.configure(tr1);
		
		Assert.assertEquals(100, tr1.getCount());
		Assert.assertEquals(54321L, tr1.getLongCount());
		Assert.assertEquals(false, tr1.getActive());		
		Assert.assertEquals("到达最小次数", tr1.getFeedback());
		Assert.assertEquals(20, TestRule1.getStaticCount());
	}
	
	@Test
	public void testMissingParam() {
		TestRule1 tr1 = new TestRule1();
		configService.configure(tr1);		
		tr1.setCount(100);
		configService.save(tr1);
		
		jdbcTemplate.execute("delete from mt_config_param where groupid='rule1' and name='count'");
		
		configService.configure(tr1);	
		//restore to default value
		Assert.assertEquals(5, tr1.getCount()); 
		
	}
	
	@Test
	public void testMissingGroup() {
		TestRule1 tr1 = new TestRule1();
		configService.configure(tr1);		
		tr1.setCount(100);
		configService.save(tr1);
		
		jdbcTemplate.execute("delete from mt_config_group where groupid='rule1'");
		
		configService.configure(tr1);	
		//restore to default value
		Assert.assertEquals(5, tr1.getCount()); 
		
	}
	
	@Test
	public void testBadParam() {
		TestRule1 tr1 = new TestRule1();
		configService.configure(tr1);		
		tr1.setCount(100);
		configService.save(tr1);
		
		jdbcTemplate.execute("update mt_config_param set value='xxx' where groupid='rule1' and name='count'");
		
		configService.configure(tr1);	
		//restore to default value
		Assert.assertEquals(5, tr1.getCount()); 
		
	}
	
	@Test
	public void testConfigureClassName() {
		TestRule2 tr2 = new TestRule2();
		configService.configure(tr2);		
		
		Group g = groupDao.getGroup(tr2.getClass().getName());
		Assert.assertNotNull(g);
		
		Parameter p = null;
		p = parameterDao.getParameter(g.getGroupId(), "count");
		Assert.assertNotNull(p);				
	}
	
	@Test
	public void testStatic() {
		configService.configureStatic(TestRule3.class);	
		Assert.assertEquals(13, TestRule3.getStaticCount());
		
		TestRule3.setStaticCount(100);
		configService.saveStatic(TestRule3.class);
		TestRule3.setStaticCount(10);
		configService.configureStatic(TestRule3.class);
		Assert.assertEquals(100, TestRule3.getStaticCount());		
	}
	
	@Test
	public void testGroupUpdate() {
		configService.configureStatic(TestRule3.class);
		
		Group g = groupDao.getGroup("TestGroup");
		Assert.assertEquals("name1", g.getName());
		Assert.assertEquals("desc1", g.getDescription());
				
		configService.configureStatic(TestRule4.class);
		g = groupDao.getGroup("TestGroup");
		Assert.assertEquals("name2", g.getName());
		Assert.assertEquals("desc2", g.getDescription());	
	}
	
	@Test
	public void testParamUpdate() {
		configService.configureStatic(TestRule3.class);	
		Parameter p = parameterDao.getParameter("TestGroup", "staticCount");
		Assert.assertEquals("static3", p.getDescriptiveName());
		Assert.assertEquals("13", p.getDefaultValue());
		
		configService.configureStatic(TestRule4.class);	
		p = parameterDao.getParameter("TestGroup", "staticCount");
		Assert.assertEquals("static4", p.getDescriptiveName());		
		Assert.assertEquals("14", p.getDefaultValue());
	}
}
