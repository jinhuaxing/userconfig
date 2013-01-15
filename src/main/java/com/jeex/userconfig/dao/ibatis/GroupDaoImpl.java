package com.jeex.userconfig.dao.ibatis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import com.jeex.userconfig.dao.GroupDao;
import com.jeex.userconfig.impl.Group;

public class GroupDaoImpl implements GroupDao {
	private static final String SQLMAP_SPACE = "MtConfigGroup.";
	@Autowired
	private SqlMapClientTemplate sqlMapClientTemplate;

	public SqlMapClientTemplate getSqlMapClientTemplate() {
		return sqlMapClientTemplate;
	}

	public void setSqlMapClientTemplate(SqlMapClientTemplate sqlMapClientTemplate) {
		this.sqlMapClientTemplate = sqlMapClientTemplate;
	}

	public Group getGroup(String groupId) {
		return (Group)this.sqlMapClientTemplate.queryForObject(SQLMAP_SPACE+"get", groupId);

	}

	public void insertGroup(Group group) {
		getSqlMapClientTemplate().insert(SQLMAP_SPACE + "insert", group);
		
	}
	
	public void updateGroup(Group group) {
		getSqlMapClientTemplate().update(SQLMAP_SPACE + "update", group);		
	}
	
}
