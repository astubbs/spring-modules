package org.springmodules.validation.util.condition.string;

import org.springmodules.validation.util.condition.Condition;

/**
 * A TestCase for {@link IsEmptyStringCondition}.
 *
 * @author Uri Boness
 */
public class IsEmptyStringConditionTests extends AbstractStringConditionTests {

    protected Condition createCondition() {
        return new IsEmptyStringCondition();
    }

    public void testCheck_Success() throws Exception {
        assertTrue("string '' is empty", condition.check(""));
    }

    public void testCheck_Failed() throws Exception {
        assertFalse("string 'bla' is not empty", condition.check("bla"));
    }

}
