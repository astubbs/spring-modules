package org.springmodules.xt.ajax;

import junit.framework.*;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springmodules.xt.ajax.support.EventHandlingException;

/**
 *
 * @author Sergio Bossa
 */
public class AjaxHandlerTest extends TestCase {
    
    public AjaxHandlerTest(String testName) {
        super(testName);
    }

    public void testHandleAjaxEvent() {
        AbstractAjaxHandler handler = new AbstractAjaxHandler() {
            public AjaxResponse testEventTwo(AjaxEvent e) {
                fail();
                return new AjaxResponseImpl();
            }
            
            public AjaxResponse testEvent(AjaxSubmitEvent e) {
                fail();
                return new AjaxResponseImpl();
            }
            
            public AjaxResponse testEvent(AjaxEvent e) {
                return new AjaxResponseImpl();
            }
        };
        
        AjaxActionEvent event = new AjaxActionEventImpl("testEvent", new MockHttpServletRequest());
        
        assertNotNull(handler.handle(event));
    }
    
    public void testHandleAjaxActionEvent() {
        AbstractAjaxHandler handler = new AbstractAjaxHandler() {
            public AjaxResponse testEvent(AjaxActionEvent e) {
                return new AjaxResponseImpl();
            }
            
            public AjaxResponse testEvent(AjaxSubmitEvent e) {
                fail();
                return new AjaxResponseImpl();
            }
        };
        
        AjaxActionEvent event = new AjaxActionEventImpl("testEvent", new MockHttpServletRequest());
        
        assertNotNull(handler.handle(event));
    }
    
    public void testHandleAjaxSubmitEvent() {
        AbstractAjaxHandler handler = new AbstractAjaxHandler() {
            public AjaxResponse testEvent(AjaxActionEvent e) {
                fail();
                return new AjaxResponseImpl();
            }
            
            public AjaxResponse testEvent(AjaxSubmitEvent e) {
                return new AjaxResponseImpl();
            }
        };
        
        AjaxSubmitEvent event = new AjaxSubmitEventImpl("testEvent", new MockHttpServletRequest());
        
        assertNotNull(handler.handle(event));
    }
    

    public void testSupportsAjaxEvent() {
        AbstractAjaxHandler handler = new AbstractAjaxHandler() {
            public AjaxResponse testEvent(AjaxEvent e) {
                return new AjaxResponseImpl();
            }
        };
        
        AjaxActionEvent event = new AjaxActionEventImpl("testEvent", new MockHttpServletRequest());
        
        assertTrue(handler.supports(event));
    }
    
    public void testSupportsAjaxActionEvent() {
        AbstractAjaxHandler handler = new AbstractAjaxHandler() {
            public AjaxResponse testEvent(AjaxActionEvent e) {
                return new AjaxResponseImpl();
            }
        };
        
        AjaxActionEvent event = new AjaxActionEventImpl("testEvent", new MockHttpServletRequest());
        
        assertTrue(handler.supports(event));
    }
    
    public void testSupportsAjaxSubmitEvent() {
        AbstractAjaxHandler handler = new AbstractAjaxHandler() {
            public AjaxResponse testEvent(AjaxSubmitEvent e) {
                return new AjaxResponseImpl();
            }
        };
        
        AjaxSubmitEvent event = new AjaxSubmitEventImpl("testEvent", new MockHttpServletRequest());
        
        assertTrue(handler.supports(event));
    }
    
    public void testEventNotSupported() {
        AbstractAjaxHandler handler = new AbstractAjaxHandler() {
            public AjaxResponse testEvent(AjaxActionEvent e) {
                return new AjaxResponseImpl();
            }
        };
        
        AjaxSubmitEvent event = new AjaxSubmitEventImpl("testEvent", new MockHttpServletRequest());
        
        assertFalse(handler.supports(event));
    }
    
    public void testThrowingExceptionDuringEventHandling() {
        AbstractAjaxHandler handler = new AbstractAjaxHandler() {
            public AjaxResponse testEvent(AjaxActionEvent e) {
                throw new IllegalStateException("Nested exception.");
            }
        };
        
        AjaxActionEvent event = new AjaxActionEventImpl("testEvent", new MockHttpServletRequest());
        
        try {
            handler.handle(event);
            fail("Should throw: " + EventHandlingException.class);
        } 
        catch(EventHandlingException ex) {
        }
    }
}
