package com.jeex.userconfig;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * To mark a parameter that could be saved to database. 
 * <p>The value of parameter is saved to database by {@link ConfigService}.
 * <p>It is used to annotate a method starting with <b>get</b>, either 
 * static or non-static.
 * <p>A parameter is identified by an internal name, which is converted 
 * from the method name, following the Java Bean pattern.
 * <p>If a parameter (identified by its internal name) is also annotated by {@link ParamFromDb},
 * the elements of this annotation will be overrided by that of {@link ParamFromDb}.
 */
@Target(ElementType.METHOD)  
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ParamToDb {
	/**
	 * Descriptive name of the parameter.
	 * <p>Defaults to "".
	 */
	String name() default "";
	
	/**
	 * Description of the parameter.
	 * <p>Defaults to "".
	 */
	String description() default "";
	
	/**
	 * If the parameter could be edited by user, or it is just a read-only parameter.
	 * <p>Defaults to <code>true</code>.
	 */	
	boolean readonly() default true;
}
