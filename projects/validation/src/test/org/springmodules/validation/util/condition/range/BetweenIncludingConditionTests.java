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
 * Tests for {@link BetweenIncludingCondition}.
 *
 * @author Uri Boness
 */
public class BetweenIncludingConditionTests extends AbstractBetweenConditionTests {

    protected AbstractBetweenCondition createBetweenCondition(Comparable lowerBound, Comparable upperBound) {
        return new BetweenIncludingCondition(lowerBound, upperBound);
    }

    protected AbstractBetweenCondition createBetweenCondition(Object lowerBound, Object upperBound, Comparator comparator) {
        return new BetweenIncludingCondition(lowerBound, upperBound, comparator);
    }

    public void testCheck_Success() throws Exception {
        assertTrue(conditionWithComparable.check(getInRangeComparable()));
        assertTrue(conditionWithComparator.check(getInRangeObject()));
    }

    public void testCheck_SuccessWithUpperBoundValue() throws Exception {
        assertTrue(conditionWithComparable.check(getComparableUpperBound()));
        assertTrue(conditionWithComparator.check(getObjectUpperBound()));
    }

    public void testCheck_SuccessWithLowerBoundValue() throws Exception {
        assertTrue(conditionWithComparable.check(getComparableLowerBound()));
        assertTrue(conditionWithComparator.check(getObjectLowerBound()));
    }

    public void testCheck_Failure() throws Exception {
        assertFalse(conditionWithComparable.check(getOutOfRangeComparable()));
        assertFalse(conditionWithComparator.check(getOutOfRangeObject()));
    }

}
