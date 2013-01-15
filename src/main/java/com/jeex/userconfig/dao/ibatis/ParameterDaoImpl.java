package com.jeex.userconfig.dao.ibatis;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import com.jeex.userconfig.dao.ParameterDao;
import com.jeex.userconfig.impl.Parameter;

public class ParameterDaoImpl implements ParameterDao {
	private static final String SQLMAP_SPACE = "MtConfigParam.";

	@Autowired
	private SqlMapClientTemplate sqlMapClientTemplate;

	public SqlMapClientTemplate getSqlMapClientTemplate() {
		return sqlMapClientTemplate;
	}

	public void setSqlMapClientTemplate(
			SqlMapClientTemplate sqlMapClientTemplate) {
		this.sqlMapClientTemplate = sqlMapClientTemplate;
	}

	public void updateParameter(Parameter parameter) {
		getSqlMapClientTemplate()
				.update(SQLMAP_SPACE + "update", parameter);
	}
	
	public void updateValue(String groupId, String name, String value) {
		Parameter param = new Parameter();
		param.setGroupId(groupId);
		param.setName(name);
		param.setValue(value);
		
		getSqlMapClientTemplate()
			.update(SQLMAP_SPACE + "updateValue", param);
	}

	public void deleteByGroupId(String groupId) {
		getSqlMapClientTemplate()
		.update(SQLMAP_SPACE + "deleteByGroupId", groupId);		
	}

	public void insertParameter(Parameter parameter) {
		getSqlMapClientTemplate().insert(SQLMAP_SPACE + "insert", parameter);
	}

	@SuppressWarnings("unchecked")
	public List<Parameter> getParametersByGroupId(String groupId) {
		return getSqlMapClientTemplate().queryForList(
				SQLMAP_SPACE + "getByGroupId", groupId);
	}

	public Parameter getParameter(String groupId, String name) {
		Parameter param = new Parameter();
		param.setGroupId(groupId);
		param.setName(name);
		return (Parameter) getSqlMapClientTemplate().queryForObject(
				SQLMAP_SPACE + "get", param);
	}

}
