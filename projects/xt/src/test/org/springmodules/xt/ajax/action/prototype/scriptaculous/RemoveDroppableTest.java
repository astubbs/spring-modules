package org.springmodules.xt.ajax.action.prototype.scriptaculous;

import org.springmodules.xt.test.xml.XMLEnhancedTestCase;

/**
 * @author Sergio Bossa
 */
public class RemoveDroppableTest extends XMLEnhancedTestCase {
    
    public RemoveDroppableTest(String testName) {
        super(testName);
    }

    public void testRender() throws Exception {
        RemoveDroppable action = new RemoveDroppable("id");
        
        String rendering = action.render();
        
        System.out.println(rendering);
    }
}
