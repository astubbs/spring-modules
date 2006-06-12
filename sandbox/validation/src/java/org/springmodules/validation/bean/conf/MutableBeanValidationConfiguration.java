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

package org.springmodules.validation.bean.conf;

import org.springframework.validation.Validator;
import org.springmodules.validation.bean.rule.ValidationRule;

/**
 * A default implementation of {@link BeanValidationConfiguration}.
 *
 * @author Uri Boness
 */
public interface MutableBeanValidationConfiguration extends BeanValidationConfiguration {

    /**
     * Sets the global rules for this configuration.
     *
     * @param globalRules The global rules for this configuration
     */
    public void setGlobalRules(ValidationRule[] globalRules);

    /**
     * Adds the given rule as global rules for this configuration.
     *
     * @param globalRule The rule to be added as global rules for this configuration.
     */
    public void addGlobalRule(ValidationRule globalRule);

    /**
     * Adds the given rules as global rules for this configuration.
     *
     * @param globalRules The rules to be added as global rules for this configuration.
     */
    public void addGlobalRules(ValidationRule[] globalRules);

    /**
     * Sets the property validation rules for the given property.
     *
     * @param propertyName The name of the property.
     * @param rules The validation rules for the given property.
     */
    public void setPropertyRules(String propertyName, ValidationRule[] rules);

    /**
     * Adds the given validation rule to the given property.
     *
     * @param propertyName The name of the property.
     * @param rule The validation rule to be added to the given property.
     */
    public void addPropertyRule(String propertyName, ValidationRule rule);

    /**
     * Adds the given validation rules to the given property.
     *
     * @param propertyName The name of the property.
     * @param extraRules The extra validation rules that will be added to the given property.
     */
    public void addPropertyRules(String propertyName, ValidationRule[] extraRules);

    /**
     * Sets the custom validator for this configuration.
     *
     * @param validator The custom validator for this configuration.
     */
    public void setCustomValidator(Validator validator);

    /**
     * Sets the custom validator for this configuration. The custom validator will be compound with the given
     * validators.
     *
     * @param validators The validators that will make the custom validator of this configuration.
     */
    public void setCustomValidators(Validator[] validators);

    /**
     * Adds the given validator to the custom validator of this configuration.
     *
     * @param validator The validator to be added to the custom validator of this configuration.
     */
    public void addCustomValidator(Validator validator);

    /**
     * Adds the given validators to the custom validator of this configuration.
     *
     * @param validators The validators to be added to the custom validator of this configuration.
     */
    public void addCustomValidators(Validator[] validators);

    /**
     * @see BeanValidationConfiguration#getRequiredValidatableProperties()
     */
    public String[] getRequiredValidatableProperties();

    /**
     * Sets the required validatable properties for this configuration.
     *
     * @param propertyNames The names of the required validatable properties.
     */
    public void setRequiredValidatableProperties(String[] propertyNames);

    /**
     * Adds the given property to the required validatable properties of this configuration.
     *
     * @param propertyName The name of the property to be added to the required validatable properties of
     *        this configuration.
     */
    public void addRequiredValidatableProperty(String propertyName);

    /**
     * Adds the given properties to the required validatable properties of this configuration.
     *
     * @param propertyNames The names of the properties to be added to the required validatable properties of
     *        this configuration.
     */
    public void addRequiredValidatableProperties(String[] propertyNames);

}
