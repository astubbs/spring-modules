package org.springmodules.xt.ajax.action.prototype.scriptaculous;

import org.springmodules.xt.test.xml.XMLEnhancedTestCase;

/**
 * @author Sergio Bossa
 */
public class EffectTest extends XMLEnhancedTestCase {
    
    public EffectTest(String testName) {
        super(testName);
    }

    public void testRender() throws Exception {
        Effect action = new Effect("Test", "id");
        action.addRequiredParameter("r1");
        action.addRequiredParameter("r2");
        action.addOption("integer", 1);
        action.addOption("boolean", true);
        action.addOption("string", "v2");
        
        String rendering = action.render();
        
        System.out.println(rendering);
    }
}
