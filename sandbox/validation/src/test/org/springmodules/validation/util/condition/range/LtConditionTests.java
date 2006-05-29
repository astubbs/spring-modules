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
 * Tests for {@link LtCondition}.
 *
 * @author Uri Boness
 */
public class LtConditionTests extends AbstractSingleBoundConditionTests {

    protected AbstractSingleBoundCondition createCondition(Comparable bound) {
        return new LtCondition(bound);
    }

    protected AbstractSingleBoundCondition createCondition(Object bound, Comparator comparator) {
        return new LtCondition(bound, comparator);
    }

    public void testCheck_Success() throws Exception {
        assertTrue(conditionWithComparable.check(getLowerComparable()));
        assertTrue(conditionWithComparator.check(getLowerObject()));
    }

    public void testCheck_Failure() throws Exception {
        assertFalse(conditionWithComparable.check(getHigherComparable()));
        assertFalse(conditionWithComparator.check(getHigherObject()));
    }

    public void testCheck_FailureWithLowerBoundAsValue() throws Exception {
        assertFalse(conditionWithComparable.check(getComparableBound()));
        assertFalse(conditionWithComparator.check(getObjectBound()));
    }
}
