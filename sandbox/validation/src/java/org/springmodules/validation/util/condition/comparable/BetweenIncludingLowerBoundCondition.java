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
 * An {@link AbstractBetweenCondition} implementation that checks whether the checked value is greater than or equals
 * the lower bound and smaller than the upper bound.
 *
 * @author Uri Boness
 */
public class BetweenIncludingLowerBoundCondition extends AbstractBetweenCondition {

    /**
     * Constructs a new BetweenIncludingLowerBoundCondition with given lower and upper bounds.
     *
     * @param lowerBound The lower bound.
     * @param upperBound The higher bound.
     */
    public BetweenIncludingLowerBoundCondition(Comparable lowerBound, Comparable upperBound) {
        super(lowerBound, upperBound);
    }

    /**
     * Checks whether the given value is greater than or equals the lower bound and smaller than the upper bound.
     *
     * @param value The value to be compared.
     * @return <code>true</code> if the value is within the range, <code>false</code> otherwise.
     */
    protected boolean check(Comparable value) {
        return value.compareTo(getLowerBound()) >= 0 && value.compareTo(getUpperBound()) < 0;
    }

}
