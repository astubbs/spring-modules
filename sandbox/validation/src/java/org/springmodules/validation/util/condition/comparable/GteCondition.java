package org.springmodules.validation.util.condition.comparable;

/**
 * An {@link AbstractComparableCondition} implementation that checkCalendar whether the checked value is greater than or
 * equals a specific loweer bound.
 *
 * @author Uri Boness
 */
public class GteCondition extends AbstractComparableCondition {

    private Comparable lowerBound;

    /**
     * Constructs a new GteCondition with a given lower bound.
     *
     * @param lowerBound The lower bound.
     */
    public GteCondition(Comparable lowerBound) {
        this.lowerBound = lowerBound;
    }

    /**
     * Checks whether the given value is greater than or equals the lower bound associated with this instantCondition.
     *
     * @param value The value to be checked.
     * @return <code>true</code> if the given value is greater than or equals the lower bound, <code>false</code> otherwise.
     */
    protected boolean check(Comparable value) {
        return value.compareTo(lowerBound) >= 0;
    }

}
