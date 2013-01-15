package com.jeex.userconfig;

/**
 * ConfigService reads the values of configurable parameters from database, and inject the values to 
 * the object. It also supports save the values of parameters to database.
 * 
 * <p>It discovers the configurable parameters which are annotated by {@link ParamFromDb} or {@link ParamToDb}, 
 * and call get/set method reflectively. 
 */
public interface ConfigService {
	/**
	 * Sets the values of configurable parameters which are defined by {@link ParamFromDb}
	 * Both static and non-static parameters are supported.
	 * @param obj - the object
	 */
	public void configure(Object obj);
	
	/**
	 * Saves the values of configurable parameters which are defined by {@link ParamToDb}
	 * Both static and non-static parameters would be saved.
	 * @param obj - the object
	 */
	public void save(Object obj);
	
	/**
	 * Sets the values of static (class level) configurable parameters which are defined 
	 * by {@link ParamFromDb}. 
	 * @param clazz - the class
	 */
	public void configureStatic(Class<?> clazz);
	
	/**
	 * Saves the values of static (class level) configurable parameters which are defined 
	 * by {@link ParamToDb}.
	 * @param obj - the class
	 */
	public void saveStatic(Class<?> clazz);
}
