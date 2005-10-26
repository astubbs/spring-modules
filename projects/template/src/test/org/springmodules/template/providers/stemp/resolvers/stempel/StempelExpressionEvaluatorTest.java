package org.springmodules.template.providers.stemp.resolvers.stempel;
/**
 * @author Uri Boness
 */

import junit.framework.*;
import org.springmodules.template.providers.stemp.resolvers.stempel.StempelExpressionEvaluator;

public class StempelExpressionEvaluatorTest extends TestCase {

    public void testParse() throws Exception {
        StempelExpressionEvaluator evaluator = new StempelExpressionEvaluator();
        String[] tokens = evaluator.parse("person.name");
        assertEquals(2, tokens.length);
        assertEquals("person", tokens[0]);
        assertEquals("name", tokens[1]);
    }

    public void testParseWithDotAtBeginnig() throws Exception {
        StempelExpressionEvaluator evaluator = new StempelExpressionEvaluator();
        String[] tokens = evaluator.parse(".person.name");
        assertEquals(2, tokens.length);
        assertEquals("person", tokens[0]);
        assertEquals("name", tokens[1]);
    }

    public void testParseWithDotOnly() throws Exception {
        StempelExpressionEvaluator evaluator = new StempelExpressionEvaluator();
        String[] tokens = evaluator.parse(".");
        assertEquals(0, tokens.length);
    }

    public void testParseWithDelimAndBracketsOnly() throws Exception {
        StempelExpressionEvaluator evaluator = new StempelExpressionEvaluator();
        String[] tokens = evaluator.parse(".[]");
        assertEquals(1, tokens.length);
        assertEquals("[]", tokens[0]);
    }

    public void testParseWithBracketsOnly() throws Exception {
        StempelExpressionEvaluator evaluator = new StempelExpressionEvaluator();
        String[] tokens = evaluator.parse("[]");
        assertEquals(1, tokens.length);
        assertEquals("[]", tokens[0]);
    }

    public void testParseWithSubExpression() throws Exception {
        StempelExpressionEvaluator evaluator = new StempelExpressionEvaluator();
        String[] tokens = evaluator.parse("person.attributes[key.id].name");
        assertEquals(3, tokens.length);
        assertEquals("person", tokens[0]);
        assertEquals("attributes[key.id]", tokens[1]);
        assertEquals("name", tokens[2]);
    }
}