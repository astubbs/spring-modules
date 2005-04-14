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
import org.apache.commons.validator.Validator;
import org.apache.commons.validator.ValidatorException;
import org.springframework.validation.Errors;

/**
 * Adaptor for the validator-validator. Provides xml-configured commons
 * services for Spring.
 * 
 * <p>The following properties must be initialized before the supports() or
 * validate() methods may be invoked:</p>
 * <ul>
 * <li>validatorFactory</li>
 * </ul>
 * 
 * @author Daniel Miller
 */
public class ValidatorAdaptor implements Serializable {
	
	private static Log log = LogFactory.getLog(ValidatorAdaptor.class);
	
	private ValidatorFactory factory;

	public ValidatorAdaptor() {
	}

	/* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 * Public commons methods
	 */

	/**
	 * TODO implement better locale resolution on this method. Ideally, it would
	 * use the locale of the current request.
	 * 
	 * @return true if the validator has a form resource that matches
	 * the given bean name. Otherwise, false.
	 */
	public boolean supports(String beanName) {
		Locale locale = Locale.getDefault();
		return supports(beanName, locale);
	}

	/**
	 * @return true if the validator has a form resource that matches
	 * the given bean name. Otherwise, false.
	 */
	public boolean supports(String beanName, Locale locale) {
		return factory.hasRulesForBean(beanName, locale);
	}

	/**
	 * @param beanName String containing the name of the bean to validate. This
	 * 		value is used to locate rules with which to validate the bean.
	 * @param obj Object to validate.
	 * @param errors Errors instance to which commons errors will be added.
	 */
	public void validate(String beanName, Object obj, Errors errors) {
		Validator validator = factory.getValidator(beanName, obj, errors);
		try {
			validator.validate();
		} catch (ValidatorException e) {
			log.error("An exception was thrown while validating bean: " + beanName, e);
		}
	}
	
	/* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 * The following properties must be initialized before the supports() or
	 * validate() methods may be invoked.
	 */

	/**
	 * Sets the factory.
	 * @param factory The factory to set
	 */
	public void setValidatorFactory(ValidatorFactory factory) {
		this.factory = factory;
	}

}
