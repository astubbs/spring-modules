package org.springmodules.xt.ajax.action.prototype.scriptaculous;

import junit.framework.*;
import java.util.HashMap;
import java.util.Map;
import net.sf.json.JSONObject;
import org.springmodules.xt.ajax.action.AbstractExecuteJavascriptAction;
import org.springmodules.xt.test.xml.XMLEnhancedTestCase;

/**
 * @author Sergio Bossa
 */
public class PulsateActionTest extends XMLEnhancedTestCase {
    
    public PulsateActionTest(String testName) {
        super(testName);
    }

    public void testRenderPart1() throws Exception {
        PulsateAction action = new PulsateAction("test");
        String rendering = action.execute();
        
        System.out.println(rendering);
        
        assertXpathEvaluatesTo("new Effect.Pulsate(\"test\");", "/taconite-execute-javascript/script", rendering);
    }
    
    public void testRenderPart2() throws Exception {
        PulsateAction action = new PulsateAction("test", new Float(1.0), new Float(0.5));
        String rendering = action.execute();
        
        System.out.println(rendering);
        
        assertXpathExists("/taconite-execute-javascript/script[contains(text(), 'Effect.Pulsate')]", rendering);
    }
}
