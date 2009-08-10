package org.springmodules.xt.ajax.action;

import java.util.HashMap;
import java.util.Map;
import org.springmodules.xt.test.xml.XMLEnhancedTestCase;

/**
 * @author Sergio Bossa
 */
public class ExecuteJavascriptFunctionActionTest extends XMLEnhancedTestCase {
    
    public ExecuteJavascriptFunctionActionTest(String testName) {
        super(testName);
    }
    
    public void testRender() throws Exception {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("message", "Greetings!");
        
        ExecuteJavascriptFunctionAction action = new ExecuteJavascriptFunctionAction("test", options);
        String rendering = action.render();
        
        System.out.println(rendering);
    }
}
