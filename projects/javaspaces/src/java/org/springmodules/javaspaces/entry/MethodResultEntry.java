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
	
	public MethodResultEntry(Method method, Serializable uid, Object result) {
		super(method);
		this.result = result;
		this.uid = uid;
	}
    
    public MethodResultEntry(Throwable t, Method method, Serializable uid) {
        super(method);
        this.failure = t;
        this.uid = uid;
    }
    
	
	// TODO forced by spaces
	public MethodResultEntry() {
		this((Method) null, null, null);
	}
    
    public boolean successful() {
        return failure == null;
    }
    
    public Throwable getFailure() {
        return failure;
    }
	
	public Object getResult() {
		return result;
	}


}
