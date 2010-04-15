package org.springmodules.validation.util.condition.string;

import junit.framework.TestCase;
import org.springmodules.validation.util.condition.Condition;

/**
 * @author Uri Boness
 */
public abstract class AbstractStringConditionTests extends TestCase {

    protected Condition condition;

    protected void setUp() throws Exception {
        condition = createCondition();
    }

    public void testCheck_WithNonString() throws Exception {
        try {
            condition.check(new Integer(1));
            fail("An IllegalArgumentException should be thrown when a non-string value is being checked");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    public void testCheck_WithNull() throws Exception {
        try {
            condition.check(null);
            fail("An IllegalArgumentException should be thrown when a null value is being checked");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    protected abstract Condition createCondition();

}
