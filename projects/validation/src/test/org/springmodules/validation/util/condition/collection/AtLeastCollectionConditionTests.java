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

package org.springmodules.validation.util.condition.collection;

import java.util.Collection;

import org.springmodules.validation.util.condition.AbstractCondition;

/**
 * Tests for {@link AtLeastCollectionCondition}.
 *
 * @author Uri Boness
 */
public class AtLeastCollectionConditionTests extends AbstractCollectionElementConditionTests {

    protected AbstractCollectionCondition createCondition() {
        return new AtLeastCollectionCondition(new StringCondition(), 2);
    }

    public void testConstructor_WithNullElementCondition() throws Exception {
        try {
            new AtLeastCollectionCondition(null, 3);
            fail("An IllegalArgumentException must be thrown if given element condition is null");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    public void testConstructor_WithNegativeCount() throws Exception {
        try {
            new AtLeastCollectionCondition(new StringCondition(), -2);
            fail("An IllegalArgumentException must be thrown if given count is negative");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    public void testCheck_WhenCountIsGreaterThenCollectionSize() throws Exception {
        Collection collection = new FluentList().addObject("this");
        assertFalse(condition.check(collection));
    }

    public void testCheck_WhenCountIsGreaterThenArraySize() throws Exception {
        Object[] array = new Object[]{"this"};
        assertFalse(condition.check(array));
    }

    public void testCheck_WhenMoreArrayElementsPass() throws Exception {
        Object[] array = new Object[]{"this", "is", "a", new Integer(3)};
        assertTrue(condition.check(array));
    }

    public void testCheck_WhenLessCollectionElementsPass() throws Exception {
        Collection collection = new FluentList().addObject("this").addObject(new Integer(5)).addObject(new Integer(4)).addObject(new Integer(3));
        assertFalse(condition.check(collection));
    }

    public void testCheck_WhenLessArrayElementsPass() throws Exception {
        Object[] array = new Object[]{"this", new Integer(5), new Integer(4), new Integer(3)};
        assertFalse(condition.check(array));
    }

    public void testCheck_WhenTheExactCollectionElementsPass() throws Exception {
        Collection collection = new FluentList().addObject("test1").addObject("test2").addObject(new Integer(4));
        assertTrue(condition.check(collection));
    }

    public void testCheck_WhenNoneArrayElementsPass() throws Exception {
        Object[] array = new Object[]{"test1", "test2", new Integer(4)};
        assertTrue(condition.check(array));
    }

    //=============================================== Helper Classes ===================================================

    private static class StringCondition extends AbstractCondition {

        public boolean doCheck(Object object) {
            return (object instanceof String);
        }
    }

}
