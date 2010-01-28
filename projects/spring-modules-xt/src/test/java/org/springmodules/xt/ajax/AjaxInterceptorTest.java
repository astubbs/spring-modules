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
import org.springmodules.xt.ajax.support.NoMatchingHandlerException;
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

    public void testPreHandleSucceeds() throws Exception {
        AjaxInterceptor ajaxInterceptor = (AjaxInterceptor) this.applicationContext.getBean("ajaxInterceptor");
        
        MockHttpServletRequest httpRequest = new MockHttpServletRequest("GET", "/ajax/test.action");
        MockHttpServletResponse httpResponse = new MockHttpServletResponse();
        SimpleFormController controller = new SimpleFormController();
        httpRequest.setParameter(ajaxInterceptor.getAjaxParameter(), ajaxInterceptor.AJAX_ACTION_REQUEST);        
        httpRequest.setParameter(ajaxInterceptor.getEventParameter(), "action");
        
        ajaxInterceptor.preHandle(httpRequest, httpResponse, controller);
        
        String response1 = httpResponse.getContentAsString();
        String response2 = new DummyHandler().action(new AjaxActionEventImpl("action", httpRequest)).render();
        
        assertEquals(response1, response2);
    }
    
    public void testPreHandleFailsWithUnsupportedEventException() throws Exception {
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
        catch(Exception ex) {
            fail("Should throw UnsupportedEventException!");
        }
    }
    
    public void testPreHandleFailsWithNoMatchingHandlerException() throws Exception {
        AjaxInterceptor ajaxInterceptor = (AjaxInterceptor) this.applicationContext.getBean("ajaxInterceptor");
        
        MockHttpServletRequest httpRequest = new MockHttpServletRequest("GET", "/ajax/test2.action");
        MockHttpServletResponse httpResponse = new MockHttpServletResponse();
        ModelAndView mv = new ModelAndView();
        SimpleFormController controller = new SimpleFormController();
        httpRequest.setParameter(ajaxInterceptor.getAjaxParameter(), ajaxInterceptor.AJAX_ACTION_REQUEST);
        httpRequest.setParameter(ajaxInterceptor.getEventParameter(), "fail");
        
        try {
            ajaxInterceptor.preHandle(httpRequest, httpResponse, controller);
            fail("Should throw NoMatchingHandlerException!");
        }
        catch(NoMatchingHandlerException ex) {}
        catch(Exception ex) {
            fail("Should throw NoMatchingHandlerException!");
        }
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
        httpRequest.setParameter(ajaxInterceptor.getEventParameter(), "submit");
        
        MockHttpServletResponse httpResponse = new MockHttpServletResponse();
        SimpleFormController controller = new SimpleFormController();
        ModelAndView mv = new ModelAndView("");
        
        ajaxInterceptor.postHandle(httpRequest, httpResponse, controller, mv);
        
        String response1 = httpResponse.getContentAsString();
        String response2 = new DummySubmitHandler().submit(new AjaxSubmitEventImpl("submit", httpRequest)).render();
        
        assertEquals(response1, response2);
    }
    
    public void testPostHandleWithAjaxRedirectPrefix() throws Exception {
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
        
        assertTrue(httpResponse.getContentAsString().indexOf("/ajax/success.page") != -1);
    }
    
    public void testPostHandleWithStandardRedirectPrefix() throws Exception {
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
        ModelAndView mv = new ModelAndView("redirect:/ajax/success.page");
        SimpleFormController controller = new SimpleFormController();
        
        ajaxInterceptor.postHandle(httpRequest, httpResponse, controller, mv);
        
        assertTrue(httpResponse.getContentAsString().indexOf("/ajax/success.page") != -1);
    }
    
    public void testPostHandleFailsWithUnsupportedEventException() throws Exception {
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
        catch(Exception ex) {
            fail("Should throw UnsupportedEventException!");
        }
    }
    
    public void testPostHandleFailsWithNoMatchingHandlerException() throws Exception {
        AjaxInterceptor ajaxInterceptor = (AjaxInterceptor) this.applicationContext.getBean("ajaxInterceptor");
        
        XmlWebApplicationContext springContext = new XmlWebApplicationContext();
        MockServletContext servletContext = new MockServletContext();
        springContext.setConfigLocations(this.getConfigLocations());
        springContext.setServletContext(servletContext);
        springContext.refresh();
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, springContext);
        
        MockHttpServletRequest httpRequest = new MockHttpServletRequest(servletContext, "POST", "/ajax/submit2.action");
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
            fail("Should throw NoMatchingHandlerException!");
        }
        catch(NoMatchingHandlerException ex) {}
        catch(Exception ex) {
            fail("Should throw NoMatchingHandlerException!");
        }
    }
    
    public void testPostHandleStopsBecauseOfNoModelAndView() throws Exception {
        AjaxInterceptor ajaxInterceptor = (AjaxInterceptor) this.applicationContext.getBean("ajaxInterceptor");
        
        XmlWebApplicationContext springContext = new XmlWebApplicationContext();
        MockServletContext servletContext = new MockServletContext();
        springContext.setConfigLocations(this.getConfigLocations());
        springContext.setServletContext(servletContext);
        springContext.refresh();
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, springContext);
        
        MockHttpServletRequest httpRequest = new MockHttpServletRequest(servletContext, "POST", "/ajax/simple.page");
        MockHttpSession session = new MockHttpSession(servletContext);
        httpRequest.setSession(session);
        httpRequest.setAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE, springContext);
        
        MockHttpServletResponse httpResponse = new MockHttpServletResponse();
        
        SimpleFormController controller = new SimpleFormController();
        
        ajaxInterceptor.postHandle(httpRequest, httpResponse, controller, null);
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
    
    protected String[] getConfigLocations() {
        return new String[]{"testApplicationContext.xml"};
    }
}
