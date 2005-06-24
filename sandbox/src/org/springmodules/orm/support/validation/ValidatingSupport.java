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
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Validator;
import org.springmodules.orm.hibernate3.support.ValidatingInterceptor;

/**
 * <p>This class supports the validation on domain objects before or after ORM life-cycle events, also
 * called to persistent object life-cycle event validation (POLEV) pattern.
 * 
 * <p>Most ORM products have hooks to receive notification of well known life-cycle events for persistent and transient
 * domain objects. This class supports declarative configuration of rules to configure the validation of domain objects
 * for certain life-cycle events. Which life-cycle events are supported is determined by vendor specific notification implementations. 
 * 
 * <p>To configure validation rules a map must be provided with rules as keys and validator instances as values.
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
 * @see org.springmodules.orm.support.validation.ValidatingMethodInterceptor
 * @see org.springmodules.orm.hibernate3.support.ValidatingInterceptor
 * @see org.springmodules.orm.hibernate.support.ValidatingInterceptor
 */
public abstract class ValidatingSupport {

	private static final String ALL = "all"; 
	
	private Map validators = null;
	private ErrorsToRuntimeExceptionTransformer transformer = new DefaultErrorsToRuntimeExceptionTransformer();
	private static ThreadLocal otherValidatingSupportList = new ThreadLocal();
	private static ThreadLocal validatingSupport = new ThreadLocal();
	
	public ValidatingSupport() {
		super();
	}
	
	
	/**
	 * <p>The [validators] property takes a map containing rules and
	 * validator instances.
	 * 
	 * <p>The rule notation allows you to specify class names and optionally the life-cycle events
	 * they should be used for. Consult vendor specific documentation for a list of supported life-cycle
	 * events for you ORM product. 
	 * 
	 * <p>Some rule notation examples:
	 * 
	 * <ul>
	 * 		<li>foo.Bar - objects assignable to foo.Bar will be validated by the associated validator for all life-cycle events.
	 * 		<li>foo.Bar,onFlushDirty - same as above except only for onFlushDirty life-cycle event
	 * 		<li>foo.Bar,onFlushDirty,onSave - same as above except only for onFlushDirty and onSave life-cycle events
	 * </ul>
	 * 
	 * <p>A Spring configuration example:
	 * 
	 * <pre>
	 * 		&lt;property name="validators"&gt;
	 * 			&lt;map&gt;
	 * 				&lt;entry key="foo.Bar,onFlushDirty,onSave" ref="myValidator"/&gt;
	 * 				&lt;entry key="foo.Bar,onDelete" ref="myOtherValidator"/&gt;
	 * 			&lt;/map&gt;
	 * 		&lt;/property&gt;
	 * </pre>
	 * 
	 * @param validators map containing rules and validators.
	 * @see Validator
	 * @see ValidatingInterceptor
	 * @see org.springmodules.orm.hibernate.support.ValidatingInterceptor
	 */
	public final void setValidators(Map validators) {
		for (Iterator iter = validators.keySet().iterator(); iter.hasNext();) {
			Object key = iter.next();
			Object value = validators.get(key);
			Class clazz = null;
			Validator validator = null;
			
			if (value != null) {
				if (value instanceof Validator) {
					validator = (Validator)value;
				} else {
					throw new BeansException("Instance for class [" + clazz + "] is not of type org.springframework.validation.Validator!") {};
				}
			} else {
				throw new BeansException("Validator for class [" + clazz + "] is not specified!") {};
			}
			
			if (key instanceof Class) {
				clazz = (Class)key;
				addValidator(ALL, clazz, validator);
			} else if (key instanceof String) {
				String s = (String)key;
				String[] strings = StringUtils.tokenizeToStringArray(s, ",", true, true);
				
				if (strings.length > 0) {
					clazz = loadClass(strings[0]);
				}
				if (strings.length == 1) {
					addValidator(ALL, clazz, validator);
				} else {					
					for (int i = 1; i < strings.length; i++) {
						addValidator(strings[i], clazz, validator);
					}
				}
			}
 		}
	}
	
	private void addValidator(String event, Class clazz, Validator validator) {
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
	
	private static Class loadClass(String className) {
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
