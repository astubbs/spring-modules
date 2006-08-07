package org.springmodules.xt.ajax.component.support;

import java.util.HashMap;
import junit.framework.*;
import java.util.Map;

/**
 *
 * @author Sergio Bossa
 */
public class ComponentUtilsTest extends TestCase {
    
    public ComponentUtilsTest(String testName) {
        super(testName);
    }

    public void testAppendAsAttributes() {
        String attribute = "attribute";
        String value = "This is a \"value\"";
        Map<String, String> params = new HashMap();
        StringBuilder builder = new StringBuilder();
        
        params.put(attribute, value);
        
        ComponentUtils.appendAsAttributes(params, builder);
        
        System.out.println(builder.toString());
        
        assertEquals(" attribute=\"This is a &quot;value&quot;\"", builder.toString());
    }
}
