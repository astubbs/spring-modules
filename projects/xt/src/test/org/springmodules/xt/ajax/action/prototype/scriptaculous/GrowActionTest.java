package org.springmodules.xt.ajax.action.prototype.scriptaculous;

import org.springmodules.xt.test.xml.XMLEnhancedTestCase;

/**
 * @author Sergio Bossa
 */
public class GrowActionTest extends XMLEnhancedTestCase {
    
    public GrowActionTest(String testName) {
        super(testName);
    }

    public void testRender() throws Exception {
        GrowAction action = new GrowAction("test");
        String rendering = action.execute();
        
        System.out.println(rendering);
        
        assertXpathEvaluatesTo("new Effect.Grow(\"test\",{\"direction\":\"top-left\"});", "/taconite-execute-javascript/script", rendering);
        
        action = new GrowAction("test", "center");
        rendering = action.execute();
        
        System.out.println(rendering);
        
        assertXpathEvaluatesTo("new Effect.Grow(\"test\",{\"direction\":\"center\"});", "/taconite-execute-javascript/script", rendering);
    }
}
