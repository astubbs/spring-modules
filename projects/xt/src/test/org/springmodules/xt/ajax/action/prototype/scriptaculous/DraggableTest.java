package org.springmodules.xt.ajax.action.prototype.scriptaculous;

import org.springmodules.xt.test.xml.XMLEnhancedTestCase;

/**
 * @author Sergio Bossa
 */
public class DraggableTest extends XMLEnhancedTestCase {
    
    public DraggableTest(String testName) {
        super(testName);
    }

    public void testRender() throws Exception {
        Draggable action = new Draggable("id");
        action.addOption("k1", "v1");
        action.addOption("k2", "v2");
        
        String rendering = action.render();
        
        System.out.println(rendering);
    }
}
