package org.springmodules.validation.util.condition.range;

import java.util.Comparator;

import org.springframework.util.comparator.ComparableComparator;

/**
 * A base class for all single bound condition.
 *
 * @author Uri Boness
 */
public abstract class AbstractSingleBoundConditionTests extends AbstractRangeConditionTests {

    protected AbstractRangeCondition createRangeConditionWithComparable() {
        return createCondition(getComparableBound());
    }

    protected AbstractRangeCondition createRangeConditionWithComparator() {
        return createCondition(getObjectBound(), new PersonByAgeComparator());
    }

    public void testConstructor_WithNullBound() throws Exception {
        try {
            createCondition(null);
            fail("An IllegalArgumentException must be thrown if the condition is initialized with a null bound");
        } catch (IllegalArgumentException iae) {
            // expected
        }

        try {
            createCondition(null, new ComparableComparator());
            fail("An IllegalArgumentException must be thrown if the condition is initialized with a null bound");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    public void testConstructor_WithNullComparator() throws Exception {
        try {
            createCondition(new Person(5), null);
            fail("An IllegalArgumentException must be thrown if the condition is initialized with a null comparator");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    public void testCheck_WithDifferentNumberTypes() {
        AbstractSingleBoundCondition cond = createCondition(new Integer(3));
        try {
            cond.check(new Double(2.0));
        } catch (ClassCastException cce) {
            fail("Comparing numbers of different types sould be possible");
        }
    }

    protected Comparable getComparableBound() {
        return new Integer(5);
    }

    protected Comparable getHigherComparable() {
        return new Integer(10);
    }

    protected Comparable getLowerComparable() {
        return new Integer(3);
    }

    protected Object getObjectBound() {
        return new Person(5);
    }

    protected Object getHigherObject() {
        return new Person(10);
    }

    protected Object getLowerObject() {
        return new Person(3);
    }

    protected abstract AbstractSingleBoundCondition createCondition(Comparable bound);

    protected abstract AbstractSingleBoundCondition createCondition(Object bound, Comparator comparator);

}
