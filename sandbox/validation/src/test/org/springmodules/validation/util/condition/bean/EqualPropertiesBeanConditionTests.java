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

package org.springmodules.validation.util.condition.bean;

import junit.framework.TestCase;
import org.springframework.beans.InvalidPropertyException;
import org.springmodules.validation.util.condition.Condition;

/**
 * Tests for {@link EqualPropertiesBeanCondition}.
 *
 * @author Uri Boness
 */
public class EqualPropertiesBeanConditionTests extends TestCase {

    public void testConstructor_WithNullPropertyNames() throws Exception {
        try {
            new EqualPropertiesBeanCondition(null);
            fail("An IllegalArgumentException must be thrown when a null array is passed in");
        } catch (IllegalArgumentException iae) {
            // expected
        }

        try {
            new EqualPropertiesBeanCondition(null, "test");
            fail("An IllegalArgumentException must be thrown when a null property name is passed in");
        } catch (IllegalArgumentException iae) {
            // expected
        }

        try {
            new EqualPropertiesBeanCondition("test", null);
            fail("An IllegalArgumentException must be thrown when a null property name is passed in");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    public void testCheck_Success() throws Exception {
        Condition condition = new EqualPropertiesBeanCondition("name", "nickname");
        assertTrue(condition.check(new Person("boness", "boness", 88)));
    }

    public void testCheck_SuccessWithArray() throws Exception {
        Condition condition = new EqualPropertiesBeanCondition(new String[] { "name", "nickname" });
        assertTrue(condition.check(new Person("boness", "boness", 88)));
    }

    public void testCheck_Failure() throws Exception {
        Condition condition = new EqualPropertiesBeanCondition("name", "nickname");
        assertFalse(condition.check(new Person("uri", "boness", 88)));
    }

    public void testCheck_FailureWithArray() throws Exception {
        Condition condition = new EqualPropertiesBeanCondition(new String[] { "name", "nickname" });
        assertFalse(condition.check(new Person("uri", "boness", 88)));
    }

    public void testCheck_WithMissingPropertyInBean() throws Exception {
        Condition condition = new EqualPropertiesBeanCondition(new String[] { "name", "missingProperty" });
        try {
            condition.check(new Person("uri", "boness", 88));
            fail("An InvalidPropertyException if the checked bean doesn't have one of the compared properties");
        } catch (InvalidPropertyException ipe) {
            // expected
        }
    }

    public void testCheck_SuccessWithNestedProperties() throws Exception {
        Condition condition = new EqualPropertiesBeanCondition(new String[] { "name", "address.street" });
        assertTrue(condition.check(new Person("uri", "boness", 88, new Address("uri"))));
    }

    public void testCheck_WithNullInNestedPropertyPath() throws Exception {
        Condition condition = new EqualPropertiesBeanCondition(new String[] { "name", "address.street" });
        try {
            condition.check(new Person("uri", "boness", 88)); // the address property here is null
            fail("An InvalidPropertyException if the checked bean holds null in the nested property path");
        } catch (InvalidPropertyException ipe) {
            // expected
        }
    }

}