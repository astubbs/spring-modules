/*
 * Copyright 2006 GigaSpaces Technologies. All rights reserved.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS," WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED INCLUDING BUT NOT LIMITED TO WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT. GIGASPACES WILL NOT BE LIABLE FOR ANY DAMAGE OR
 * LOSS IN CONNECTION WITH THE SOFTWARE.
 */

package org.springmodules.javaspaces.gigaspaces.remote;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.springmodules.javaspaces.entry.RunnableMethodCallEntry;

/**
 * Description:  
 * <p>
 * This class extends RunnableMethodCallEntry
 * representing a method call that includes the necessary code in a
 * target object. Moves code around the network as well as invocation.
 * </p>
 * Copyright:    Copyright 2006 GigaSpaces Technologies. All rights reserved.
 * Company:      Gigaspaces Technologies
 * @author       Lior Ben Yizhak
 * @version      5.0
 */
public class GigaSpacesRunnableMethodCallEntry extends RunnableMethodCallEntry{

	/**
	 * Constructor
	 * @param method The method
	 * @param args the method arguments
	 * @param target the target
	 * @param uid the uid
	 */
	public GigaSpacesRunnableMethodCallEntry(Method method, Object[] args, Object target, Serializable uid) {
		super(method, args,target);
		this.uid = uid;
	}

	/**
	 * Constructor
	 * @param method The method
	 * @param args the method arguments
	 * @param target the target
	 */
	public GigaSpacesRunnableMethodCallEntry(Method method, Object[] args, Object target) {
		super(method, args,target);
	}

	/**
	 * Empty Constructor
	 */
	public GigaSpacesRunnableMethodCallEntry() {
		super();
	}

	/**
	 * Make an index for hash-based load balancing.
	 * @return the cache id
	 */
	public static String[] __getSpaceIndexedFields() {
        String[] indexedFields = {METHOD_STRING};
        return indexedFields;
    }

	protected static String METHOD_STRING ="methodString";

}
