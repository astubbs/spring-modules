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
import org.springmodules.javaspaces.entry.ServiceSeekingMethodCallEntry;

/**
 * <p>
 * Description: This class extends ServiceSeekingMethodCallEntry
 * representing a method call that hits a remote service.
 * That is, the endpoint is expected to host the service; only the
 * method and arguments will be shipped, and not the implementing code.
 * </p>
 * Copyright:    Copyright 2006 GigaSpaces Technologies. All rights reserved.
 * Company:      Gigaspaces Technologies
 * @author       Lior Ben Yizhak
 * @version      5.0
 */
public class GigaSpacesServiceSeekingMethodCallEntry extends ServiceSeekingMethodCallEntry
{
	/**
	 * Constructor
	 * @param method the method
	 * @param args the method arguments
	 * @param uid the uid
	 */
	public GigaSpacesServiceSeekingMethodCallEntry(Method method, Object[] args, Serializable uid) {
		super(method, args);
		this.uid = uid;
	}

	/**
	 * Constructor
	 * @param method the method
	 * @param args the method arguments
	 */
	public GigaSpacesServiceSeekingMethodCallEntry(Method method, Object[] args) {
		super(method, args);
	}

	/**
	 * Empty Constructor
	 */
	public GigaSpacesServiceSeekingMethodCallEntry() {
		super();
	}

	/**
	 * Make an index for hash-based load balancing.
	 * @return the cache id
	 */
	public static String[] __getSpaceIndexedFields() {
        String[] indexedFields = {GigaSpacesRunnableMethodCallEntry.UID};
        return indexedFields;
    }

	/**
	 * Invoke the method using the given delegate (target).
	 *
	 * @see org.springmodules.javaspaces.entry.AbstractMethodCallEntry#doInvocation(java.lang.Object)
	 */
	protected MethodResultEntry doInvocation(Object delegate) throws InvocationTargetException, IllegalAccessException {
		Method method = getMethod();
		Object resultObject = method.invoke(delegate, getArguments());
		return new GigaSpacesMethodResultEntry(method, uid, resultObject);
	}

}
