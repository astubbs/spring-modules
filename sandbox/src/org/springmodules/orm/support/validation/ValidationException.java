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

import org.springframework.core.NestedRuntimeException;
import org.springframework.validation.Errors;

/**
 * <p>Runtime exception that holds a org.springframework.validation.Errors instance.
 * 
 * @author Steven Devijver
 * @since Jun 19, 2005
 * @see org.springframework.validation.Errors
 */
public class ValidationException extends NestedRuntimeException {

	
	private Errors errors = null;
	
	public ValidationException(Errors errors) {
		super("A validation error has occured");
		setErrors(errors);
	}

	/**
	 * <p>Returns the Errors instance that is the cause for this exception. The Errors instance should contain errors.
	 * 
	 * @return the Errors instance that is the cause for this exception.
	 */
	public Errors getErrors() {
		return this.errors;
	}
	
	private void setErrors(Errors errors) {
		if (errors == null) {
			throw new IllegalArgumentException("Errors parameters must not be null!");
		}
		this.errors = errors;
	}
}
