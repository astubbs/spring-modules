package org.springmodules.javaspaces.entry;

import java.io.Serializable;
import java.lang.reflect.Method;

import net.jini.core.entry.Entry;

import org.springmodules.javaspaces.MethodIdentifier;

/**
 * JINI entry representing the result of a method call.
 * This will include the result (if successful) or the
 * Throwable in the event of failure.
 *
 * @author Rod Johnson
 * @author Costin Leau
 */
public class MethodResultEntry extends MethodIdentifier implements Entry {

	public Object result;

    public Throwable failure;

    /**
     * Constructor
     * @param method the method to be invoke
     * @param uid the id of the RMI
     * @param result the result of the invocation method
     */
	public MethodResultEntry(Method method, Serializable uid, Object result) {
		super(method);
		this.result = result;
		this.uid = uid;
	}

	/**
	 * Constructor
	 * @param t the exception if the invocation failed
	 * @param method the method
	 * @param uid the id of the invocation
	 */
    public MethodResultEntry(Throwable t, Method method, Serializable uid) {
        super(method);
        this.failure = t;
        this.uid = uid;
    }


    /**
     * Empty constructor
     *
     */
	public MethodResultEntry() {
		this((Method) null, null, null);
	}

	/**
	 * Indicates if the RMI succeed
	 * @return
	 */
    public boolean successful() {
        return failure == null;
    }
    /**
     * Gets the failure exception
     * @return the exception
     */
    public Throwable getFailure() {
        return failure;
    }

    /**
     * Gets the result of the RMI
     * @return the result
     */
	public Object getResult() {
		return result;
	}


}
