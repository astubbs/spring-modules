/*
 * Copyright 2002-2005 the original author or authors.
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

package org.springmodules.resource.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springmodules.resource.ResourceStatus;

/**
 * @author Thierry Templier
 */
public class ResourceInterceptor extends ResourceAspectSupport implements MethodInterceptor {

	private ResourceStatus doOpen() {
		System.err.println("--- interceptor doOpen ---");
		return resourceManager.open();
	}

	public void doClose(ResourceStatus status) {
		System.err.println("--- interceptor doClose ---");
		resourceManager.close(status);
	}

	private void doOnThrowable(Throwable ex) {
	}
	
	public Object invoke(MethodInvocation invocation) throws Throwable {
		// Work out the target class: may be null.
		// The TransactionAttributeSource should be passed the target class
		// as well as the method, which may be from an interface
		Class targetClass = (invocation.getThis() != null) ? invocation.getThis().getClass() : null;
		
		ResourceStatus status=doOpen();
	
		Object retVal = null;
		try {
			// This is an around advice.
			// Invoke the next interceptor in the chain.
			// This will normally result in a target object being invoked.
			retVal = invocation.proceed();
		}
		catch (Throwable ex) {
			// target invocation exception
			doOnThrowable(ex);
			throw ex;
		}
		finally {
			doClose(status);
		}
		return retVal;
	}

}
