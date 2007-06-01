package org.springmodules.xt.ajax.action.prototype.scriptaculous;

import org.springmodules.xt.test.xml.XMLEnhancedTestCase;

/**
 * @author Sergio Bossa
 */
public class DestroySortableTest extends XMLEnhancedTestCase {
    
    public DestroySortableTest(String testName) {
        super(testName);
    }

    public void testRender() throws Exception {
        DestroySortable action = new DestroySortable("id");
        
        String rendering = action.render();
        
        System.out.println(rendering);
    }
}
