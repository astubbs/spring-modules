package org.springmodules.javaspaces.entry;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * JINI entry representing a method call that includes the necessary code in a
 * target object. Moves code around the network as well as invocation.
 * 
 * @author Rod Johnson
 * @author Costin Leau
 */
public class RunnableMethodCallEntry extends AbstractMethodCallEntry {

	/**
	 * Target of the method call. This is serialized from the caller.
	 */
	public Object target;

	public RunnableMethodCallEntry(Method method, Object[] args, Object target) {
		super(method, args);
		this.target = target;
	}

	// TODO forced by spaces
	public RunnableMethodCallEntry() {
		this(null, null, null);
	}


	/**
	 * Invoke the method ignoring the given delegate by using the internal target object.
	 */
	protected MethodResultEntry doInvocation(Object delegate) throws InvocationTargetException, IllegalAccessException {
		Method method = getMethod();
		Object resultObject = method.invoke(target, getArguments());
		return new MethodResultEntry(method, uid, resultObject);
	}

}
