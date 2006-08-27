package org.springmodules.javaspaces.entry;

import java.io.Serializable;
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

	/**
	 * Constructor
	 * @param method the method to be invoke.
	 * @param args the arguments of the method
	 * @param target the target through invoke the method.
	 */
	public RunnableMethodCallEntry(Method method, Object[] args, Object target) {
		super(method, args);
		this.target = target;
	}

	/**
	 * Empty constructor
	 *
	 */
	public RunnableMethodCallEntry() {
		this(null, null, null);
	}

	/**
	 * Constructor
	 * @param method the method to be invoked
	 * @param args the method's argument
	 * @param target the target buisness logic object to be invoked through RMI.
	 * @param uid the id of the RMI
	 */
	public RunnableMethodCallEntry(Method method, Object[] args, Object target, Serializable uid) {
		super(method, args,uid);
		this.target = target;
	}

	/**
	 * Invoke the method ignoring the given delegate by using the internal target object.
	 * @param delegate in this case the delegate object is not passed through the  client
	 * but is in the server.
	 * @see org.springmodules.javaspaces.entry.AbstractMethodCallEntry#doInvocation(java.lang.Object)
	 */
	protected MethodResultEntry doInvocation(Object delegate) throws InvocationTargetException, IllegalAccessException {
		Method method = getMethod();
		Object resultObject = method.invoke(target, getArguments());
		return new MethodResultEntry(method, uid, resultObject);
	}

}
