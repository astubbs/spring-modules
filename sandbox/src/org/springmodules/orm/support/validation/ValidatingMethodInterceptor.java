/*
 * Copyright 2004-2005 the original author or authors.
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
package org.springmodules.orm.support.validation;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * <p>AOP interceptor for the persistent object life-cycle event validation (POLEV) pattern.
 * 
 * @author Steven Devijver
 * @since Jun 17, 2005
 * @see org.springmodules.orm.hibernate.support.ValidatingInterceptor
 * @see org.springmodules.orm.hibernate3.support.ValidatingInterceptor
 */
public class ValidatingMethodInterceptor extends ValidatingSupport implements
		MethodInterceptor {

	public ValidatingMethodInterceptor() {
		super();
	}

	/**
	 * <p>This method binds and unbinds the validator maps configured in the validators
	 * property.
	 * 
	 * @param methodInvocation the method invocation
	 * @return the result of the method invocation
	 */
	public Object invoke(MethodInvocation invocation) throws Throwable {
		
		try {
			bindValidators();
			
			return invocation.proceed();
		} finally {
			unbindValidators();
		}
		
	}

	
}
