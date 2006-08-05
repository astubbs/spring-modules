package org.springmodules.validation.util.condition.string;

import org.springmodules.validation.util.condition.Condition;

/**
 * A TestCase for {@link EqualsIgnoreCaseStringCondition}.
 *
 * @author Uri Boness
 */
public class EqualsIgnoreCaseStringConditionTests extends AbstractStringConditionTests {

    protected Condition createCondition() {
        return new EqualsIgnoreCaseStringCondition("test");
    }

    public void testCheck_Success1() throws Exception {
        assertEqualsIgnoreCase("TeSt");
    }

    public void testCheck_Failed1() throws Exception {
        assertNotEqualsIgnoreCase("BEST");
    }


    //=============================================== Helper Methods ===================================================

    protected void assertEqualsIgnoreCase(String text) {
        assertTrue("string 'test' equals '" + text + "' when casing is ignored", condition.check(text));
    }

    protected void assertNotEqualsIgnoreCase(String text) {
        assertFalse("string 'test' does not equal '" + text + "' when casing is ignored", condition.check(text));
    }
}
