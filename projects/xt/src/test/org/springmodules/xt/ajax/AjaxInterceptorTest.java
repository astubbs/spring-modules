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

import java.util.List;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springmodules.xt.ajax.support.EventHandlingException;
import org.springmodules.xt.ajax.support.RedirectExceptionResolver;
import org.springmodules.xt.ajax.support.UnsupportedEventException;
import org.springmodules.xt.test.ajax.DummyHandler;
import org.springmodules.xt.test.ajax.DummySubmitHandler;

/**
 *
 * @author Sergio Bossa
 */
public class AjaxInterceptorTest extends AbstractDependencyInjectionSpringContextTests {
    
    public AjaxInterceptorTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(AjaxInterceptorTest.class);
        
        return suite;
    }

    public void testPreHandleSucceeds() throws Exception {
        AjaxInterceptor ajaxInterceptor = (AjaxInterceptor) this.applicationContext.getBean("ajaxInterceptor");
        
        MockHttpServletRequest httpRequest = new MockHttpServletRequest("GET", "/ajax/test.action");
        MockHttpServletResponse httpResponse = new MockHttpServletResponse();
        SimpleFormController controller = new SimpleFormController();
        httpRequest.setParameter(ajaxInterceptor.getAjaxParameter(), ajaxInterceptor.AJAX_ACTION_REQUEST);        
        httpRequest.setParameter(ajaxInterceptor.getEventParameter(), "action");
        
        ajaxInterceptor.preHandle(httpRequest, httpResponse, controller);
        
        String response1 = httpResponse.getContentAsString();
        String response2 = new DummyHandler().action(new AjaxActionEventImpl("action", httpRequest)).getResponse();
        
        assertEquals(response1, response2);
    }
    
    public void testPreHandleFails() throws Exception {
        AjaxInterceptor ajaxInterceptor = (AjaxInterceptor) this.applicationContext.getBean("ajaxInterceptor");
        
        MockHttpServletRequest httpRequest = new MockHttpServletRequest("GET", "/ajax/test.action");
        MockHttpServletResponse httpResponse = new MockHttpServletResponse();
        ModelAndView mv = new ModelAndView();
        SimpleFormController controller = new SimpleFormController();
        httpRequest.setParameter(ajaxInterceptor.getAjaxParameter(), ajaxInterceptor.AJAX_ACTION_REQUEST);
        httpRequest.setParameter(ajaxInterceptor.getEventParameter(), "fail");
        
        try {
            ajaxInterceptor.preHandle(httpRequest, httpResponse, controller);
            fail("Should throw UnsupportedEventException!");
        }
        catch(UnsupportedEventException ex) {}
    }
    
    public void testPreHandleWithException() throws Exception {
        AjaxInterceptor ajaxInterceptor = (AjaxInterceptor) this.applicationContext.getBean("ajaxInterceptorWithExceptionMappings");
        
        MockHttpServletRequest httpRequest = new MockHttpServletRequest("GET", "/ajax/test.action");
        MockHttpServletResponse httpResponse = new MockHttpServletResponse();
        SimpleFormController controller = new SimpleFormController();
        httpRequest.setParameter(ajaxInterceptor.getAjaxParameter(), ajaxInterceptor.AJAX_ACTION_REQUEST);        
        httpRequest.setParameter(ajaxInterceptor.getEventParameter(), "unsupportedEvent");
        
        ajaxInterceptor.preHandle(httpRequest, httpResponse, controller);
        
        String response = httpResponse.getContentAsString();
        
        assertTrue(response.contains("/test/redirect1.html"));
        
        System.out.println(response);
        
        // New test:
        
        httpRequest = new MockHttpServletRequest("GET", "/ajax/test.action");
        httpResponse = new MockHttpServletResponse();
        controller = new SimpleFormController();
        httpRequest.setParameter(ajaxInterceptor.getAjaxParameter(), ajaxInterceptor.AJAX_ACTION_REQUEST);        
        httpRequest.setParameter(ajaxInterceptor.getEventParameter(), "actionWithException");
        
        ajaxInterceptor.preHandle(httpRequest, httpResponse, controller);
        
        response = httpResponse.getContentAsString();
        
        assertTrue(response.contains("/test/redirect2.html"));
        
        System.out.println(response);
    }

    public void testPostHandleSucceeds() throws Exception {
        AjaxInterceptor ajaxInterceptor = (AjaxInterceptor) this.applicationContext.getBean("ajaxInterceptor");
        
        XmlWebApplicationContext springContext = new XmlWebApplicationContext();
        MockServletContext servletContext = new MockServletContext();
        springContext.setConfigLocations(this.getConfigLocations());
        springContext.setServletContext(servletContext);
        springContext.refresh();
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, springContext);
        
        MockHttpServletRequest httpRequest = new MockHttpServletRequest(servletContext, "POST", "/ajax/submit.action");
        MockHttpSession session = new MockHttpSession(servletContext);
        httpRequest.setSession(session);
        httpRequest.setAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE, springContext);
        httpRequest.setParameter(ajaxInterceptor.getAjaxParameter(), ajaxInterceptor.AJAX_SUBMIT_REQUEST);        
        httpRequest.setParameter(ajaxInterceptor.getEventParameter(), "validate");
        
        MockHttpServletResponse httpResponse = new MockHttpServletResponse();
        
        ModelAndView mv = new ModelAndView("ajax-redirect:/ajax/success.page");
        SimpleFormController controller = new SimpleFormController();
        
        ajaxInterceptor.postHandle(httpRequest, httpResponse, controller, mv);
        
        assertEquals("<?xml version=\"1.0\"?> <taconite-root xml:space=\"preserve\"> <taconite-redirect targetUrl=\"/ajax/success.page\" parseInBrowser=\"true\"></taconite-redirect> </taconite-root>", 
                httpResponse.getContentAsString());
        
        // New test:
        
        httpRequest = new MockHttpServletRequest(servletContext, "POST", "/ajax/submit.action");
        session = new MockHttpSession(servletContext);
        httpRequest.setSession(session);
        httpRequest.setAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE, springContext);
        httpRequest.setParameter(ajaxInterceptor.getAjaxParameter(), ajaxInterceptor.AJAX_SUBMIT_REQUEST);        
        httpRequest.setParameter(ajaxInterceptor.getEventParameter(), "submit");
        
        httpResponse = new MockHttpServletResponse();
        
        controller = new SimpleFormController();
        mv = new ModelAndView("");
        
        ajaxInterceptor.postHandle(httpRequest, httpResponse, controller, mv);
        
        String response1 = httpResponse.getContentAsString();
        String response2 = new DummySubmitHandler().submit(new AjaxSubmitEventImpl("submit", httpRequest)).getResponse();
        
        assertEquals(response1, response2);
    }
    
    public void testPostHandleFails() throws Exception {
        AjaxInterceptor ajaxInterceptor = (AjaxInterceptor) this.applicationContext.getBean("ajaxInterceptor");
        
        XmlWebApplicationContext springContext = new XmlWebApplicationContext();
        MockServletContext servletContext = new MockServletContext();
        springContext.setConfigLocations(this.getConfigLocations());
        springContext.setServletContext(servletContext);
        springContext.refresh();
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, springContext);
        
        MockHttpServletRequest httpRequest = new MockHttpServletRequest(servletContext, "POST", "/ajax/submit.action");
        MockHttpSession session = new MockHttpSession(servletContext);
        httpRequest.setSession(session);
        httpRequest.setAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE, springContext);
        httpRequest.setParameter(ajaxInterceptor.getAjaxParameter(), ajaxInterceptor.AJAX_SUBMIT_REQUEST);
        httpRequest.setParameter(ajaxInterceptor.getEventParameter(), "fail");
        
        MockHttpServletResponse httpResponse = new MockHttpServletResponse();
        
        ModelAndView mv = new ModelAndView("ajax-redirect:/ajax/success.page");
        SimpleFormController controller = new SimpleFormController();
        
        try {
            ajaxInterceptor.postHandle(httpRequest, httpResponse, controller, mv);
            fail("Should throw UnsupportedEventException!");
        }
        catch(UnsupportedEventException ex) {}
    }
    
    public void testPostHandleWithException() throws Exception {
        AjaxInterceptor ajaxInterceptor = (AjaxInterceptor) this.applicationContext.getBean("ajaxInterceptorWithExceptionMappings");
        
        XmlWebApplicationContext springContext = new XmlWebApplicationContext();
        MockServletContext servletContext = new MockServletContext();
        springContext.setConfigLocations(this.getConfigLocations());
        springContext.setServletContext(servletContext);
        springContext.refresh();
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, springContext);
        
        MockHttpServletRequest httpRequest = new MockHttpServletRequest(servletContext, "POST", "/ajax/submit.action");
        MockHttpSession session = new MockHttpSession(servletContext);
        httpRequest.setSession(session);
        httpRequest.setAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE, springContext);
        httpRequest.setParameter(ajaxInterceptor.getAjaxParameter(), ajaxInterceptor.AJAX_SUBMIT_REQUEST);        
        httpRequest.setParameter(ajaxInterceptor.getEventParameter(), "unsupportedEvent");
        
        MockHttpServletResponse httpResponse = new MockHttpServletResponse();
        
        ModelAndView mv = new ModelAndView("");
        SimpleFormController controller = new SimpleFormController();
        
        ajaxInterceptor.postHandle(httpRequest, httpResponse, controller, mv);
        
        String response = httpResponse.getContentAsString();
        
        assertTrue(response.contains("/test/redirect1.html"));
        
        System.out.println(response);
        
        // New test:
        
        httpRequest = new MockHttpServletRequest(servletContext, "POST", "/ajax/submit.action");
        session = new MockHttpSession(servletContext);
        httpRequest.setSession(session);
        httpRequest.setAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE, springContext);
        httpRequest.setParameter(ajaxInterceptor.getAjaxParameter(), ajaxInterceptor.AJAX_SUBMIT_REQUEST);        
        httpRequest.setParameter(ajaxInterceptor.getEventParameter(), "submitWithException");
        
        httpResponse = new MockHttpServletResponse();
        
        controller = new SimpleFormController();
        mv = new ModelAndView("");
        
        ajaxInterceptor.postHandle(httpRequest, httpResponse, controller, mv);
        
        response = httpResponse.getContentAsString();
        
        assertTrue(response.contains("/test/redirect2.html"));
        
        System.out.println(response);
    }
    
    public void testLookupHandlers() throws Exception {
        AjaxInterceptor ajaxInterceptor = (AjaxInterceptor) this.applicationContext.getBean("ajaxInterceptor");
        
        MockHttpServletRequest httpRequest = null;
        List<AjaxHandler> handlers = null;
        
        httpRequest = new MockHttpServletRequest("GET", "/ajax/test.action");
        handlers = ajaxInterceptor.lookupHandlers(httpRequest);
        assertEquals(1, handlers.size());
        
        httpRequest = new MockHttpServletRequest("GET", "/ajax/submit.action");
        handlers = ajaxInterceptor.lookupHandlers(httpRequest);
        assertEquals(2, handlers.size());
        
        httpRequest = new MockHttpServletRequest("GET", "/ajax/chainedSubmit.action");
        handlers = ajaxInterceptor.lookupHandlers(httpRequest);
        assertEquals(2, handlers.size());
        
        httpRequest = new MockHttpServletRequest("GET", "/ajax/no.action");
        handlers = ajaxInterceptor.lookupHandlers(httpRequest);
        assertEquals(0, handlers.size());
    }
    
    public void testLookupExceptionResolver() {
        AjaxInterceptor ajaxInterceptor = (AjaxInterceptor) this.applicationContext.getBean("ajaxInterceptorWithExceptionMappings");
        
        AjaxExceptionResolver resolver = null;
        
        resolver = ajaxInterceptor.lookupExceptionResolver(new UnsupportedEventException("exception"));
        assertNotNull(resolver);
        assertTrue(resolver instanceof RedirectExceptionResolver);
        assertEquals("/test/redirect1.html", ((RedirectExceptionResolver) resolver).getRedirectUrl());
        
        resolver = ajaxInterceptor.lookupExceptionResolver(new EventHandlingException("exception"));
        assertNotNull(resolver);
        assertTrue(resolver instanceof RedirectExceptionResolver);
        assertEquals("/test/redirect2.html", ((RedirectExceptionResolver) resolver).getRedirectUrl());
    }
    
    protected String[] getConfigLocations() {
        return new String[]{"testApplicationContext.xml"};
    }
}
