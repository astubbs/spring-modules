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

import org.springframework.validation.Errors;

/**
 * <p>Default transformer converts org.springframework.validation.Errors instances to 
 * org.apache.commons.attributes.validation.ValidationException instances.
 * 
 * @author Steven Devijver
 * @since Jun 19, 2005
 * @see org.springframework.validation.Errors
 * @see org.apache.commons.attributes.validation.ValidationException
 */
public class DefaultErrorsToRuntimeExceptionTransformer implements
		ErrorsToRuntimeExceptionTransformer {

	public DefaultErrorsToRuntimeExceptionTransformer() {
		super();
	}

	public RuntimeException transform(Errors errors) {
		return new ValidationException(errors);
	}

}
