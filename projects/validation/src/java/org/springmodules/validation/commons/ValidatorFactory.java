package org.springmodules.validation.commons;

import java.util.Locale;

import org.apache.commons.validator.Validator;
import org.apache.commons.validator.ValidatorResources;
import org.springframework.validation.Errors;

/**
 * @author Daniel Miller
 */
public interface ValidatorFactory {

    /**
     * Gets a new instance of a validator for the given bean (form).
     *
     * @param beanName The name of the bean for which this validator will be created.
     */
    public Validator getValidator(String beanName, Object bean, Errors errors);

    /**
     * Returns true if this validator factory can create a validator that
     * supports the given <code>beanName</code> and <code>locale</code>.
     *
     * @param beanName String name of the bean to be validated.
     * @param locale Locale of the validator to create.
     * @return true if this validator factory can create a validator for the
     *         given bean name.
     */
    public boolean hasRulesForBean(String beanName, Locale locale);

    /**
     * @return Returns the validatorResources.
     */
    public ValidatorResources getValidatorResources();
}
