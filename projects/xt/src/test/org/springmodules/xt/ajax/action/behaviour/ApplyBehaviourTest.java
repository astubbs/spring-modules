package org.springmodules.xt.ajax.action.behaviour;

import org.springmodules.xt.test.xml.XMLEnhancedTestCase;

/**
 * @author Sergio Bossa
 */
public class ApplyBehaviourTest extends XMLEnhancedTestCase {
    
    public ApplyBehaviourTest(String testName) {
        super(testName);
    }

    public void testRender() throws Exception {
        ApplyBehaviour action = new ApplyBehaviour();
        
        String rendering = action.render();
        
        System.out.println(rendering);
    }
}
