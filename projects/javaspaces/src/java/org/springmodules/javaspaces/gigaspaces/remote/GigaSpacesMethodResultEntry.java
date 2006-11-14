/*
 * @(#)GigaSpaceMethodResultEntry.java   Nov 14, 2006
 *
 * Copyright 2006 GigaSpaces Technologies Inc.
 */

package org.springmodules.javaspaces.gigaspaces.remote;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.springmodules.javaspaces.entry.MethodResultEntry;

/**
 * Description:
 * <p>
 * This class extends MethodResultEntry
 * representing the gigaspaces result of a method call.
 * This will include the result (if successful) or the
 * Throwable in the event of failure.
 * </p>
 * Copyright:    Copyright 2006 GigaSpaces Technologies. All rights reserved.
 * Company:      Gigaspaces Technologies
 * @author       Lior Ben Yizhak
 * @version      5.0
 */
public class GigaSpacesMethodResultEntry extends MethodResultEntry
{


	 /**
    * Constructor
    * @param method the method to be invoke
    * @param uid the id of the RMI
    * @param result the result of the invocation method
    */
	public GigaSpacesMethodResultEntry(Method method, Serializable uid, Object result) {
		super(method,uid, result);
	}

	/**
	 * Constructor
	 * @param t the exception if the invocation failed
	 * @param method the method
	 * @param uid the id of the invocation
	 */
   public GigaSpacesMethodResultEntry(Throwable t, Method method, Serializable uid) {
       super(t,method, uid);
   }

   /**
    * Empty constructor
    *
    */
	public GigaSpacesMethodResultEntry() {
		super((Method) null, null, null);
	}
	/**
	 * Make an index for hash-based load balancing.
	 * @return the cache id
	 */
	public static String[] __getSpaceIndexedFields() {
        String[] indexedFields = {GigaSpacesRunnableMethodCallEntry.UID};
        return indexedFields;
    }

}
