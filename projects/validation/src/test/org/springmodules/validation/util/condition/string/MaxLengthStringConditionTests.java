package org.springmodules.validation.util.condition.string;

import org.springmodules.validation.util.condition.Condition;

/**
 * A TestCase for {@link MaxLengthStringCondition}.
 *
 * @author Uri Boness
 */
public class MaxLengthStringConditionTests extends AbstractStringConditionTests {

    protected Condition createCondition() {
        return new MaxLengthStringCondition(4);
    }

    public void testCheck_WithNegativeMaxLength() throws Exception {
        try {
            new MaxLengthStringCondition(-1);
            fail("An IllegalArgumentException must be thrown if the max length is a negative value");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    public void testCheck_Success() throws Exception {
        assertTrue("string 'bla' has length of 3", condition.check("bla"));
    }

    public void testCheck_SuccessWithEmptyString() throws Exception {
        assertTrue("string '' has length of 0", condition.check(""));
    }

    public void testCheck_SuccessWithMaxLength() throws Exception {
        assertTrue("string 'test' has length of 4", condition.check("test"));
    }

    public void testCheck_Failed() throws Exception {
        assertFalse("string 'spring' has length of 6", condition.check("spring"));
    }

}
