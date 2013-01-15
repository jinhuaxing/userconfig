package com.jeex.userconfig.dao.ibatis;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jeex.userconfig.dao.ParameterDao;
import com.jeex.userconfig.impl.Parameter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-context.xml")
public class ParameterDaoImplTest extends AbstractTransactionalJUnit4SpringContextTests {
	@Autowired
	ParameterDao parameterDao;
	
	@Test 
	public void testInsertAndGet() {
		Parameter p = new Parameter();
		p.setGroupId("group1");
		p.setName("name1");
		p.setDescription("desc1");
		p.setDefaultValue("default1");
		p.setReadonly(Integer.valueOf(1));
		p.setDescriptiveName("dname1");
		parameterDao.insertParameter(p);
		
		Parameter p2 = parameterDao.getParameter(p.getGroupId(), p.getName());
		Assert.assertEquals(p.getDefaultValue(), p2.getDefaultValue());
		Assert.assertEquals(p.getDescription(), p2.getDescription());
		Assert.assertEquals(p.getReadonly(), p2.getReadonly());
		Assert.assertEquals(p.getDescriptiveName(), p2.getDescriptiveName());
	}
	
	@Test
	public void testUpdateParamter() {
		Parameter p = new Parameter();
		p.setGroupId("group1");
		p.setName("name1");
		p.setDescription("desc1");
		p.setDefaultValue("default1");
		p.setReadonly(Integer.valueOf(1));
		p.setDescriptiveName("dname1");
		parameterDao.insertParameter(p);
		
		p.setDescription("desc2");
		p.setDefaultValue("default2");
		p.setReadonly(Integer.valueOf(2));
		p.setDescriptiveName("dname2");
		parameterDao.updateParameter(p);

		Parameter p2 = parameterDao.getParameter(p.getGroupId(), p.getName());
		Assert.assertEquals(p.getDefaultValue(), p2.getDefaultValue());
		Assert.assertEquals(p.getDescription(), p2.getDescription());
		Assert.assertEquals(p.getReadonly(), p2.getReadonly());
		Assert.assertEquals(p.getDescriptiveName(), p2.getDescriptiveName());
		
	}
	@Test
	public void testUpdateValue() {
		Parameter p = new Parameter();
		p.setGroupId("group1");
		p.setName("name1");
		p.setDescription("desc1");
		p.setDefaultValue("default1");
		p.setReadonly(Integer.valueOf(1));
		p.setDescriptiveName("dname1");
		parameterDao.insertParameter(p);
		
		parameterDao.updateValue(p.getGroupId(), p.getName(), "value1");
		
		Parameter p2 = parameterDao.getParameter(p.getGroupId(), p.getName());
		Assert.assertEquals("value1", p2.getValue());
	}

}
