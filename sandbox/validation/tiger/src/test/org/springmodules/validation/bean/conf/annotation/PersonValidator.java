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

package org.springmodules.validation.bean.conf.annotation;

import org.springframework.validation.Validator;
import org.springframework.validation.Errors;

/**
 *
 *
 * @author Uri Boness
 */
public class PersonValidator implements Validator {

    /**
     * Return whether or not this object can validate objects
     * of the given class.
     */
    public boolean supports(Class clazz) {
        return Person.class.isAssignableFrom(clazz);
    }

    /**
     * Validate an object, which must be of a class for which
     * the supports() method returned true.
     *
     * @param obj    Populated object to validate
     * @param errors Errors object we're building. May contain
     *               errors for this field relating to types.
     */
    public void validate(Object obj, Errors errors) {
        Person person = (Person)obj;
        if (person.getFirstName() == null || person.getNickname() == null) {
            return;
        }
        if (person.getFirstName().equals(person.getNickname())) {
            errors.reject("firstName.equals.nickname");
        }
    }

}
