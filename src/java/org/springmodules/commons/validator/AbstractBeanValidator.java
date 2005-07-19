
package org.springmodules.commons.validator;
/*
 * Copyright 2002-2005 the original author or authors.
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

import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.ValidatorException;

import org.springframework.validation.Validator;
import org.springframework.validation.Errors;

/**
 * @author Rob Harrop
 */
public abstract class AbstractBeanValidator implements Validator {

	private ValidatorFactory validatorFactory;
	
	private static final Log log = LogFactory.getLog(AbstractBeanValidator.class);

	/**
	 * Checks if the validatorFactory is configured to handle this class.  Will
	 * convert the class into a form name, suitable for commons validator.
	 * 
	 * @see #getFormName(Class)
	 * @return <code>true</code> if the validatorFactory supports the class,
	 * or <code>false</code> if not
	 */
	public boolean supports(Class clazz) {
		boolean canSupport = validatorFactory.hasRulesForBean(
				getFormName(clazz), getLocale());
		if (log.isDebugEnabled()) {
			log.debug("validatorFactory " + (canSupport ? "does" : "does not") 
					+ " support class " + clazz + " with form name " + getFormName(clazz));
		}
		return canSupport;
	}

	/**
	 * Validates the supplied object using a <code>org.apache.commons.validator.Validator</code> loaded from
	 * the configured <code>ValidatorFactory</code>.
	 *
	 * @see ValidatorFactory
	 */
	public void validate(Object obj, Errors errors) {
		org.apache.commons.validator.Validator commonsValidator = getValidator(obj, errors);
		try {
			commonsValidator.validate();
		}
		catch (ValidatorException e) {
			log.error("Exception while validating object " + obj, e);
		}
	}

	public void setValidatorFactory(ValidatorFactory validatorFactory) {
		this.validatorFactory = validatorFactory;
	}

	/**
	 * Gets the <code>Locale</code> used to lookup <code>Validator</code> instances. Default implementation
	 * returns the value of <code>Locale.getDefault()</code>. Subclasses can override this to change
	 * <code>Locale</code>-handling behavior.
	 */
	protected Locale getLocale() {
		return Locale.getDefault();
	}

	/**
	 * Retrieves an instance of <code>org.apache.commons.validator.Validator</code>
	 * for the specified <code>Object</code>
	 * from the configured <code>ValidatorFactory</code>.
	 *
	 * @param obj the <code>Object</code> being validated.
	 * @param errors the <code>Errors</code> object to write validation errors to.
	 */
	private org.apache.commons.validator.Validator getValidator(Object obj, Errors errors) {
		return this.validatorFactory.getValidator(getFormName(obj.getClass()), obj, errors);
	}

	/**
	 * Returns the name of the Commons Validator <code>Form</code> used to
	 * validate instances of the supplied class.
	 *
	 * @param aClass
	 * @return the form name that Commons Validator can use to look up a form
	 */
	protected abstract String getFormName(Class aClass);
}
