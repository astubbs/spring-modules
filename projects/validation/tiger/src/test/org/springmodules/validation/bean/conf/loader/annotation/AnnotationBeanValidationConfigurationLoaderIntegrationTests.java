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
import org.springmodules.validation.bean.BeanValidator;

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

}
