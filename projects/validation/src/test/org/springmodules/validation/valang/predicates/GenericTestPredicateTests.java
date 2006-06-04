package org.springmodules.validation.valang.predicates;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import junit.framework.TestCase;
import org.apache.commons.collections.iterators.IteratorEnumeration;
import org.springmodules.validation.util.date.DateParseException;
import org.springmodules.validation.util.date.DefaultDateParser;
import org.springmodules.validation.valang.functions.BeanPropertyFunction;
import org.springmodules.validation.valang.functions.LiteralFunction;

/**
 * @author Steven Devijver
 * @since 23-04-2005
 */
public class GenericTestPredicateTests extends TestCase {

    public void testNullOperatorSuccess() {
        assertTrue(null, OperatorConstants.NULL_OPERATOR, null);
        assertFalse("five", OperatorConstants.NULL_OPERATOR, null);
    }

    public void testNotNullOperatorSuccess() {
        assertTrue("five", OperatorConstants.NOT_NULL_OPERATOR, null);
        assertFalse(null, OperatorConstants.NOT_NULL_OPERATOR, null);
    }

    public void testEqualOperatorSuccess() {
        assertTrue(5, OperatorConstants.EQUALS_OPERATOR, 5);
        assertFalse(6, OperatorConstants.EQUALS_OPERATOR, 5);
    }

    public void testNotEqualOperatorSuccess() {
        assertTrue(5, OperatorConstants.NOT_EQUAL_OPERATOR, 6);
        assertFalse(5, OperatorConstants.NOT_EQUAL_OPERATOR, 5);
    }

    public void testLessThanOperatorSuccess() {
        assertTrue(5, OperatorConstants.LESS_THAN_OPERATOR, 6);
        assertFalse(6, OperatorConstants.LESS_THAN_OPERATOR, 5);
    }

    public void testLessThanOrEqualOperatorSuccess() {
        assertTrue(5, OperatorConstants.LESS_THAN_OR_EQUAL_OPERATOR, 5);
        assertTrue(5, OperatorConstants.LESS_THAN_OR_EQUAL_OPERATOR, 6);
        assertFalse(6, OperatorConstants.LESS_THAN_OR_EQUAL_OPERATOR, 5);
    }

    public void testMoreThanOperatorSuccess() {
        assertTrue(6, OperatorConstants.MORE_THAN_OPERATOR, 5);
        assertFalse(5, OperatorConstants.MORE_THAN_OPERATOR, 6);
    }

    public void testMoreThanOrEqualOperatorSuccess() {
        assertTrue(5, OperatorConstants.MORE_THAN_OR_EQUAL_OPERATOR, 5);
        assertTrue(6, OperatorConstants.MORE_THAN_OR_EQUAL_OPERATOR, 5);
        assertFalse(5, OperatorConstants.MORE_THAN_OR_EQUAL_OPERATOR, 6);
    }

    public void testStringInNotInOperatorSuccess() {
        Collection coll = new ArrayList();
        coll.add(new LiteralFunction(new BigDecimal("1")));
        coll.add(new LiteralFunction(new BigDecimal("2")));
        coll.add(new LiteralFunction(new BigDecimal("3")));
        coll.add(new LiteralFunction(new BigDecimal("4")));
        coll.add(new LiteralFunction(new BigDecimal("5")));

        assertTrue(5, OperatorConstants.IN_OPERATOR, coll);
        assertFalse(6, OperatorConstants.IN_OPERATOR, coll);
        assertTrue(5, OperatorConstants.IN_OPERATOR, coll.iterator());
        assertFalse(6, OperatorConstants.IN_OPERATOR, coll.iterator());
        assertTrue(5, OperatorConstants.IN_OPERATOR, new IteratorEnumeration(coll.iterator()));
        assertFalse(6, OperatorConstants.IN_OPERATOR, new IteratorEnumeration(coll.iterator()));
        assertTrue(5, OperatorConstants.IN_OPERATOR, coll.toArray());
        assertFalse(6, OperatorConstants.IN_OPERATOR, coll.toArray());

        assertTrue(6, OperatorConstants.NOT_IN_OPERATOR, coll);
        assertFalse(5, OperatorConstants.NOT_IN_OPERATOR, coll);
        assertTrue(6, OperatorConstants.NOT_IN_OPERATOR, coll.iterator());
        assertFalse(5, OperatorConstants.NOT_IN_OPERATOR, coll.iterator());
        assertTrue(6, OperatorConstants.NOT_IN_OPERATOR, new IteratorEnumeration(coll.iterator()));
        assertFalse(5, OperatorConstants.NOT_IN_OPERATOR, new IteratorEnumeration(coll.iterator()));
        assertTrue(6, OperatorConstants.NOT_IN_OPERATOR, coll.toArray());
        assertFalse(5, OperatorConstants.NOT_IN_OPERATOR, coll.toArray());
    }

    public void testBetweenNotBetweenOperatorSuccess() {
        Collection coll = new ArrayList();
        coll.add(new LiteralFunction(new BigDecimal("1")));
        coll.add(new LiteralFunction(new BigDecimal("5")));

        assertTrue(5, OperatorConstants.BETWEEN_OPERATOR, coll);
        assertFalse(6, OperatorConstants.BETWEEN_OPERATOR, coll);
        assertTrue(5, OperatorConstants.BETWEEN_OPERATOR, coll.iterator());
        assertFalse(6, OperatorConstants.BETWEEN_OPERATOR, coll.iterator());
        assertTrue(5, OperatorConstants.BETWEEN_OPERATOR, new IteratorEnumeration(coll.iterator()));
        assertFalse(6, OperatorConstants.BETWEEN_OPERATOR, new IteratorEnumeration(coll.iterator()));
        assertTrue(5, OperatorConstants.BETWEEN_OPERATOR, coll.toArray());
        assertFalse(6, OperatorConstants.BETWEEN_OPERATOR, coll.toArray());

        assertTrue(6, OperatorConstants.NOT_BETWEEN_OPERATOR, coll);
        assertFalse(5, OperatorConstants.NOT_BETWEEN_OPERATOR, coll);
        assertTrue(6, OperatorConstants.NOT_BETWEEN_OPERATOR, coll.iterator());
        assertFalse(5, OperatorConstants.NOT_BETWEEN_OPERATOR, coll.iterator());
        assertTrue(6, OperatorConstants.NOT_BETWEEN_OPERATOR, new IteratorEnumeration(coll.iterator()));
        assertFalse(5, OperatorConstants.NOT_BETWEEN_OPERATOR, new IteratorEnumeration(coll.iterator()));
        assertTrue(6, OperatorConstants.NOT_BETWEEN_OPERATOR, coll.toArray());
        assertFalse(5, OperatorConstants.NOT_BETWEEN_OPERATOR, coll.toArray());
    }

    public void testStringEqualsOperatorSuccess() {
        assertTrue("five", OperatorConstants.EQUALS_OPERATOR, "five");
        assertFalse("five", OperatorConstants.EQUALS_OPERATOR, "six");
    }

    public void testStringNotEqualsOperatorSuccess() {
        assertTrue("five", OperatorConstants.NOT_EQUAL_OPERATOR, "six");
        assertFalse("five", OperatorConstants.NOT_EQUAL_OPERATOR, "five");
    }

    public void testMoreThanOperatorFail() {
        try {
            assertTrue("six", OperatorConstants.MORE_THAN_OPERATOR, "five");
            fail();
        } catch (Exception e) {}
    }

    public void testInNotInOperatorSuccess() {
        Collection coll = new ArrayList();
        coll.add(new LiteralFunction("one"));
        coll.add(new LiteralFunction("two"));
        coll.add(new LiteralFunction("three"));
        coll.add(new LiteralFunction("four"));
        coll.add(new LiteralFunction("five"));

        assertTrue("five", OperatorConstants.IN_OPERATOR, coll);
        assertFalse("six", OperatorConstants.IN_OPERATOR, coll);

        assertTrue("six", OperatorConstants.NOT_IN_OPERATOR, coll);
        assertFalse("five", OperatorConstants.NOT_IN_OPERATOR, coll);
    }

    public void testNullNotNullOperatorSuccess() {
        assertTrue(null, OperatorConstants.NULL_OPERATOR, null);
        assertFalse("five", OperatorConstants.NULL_OPERATOR, null);

        assertTrue("five", OperatorConstants.NOT_NULL_OPERATOR, null);
        assertFalse(null, OperatorConstants.NOT_NULL_OPERATOR, null);
    }

    public void testHasTextHasNoTextOperatorSuccess() {
        assertTrue("five", OperatorConstants.HAS_TEXT_OPERATOR, null);
        assertFalse("    ", OperatorConstants.HAS_TEXT_OPERATOR, null);
        assertFalse(null, OperatorConstants.HAS_TEXT_OPERATOR, null);

        assertTrue("    ", OperatorConstants.HAS_NO_TEXT_OPERATOR, null);
        assertTrue(null, OperatorConstants.HAS_NO_TEXT_OPERATOR, null);
        assertFalse("five", OperatorConstants.HAS_NO_TEXT_OPERATOR, null);
    }

    public void testHasLengthHasNotLengthOperatorSuccess() {
        assertTrue("five", OperatorConstants.HAS_LENGTH_OPERATOR, null);
        assertFalse("", OperatorConstants.HAS_LENGTH_OPERATOR, null);
        assertFalse(null, OperatorConstants.HAS_LENGTH_OPERATOR, null);

        assertTrue("", OperatorConstants.HAS_NO_LENGTH_OPERATOR, null);
        assertTrue(null, OperatorConstants.HAS_NO_LENGTH_OPERATOR, null);
        assertFalse("five", OperatorConstants.HAS_NO_LENGTH_OPERATOR, null);
    }

    public void testIsBlankIsNotBlankOperatorSuccess() {
        assertTrue("", OperatorConstants.IS_BLANK_OPERATOR, null);
        assertTrue(null, OperatorConstants.IS_BLANK_OPERATOR, null);
        assertFalse("five", OperatorConstants.IS_BLANK_OPERATOR, null);

        assertTrue("five", OperatorConstants.IS_NOT_BLANK_OPERATOR, null);
        assertFalse("", OperatorConstants.IS_NOT_BLANK_OPERATOR, null);
        assertFalse(null, OperatorConstants.IS_NOT_BLANK_OPERATOR, null);
    }

    public void testIsWordIsNotWordOperatorSuccess() {
        assertTrue("five", OperatorConstants.IS_WORD_OPERATOR, null);
        assertFalse("five six", OperatorConstants.IS_WORD_OPERATOR, null);

        assertTrue("five six", OperatorConstants.IS_NOT_WORD_OPERATOR, null);
        assertFalse("five", OperatorConstants.IS_NOT_WORD_OPERATOR, null);
    }

    public void testIsUpperCaseIsNotUpperCaseOperatorSuccess() {
        assertTrue("FIVE", OperatorConstants.IS_UPPER_CASE_OPERATOR, null);
        assertFalse("five", OperatorConstants.IS_UPPER_CASE_OPERATOR, null);

        assertTrue("five", OperatorConstants.IS_NOT_UPPER_CASE_OPERATOR, null);
        assertFalse("FIVE", OperatorConstants.IS_NOT_UPPER_CASE_OPERATOR, null);
    }

    public void testIsLowerCaseIsNotLowerCaseOperatorSuccess() {
        assertTrue("five", OperatorConstants.IS_LOWER_CASE_OPERATOR, null);
        assertFalse("Five", OperatorConstants.IS_LOWER_CASE_OPERATOR, null);

        assertTrue("Five", OperatorConstants.IS_NOT_LOWER_CASE_OPERATOR, null);
        assertFalse("five", OperatorConstants.IS_NOT_LOWER_CASE_OPERATOR, null);
    }

    public void testBooleanEqualsOperatorSuccess() {
        assertTrue(Boolean.TRUE, OperatorConstants.EQUALS_OPERATOR, Boolean.TRUE);
        assertFalse(Boolean.TRUE, OperatorConstants.EQUALS_OPERATOR, Boolean.FALSE);
    }

    public void testBooleanNotEqualOperatorSuccess() {
        assertTrue(Boolean.TRUE, OperatorConstants.NOT_EQUAL_OPERATOR, Boolean.FALSE);
        assertFalse(Boolean.TRUE, OperatorConstants.NOT_EQUAL_OPERATOR, Boolean.TRUE);
    }

    public void testDateEqualsOperatorSuccess() {
        assertTrue(md("20050409"), OperatorConstants.EQUALS_OPERATOR, md("2005-04-09"));
        assertFalse(md("20050409"), OperatorConstants.EQUALS_OPERATOR, md("2005-04-08"));
    }

    public void testDateNotEqualOperatorSuccess() {
        assertTrue(md("20050409"), OperatorConstants.NOT_EQUAL_OPERATOR, md("2005-04-08"));
        assertFalse(md("20050409"), OperatorConstants.NOT_EQUAL_OPERATOR, md("2005-04-09"));
    }

    public void testDateLessThanOperatorSuccess() {
        assertTrue(md("20050409"), OperatorConstants.LESS_THAN_OPERATOR, md("2005-04-10"));
        assertFalse(md("20050409"), OperatorConstants.LESS_THAN_OPERATOR, md("2005-04-08"));
    }

    public void testDateLessThanOrEqualOperatorSuccess() {
        assertTrue(md("20050409"), OperatorConstants.LESS_THAN_OR_EQUAL_OPERATOR, md("2005-04-10"));
        assertFalse(md("20050409"), OperatorConstants.LESS_THAN_OR_EQUAL_OPERATOR, md("2005-04-08"));
    }

    public void testDateMoreThanOperatorSuccess() {
        assertTrue(md("20050409"), OperatorConstants.MORE_THAN_OPERATOR, md("2005-04-08"));
        assertFalse(md("20050409"), OperatorConstants.MORE_THAN_OPERATOR, md("2005-04-09"));
    }

    public void testDateMoreThanOrEqualOperatorSuccess() {
        assertTrue(md("20050409"), OperatorConstants.MORE_THAN_OR_EQUAL_OPERATOR, md("2005-04-08"));
        assertFalse(md("20050409"), OperatorConstants.MORE_THAN_OR_EQUAL_OPERATOR, md("2005-04-10"));
    }

    public Date md(String s) {
        try {
            return new DefaultDateParser().parse(s);
        } catch (DateParseException e) {
            throw new RuntimeException(e);
        }
    }


    public GenericTestPredicateTests() {
        super();
    }

    public GenericTestPredicateTests(String arg0) {
        super(arg0);
    }

    public class GenericContainer {
        private Object value = null;

        public GenericContainer(Object value) {
            super();
            this.value = value;
        }

        public Object getValue() {
            return this.value;
        }
    }

    private boolean runTest(Object leftValue, Operator operator, Object rightValue) {
        return new GenericTestPredicate(new BeanPropertyFunction("value"), operator, new LiteralFunction(rightValue), 0, 0).evaluate(new GenericContainer(leftValue));
    }

    private void assertTrue(Object leftValue, Operator operator, Object rightValue) {
        assertTrue(runTest(leftValue, operator, rightValue));
    }

    private void assertFalse(Object leftValue, Operator operator, Object rightValue) {
        assertFalse(runTest(leftValue, operator, rightValue));
    }

    private void assertTrue(int leftValue, Operator operator, int rightValue) {
        assertTrue(new Integer(leftValue), operator, new Integer(rightValue));
    }

    private void assertFalse(int leftValue, Operator operator, int rightValue) {
        assertFalse(new Integer(leftValue), operator, new Integer(rightValue));
    }

    private void assertTrue(int leftValue, Operator operator, Object rightValue) {
        assertTrue(new Integer(leftValue), operator, rightValue);
    }

    private void assertFalse(int leftValue, Operator operator, Object rightValue) {
        assertFalse(new Integer(leftValue), operator, rightValue);
    }
}
