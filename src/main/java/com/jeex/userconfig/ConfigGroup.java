package com.jeex.userconfig;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ConfigGroup is used to annotate a class. All the configurable parameters in the 
 * class belong to this group. A group is identified by <code>id</code>.
 * <p>This is optional. If the annotation is not present, it functions like all the 
 * values use default ones.
 * <p>ConfigGroup forms a namespace for the parameters. 
 * <p>Different classes may share the same group (providing the same id of ConfigGroup).
 */
@Target(ElementType.TYPE)  
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ConfigGroup {
	/**
	 * Identifier of a group. 
	 * Defaults to "".
	 * <p>If the default value is used, the system would use the class name of 
	 * the annotated class as the id for the group.
	 */
	String id() default "";
	
	/**
	 * A descriptive name of the group. 
	 * Defaults to "".
	 */
	String name() default "";
	
	/**
	 * Detail description for the group.
	 * Defaults to "".
	 */
	String description() default "";
}
