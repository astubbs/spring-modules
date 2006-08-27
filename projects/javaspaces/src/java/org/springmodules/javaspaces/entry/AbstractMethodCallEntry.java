package org.springmodules.javaspaces.entry;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.jini.core.entry.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.javaspaces.MethodIdentifier;

/**
 * JINI entry representing a method call. Subclasses vary depending on whether
 * the entry includes the code to be executed.
 *
 * @author Rod Johnson
 */
public class AbstractMethodCallEntry extends MethodIdentifier implements Entry {

	private final Log log = LogFactory.getLog(getClass());

	public Object[] args;

	/**
	 * Constructor
	 * @param method the method of the RMI
	 * @param args the arguments of the method
	 */
	public AbstractMethodCallEntry(Method method, Object[] args) {
		this(method, args, null);
	}

	/**
	 * Constructor
	 * @param method the method of the RMI
	 * @param args the arguments of the method
	 * @param uid the id of the method invocation
	 */
	public AbstractMethodCallEntry(Method method, Object[] args, Serializable uid) {
		super(method);
		this.args = args;
		this.uid = uid;
	}

	/**
	 * Default constructor
	 *
	 */
	public AbstractMethodCallEntry() {
		this(null, null);
	}

	public Object[] getArguments() {
		return args;
	}

	/**
	 * Invoke the delegate or run the code contained in the MethodCallEntry. Note that the delegate
	 * is an optional parameter - some call implementations (like ServiceSeekingMethodCallEntry) require
	 * it while other do not.
	 *
	 * @param call
	 * @param delegate
	 * @return MethodResultEntry the result of the invocation
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	public MethodResultEntry invokeMethod(Object delegate) throws IllegalAccessException {
		try {
			return doInvocation(delegate);
		}
		catch (InvocationTargetException ex) {
			// Target method threw exception
			// That needs to go into the space also to be thrown on the
			// caller-side.
			return new MethodResultEntry(ex.getTargetException(), getMethod(), uid);
		}
	}

	protected MethodResultEntry doInvocation(Object delegate) throws InvocationTargetException, IllegalAccessException {
		throw new UnsupportedOperationException("subclasses have to extend this method");
	}

}
