package org.springmodules.xt.ajax.action.prototype.scriptaculous;

import junit.framework.*;
import org.springmodules.xt.ajax.action.AbstractExecuteJavascriptAction;
import org.springmodules.xt.test.xml.XMLEnhancedTestCase;

/**
 * @author Sergio Bossa
 */
public class ShrinkActionTest extends XMLEnhancedTestCase {
    
    public ShrinkActionTest(String testName) {
        super(testName);
    }

    public void testRender() throws Exception {
        ShrinkAction action = new ShrinkAction("test");
        String rendering = action.execute();
        
        System.out.println(rendering);
        
        assertXpathEvaluatesTo("new Effect.Shrink(\"test\");", "/taconite-execute-javascript/script", rendering);
    }
}
