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

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.validation.BindException;
import org.springframework.validation.Validator;
import org.springmodules.validation.bean.conf.DefaultBeanValidationConfiguration;
import org.springmodules.validation.bean.conf.loader.SimpleBeanValidationConfigurationLoader;
import org.springmodules.validation.bean.conf.xml.ClassValidationElementHandler;
import org.springmodules.validation.bean.conf.xml.DefaultValidationRuleElementHandlerRegistry;
import org.springmodules.validation.bean.conf.xml.DefaultXmlBeanValidationConfigurationLoader;
import org.springmodules.validation.bean.conf.xml.PropertyValidationElementHandler;
import org.springmodules.validation.bean.conf.xml.handler.ValangClassValidationElementHandler;
import org.springmodules.validation.bean.conf.xml.handler.ValangPropertyValidationElementHandler;
import org.springmodules.validation.bean.converter.DefaultErrorCodeConverter;
import org.springmodules.validation.bean.rule.DefaultValidationRule;
import org.springmodules.validation.util.condition.Conditions;
import org.springmodules.validation.util.condition.parser.valang.ValangCondition;
import org.springmodules.validation.valang.functions.UpperCaseFunction;

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

        BeanValidator validator = new BeanValidator();
        validator.setErrorCodeConverter(new DefaultErrorCodeConverter());
        validator.setConfigurationLoader(loader);

        Person person = new Person("Uri");
        BindException errors = new BindException(person, "person");

        validator.validate(person, errors);

        assertTrue(errors.hasFieldErrors("name"));
        assertTrue(errors.hasGlobalErrors());
        assertEquals(1, errors.getFieldErrorCount("name"));
        assertEquals(1, errors.getGlobalErrorCount());
        assertEquals("Person[minLength]", errors.getGlobalError().getCode());
        assertEquals("Person.name[minLength]", errors.getFieldError("name").getCode());

    }

    public void testBeanValidator_WithCustomValangFunctions() throws Exception {

        Map functionsByName = new HashMap();
        functionsByName.put("tupper", UpperCaseFunction.class.getName());
        ValangCondition goodCondition = new ValangCondition("tupper(name) == 'URI'", functionsByName, null);
        ValangCondition badCondition = new ValangCondition("tupper(name) == 'Uri'", functionsByName, null);

        // creating the validation configuration for the bean.
        DefaultBeanValidationConfiguration personValidationConfiguration = new DefaultBeanValidationConfiguration();
        personValidationConfiguration.addGlobalRule(new DefaultValidationRule(goodCondition, "good"));
        personValidationConfiguration.addGlobalRule(new DefaultValidationRule(badCondition, "bad"));
        SimpleBeanValidationConfigurationLoader loader = new SimpleBeanValidationConfigurationLoader();
        loader.setClassValidation(Person.class, personValidationConfiguration);

        BeanValidator validator = new BeanValidator(loader);

        validator.setErrorCodeConverter(new DefaultErrorCodeConverter());
        validator.setConfigurationLoader(loader);

        Person person = new Person("Uri");
        BindException errors = new BindException(person, "person");

        validator.validate(person, errors);

        assertTrue(errors.hasGlobalErrors());
        assertEquals(1, errors.getGlobalErrorCount());
        assertEquals("Person[bad]", errors.getGlobalError().getCode());
    }

    public void testBeanValidator_WithCustomValangFunctions2() throws Exception {

        Map functionsByName = new HashMap();
        functionsByName.put("tupper", UpperCaseFunction.class.getName());
        ValangPropertyValidationElementHandler propertyHandler = new ValangPropertyValidationElementHandler();
        propertyHandler.setCustomFunctions(functionsByName);
        ValangClassValidationElementHandler classHandler = new ValangClassValidationElementHandler();
        classHandler.setCustomFunctions(functionsByName);

        DefaultValidationRuleElementHandlerRegistry registry = new DefaultValidationRuleElementHandlerRegistry();
        registry.setExtraPropertyHandlers(new PropertyValidationElementHandler[] { propertyHandler });
        registry.setExtraClassHandlers(new ClassValidationElementHandler[] { classHandler });
        registry.afterPropertiesSet();

        DefaultXmlBeanValidationConfigurationLoader loader = new DefaultXmlBeanValidationConfigurationLoader();
        loader.setResource(new ClassPathResource("validation.xml", getClass()));
        loader.setElementHandlerRegistry(registry);
        loader.afterPropertiesSet();

        BeanValidator validator = new BeanValidator(loader);
        validator.setErrorCodeConverter(new DefaultErrorCodeConverter());

        Person person = new Person("Uri");
        person.setHomeless(false);
        person.setAddress(new Address(null, "Amsterdam"));
        BindException errors = new BindException(person, "person");

        validator.validate(person, errors);

        assertTrue(errors.hasGlobalErrors());
        assertEquals(1, errors.getGlobalErrorCount());
        assertEquals(1, errors.getFieldErrorCount());
        assertEquals("Person[bad]", errors.getGlobalError().getCode());
        assertEquals("Address.street[not.null]", errors.getFieldError("address.street").getCode());
    }

    public void testBeanValidator_WithApplicationContext() throws Exception {
        ClassPathXmlApplicationContext context =
            new ClassPathXmlApplicationContext("org/springmodules/validation/bean/beanValidator-tests.xml");

        Person person = new Person("Uri");
        BindException errors = new BindException(person, "person");

        Validator validator = (Validator)context.getBean("validator");
        validator.validate(person, errors);

        assertTrue(errors.hasGlobalErrors());
        assertEquals(1, errors.getGlobalErrorCount());
        assertEquals("Person[bad]", errors.getGlobalError().getCode());

    }

}
