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

import java.util.Iterator;
import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.BeansException;
import org.springframework.util.StringUtils;
import org.springframework.validation.Validator;
import org.springmodules.orm.hibernate3.support.PolevValidatingInterceptor;

/** 
 * <p>This method interceptor supports the validation of domain objects before or after ORM life-cycle events, also
 * called the persistent object life-cycle event validation (POLEV) pattern.
 * 
 * <p>Most ORM products have hooks to receive notification of well defined life-cycle events for persistent and transient
 * domain objects. This class supports declarative configuration of rules to configure the validation of domain objects
 * for certain life-cycle events. Which life-cycle events are supported is determined by vendor specific notification implementations. 
 * 
 * <p>To configure validation rules a map must be provided with rules as keys and validator instances as values.
 * 
 * @author Steven Devijver
 * @since Jun 17, 2005
 * @see org.springmodules.orm.support.validation.ValidatingSupport
 * @see org.springmodules.orm.hibernate.support.PolevValidatingInterceptor
 * @see org.springmodules.orm.hibernate3.support.PolevValidatingInterceptor
 */
public class PolevValidatingMethodInterceptor extends ValidatingSupport implements
		MethodInterceptor {


	
	public PolevValidatingMethodInterceptor() {
		super();
	}

	/**
	 * <p>The [validators] property takes a map containing rules and
	 * validator instances.
	 * 
	 * <p>The rule notation allows you to specify class names and optionally the life-cycle events
	 * they should be used for. Consult vendor specific documentation for a list of supported life-cycle
	 * events for your ORM product. 
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
	 * @see PolevValidatingInterceptor
	 * @see org.springmodules.orm.hibernate.support.PolevValidatingInterceptor
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
