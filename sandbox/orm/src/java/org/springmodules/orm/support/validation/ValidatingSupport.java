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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.util.ClassUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Validator;

/** 
 * <p>An instance that holds validators tied to specific keys. These keys are determined by the configuration rules that have to be implemented by classes that extend this class.
 * 
 * <p>{@link ValidatingSupport#validate(Object, String)} is a static method called by classes that want to execute the validators tied to a specific key.
 * 
 * <p>Known subclasses of this class are:
 * 
 * <p>To support scoped validation rules instances bind and unbind themselves to the current thread. When an instance is already
 * bound to the thread other instances will register and unregister themselves with the thread-bound instance within the boundries of their scope.
 * 
 * <p>When a least one validator fails to validate a domain object a org.springframework.validation.Errors containing the errors
 * must be converted to a RuntimeException. This conversion is pluggable by means of the org.springmodules.orm.support.validation.ErrorsToRuntimeExceptionTransformer
 * interface. 
 * 
 * @author Steven Devijver
 * @since Jun 16, 2005
 * @see org.springframework.validation.Errors
 * @see org.springframework.validation.Validator
 * @see org.springmodules.orm.support.validation.ErrorsToRuntimeExceptionTransformer
 * @see org.springmodules.orm.support.validation.PolevValidatingMethodInterceptor
 * @see org.springmodules.orm.hibernate3.support.PolevValidatingInterceptor
 * @see org.springmodules.orm.hibernate.support.PolevValidatingInterceptor
 */
public abstract class ValidatingSupport {

	protected static final String ALL = "all"; 
	
	private Map validators = null;
	private ErrorsToRuntimeExceptionTransformer transformer = new DefaultErrorsToRuntimeExceptionTransformer();
	private static ThreadLocal otherValidatingSupportList = new ThreadLocal();
	private static ThreadLocal validatingSupport = new ThreadLocal();
	
	public ValidatingSupport() {
		super();
	}
	
	
	
	protected void addValidator(String event, Class clazz, Validator validator) {
		Map validatorMap = null;
		String upperCaseEvent = event.toUpperCase();
		
		if (this.validators == null) {
			this.validators = new HashMap();
		}
		if (this.validators.containsKey(upperCaseEvent)) {
			validatorMap = ((Map)this.validators.get(upperCaseEvent));
		} else {
			validatorMap = new HashMap();
			this.validators.put(upperCaseEvent, validatorMap);
		}
		validatorMap.put(clazz, validator);
	}
	
	private Map getValidators(String event) {
		Map validatorMap = new HashMap();
		String upperCaseEvent = event.toUpperCase();
		if (this.validators.containsKey(upperCaseEvent)) {
			validatorMap.putAll(((Map)this.validators.get(upperCaseEvent)));
		}
		if (this.validators.containsKey(ALL)) {
			validatorMap.putAll(((Map)this.validators.get(ALL)));
		}
		return validatorMap;
	}
	
	protected Class loadClass(String className) {
		try {
			return ClassUtils.forName(className);
		} catch (ClassNotFoundException e) {
			throw new BeansException("Could not find class [" + className + "]", e) {};
		}
	}
	
	private void doValidation(Object target, String event) {
		BindException errors = new BindException(target, "target");
		doValidation(target, event, errors);
		
		for (Iterator iter = getOtherValidationSupportList().iterator(); iter.hasNext();) {
			ValidatingSupport otherValidatingSupport = (ValidatingSupport)iter.next();
			otherValidatingSupport.doValidation(target, event, errors);
		}
		
		if (errors.hasErrors() || errors.hasGlobalErrors()) {
			throw transformer.transform(errors);
		}
	}
	
	private void doValidation(Object target, String event, BindException errors) {
		Map validatorsMap = getValidators(event);
		Class clazz = target.getClass();
		
		for (Iterator iter = validatorsMap.keySet().iterator(); iter.hasNext();) {
			Class tmpClazz = (Class)iter.next();
			Validator validator = (Validator)validatorsMap.get(tmpClazz);
			
			if (tmpClazz.isAssignableFrom(clazz)) {
				validator.validate(target, errors);
			}
		}		
	}
	
	public void setErrorsToRuntimeExceptionTransformer(ErrorsToRuntimeExceptionTransformer transformer) {
		if (transformer == null) {
			throw new IllegalArgumentException("Transformer should not be null!");
		}
		this.transformer = transformer;
	}
	
	protected void bindValidators() {
		if (validatingSupport.get() != null) {
			ValidatingSupport otherValidatingSupport = (ValidatingSupport)validatingSupport.get();
			otherValidatingSupport.push(this);
		} else {
			validatingSupport.set(this);
		}
	}
	
	protected void unbindValidators() {
		if (validatingSupport.get() != null) {
			ValidatingSupport otherValidatingSupport = (ValidatingSupport)validatingSupport.get();
			if (otherValidatingSupport.hasRegisteredChildren()) {
				otherValidatingSupport.pop();
			} else {
				validatingSupport.set(null);
				otherValidatingSupportList.set(null);
			}
		}
	}
	
	private List getOtherValidationSupportList() {
		if (otherValidatingSupportList.get() == null) {
			otherValidatingSupportList.set(new ArrayList());
		}
		return ((List)otherValidatingSupportList.get());
	}
	
	public void push(ValidatingSupport validatingSupport) {
		getOtherValidationSupportList().add(validatingSupport);
	}
	
	public void pop() {
		getOtherValidationSupportList().remove(getOtherValidationSupportList().size() - 1);
	}
	
	public boolean hasRegisteredChildren() {
		return !getOtherValidationSupportList().isEmpty();
	}
	
	public static void validate(Object target, String event) {
		if (validatingSupport.get() != null) {
			ValidatingSupport tmpValidatingSupport = ((ValidatingSupport)validatingSupport.get());
			tmpValidatingSupport.doValidation(target, event);
		} else {
			throw new IllegalStateException("No validators bound to current thread!");
		}
	}
	
}
