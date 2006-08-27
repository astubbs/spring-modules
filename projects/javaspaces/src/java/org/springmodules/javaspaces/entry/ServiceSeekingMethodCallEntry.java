package org.springmodules.javaspaces.entry;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * JINI entry representing a method call that hits a remote service.
 * That is, the endpoint is expected to host the service; only the
 * method and arguments will be shipped, and not the implementing code.
 * @author Rod Johnson
 */
public class ServiceSeekingMethodCallEntry extends AbstractMethodCallEntry {


	/**
	 * Constructor
	 * @param method the method to be invoke
	 * @param args the method's arguments
	 */
	public ServiceSeekingMethodCallEntry(Method method, Object[] args) {
		super(method, args);
	}


	/**
	 * Default constructor.
	 *
	 */
	public ServiceSeekingMethodCallEntry() {
		this(null, null);
	}

	/**
	 * Constructor
	 * @param method the method to be invoked
	 * @param args the method's arguments
	 * @param uid the id of the RMI
	 */
	public ServiceSeekingMethodCallEntry(Method method, Object[] args, Serializable uid) {
		super(method, args , uid);
	}

	/**
	 * Invoke the method using the given delegate (target).
	 *
	 * @see org.springmodules.javaspaces.entry.AbstractMethodCallEntry#doInvocation(java.lang.Object)
	 */
	protected MethodResultEntry doInvocation(Object delegate) throws InvocationTargetException, IllegalAccessException {
		Method method = getMethod();
		Object resultObject = method.invoke(delegate, getArguments());
		return new MethodResultEntry(method, uid, resultObject);
	}
}
