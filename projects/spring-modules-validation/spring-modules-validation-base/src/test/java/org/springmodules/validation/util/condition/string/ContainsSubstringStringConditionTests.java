package org.springmodules.validation.util.condition.string;

import org.springmodules.validation.util.condition.Condition;

/**
 * A TestCase for {@link ContainsSubstringStringCondition}.
 *
 * @author Uri Boness
 */
public class ContainsSubstringStringConditionTests extends AbstractStringConditionTests {

    protected Condition createCondition() {
        return new ContainsSubstringStringCondition("test");
    }

    public void testCheck_Success() throws Exception {
        assertTrue("the string 'this is a test' contains the sub-string 'test'", condition.check("this is a test"));
    }

    public void testCheck_Failed() throws Exception {
        assertFalse("the string 'this is the best' does not contain the sub-string 'test'", condition.check("this is the best"));
    }
}
