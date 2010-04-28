package org.springmodules.validation.util.condition.string;

import org.springmodules.validation.util.condition.Condition;

/**
 * A TestCase for {@link MinLengthStringCondition}.
 *
 * @author Uri Boness
 */
public class MinLengthStringConditionTests extends AbstractStringConditionTests {

    protected Condition createCondition() {
        return new MinLengthStringCondition(4);
    }

    public void testCheck_WithNegativeMinLength() throws Exception {
        try {
            new MinLengthStringCondition(-1);
            fail("An IllegalArgumentException must be thrown if the min length is a negative value");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    public void testCheck_Success() throws Exception {
        assertTrue("string 'spring' has length of 6", condition.check("spring"));
    }

    public void testCheck_SuccessWithMinLength() throws Exception {
        assertTrue("string 'test' has length of 4", condition.check("test"));
    }

    public void testCheck_Failed() throws Exception {
        assertFalse("string 'bla' has length of 3", condition.check("bla"));
    }

}
