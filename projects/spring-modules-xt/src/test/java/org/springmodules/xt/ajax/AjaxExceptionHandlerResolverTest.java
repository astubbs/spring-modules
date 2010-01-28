/*
 * Copyright 2006 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springmodules.xt.ajax;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springmodules.xt.ajax.support.EventHandlingException;
import org.springmodules.xt.ajax.support.RedirectExceptionHandler;
import org.springmodules.xt.ajax.support.UnsupportedEventException;

/**
 *
 * @author sergio
 */
public class AjaxExceptionHandlerResolverTest extends AbstractDependencyInjectionSpringContextTests {
    
    public AjaxExceptionHandlerResolverTest(String testName) {
        super(testName);
    }

    public void testResolveException1() throws Exception {
        AjaxExceptionHandlerResolver ajaxExceptionResolver = (AjaxExceptionHandlerResolver) this.applicationContext.getBean("ajaxExceptionResolver");
        
        MockHttpServletRequest httpRequest = new MockHttpServletRequest("GET", "/ajax/test.action");
        MockHttpServletResponse httpResponse = new MockHttpServletResponse();
        SimpleFormController controller = new SimpleFormController();
        httpRequest.setParameter("ajax-request", "ajax-action");
        
        ModelAndView mv = ajaxExceptionResolver.resolveException(httpRequest, httpResponse, controller, new UnsupportedEventException("exception"));
        
        assertTrue(mv.wasCleared());
    }
    
    public void testResolveException2() throws Exception {
        AjaxExceptionHandlerResolver ajaxExceptionHandler = (AjaxExceptionHandlerResolver) this.applicationContext.getBean("ajaxExceptionResolver");
        
        MockHttpServletRequest httpRequest = new MockHttpServletRequest("GET", "/ajax/test.action");
        MockHttpServletResponse httpResponse = new MockHttpServletResponse();
        SimpleFormController controller = new SimpleFormController();
        httpRequest.setParameter("ajax-request", "ajax-submit");
        
        ModelAndView mv = ajaxExceptionHandler.resolveException(httpRequest, httpResponse, controller, new UnsupportedEventException("exception"));
        
        assertTrue(mv.wasCleared());
    }
    
    public void testDoNotResolveException() throws Exception {
        AjaxExceptionHandlerResolver ajaxExceptionHandler = (AjaxExceptionHandlerResolver) this.applicationContext.getBean("ajaxExceptionResolver");
        
        MockHttpServletRequest httpRequest = new MockHttpServletRequest("GET", "/test.action");
        MockHttpServletResponse httpResponse = new MockHttpServletResponse();
        SimpleFormController controller = new SimpleFormController();
        
        ModelAndView mv = ajaxExceptionHandler.resolveException(httpRequest, httpResponse, controller, new UnsupportedEventException("exception"));
        
        assertNull(mv);
    }
    
    public void testLookupExceptionResolver() {
        AjaxExceptionHandlerResolver ajaxExceptionHandler = (AjaxExceptionHandlerResolver) this.applicationContext.getBean("ajaxExceptionResolver");
        
        AjaxExceptionHandler resolver = null;
        
        resolver = ajaxExceptionHandler.lookupExceptionHandler(new UnsupportedEventException("exception"));
        assertNotNull(resolver);
        assertTrue(resolver instanceof RedirectExceptionHandler);
        assertEquals("/test/redirect1.html", ((RedirectExceptionHandler) resolver).getRedirectUrl());
        
        resolver = ajaxExceptionHandler.lookupExceptionHandler(new EventHandlingException("exception"));
        assertNotNull(resolver);
        assertTrue(resolver instanceof RedirectExceptionHandler);
        assertEquals("/test/redirect2.html", ((RedirectExceptionHandler) resolver).getRedirectUrl());
    }
    
    protected String[] getConfigLocations() {
        return new String[]{"testApplicationContext.xml"};
    }
}
