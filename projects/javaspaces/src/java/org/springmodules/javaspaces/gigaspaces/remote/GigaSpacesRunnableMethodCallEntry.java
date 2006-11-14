/*
* Copyright 2006 GigaSpaces, Inc.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.springmodules.javaspaces.gigaspaces.remote;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.springmodules.javaspaces.entry.MethodResultEntry;
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
        String[] indexedFields = {UID};
        return indexedFields;
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
		return new GigaSpacesMethodResultEntry(method, uid, resultObject);
	}
	
	
	protected static String METHOD_STRING ="methodString";
	protected static String UID ="uid";

	
}
