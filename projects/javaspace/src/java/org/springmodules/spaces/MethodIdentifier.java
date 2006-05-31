package org.springmodules.spaces;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Serializable representation of a method and invocation id, solving the
 * problem of java.lang.reflect.Method not being serializable.
 * 
 * @author Rod Johnson
 */
public class MethodIdentifier implements Serializable {

	private transient static final Log log = LogFactory.getLog(MethodIdentifier.class);

	// TODO do properly
	private transient Method method;

	/** Fully qualified class name */
	public String className;

	public String methodString;

	/**
	 * Unique identifier. Has to be set up externally depending on the chosen strategy (id unique on the same machine
	 * generated very fast or ids unique across a cluster or worldwide which have more expensive creation process).
	 */
	public Serializable uid;

	public MethodIdentifier(Method method) {
		this.method = method;
		if (method != null) {
			methodToString(method);
		}
	}

	public MethodIdentifier() {
	}

	
	public synchronized Method getMethod() {
		if (method == null) {
			method = stringToMethod();
		}

		return method;
	}

	/*
	 * public Method getMethod() { return method; }
	 */

	/**
	 * Method to String conversion.
	 * 
	 * @param m
	 */
	private void methodToString(Method m) {
		this.className = method.getDeclaringClass().getName();
		this.methodString = method.toString();
	}

	/**
	 * String to method conversion.
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 */
	private Method stringToMethod(String className) {

		if (log.isDebugEnabled())
			log.debug("Fqn='" + className + "'; methodName='" + methodString + "'");
		Class clazz;
		try {
			clazz = Class.forName(className);
		}
		catch (ClassNotFoundException e) {
			throw new IllegalArgumentException(e);
		}
		for (int i = 0; i < clazz.getMethods().length; i++) {
			Method m = clazz.getMethods()[i];
			if (m.toString().equals(methodString)) {
				return m;
			}
		}

		throw new IllegalArgumentException("Can't resolve method from '" + className + "." + methodString + "'");
	}

	private Method stringToMethod() {
		return stringToMethod(className);
	}

	public String toString() {
		return this.getClass().getName() + "uid='" + uid + "': method=" + method + "; className=" + className
				+ "; methodString=" + methodString;
	}

	// TODO: check the logic of these methods and see why they break the tests.
	// It would be good to put the method resolving here
	// and keep the getter unsynchronized.

	/*
	 * private void writeObject(ObjectOutputStream oos) throws IOException {
	 * oos.defaultWriteObject(); //oos.writeObject(className);
	 * //oos.writeObject(methodString); // oos.writeObject(); }
	 * 
	 * private void readObject(ObjectInputStream ois) throws IOException,
	 * ClassNotFoundException { ois.defaultReadObject(); //className = (String)
	 * ois.readObject(); //methodString = (String) ois.readObject(); // TODO
	 * pass out to build method // // TODO args method = stringToMethod(); }
	 */

}
