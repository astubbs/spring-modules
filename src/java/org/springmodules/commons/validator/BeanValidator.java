/*
 * Copyright 2002-2004 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.springmodules.commons.validator;

import java.io.Serializable;

import org.springframework.validation.Errors;

/**
 * Wrapper for the validator validator to be used with the spring framework.
 * <p/>
 * <p>Use this class to validate beans with a known base class name. The name of
 * the object's base class is used to resolve validation rules for the bean
 * being validated.</p>
 * <p/>
 * <p>The following properties must be initialized (only if using zero-argument
 * construction) before the supports() or validate() methods may be invoked:</p>
 * <ul>
 * <li>validatorFactory</li>
 * </ul>
 * <p/>
 * <p>The following is an optional setting.</p>
 * <ul>
 * <li>useFullyQualifiedBeanName (defaults to false) allows specification of
 * fully qualified bean (form) names in the validation.xml file.</li>
 * </ul>
 *
 * @author Daniel Miller
 */
public class BeanValidator implements org.springframework.validation.Validator, Serializable {

	private boolean useFullyQualifiedBeanName = false;

	private ValidatorAdaptor validator;

	public BeanValidator() {
		validator = new ValidatorAdaptor();
	}

	public BeanValidator(ValidatorFactory factory) {
		this();
		setValidatorFactory(factory);
	}


	/**
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	public boolean supports(Class clazz) {
		return validator.supports(getBeanName(clazz));
	}

	/**
	 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
	public void validate(Object obj, Errors errors) {
		validator.validate(getBeanName(obj.getClass()), obj, errors);
	}


	/**
	 * Sets the useFullyQualifiedBeanName. Note: this setting is not mandatory.
	 * It defaults to false.
	 *
	 * @param useFullyQualifiedBeanName The useFullyQualifiedBeanName to set
	 */
	public void setUseFullyQualifiedBeanName(boolean useFullyQualifiedBeanName) {
		this.useFullyQualifiedBeanName = useFullyQualifiedBeanName;
	}

	/**
	 * Sets the validator factory for this validator.
	 *
	 * @param factory an initialized instance of ValidatorFactory.
	 */
	public void setValidatorFactory(ValidatorFactory factory) {
		validator.setValidatorFactory(factory);
	}


	/**
	 * If <code>useFullyQualifiedBeanName</code> is false (default value),
	 * this function returns a string containing a short name for the given
	 * class (e.g. myBean for the class com.domain.test.MyBean). Otherwise, it
	 * returns the value returned by Class.getName().
	 *
	 * @param clazz Class of the bean to be validated.
	 * @return String containing the bean name.
	 */
	protected String getBeanName(Class clazz) {
		String name = clazz.getName();
		if (useFullyQualifiedBeanName) {
			return name;
		}
		else {
			int afterDot = name.lastIndexOf(".") + 1;
			String firstChar = name.substring(afterDot, afterDot + 1).toLowerCase();
			String otherChars = "";
			if (afterDot + 1 <= name.length()) {
				// Get the rest of the bean name
				otherChars = name.substring(afterDot + 1);
			}
			return firstChar + otherChars;
		}
	}

}
