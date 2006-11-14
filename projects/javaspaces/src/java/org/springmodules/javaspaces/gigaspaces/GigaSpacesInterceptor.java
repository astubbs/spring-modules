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
package org.springmodules.javaspaces.gigaspaces;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.springframework.remoting.RemoteAccessException;
import org.springmodules.javaspaces.JavaSpaceInterceptor;
import org.springmodules.javaspaces.entry.AbstractMethodCallEntry;
import org.springmodules.javaspaces.entry.MethodResultEntry;
import org.springmodules.javaspaces.entry.RunnableMethodCallEntry;
import org.springmodules.javaspaces.entry.ServiceSeekingMethodCallEntry;

import org.springmodules.javaspaces.gigaspaces.remote.GigaSpacesMethodResultEntry;
import org.springmodules.javaspaces.gigaspaces.remote.GigaSpacesRunnableMethodCallEntry;
import org.springmodules.javaspaces.gigaspaces.remote.GigaSpacesServiceSeekingMethodCallEntry;

/**
 * <p>
 * Generic invoker for GigaSpaces -implemented methods. It allows method
 * invocations on a given proxy interface to be transparently implemented as
 * write/take operations of a generic entry to a JavaSpace. This is used as the
 * terminal interceptor for a Spring AOP proxy. See the Spring implementation of
 * local and remote EJB proxies, which illustrates the same concepts.
 * <p>
 * This class supports two modes of GigaSpaces usage:
 * <li>The endpoint hosts the service, and only the method to be invoked and
 * arguments are passed around the network. This is somewhat like a SLSB
 * approach to remoting, although there is no assumption as to where the
 * endpoint is.
 * <li>The endpoint does not necessarily host the service. The code to execute
 * as well as the method and arguments are shipped to the node that takes from
 * the space. This is a "runnable entry" approach. To enable this, simply set
 * the serializableTarget property on this object to an object that can be
 * serialized to implement the method. Typically this will be dependency
 * injected with Spring, allowing the object to be configured. Make sure this
 * object is serializable. Alternatively, override the serializableTarget()
 * method to return a custom object.
 *
 * This interceptor is generic. One interceptor instance can serve multiple
 * methods.
 *	</p>
 * Copyright:    Copyright 2006 GigaSpaces Technologies. All rights reserved.
 * Company:      Gigaspaces Technologies
 * @author       Lior Ben Yizhak
 * @version      5.0
 */
public class GigaSpacesInterceptor extends JavaSpaceInterceptor {

	/**
	 * Gets the ServiceSeekingMethodCallEntry
	 * @param method the method
	 * @param args the method agrs
	 * @return ServiceSeekingMethodCallEntry
	 */
	protected ServiceSeekingMethodCallEntry createServiceSeekingMethodCallEntry(Method method, Object[] args){
		Serializable uid = null;
		if(getUidFactory() != null){
			uid = getUidFactory().generateUid();
		}
		GigaSpacesServiceSeekingMethodCallEntry serviceSeekingMethodCallEntry = new GigaSpacesServiceSeekingMethodCallEntry(method, args,uid);
		return serviceSeekingMethodCallEntry;
	}

	/**
	 * Gets RunnableMethodCallEntry
	 * @param method the method
	 * @param args the method arg's
	 * @param target the target
	 * @return RunnableMethodCallEntry
	 */
	protected RunnableMethodCallEntry createRunnableMethodCallEntry(Method method, Object[] args, Object target){
		Serializable uid = null;
		if(getUidFactory() != null){
			uid = getUidFactory().generateUid();
		}
		GigaSpacesRunnableMethodCallEntry runnableMethodCallEntry = new GigaSpacesRunnableMethodCallEntry(method, args, target, uid);
		return runnableMethodCallEntry;
	}


	/**
	 * Create the specific method result entry
	 * @param call the call entry method
	 * @return the method result entry.
	 */
	protected MethodResultEntry createMethodResultEntry(AbstractMethodCallEntry call){
		return new GigaSpacesMethodResultEntry((Method) null, call.uid, null);
	}

}
