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

import java.util.Comparator;

/**
 * Tests for {@link GreaterThanCondition}.
 *
 * @author Uri Boness
 */
public class GreaterThanConditionTests extends AbstractSingleBoundConditionTests {

    protected AbstractSingleBoundCondition createCondition(Comparable bound) {
        return new GreaterThanCondition(bound);
    }

    protected AbstractSingleBoundCondition createCondition(Object bound, Comparator comparator) {
        return new GreaterThanCondition(bound, comparator);
    }

    public void testCheck_Success() throws Exception {
        assertTrue(conditionWithComparable.check(getHigherComparable()));
        assertTrue(conditionWithComparator.check(getHigherObject()));
    }

    public void testCheck_Failure() throws Exception {
        assertFalse(conditionWithComparable.check(getLowerComparable()));
        assertFalse(conditionWithComparator.check(getLowerObject()));
    }

    public void testCheck_FailureWithLowerBoundAsValue() throws Exception {
        assertFalse(conditionWithComparable.check(getComparableBound()));
        assertFalse(conditionWithComparator.check(getObjectBound()));
    }

    public void testCheck_SuccessWithNumbers() throws Exception {
        AbstractSingleBoundCondition cond = createCondition(new Integer(3));
        assertFalse(cond.check(new Double(2.0)));
    }

    public void testCheck_SuccessWithNumbers2() throws Exception {
        AbstractSingleBoundCondition cond = createCondition(new Double(3));
        assertFalse(cond.check(new Integer(2)));
    }

    public void testCheck_SuccessWithNumbers3() throws Exception {
        AbstractSingleBoundCondition cond = createCondition(new Float(3.3f));
        assertFalse(cond.check(new Long(2)));
    }

}
