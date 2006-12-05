package org.springmodules.aop.framework;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ognl.Ognl;
import ognl.OgnlException;

import org.springframework.aop.AfterReturningAdvice;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
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
 * <p>Property values can be either property names or maps containing property names
 * as keys and lists containing property names as values. Maps entries represent bean
 * properties (the key of the map entries) that are collections. All elements of those collection will
 * be touched by the properties (the values of the map entries). This construct can
 * be cascaded.
 * 
 * <p>OGNL expressions are also supported. These expression will be executed after the properties
 * have been executed on the return value of the method invocation. To support the evaluation of collections
 * you can use the <code>#returned</code> variable in the OGNL context which holds the return value.
 * 
 * <p>To load all elements of a returned collection instance use OGNL's pseudo-property size:
 * 
 * <pre>
 * #returned.size
 * </pre>
 * 
 * @author Steven Devijver
 * @since 20-06-2005
 *
 */
public class TouchingAfterReturningAdvice implements AfterReturningAdvice {

	private Object[] properties = null;
	private String[] ognlExpressions = null;
	
	/**
	 * <p>The [properties] properties takes a list of property strings.
	 * The return value of the method invocation will be touched with these
	 * properties.
	 * 
	 * @param properties the property strings
	 */
	public void setProperties(Object[] properties) {
		this.properties = properties;
	}
	
	/**
	 * <p>The [ognl] property takes a list of OGNL expressions that will be
	 * evaluated on the return value of the method invocation.
	 * 
	 * @param ognlExpressions the OGNL expressions
	 */
	public void setOgnl(String[] ognlExpressions) {
		this.ognlExpressions = ognlExpressions;
	}
	
	public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
		if (returnValue == null) {
			return;
		}
		
		if (returnValue instanceof Void) {
			return;
		} else if (returnValue instanceof Collection) {
			touch(((Collection)returnValue).toArray(), properties);
		} else if (returnValue instanceof Object[]) {
			touch((Object[])returnValue, properties);
		} else {
			touch(returnValue, properties);
		}
		touchOgnl(returnValue, ognlExpressions);
		
	}
	
	private static void touch(Object[] objects, Object[] properties) {
		for (int i = 0; objects != null && properties != null && i < objects.length; i++) {
			Object target = objects[i];
			touch(target, properties);
		}
	}
	
	private static void touch(Object target, Object[] properties) {
		BeanWrapper beanWrapper = new BeanWrapperImpl(target);
		for (int x = 0; properties != null && x < properties.length; x++) {
			Object property = properties[x];
			if (property instanceof String) {
				Object result = beanWrapper.getPropertyValue((String)property);
				if (result instanceof Collection) {
					((Collection)result).size();
				}
			} else if (property instanceof Map) {
				for (Iterator iter = ((Map)property).keySet().iterator(); iter.hasNext();) {
					Object key = iter.next();
					Object tmpProperties = ((Map)property).get(key);
					if (!(key instanceof String)) {
						throw new IllegalArgumentException("Maps configured in the [properties] property should have a string key!");
					}
					if (!(tmpProperties instanceof Collection)) {
						throw new IllegalArgumentException("Maps configured in the [properties] property should have a list value!");
					}
					Object result = beanWrapper.getPropertyValue((String)key);
					if (result instanceof Collection) {
						touch(((Collection)result).toArray(), ((Collection)tmpProperties).toArray());
					} else if (result instanceof Object[]) {
						touch((Object[])result, ((Collection)tmpProperties).toArray());
					} else {
						touch(result, ((Collection)tmpProperties).toArray());
					}
				}
			} else {
				throw new IllegalArgumentException("[properties] property should only contain string and map instances!");
			}
		}
	}
	
	private static void touchOgnl(Object target, String[] ognlExpressions) {
		for (int i = 0; ognlExpressions != null && i < ognlExpressions.length; i++) {
			String ognlExpression = ognlExpressions[i];
			try {
				Map context = new HashMap();
				context.put("returned", target);
				Ognl.getValue(ognlExpression, context, target);
			} catch (OgnlException e) {
				throw new BeansException("Error occured while evaluating OGNL expression [" + ognlExpression + "]!", e) {};
			}
		}
	}
}
