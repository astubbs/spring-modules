/*
 * Copyright 2002-2007 the original author or authors.
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

package org.springmodules.validation.bean.conf.loader.xml;

import java.util.List;

import junit.framework.TestCase;
import org.springframework.core.io.ClassPathResource;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springmodules.validation.bean.BeanValidator;
import org.springmodules.validation.bean.context.ValidationContextUtils;
import org.springmodules.validation.bean.conf.BeanValidationConfiguration;
import org.springmodules.validation.bean.conf.ResourceConfigurationLoadingException;
import org.springmodules.validation.util.cel.ognl.OgnlConditionExpressionParser;
import org.springmodules.validation.util.fel.parser.OgnlFunctionExpressionParser;
import org.apache.commons.lang.ArrayUtils;

/**
 * Tests for {@link org.springmodules.validation.bean.conf.loader.xml.DefaultXmlBeanValidationConfigurationLoader}.
 *
 * @author Uri Boness
 */
public class DefaultXmlBeanValidationConfigurationLoaderIntegrationTests extends TestCase {

    public void testLoadConfiguration() throws Exception {
        DefaultXmlBeanValidationConfigurationLoader loader = createLoader("Person.vld.xml");
        BeanValidationConfiguration config = loader.loadConfiguration(Person.class);
        assertEquals(2, config.getGlobalRules().length);

        Person person = new Person();
        person.setFirstName("Uri");
        person.setLastName("Boness");
        person.setAge(-1);
        person.setEmail("uri@b");
        person.setPassword("pa");
        person.setConfirmPassword("pa1");

        BindException errors = new BindException(person, "person");
        BeanValidator validator = new BeanValidator(loader);
        validator.validate(person, errors);

        assertEquals(2, errors.getGlobalErrorCount());
        assertEquals(1, errors.getFieldErrorCount("smallInteger"));
        assertEquals(1, errors.getFieldErrorCount("lastName"));
        assertEquals("Person.lastName[validateLastNameIsLongerThanTen()]", errors.getFieldError("lastName").getCode());

    }

    public void testValidateWithContext() throws Exception {
        DefaultXmlBeanValidationConfigurationLoader loader = createLoader("PersonWithContext.vld.xml");
        BeanValidationConfiguration config = loader.loadConfiguration(Person.class);
        assertEquals(2, config.getGlobalRules().length);

        BeanValidator validator = new BeanValidator(loader);

        Person person = new Person();
        person.setFirstName("Uri");
        person.setLastName("Boness");
        person.setAge(-1);
        person.setEmail("uri@b");
        person.setPassword("pa");
        person.setConfirmPassword("pa1");

        ValidationContextUtils.setContext("ctx1");

        BindException errors = new BindException(person, "person");
        validator.validate(person, errors);

        assertEquals(1, errors.getGlobalErrorCount());
        assertFalse(errors.hasFieldErrors("smallInteger"));
        assertEquals(1, errors.getFieldErrorCount("lastName"));

        ValidationContextUtils.clearContext();

        ValidationContextUtils.setContext("ctx2");

        errors = new BindException(person, "person");
        validator.validate(person, errors);

        assertEquals(1, errors.getGlobalErrorCount());
        assertEquals(1, errors.getFieldErrorCount("smallInteger"));
        assertEquals(1, errors.getFieldErrorCount("lastName"));
        assertEquals("Person.lastName[validateLastNameIsLongerThanTen()]", errors.getFieldError("lastName").getCode());

        ValidationContextUtils.clearContext();

    }

    public void testLoadDefaultConfiguration() throws Exception {
        DefaultXmlBeanValidationConfigurationLoader loader = new DefaultXmlBeanValidationConfigurationLoader();
        loader.afterPropertiesSet();
        BeanValidationConfiguration conf = loader.loadConfiguration(Person.class);
        assertNotNull(conf);
    }

    public void testValidationBean_WhenDeployedInApplicationContext() throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("appCtxt.xml", getClass());
        DefaultXmlBeanValidationConfigurationLoader loader = createLoader("TestBean1.vld.xml", context);

        BeanValidator validator = new BeanValidator(loader);
        
        TestBean bean = new TestBean();
        BindException errors = new BindException(bean, "bean");
        validator.validate(bean, errors);
        assertTrue(errors.hasErrors());
        assertTrue(errors.hasFieldErrors("name"));

        bean = new TestBean("name");
        errors = new BindException(bean, "bean");
        validator.validate(bean, errors);
        assertFalse(errors.hasErrors());
    }

    public void testValidationBean_WhenNotDeployedInApplicationContext() throws Exception {
        try {
            createLoader("TestBean1.vld.xml");
            fail("Expecting an UnsupportedOperationException for the configuration loader is not deployed in " +
                "an application context");
        } catch (ResourceConfigurationLoadingException rcle) {
            // expected
        }
    }

    public void testConditionBean_WhenDeployedInApplicationContext() throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("appCtxt.xml", getClass());
        DefaultXmlBeanValidationConfigurationLoader loader = createLoader("TestBean2.vld.xml", context);

        BeanValidator validator = new BeanValidator(loader);

        TestBean bean = new TestBean();
        BindException errors = new BindException(bean, "bean");
        validator.validate(bean, errors);
        assertTrue(errors.hasErrors());
        assertTrue(errors.hasFieldErrors("name"));
        assertEquals("TestBean1.name[test.bean.condition]", errors.getFieldError("name").getCode());

        bean = new TestBean("name");
        errors = new BindException(bean, "bean");
        validator.validate(bean, errors);
        assertFalse(errors.hasErrors());
    }

    public void testConditionBean_WhenNotDeployedInApplicationContext() throws Exception {
        try {
            createLoader("TestBean2.vld.xml");
            fail("Expecting an UnsupportedOperationException for the configuration loader is not deployed in " +
                "an application context");
        } catch (ResourceConfigurationLoadingException rcle) {
            // expected
        }
    }

    public void testShortCircuiteValidationDisabled() throws Exception {
        DefaultXmlBeanValidationConfigurationLoader loader = createLoader("TestBean3.vld.xml");
        BeanValidator validator = new BeanValidator(loader);
        validator.setShortCircuitFieldValidation(false);

        TestBean bean = new TestBean();
        BindException errors = new BindException(bean, "bean");
        validator.validate(bean, errors);

        assertTrue(errors.hasErrors());
        assertTrue(errors.hasFieldErrors("name"));
        assertEquals(2, errors.getFieldErrorCount("name"));
        List fieldErrors = errors.getFieldErrors("name");
        assertTrue(ArrayUtils.contains(((FieldError)fieldErrors.get(0)).getCodes(), "TestBean.name[not.null]"));
        assertTrue(ArrayUtils.contains(((FieldError)fieldErrors.get(1)).getCodes(), "TestBean.name[not.null.2]"));
        
    }

    public void testShortCircuiteValidationEnabled() throws Exception {
        DefaultXmlBeanValidationConfigurationLoader loader = createLoader("TestBean3.vld.xml");
        BeanValidator validator = new BeanValidator(loader);

        TestBean bean = new TestBean();
        BindException errors = new BindException(bean, "bean");
        validator.validate(bean, errors);

        assertTrue(errors.hasErrors());
        assertTrue(errors.hasFieldErrors("name"));
        assertEquals(1, errors.getFieldErrorCount("name"));
        assertTrue(ArrayUtils.contains(errors.getFieldError("name").getCodes(), "TestBean.name[not.null]"));
    }

    protected DefaultXmlBeanValidationConfigurationLoader createLoader(String resource) throws Exception {
        return createLoader(resource, null);
    }

    protected DefaultXmlBeanValidationConfigurationLoader createLoader(String resource, ApplicationContext context) throws Exception {
        DefaultXmlBeanValidationConfigurationLoader loader = new DefaultXmlBeanValidationConfigurationLoader();
        loader.setResource(new ClassPathResource(resource, getClass()));
        loader.setConditionExpressionParser(new OgnlConditionExpressionParser());
        loader.setFunctionExpressionParser(new OgnlFunctionExpressionParser());
        loader.setApplicationContext(context);
        loader.afterPropertiesSet();
        return loader;
    }

}