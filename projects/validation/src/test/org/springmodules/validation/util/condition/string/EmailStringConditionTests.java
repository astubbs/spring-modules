package org.springmodules.validation.util.condition.string;

import org.springmodules.validation.util.condition.Condition;

/**
 * A TestCase for {@link EmailStringCondition}.
 *
 * @author Uri Boness
 */
public class EmailStringConditionTests extends AbstractStringConditionTests {

    protected Condition createCondition() {
        return new EmailStringCondition();
    }

    public void testCheck_Success1() throws Exception {
        assertValidEmail("someone@world.com");
    }

    public void testCheck_Success2() throws Exception {
        assertValidEmail("some_one@world.com");
    }

    public void testCheck_Failed1() throws Exception {
        assertNotValidEmail("*@bla.com");
    }

    public void testCheck_Failed2() throws Exception {
        assertNotValidEmail("someone@bla");
    }

    public void testCheck_Failed3() throws Exception {
        assertNotValidEmail("someone@");
    }

    public void testCheck_Failed4() throws Exception {
        assertNotValidEmail("someone_@bla.com");
    }

    public void testCheck_Failed5() throws Exception {
        assertNotValidEmail("_someone@bla.com");
    }

    //=============================================== Helper Methods ===================================================

    protected void assertValidEmail(String email) {
        assertTrue("the email '" + email + "' is valid", condition.check(email));
    }

    protected void assertNotValidEmail(String email) {
        assertFalse("the email '" + email + "' is not valid", condition.check(email));
    }

}
