package org.springmodules.db4o.examples.chapter1;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * A utility class that helps to keep the spring configuration in one place.
 * 
 * @author Daniel Mitterdorfer
 * 
 */
public final class ExampleUtils {
	/**
	 * Bean ID of Db4o object container as specified in Spring's application
	 * context file.
	 */
	public static final String CONTAINER_BEAN_ID = "container";

	/**
	 * Creates a new application context.
	 * 
	 * @return An instance of AbstractApplicationContext for this application.
	 *         This allows you to invoke #close on the application context at
	 *         the end of your application's life which gives Spring a chance to
	 *         close all open resources such as the object container.
	 */
	public static AbstractApplicationContext getContext() {
		return new ClassPathXmlApplicationContext("/applicationContext-db4o.xml");
	}
}
