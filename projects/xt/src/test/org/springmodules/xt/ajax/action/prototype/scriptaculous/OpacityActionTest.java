package org.springmodules.xt.ajax.action.prototype.scriptaculous;

import org.springmodules.xt.test.xml.XMLEnhancedTestCase;

/**
 *
 * @author Sergio Bossa
 */
public class OpacityActionTest extends XMLEnhancedTestCase {
    
    public OpacityActionTest(String testName) {
        super(testName);
    }
    
    public void testRender() throws Exception {
        OpacityAction action = new OpacityAction("test", new Float(0.5), new Float(0.1), new Float(1.0));
        String rendering = action.execute();
        
        System.out.println(rendering);
        
        assertXpathExists("/taconite-execute-javascript/script[contains(text(), 'Effect.Opacity')]", rendering);
    }
}
