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

package org.springmodules.validation.util.condition.comparable;

/**
 * An {@link AbstractComparableCondition} implementation that checks whether the checked value is less than
 * as specific upper bound.
 *
 * @author Uri Boness
 */
public class LtCondition extends AbstractComparableCondition {

    private Comparable upperBound;

    /**
     * Constructs a new LtCondition with a given upper bound.
     *
     * @param upperBound The upper bound.
     */
    public LtCondition(Comparable upperBound) {
        this.upperBound = upperBound;
    }

    /**
     * Checks whether the given value is lower than the upper bound associated with this instantCondition.
     *
     * @param value The value to be checked.
     * @return <code>true</code> if the given value is less than the upper bound, <code>false</code> otherwise.
     */
    protected boolean check(Comparable value) {
        return value.compareTo(upperBound) < 0;
    }

}
