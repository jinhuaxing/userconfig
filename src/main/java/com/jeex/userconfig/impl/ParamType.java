package com.jeex.userconfig.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

enum ParamType {
	
	INT(int.class),
	LONG(long.class),
	BOOL(boolean.class),
	STR(String.class);

	private static final Log log = LogFactory.getLog(ParamType.class);
	
	private Class<?> typeClass;
	
	private ParamType(Class<?> typeClass) {
		this.typeClass = typeClass;
	}
		
	public Class<?> getTypeClass() {
		return typeClass;
	}
		
	public static ParamType fromTypeClass(Class<?> clazz) {
		if (clazz == int.class) {
			return ParamType.INT;
		}
		if (clazz == long.class) {
			return ParamType.LONG;
		}
		if (clazz == boolean.class) {
			return ParamType.BOOL;
		}
		if (clazz == String.class) {
			return ParamType.STR;
		}
		return null;			
	}

	public static Object parseValue(Parameter p, String value) {
		try {
			switch (p.getType()) {
				case INT: {
					return intArg(p, value);
				}
				case LONG: {
					return longArg(p, value);
				}			
				case BOOL: {
					return boolArg(p, value);
				}			
				case STR: {
					return value == null? p.getDefaultValue(): value;
				}			
				default: {
					throw new RuntimeException("Should not happen");
				}		
			}
		} catch (Exception e) {
			log.warn("Parameter converstion fail, check defaut value of param=" 
						+ p.getFullName(), e);
			return null;
		}
		
	}
		
	private static Boolean boolArg(Parameter p, String value) {
		if (value == null) {
			return Boolean.valueOf(p.getDefaultValue());
		}
		try {
			return Boolean.valueOf(value);
		} catch (Exception e) {
			logWarn(p, e);
			return Boolean.valueOf(p.getDefaultValue());
		}
	}
	
	private static Integer intArg(Parameter p, String value) {
		if (value == null) {
			return Integer.valueOf(p.getDefaultValue());
		}
		try {
			return Integer.valueOf(value);
		} catch (Exception e) {
			logWarn(p, e);
			return Integer.valueOf(p.getDefaultValue());
		}
	}
		
	private static Long longArg(Parameter p, String value) {
		if (value == null) {
			return Long.valueOf(p.getDefaultValue());
		}
		try {
			return Long.valueOf(value);
		} catch (Exception e) {
			logWarn(p, e);
			return Long.valueOf(p.getDefaultValue());
		}
	}
	
	private static void logWarn(Parameter p, Exception e) {
		log.warn("Parameter converstion fail, check configured value in DB, param=" 
				+ p.getFullName(), e);
	}
		
}
