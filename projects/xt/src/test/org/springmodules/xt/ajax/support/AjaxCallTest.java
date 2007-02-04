package org.springmodules.xt.ajax.support;

import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;

/**
 *
 * @author Sergio Bossa
 */
public class AjaxCallTest extends TestCase {
    
    public AjaxCallTest(String testName) {
        super(testName);
    }

    public void testAjaxActionGetCall() {
        assertEquals("XT.doAjaxAction(\"test\",this);", AjaxCall.AJAX_ACTION.getCall("test"));
        
        Map<String, String> params = new HashMap();
        params.put("param1", "value1");
        params.put("param2", "value2");
        
        // Just print:
        System.out.println(AjaxCall.AJAX_ACTION.getCall("test", params));
    }
    
    public void testAjaxSubmitGetCall() {
        assertEquals("XT.doAjaxSubmit(\"test\",this);", AjaxCall.AJAX_SUBMIT.getCall("test"));
        
        Map<String, String> params = new HashMap();
        params.put("param1", "value1");
        params.put("param2", "value2");
        
        // Just print:
        System.out.println(AjaxCall.AJAX_SUBMIT.getCall("test", params));
    }
}
