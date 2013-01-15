package com.jeex.userconfig.dao;

import java.util.List;

import com.jeex.userconfig.impl.Parameter;

public interface ParameterDao {

	void updateParameter(Parameter parameter);

	void updateValue(String groupId, String name, String value);

	void deleteByGroupId(String groupId);

	void insertParameter(Parameter parameter);

	List<Parameter> getParametersByGroupId(String groupId);

	Parameter getParameter(String groupId, String name);

}