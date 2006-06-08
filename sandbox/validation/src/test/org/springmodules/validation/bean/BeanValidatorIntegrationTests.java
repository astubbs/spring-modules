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

package org.springmodules.validation.bean;

import junit.framework.TestCase;
import org.springframework.validation.BindException;
import org.springmodules.validation.bean.conf.DefaultBeanValidationConfiguration;
import org.springmodules.validation.bean.conf.SimpleBeanValidationConfigurationLoader;
import org.springmodules.validation.bean.converter.DefaultErrorCodeConverter;
import org.springmodules.validation.bean.rule.DefaultValidationRule;
import org.springmodules.validation.util.condition.Conditions;

/**
 * Integration tests for the bean validator framework.
 *
 * @author Uri Boness
 */
public class BeanValidatorIntegrationTests extends TestCase {

    public void testBeanValidator_WithManualConfiguration() throws Exception {

        // creating the validation configuration for the bean.
        DefaultBeanValidationConfiguration personValidationConfiguration = new DefaultBeanValidationConfiguration();
        personValidationConfiguration.addGlobalRule(new DefaultValidationRule(Conditions.minLength("name", 4), "minLength"));
        personValidationConfiguration.addPropertyRule("name", new DefaultValidationRule(Conditions.minLength("name", 5), "minLength"));
        SimpleBeanValidationConfigurationLoader loader = new SimpleBeanValidationConfigurationLoader();
        loader.setClassValidation(Person.class, personValidationConfiguration);

        BeanValidator validator = new BeanValidator(Person.class);
        validator.setErrorCodeConverter(new DefaultErrorCodeConverter());
        validator.setConfigurationLoader(loader);

        Person person = new Person("Uri");
        BindException errors = new BindException(person, "person");

        validator.validate(person, errors);

        assertTrue(errors.hasFieldErrors("name"));
        assertTrue(errors.hasGlobalErrors());
        assertEquals(1, errors.getFieldErrorCount("name"));
        assertEquals(1, errors.getGlobalErrorCount());
        assertEquals("BeanValidatorIntegrationTests.Person[minLength]", errors.getGlobalError().getCode());
        assertEquals("BeanValidatorIntegrationTests.Person.name[minLength]", errors.getFieldError("name").getCode());

    }

    //================================================ Inner Classes ===================================================

    private static class Person {

        private String name;

        public Person() {
        }

        public Person(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}
