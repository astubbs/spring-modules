package org.springmodules.template.providers.stemp.parsers;
/**
 * @author Uri Boness
 */

import java.io.*;
import java.util.*;

import junit.framework.*;
import org.easymock.*;
import org.springmodules.template.providers.stemp.*;
import org.springmodules.template.providers.stemp.stemplets.*;

public class SimpleExpressionsPlaceholdersParserTest extends TestCase {

    private SimpleExpressionsPlaceholdersParser parser;

    private ExpressionResolver resolver;
    private MockControl resolverControl;

    private ExpressionWrapping wrapping;

    protected void setUp() throws Exception {
        parser = new SimpleExpressionsPlaceholdersParser();
        resolverControl = MockControl.createControl(ExpressionResolver.class);
        resolver = (ExpressionResolver)resolverControl.getMock();
        wrapping = new ExpressionWrapping("#");
    }

    /**
     * Tests the {@link SimpleExpressionsPlaceholdersParser#readToString(java.io.Reader)} method.
     */
    public void testReadToString() throws Exception {
        String expected = "This is a normal text";
        StringReader reader = new StringReader(expected);
        String actual = parser.readToString(reader);
        assertEquals(expected, actual);
    }

    /**
     * Tests the {@link SimpleExpressionsPlaceholdersParser#readToString(java.io.Reader)} method. In this test
     * the reader will read line breaks as well.
     */
    public void testReadToStringWithLineBreaks() throws Exception {
        String expected = "This is a text\n with line\n breaks";
        StringReader reader = new StringReader(expected);
        String actual = parser.readToString(reader);
        assertEquals(expected, actual);
    }

    /**
     * Tests the {@link SimpleExpressionsPlaceholdersParser#parse(
     * java.io.Reader, org.springmodules.template.providers.stemp.ExpressionResolver,
     *   org.springmodules.template.providers.stemp.ExpressionWrapping)}
     * method.
     */
    public void testParse() throws Exception {
        String text = "this is a #param# template text";
        StringReader reader = new StringReader(text);
        resolverControl.replay();
        List stemplets = parser.parse(reader, resolver, wrapping);

        assertNotNull("stemplet list should not be null", stemplets);
        assertEquals("there should be 3 stemplates for a text with only 1 parameter", 3, stemplets.size());
        assertTrue(stemplets.get(0) instanceof StaticStemplet);
        assertTrue(stemplets.get(1) instanceof ExpressionStemplet);
        assertTrue(stemplets.get(2) instanceof StaticStemplet);

        assertEquals(0, ((StaticStemplet)stemplets.get(0)).getExpressions().length);
        assertEquals("this is a ", ((StaticStemplet)stemplets.get(0)).getStaticText());

        assertEquals(1, ((ExpressionStemplet)stemplets.get(1)).getExpressions().length);
        assertEquals("param", ((ExpressionStemplet)stemplets.get(1)).getExpressions()[0]);
        assertSame(resolver, ((ExpressionStemplet)stemplets.get(1)).getExpressionResolver());

        assertEquals(0, ((StaticStemplet)stemplets.get(2)).getExpressions().length);
        assertEquals(" template text", ((StaticStemplet)stemplets.get(2)).getStaticText());

        resolverControl.verify();
    }
}