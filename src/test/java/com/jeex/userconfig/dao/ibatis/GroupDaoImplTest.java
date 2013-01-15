package com.jeex.userconfig.dao.ibatis;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jeex.userconfig.dao.GroupDao;
import com.jeex.userconfig.impl.Group;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-context.xml")
public class GroupDaoImplTest extends AbstractTransactionalJUnit4SpringContextTests {
	@Autowired
	GroupDao groupDao;
	
	@Test 
	public void testInsertAndGet() {
		Group g = new Group();
		g.setGroupId("test1");
		g.setDescription("desc1");
		g.setName("test1 name");		
		groupDao.insertGroup(g);
		
		Group fromDb = groupDao.getGroup(g.getGroupId());
		
		Assert.assertEquals(g.getGroupId(), fromDb.getGroupId());
		Assert.assertEquals(g.getName(), fromDb.getName());
		Assert.assertEquals(g.getDescription(), fromDb.getDescription());
	}
	
	@Test
	public void testUpdateGroup() {
		Group g = new Group();
		g.setGroupId("test1");
		g.setDescription("desc1");
		g.setName("name1");		
		groupDao.insertGroup(g);
		
		g.setName("name2");
		g.setDescription("desc2");
		groupDao.updateGroup(g);
		
		Group g2 = groupDao.getGroup(g.getGroupId());
		Assert.assertEquals("name2", g2.getName());
		Assert.assertEquals("desc2", g2.getDescription());		
		
	}

}
