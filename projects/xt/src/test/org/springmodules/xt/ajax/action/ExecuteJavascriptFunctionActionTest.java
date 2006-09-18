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
        Map<String, String> options = new HashMap<String, String>();
        options.put("message", "Greetings!");
        
        ExecuteJavascriptFunctionAction action = new ExecuteJavascriptFunctionAction("test", options);
        String rendering = action.execute();
        
        System.out.println(rendering);
        
        assertXpathEvaluatesTo("test({\"message\":\"Greetings!\"});", "/taconite-execute-javascript/script", rendering);
    }
}
