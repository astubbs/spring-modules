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
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.validation.Errors;

/**
 * Wrapper for the validator validator to be used with the spring framework.
 * <p/>
 * <p>Use this class to validate any generic bean that does not have a known
 * class name. The bean (class) name can be configured in the Application Context.
 * Such a configuration has the advantage of automatic validation by the Spring
 * framework (e.g. in MVC controllers), but has the downside of needing a
 * separate NamedBeanValidator for each different type of been to be validated.</p>
 * <p/>
 * <p>With the validate(beanName, obj, errors) method, a single NamedBeanValidator
 * can be used to validate multiple beans of different types at runtime. The
 * downside to this approach is that the bean name must be specified when the
 * validate() method is invoked. Consequently, the Spring framework will not be
 * able to perform automatic validation (e.g. in MVC controllers) because the
 * convenience method does not comply with the
 * org.springframework.commons.Validator interface.</p>
 * <p/>
 * <p>The following properties must be initialized before the supports() or
 * validate() methods may be invoked (for zero-argument construction only):</p>
 * <ul>
 * <li>validatorFactory</li>
 * <li>beanName (only necessary for automatic validation by Spring e.g. in MVC
 * controllers)</li>
 * </ul>
 *
 * @author Daniel Miller
 */
public class NamedBeanValidator implements org.springframework.validation.Validator, Serializable {

	private Log log = LogFactory.getLog(NamedBeanValidator.class);

	private String beanName = null;

	private ValidatorAdaptor validator;

	public NamedBeanValidator() {
		validator = new ValidatorAdaptor();
	}

	public NamedBeanValidator(ValidatorFactory factory) {
		this();
		setValidatorFactory(factory);
	}

	public NamedBeanValidator(ValidatorFactory factory, String beanName) {
		this();
		setValidatorFactory(factory);
		setBeanName(beanName);
	}

	/**
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	public boolean supports(Class clazz) {
		Locale locale = Locale.getDefault();
		return validator.supports(beanName, locale);
	}

	/**
	 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
	public void validate(Object obj, Errors errors) {
		if (beanName != null) {
			validator.validate(beanName, obj, errors);
		}
		else {
			log.error("Cannot validate bean (" + obj.getClass()
					+ "). NamedBeanValidator bean name not set. "
					+ "Set the bean name in the validator configuration or "
					+ "explicitly call validate(beanName, obj, errors).");
		}
	}


	/**
	 * Convenience method to perform validation on any bean at runtime. This
	 * method allows a single NamedBeanValidator instance to validate multiple
	 * bean types. However, it must be invoked explicitly as it does not comply
	 * with the validate signature in the Validator interface.
	 *
	 * @param beanName String containing the name of the bean to validate. This
	 * value is used to locate rules with which to validate the bean.
	 * @param obj Object to validate.
	 * @param errors Errors instance to which validation errors will be added.
	 */
	public void validate(String beanName, Object obj, Errors errors) {
		validator.validate(beanName, obj, errors);
	}


	/* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 * Public accessors for the beanName.
	 */

	/**
	 * @return the beanName.
	 */
	public String getBeanName() {
		return beanName;
	}

	/**
	 * @param beanName The beanName to set.
	 */
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	/**
	 * Sets the validator factory for this validator.
	 *
	 * @param factory an initialized instance of ValidatorFactory.
	 */
	public void setValidatorFactory(ValidatorFactory factory) {
		validator.setValidatorFactory(factory);
	}

}
