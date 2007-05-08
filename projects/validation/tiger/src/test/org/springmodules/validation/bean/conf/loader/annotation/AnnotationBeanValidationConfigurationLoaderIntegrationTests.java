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

package org.springmodules.validation.bean.conf.loader.annotation;

import java.util.ArrayList;
import java.util.Calendar;

import junit.framework.TestCase;
import org.springframework.validation.BindException;
import org.springframework.validation.Validator;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springmodules.validation.bean.BeanValidator;
import org.springmodules.validation.bean.context.ValidationContextHolder;
import org.springmodules.validation.bean.context.DefaultValidationContext;

/**
 * Integration tests for {@link AnnotationBeanValidationConfigurationLoader}.
 *
 * @author Uri Boness
 */
public class AnnotationBeanValidationConfigurationLoaderIntegrationTests extends TestCase {

    public void test() throws Exception {

        Person person = new Person();
        person.setFirstName("a"); // invalid - must be at least 2 chars
        person.setLastName("qwertyuiopasdfghjklzx"); // invalid - too long (max 20 chars)
        person.setNickname("a"); // invalid - the PersonValidator should register a global error

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, -3);
        person.setBirthday(cal.getTime()); // valid - date in the past
        person.setAge(-1); // invalid - must be 0 or more
        person.setFather(null); // invalid - father cannot be null
        person.setMother(new Person()); // invalid - mother must be valid
        person.setFriends(new ArrayList<Person>()); // invalid - friends cannot be empty
        person.setHomeless(false);
        person.setAddress(new Address(null, "Amsterdam"));
        person.setNullableInteger(new Integer(3));

        AnnotationBeanValidationConfigurationLoader loader = new AnnotationBeanValidationConfigurationLoader();
        BeanValidator validator = new BeanValidator(loader);

        BindException errors = new BindException(person, "person");

        validator.validate(person, errors);

        assertTrue(errors.hasGlobalErrors());
        assertEquals(3, errors.getGlobalErrorCount());
        assertTrue(errors.hasFieldErrors());
        assertTrue(errors.hasFieldErrors("firstName"));
        assertTrue(errors.hasFieldErrors("lastName"));
        assertEquals(1, errors.getFieldErrorCount("lastName"));
        assertTrue(errors.hasFieldErrors("birthday"));
        assertEquals(1, errors.getFieldErrorCount("birthday"));
        assertEquals(1, errors.getFieldErrorCount("age"));
        assertEquals("Person.age[just.another.error.code]", errors.getFieldError("age").getCode());
        assertTrue(errors.hasFieldErrors("father"));
        assertTrue(errors.hasFieldErrors("mother.*"));
        assertTrue(errors.hasFieldErrors("friends"));
        assertTrue(errors.hasFieldErrors("address.street"));
        assertFalse(errors.hasFieldErrors("nullableString"));
        assertTrue(errors.hasFieldErrors("nullableInteger"));
        assertEquals("Address.street[not.null]", errors.getFieldError("address.street").getCode());

    }

    public void testWithContext() throws Exception {

        PersonWithContext person = new PersonWithContext();
        person.setFirstName("a"); // invalid (ctx1) - must be at least 2 chars
        person.setLastName("qwertyuiopasdfghjklzx"); // invalid (ctx1) - too long (max 20 chars)
        person.setNickname("a"); // invalid (ctx1) - the PersonValidator should register a global error

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, -3);
        person.setBirthday(cal.getTime()); // valid (ctx1) - date in the past
        person.setAge(-1); // invalid (ctx2) - must be 0 or more
        person.setFather(null); // invalid (ctx1) - father cannot be null
        person.setMother(new PersonWithContext()); // invalid (ctx2) - mother must be valid
        person.setFriends(new ArrayList<PersonWithContext>()); // invalid (ctx1) - friends cannot be empty
        person.setHomeless(false);
        person.setAddress(new Address(null, "Amsterdam")); // (all contexts)
        person.setNullableInteger(new Integer(3)); // invalid (ctx2) - min value is 10

        AnnotationBeanValidationConfigurationLoader loader = new AnnotationBeanValidationConfigurationLoader();
        BeanValidator validator = new BeanValidator(loader);

        BindException errors = new BindException(person, "person");

        setContext("ctx1");

        validator.validate(person, errors);

        assertTrue(errors.hasGlobalErrors());
        assertEquals(2, errors.getGlobalErrorCount());
        assertTrue(errors.hasFieldErrors());
        assertTrue(errors.hasFieldErrors("firstName"));
        assertTrue(errors.hasFieldErrors("lastName"));
        assertEquals(1, errors.getFieldErrorCount("lastName"));
        assertFalse(errors.hasFieldErrors("birthday"));
        assertEquals(0, errors.getFieldErrorCount("age"));
        assertTrue(errors.hasFieldErrors("father"));
        assertTrue(errors.hasFieldErrors("mother.*"));
        assertTrue(errors.hasFieldErrors("friends"));
        assertTrue(errors.hasFieldErrors("address.street"));
        assertFalse(errors.hasFieldErrors("nullableString"));
        assertFalse(errors.hasFieldErrors("nullableInteger"));
        assertEquals("Address.street[not.null]", errors.getFieldError("address.street").getCode());

        clearContext();

        setContext("ctx2");

        errors = new BindException(person, "person");
        validator.validate(person, errors);

        assertFalse(errors.hasGlobalErrors());
        assertTrue(errors.hasFieldErrors());
        assertFalse(errors.hasFieldErrors("firstName"));
        assertFalse(errors.hasFieldErrors("lastName"));
        assertEquals(0, errors.getFieldErrorCount("lastName"));
        assertTrue(errors.hasFieldErrors("birthday"));
        assertEquals(1, errors.getFieldErrorCount("birthday"));
        assertEquals(1, errors.getFieldErrorCount("age"));
        assertEquals("PersonWithContext.age[just.another.error.code]", errors.getFieldError("age").getCode());
        assertFalse(errors.hasFieldErrors("father"));
        assertTrue(errors.hasFieldErrors("mother.*"));
        assertFalse(errors.hasFieldErrors("friends"));
        assertTrue(errors.hasFieldErrors("address.street"));
        assertFalse(errors.hasFieldErrors("nullableString"));
        assertTrue(errors.hasFieldErrors("nullableInteger"));
        assertEquals("Address.street[not.null]", errors.getFieldError("address.street").getCode());

        clearContext();

    }

    public void testValidatorRef_WhenDeployedInApplicationContext() throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("appCtxt.xml", getClass());
        Validator validator = (Validator)context.getBean("validator");

        TestBean1 bean = new TestBean1();
        BindException errors = new BindException(bean, "bean");
        validator.validate(bean, errors);
        assertTrue(errors.hasErrors());
        assertTrue(errors.hasFieldErrors("name"));

        bean = new TestBean1("test");
        errors = new BindException(bean, "bean");
        validator.validate(bean, errors);
        assertFalse(errors.hasErrors());

    }

    public void testValidatorRef_WhenNotDeployedInApplicationContext() throws Exception {

        BeanValidator validator = new BeanValidator();
        validator.setConfigurationLoader(new AnnotationBeanValidationConfigurationLoader());

        TestBean1 bean = new TestBean1();
        BindException errors = new BindException(bean, "bean");

        try {
            validator.validate(bean, errors);
            fail("Expecting an UnsupportedOperationException for the validator was not deployed within an application context");
        } catch (UnsupportedOperationException uso) {
            // expected
        }
    }

    public void testConditionRef_WhenDeployedInApplicationContext() throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("appCtxt.xml", getClass());
        Validator validator = (Validator)context.getBean("validator");

        TestBean2 bean = new TestBean2();
        BindException errors = new BindException(bean, "bean");
        validator.validate(bean, errors);
        assertTrue(errors.hasErrors());
        assertTrue(errors.hasFieldErrors("name"));

        bean = new TestBean2("test");
        errors = new BindException(bean, "bean");
        validator.validate(bean, errors);
        assertFalse(errors.hasErrors());

    }

    public void testConditionRef_WhenNotDeployedInApplicationContext() throws Exception {

        BeanValidator validator = new BeanValidator();
        validator.setConfigurationLoader(new AnnotationBeanValidationConfigurationLoader());

        TestBean2 bean = new TestBean2();
        BindException errors = new BindException(bean, "bean");

        try {
            validator.validate(bean, errors);
            fail("Expecting an UnsupportedOperationException for the validator was not deployed within an application context");
        } catch (UnsupportedOperationException uso) {
            // expected
        }
    }
    
    protected void setContext(String context) {
        ValidationContextHolder.setValidationContext(new DefaultValidationContext(context));
    }

    protected void clearContext() {
        ValidationContextHolder.clearContext();
    }



}
