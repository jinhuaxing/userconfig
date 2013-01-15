package com.jeex.userconfig.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jeex.userconfig.ConfigGroup;
import com.jeex.userconfig.ParamFromDb;
import com.jeex.userconfig.ParamToDb;

/**
 * ConfigInfo contains all information about configurable parameter for an object (or class).  
 * The info are extracted from <code>Class<T></code> and the annotations on its methods.
 *
 */
class ConfigInfo {
	private static final Log log = LogFactory.getLog(ConfigInfo.class);
	
	private Group group;
	private List<Parameter> params;
	
	public ConfigInfo() {}
	
	public Group getGroup() {
		return group;
	}
	public void setGroup(Group group) {
		this.group = group;
	}
	public List<Parameter> getParameters() {
		return params;
	}
	
	public void setParameters(List<Parameter> params) {
		this.params = params;
	}
	
	static ConfigInfo contructConfigInfo(Class<?> clazz) {	
		ConfigGroup annotation = clazz.getAnnotation(ConfigGroup.class);	
		Group group = new Group();
		if (annotation == null) {
			group.setGroupId(clazz.getName());
			group.setName("");
			group.setDescription("");			
		} else {
			if (annotation.id().equals("")) {
				group.setGroupId(clazz.getName());
			} else {
				group.setGroupId(annotation.id());
			}
			group.setName(annotation.name());
			group.setDescription(annotation.description());
		}
   	
		ConfigInfo ci = new ConfigInfo();
		ci.setGroup(group);
		ci.setParameters(
				contructParams(clazz, group.getGroupId()));
    	
    	if (ci.getParameters().isEmpty()) {
    		log.warn("Empty parameters for class " + clazz);
    	}
    	
		if(log.isDebugEnabled()) {
			log.debug("ConfigInfo Created: " + ci);
		}
		    	
		return ci;
	}
	
	static List<Parameter> contructParams(Class<?> clazz, String groupId) { 
		List<Parameter> paramList = new ArrayList<Parameter>();
		Method[] methods = clazz.getMethods();
    	for (Method m: methods) {
    		ParamFromDb fromcp = m.getAnnotation(ParamFromDb.class);
    		ParamToDb tocp = m.getAnnotation(ParamToDb.class);
    		if (fromcp == null && tocp == null) {
    				continue;
    		} 
    		boolean fromDb = (fromcp != null);
    		
    		if (log.isDebugEnabled()) {
    			log.debug("Processing method " + m);
    		}
    		
    		String name = null;
    		Direction direct = null; 
    		Class<?> paramType = null;
    		String methodName = m.getName();
    		if (methodName.startsWith("set") && fromDb) {
    			name = methodName.substring(3,4).toLowerCase() + methodName.substring(4);
    			direct = Direction.FROMDB;
    			paramType = m.getParameterTypes()[0];
    		} else if (methodName.startsWith("get") && !fromDb) {
    			name = methodName.substring(3,4).toLowerCase() + methodName.substring(4);
    			direct = Direction.TODB;
    			paramType = m.getReturnType();
    		} else {
    			log.error("Incorrect use of Annotation ParamFrom(To)Db, methond=" + m);
    			continue;
    		}
    		ParamType pt = ParamType.fromTypeClass(paramType);
    		if (pt == null) {
    			log.error("Unsupported parameter type, methond=" + m);
    			continue;
    		}
    			
    		Parameter rp = findParameter(paramList, name);
    		if (rp == null) {
    			rp = new Parameter();
    	   		rp.setGroupId(groupId);
    	   		rp.setName(name);
    	   		rp.setType(pt);
    	   		if (fromDb) {
	    			rp.setDescriptiveName(fromcp.name());
	    			rp.setDescription(fromcp.description());
	    			rp.setReadonlyBool(fromcp.readonly());
	    			rp.setDefaultValue(fromcp.defaultValue());
    	   		} else {
	    			rp.setDescriptiveName(tocp.name());
	    			rp.setDescription(tocp.description());    	 
	    			rp.setReadonlyBool(tocp.readonly());
	    		}
    	   		rp.setDirection(direct);    	   		
    			paramList.add(rp);
     		} else {
     			if (rp.getDirection() == Direction.BOTH) {
        			log.error("Already BOTH direction, methond=" + m);
        			continue;    					
     			}
     			if (rp.getDirection() == direct) {
     				log.error("Duplicate direction, methond=" + m);
     				continue;
     			}    				
    			if (pt != rp.getType()) {
    				log.error("Different type, methond=" + m);
    				continue;
    			}
    			
    			// The descriptions in ParamFromDb override those in *To* annotation
    	   		if (fromDb) {
	    			rp.setDescriptiveName(fromcp.name());
	    			rp.setDescription(fromcp.description());
	    			rp.setReadonlyBool(fromcp.readonly());
	    			rp.setDefaultValue(fromcp.defaultValue());
	    		}    	   		   			
     			rp.setDirection(Direction.BOTH);
     		}
     	}
    	
    	return paramList;
	}
	
	static Parameter findParameter(List<Parameter> paramList, String name) {
		for (Parameter param: paramList) {
			if (name.equalsIgnoreCase(param.getName())) {
				return param;
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		return "ConfigInfo [group=" + group + ", params=" + params + "]";
	}
}
