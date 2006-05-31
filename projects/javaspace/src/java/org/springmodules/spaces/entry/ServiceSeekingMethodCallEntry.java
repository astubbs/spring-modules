package org.springmodules.spaces.entry;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * JINI entry representing a method call that hits a remote service.
 * That is, the endpoint is expected to host the service; only the
 * method and arguments will be shipped, and not the implementing code.
 * @author Rod Johnson
 */
public class ServiceSeekingMethodCallEntry extends AbstractMethodCallEntry {
    
	
	public ServiceSeekingMethodCallEntry(Method method, Object[] args) {
		super(method, args);
	}
	
	// TODO forced by spaces
	public ServiceSeekingMethodCallEntry() {
		this(null, null);
	}


	/**
	 * Invoke the method using the given delegate (target).
	 * 
	 * @see org.springmodules.spaces.entry.AbstractMethodCallEntry#doInvocation(java.lang.Object)
	 */
	protected MethodResultEntry doInvocation(Object delegate) throws InvocationTargetException, IllegalAccessException {
		Method method = getMethod();
		Object resultObject = method.invoke(delegate, getArguments());
		return new MethodResultEntry(method, uid, resultObject);
	}
}
