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

import org.springframework.util.comparator.ComparableComparator;

/**
 * A base class for all between conditions test classes.
 *
 * @author Uri Boness
 */
public abstract class AbstractBetweenConditionTests extends AbstractRangeConditionTests {

    public void testConstructor_WithNullLowerBound() throws Exception {
        try {
            createBetweenCondition(null, new Integer(3));
            fail("An IllegalArgumentException must be thrown if the condition is initialized with a null lower bound");
        } catch (IllegalArgumentException iae) {
            // expected
        }

        try {
            createBetweenCondition(null, new Integer(3), new ComparableComparator());
            fail("An IllegalArgumentException must be thrown if the condition is initialized with a null lower bound");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    public void testConstructor_WithNullUpperBound() throws Exception {
        try {
            createBetweenCondition(new Integer(3), null);
            fail("An IllegalArgumentException must be thrown if the condition is initialized with a null upper bound");
        } catch (IllegalArgumentException iae) {
            // expected
        }

        try {
            createBetweenCondition(new Integer(3), null, new ComparableComparator());
            fail("An IllegalArgumentException must be thrown if the condition is initialized with a null upper bound");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    public void testConstructor_WithNullComparator() throws Exception {
        try {
            createBetweenCondition(new Integer(3), new Integer(4), null);
            fail("An IllegalArgumentException must be thrown if the condition is initialized with a null comparator");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    public void testCheck_WithDifferentNumberTypes() {
        AbstractBetweenCondition cond = createBetweenCondition(new Integer(3), new Double(2.0));
        try {
            cond.check(new Double(2.5));
        } catch (ClassCastException cce) {
            fail("Comparing numbers of different types sould be possible");
        }
    }

    protected AbstractRangeCondition createRangeConditionWithComparable() {
        return createBetweenCondition(getComparableLowerBound(), getComparableUpperBound());
    }

    protected AbstractRangeCondition createRangeConditionWithComparator() {
        return createBetweenCondition(getObjectLowerBound(), getObjectUpperBound(), new PersonByAgeComparator());
    }

    protected Comparable getComparableLowerBound() {
        return new Integer(5);
    }

    protected Comparable getComparableUpperBound() {
        return new Integer(10);
    }

    protected Object getObjectLowerBound() {
        return new Person(5);
    }

    protected Object getObjectUpperBound() {
        return new Person(10);
    }

    protected Comparable getInRangeComparable() {
        return new Integer(7);
    }

    protected Object getInRangeObject() {
        return new Person(7);
    }

    protected Comparable getOutOfRangeComparable() {
        return new Integer(3);
    }

    protected Object getOutOfRangeObject() {
        return new Person(3);
    }

    protected abstract AbstractBetweenCondition createBetweenCondition(Comparable lowerBound, Comparable upperBound);

    protected abstract AbstractBetweenCondition createBetweenCondition(Object lowerBound, Object upperBound, Comparator comparator);

}
