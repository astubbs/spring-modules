package org.springmodules.validation.util.condition.range;

import java.util.Comparator;

/**
 * An {@link AbstractSingleBoundCondition} implementation that checkCalendar whether the checked value is greater than or
 * equals a specific loweer bound.
 *
 * @author Uri Boness
 */
public class GteCondition extends AbstractSingleBoundCondition {

    /**
     * Constructs a new GteCondition with a given lower bound.
     *
     * @param lowerBound The lower bound.
     */
    public GteCondition(Comparable lowerBound) {
        super(lowerBound);
    }

    /**
     * Constructs a new GteCondition with given lower bound and the comparator to be used to compare the checked value.
     *
     * @param lowerBound The lower bound.
     * @param comparator The comparator.
     */
    public GteCondition(Object lowerBound, Comparator comparator) {
        super(lowerBound, comparator);
    }

    /**
     * Checks whether the given value is greater than or equals the lower bound associated with this instantCondition.
     *
     * @param value The value to be checked.
     * @param comparator The comparator used to compare the checked value.
     * @return <code>true</code> if the given value is greater than or equals the lower bound, <code>false</code> otherwise.
     */
    protected boolean checkRange(Object value, Comparator comparator) {
        return comparator.compare(value, bound) >= 0;
    }

}
