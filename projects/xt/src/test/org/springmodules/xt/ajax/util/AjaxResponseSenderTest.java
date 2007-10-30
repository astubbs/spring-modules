package org.springmodules.xt.ajax.util;

import junit.framework.TestCase;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxResponseImpl;
import org.springmodules.xt.ajax.action.ReplaceContentAction;
import org.springmodules.xt.ajax.component.SimpleText;

/**
 * @author Sergio Bossa
 */
public class AjaxResponseSenderTest extends TestCase {
    
    private AjaxResponseSender sender;
    
    public AjaxResponseSenderTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
        this.sender = new AjaxResponseSender();
    }
    
    public void testSendResponse() throws Exception {
        MockHttpServletResponse httpResponse = new MockHttpServletResponse();
        AjaxResponse ajaxResponse = new AjaxResponseImpl();
        
        String text = "simple text";
        
        ajaxResponse.addAction(new ReplaceContentAction("test", new SimpleText(text)));
        
        this.sender.sendResponse(httpResponse, ajaxResponse);
        
        assertTrue(httpResponse.getContentAsString().contains(text));
    }
    
    public void testSendResponseWithI18nCharsSucceeds() throws Exception {
        MockHttpServletResponse httpResponse = new MockHttpServletResponse();
        AjaxResponse ajaxResponse = new AjaxResponseImpl("UTF-8");
        
        String text = "questo è un semplice testo";
        
        ajaxResponse.addAction(new ReplaceContentAction("test", new SimpleText(text)));
        
        this.sender.sendResponse(httpResponse, ajaxResponse);
        
        assertTrue(httpResponse.getContentAsString().contains(text));
    }
}
