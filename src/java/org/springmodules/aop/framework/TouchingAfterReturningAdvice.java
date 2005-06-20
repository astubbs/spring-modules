package org.springmodules.aop.framework;

import java.lang.reflect.Method;
import java.util.Collection;

import org.springframework.aop.AfterReturningAdvice;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.MethodInvoker;

/**
 * <p>Touches configurable properties on the return value of a method invocation.
 * This approach is an alternative to the open session in view pattern.
 * 
 * <p>If the return value is a single object the properties are touched on this
 * object. If the return value is an instance of java.util.Collection or Object[] 
 * the properties are touched on every element.
 * 
 * <p>If no properties are specified collections will still be iterated entirely.
 * 
 * 
 * @author Steven Devijver
 * @since 20-06-2005
 *
 */
public class TouchingAfterReturningAdvice implements AfterReturningAdvice {

	private String[] properties = null;
	
	/**
	 * <p>The [properties] properties takes a number of property string.
	 * The return value of the method invocation will be touched with these
	 * properties.
	 * 
	 * @param properties the property strings
	 */
	public void setProperties(String[] properties) {
		this.properties = properties;
	}
	
	public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
		if (returnValue == null) {
			return;
		}
		
		if (returnValue instanceof MethodInvoker.VoidType) {
			return;
		} else if (returnValue instanceof Collection) {
			touch(((Collection)returnValue).toArray(), properties);
		} else if (returnValue instanceof Object[]) {
			touch((Object[])returnValue, properties);
		} else {
			touch(returnValue, properties);
		}
		
	}
	
	private static void touch(Object[] objects, String[] properties) {
		for (int i = 0; objects != null && properties != null && i < objects.length; i++) {
			Object target = objects[i];
			touch(target, properties);
		}
	}
	
	private static void touch(Object target, String[] properties) {
		BeanWrapper beanWrapper = new BeanWrapperImpl(target);
		for (int x = 0; properties != null && x < properties.length; x++) {
			String property = properties[x];
			Object result = beanWrapper.getPropertyValue(property);
			if (result instanceof Collection) {
				((Collection)result).size();
			}
		}
	}
}
