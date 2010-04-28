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
import org.springmodules.validation.util.condition.AbstractCondition;
import org.springmodules.validation.util.condition.Condition;

/**
 * Tests for {@link PropertyBeanCondition}.
 *
 * @author Uri Boness
 */
public class PropertyBeanConditionTests extends TestCase {

    public void testConstructor_WithNullPropertyName() throws Exception {
        try {
            new PropertyBeanCondition(null, new StringCondition());
            fail("An IllegalArgumentException must be thrown if given property name is null");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    public void testConstructor_WithNullPropertyCondition() throws Exception {
        try {
            new PropertyBeanCondition("name", null);
            fail("An IllegalArgumentException must be thrown if given property condition is null");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    public void testCheck_Success() throws Exception {
        Condition condition = new PropertyBeanCondition("name", new StringCondition());
        assertTrue(condition.check(new Person("Uri", "Boness", 88)));
    }

    public void testCheck_SuccessWithNestedProperty() throws Exception {
        Condition condition = new PropertyBeanCondition("address.street", new StringCondition());
        assertTrue(condition.check(new Person("Uri", "Boness", 88, new Address("street"))));
    }

    public void testCheck_Failure() throws Exception {
        Condition condition = new PropertyBeanCondition("age", new StringCondition());
        assertFalse(condition.check(new Person("Uri", "Boness", 88)));
    }

    public void testCheck_WhenBeanMissingThePropoerty() throws Exception {
        Condition condition = new PropertyBeanCondition("missingProperty", new StringCondition());
        try {
            assertFalse(condition.check(new Person("Uri", "Boness", 88)));
            fail("An InvalidPropertyException must be thrown if the checked bean doesn't have the checked property");
        } catch (InvalidPropertyException ipe) {
            // expected
        }
    }

    public void testCheck_WhenBeanHasNullInNestedPath() throws Exception {
        Condition condition = new PropertyBeanCondition("address.street", new StringCondition());
        try {
            assertFalse(condition.check(new Person("Uri", "Boness", 88)));
            fail("An InvalidPropertyException must be thrown if the checked bean has a null value in the nested property path");
        } catch (InvalidPropertyException ipe) {
            // expected
        }
    }

    //=============================================== Helper Classes ===================================================

    private class StringCondition extends AbstractCondition {

        public boolean doCheck(Object object) {
            return (object instanceof String);
        }
    }

}