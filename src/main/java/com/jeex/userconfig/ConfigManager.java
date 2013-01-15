package com.jeex.userconfig;

/**
 * ConfigManager periodically configures/saves the parameters for registered objects.
 * 
 * <p>It uses {@link ConfigService} to do the configure and save.
 */
public interface ConfigManager {
	/**
	 * Register the object to this ConfigManager.
	 * @param obj - object to be registered.
	 */
	public void register(Object obj);
	
	/**
	 * Register the class to this ConfigManager.
	 * @param clazz - class to be registered.
	 */
	public void registerStatic(Class<?> clazz);
}
