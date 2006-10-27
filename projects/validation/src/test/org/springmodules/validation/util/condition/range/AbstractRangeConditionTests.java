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

package org.springmodules.validation.util.condition.range;

import junit.framework.TestCase;

/**
 * A base class to all range conditions test classes.
 *
 * @author Uri Boness
 */
public abstract class AbstractRangeConditionTests extends TestCase {

    protected AbstractRangeCondition conditionWithComparable;

    protected AbstractRangeCondition conditionWithComparator;

    protected void setUp() throws Exception {
        conditionWithComparable = createRangeConditionWithComparable();
        conditionWithComparator = createRangeConditionWithComparator();
    }

    public void testCheck_WithComparableConditionWithNonComparableValue() {
        try {
            conditionWithComparable.check(new TestBean());
            fail("An IllegalArgumentException must be thrown when condition expects a comparable value but non-comparable value is passed in");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    public void testCheck_WithNull() throws Exception {
        try {
            conditionWithComparable.check(null);
            fail("An IllegalArgumentException must be thrown if the checked value is null");
        } catch (IllegalArgumentException iae) {
            // expected
        }

        try {
            conditionWithComparator.check(null);
            fail("An IllegalArgumentException must be thrown if the checked value is null");
        } catch (IllegalArgumentException iae) {
            // expected
        }

    }

    protected abstract AbstractRangeCondition createRangeConditionWithComparable();

    protected abstract AbstractRangeCondition createRangeConditionWithComparator();

    //=============================================== Inner Classes ====================================================

    private class TestBean {

    }

}
