package com.jeex.userconfig.dao;

import com.jeex.userconfig.impl.Group;

public interface GroupDao {

	Group getGroup(String groupId);

	void insertGroup(Group group);

	void updateGroup(Group group);

}