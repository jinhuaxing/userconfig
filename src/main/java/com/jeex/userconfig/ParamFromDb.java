package com.jeex.userconfig;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * To mark a configurable parameter. 
 * <p>The value of parameter could be read from database by {@link ConfigService}.
 * <p>It is used to annotate a method starting with <b>set</b>,
 * either static or non-static.
 * <p>A parameter is identified by an internal name, which is converted 
 * from the method name, following the Java Bean pattern.
 * <p>If a parameter (identified by its internal name) is also annotated by {@link ParamToDb},
 * the elements of this annotation will override that of {@link ParamToDb}.
 */
@Target(ElementType.METHOD)  
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ParamFromDb {
	/**
	 * Descriptive name of the parameter.
	 */
	String name() default "";
	
	/**
	 * Default value of the parameter. 
	 * It would be used if the value is not defined in database, or any error occurs during the process.
	 */
	String defaultValue();
	
	/**
	 * Description of the parameter.
	 * <p>Defaults to "".
	 */
	String description() default "";
	
	/**
	 * If the parameter could be edited by user, or it is just a read-only parameter.
	 * <p>Defaults to <code>false</code>.
	 */
	boolean readonly() default false;
}
