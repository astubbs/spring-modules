package org.springmodules.validation.util.condition.string;

import java.util.regex.Pattern;

import org.springmodules.validation.util.condition.Condition;

/**
 * A TestCase for {@link RegExpStringCondition}.
 *
 * @author Uri Boness
 */
public class RegExpStringConditionTests extends AbstractStringConditionTests {

    protected Condition createCondition() {
        return new RegExpStringCondition("(t|b)est");
    }

    public void testConstructor_WithNullRegularExpression() throws Exception {
        try {
            new RegExpStringCondition((String) null);
            fail("An IllegalArgumentException must be thrown if the regular expression is null");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    public void testConstructor_WithNullPattern() throws Exception {
        try {
            new RegExpStringCondition((Pattern) null);
            fail("An IllegalArgumentException must be thrown if the pattern is null");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    public void testConstructor_WithIllegalRegExp() throws Exception {
        try {
            new RegExpStringCondition("(");
            fail("An IllegalArgumentException must be thrown if the regular expression is illegal");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    public void testCheck_SuccessWithGivenRegExp() throws Exception {
        assertTrue("string 'test' matches regular expression '(t|b)est'", condition.check("test"));
        assertTrue("string 'best' matches regular expression '(t|b)est'", condition.check("best"));
    }

    public void testCheck_SuccessWithGivenPattern() throws Exception {
        assertTrue("string 'test' matches regular expression '(t|b)est'", condition.check("test"));
        assertTrue("string 'best' matches regular expression '(t|b)est'", condition.check("best"));
    }

    public void testCheck_FailedWithGivenRegExp() throws Exception {
        assertFalse("string 'rest' does not match regular expression '(t|b)est'", condition.check("rest"));
    }

    public void testCheck_FailedWithGivenPattern() throws Exception {
        assertFalse("string 'rest' does not match regular expression '(t|b)est'", condition.check("rest"));
    }

    public void testGetRegExp_WithGivenRegExp() throws Exception {
        assertEquals("(t|b)est", ((RegExpStringCondition) condition).getRegExp());
    }

    public void testGetRegExp_WithGivenPattern() throws Exception {
        RegExpStringCondition condition = new RegExpStringCondition(Pattern.compile("(t|b)est"));
        assertEquals("(t|b)est", condition.getRegExp());
    }

    public void testGetPattern_WithGivenRegExp() throws Exception {
        assertEquals("(t|b)est", ((RegExpStringCondition) condition).getPattern().pattern());
    }

    public void testGetPattern_WithGivenPattern() throws Exception {
        Pattern pattern = Pattern.compile("(t|b)est");
        RegExpStringCondition condition = new RegExpStringCondition(pattern);
        assertEquals(pattern, condition.getPattern());
    }

}
