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

    public void testPreHandle() throws Exception {
        MockHttpServletRequest httpRequest = new MockHttpServletRequest("GET", "/ajax/test.action");
        MockHttpServletResponse httpResponse = new MockHttpServletResponse();
        ModelAndView mv = new ModelAndView();
        SimpleFormController controller = new SimpleFormController();
        AjaxInterceptor ajaxInterceptor = (AjaxInterceptor) this.applicationContext.getBean("ajaxInterceptor");
        
        httpRequest.setParameter(ajaxInterceptor.getAjaxParameter(), ajaxInterceptor.AJAX_ACTION_REQUEST);
        httpRequest.setParameter(ajaxInterceptor.getEventParameter(), "fail");
        
        try {
            ajaxInterceptor.preHandle(httpRequest, httpResponse, controller);
            fail();
        }
        catch(Exception ex) {}
        
        httpResponse = new MockHttpServletResponse();
        
        httpRequest.setParameter(ajaxInterceptor.getEventParameter(), "action");
        
        ajaxInterceptor.preHandle(httpRequest, httpResponse, controller);
        
        String response1 = httpResponse.getContentAsString();
        String response2 = new DummyHandler().action(new AjaxActionEventImpl("action", httpRequest)).getResponse();
        
        assertEquals(response1, response2);
    }

    public void testPostHandle() throws Exception {
        MockServletContext servletContext = new MockServletContext();
        XmlWebApplicationContext springContext = new XmlWebApplicationContext();
        
        springContext.setConfigLocations(this.getConfigLocations());
        springContext.setServletContext(servletContext);
        springContext.refresh();
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, springContext);
        
        MockHttpServletRequest httpRequest = new MockHttpServletRequest(servletContext, "POST", "/ajax/submit.action");
        MockHttpSession session = new MockHttpSession(servletContext);
        httpRequest.setSession(session);
        httpRequest.setAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE, springContext);
        
        MockHttpServletResponse httpResponse = new MockHttpServletResponse();
        
        ModelAndView mv = new ModelAndView("ajax-redirect:/ajax/success.page");
        SimpleFormController controller = new SimpleFormController();
        
        
        AjaxInterceptor ajaxInterceptor = (AjaxInterceptor) this.applicationContext.getBean("ajaxInterceptor");
        
        httpRequest.setParameter(ajaxInterceptor.getAjaxParameter(), ajaxInterceptor.AJAX_SUBMIT_REQUEST);
        httpRequest.setParameter(ajaxInterceptor.getEventParameter(), "fail");
        
        try {
            ajaxInterceptor.postHandle(httpRequest, httpResponse, controller, mv);
            fail();
        }
        catch(Exception ex) {}
        
        httpResponse = new MockHttpServletResponse();
        
        httpRequest.setParameter(ajaxInterceptor.getEventParameter(), "validate");
        
        ajaxInterceptor.postHandle(httpRequest, httpResponse, controller, mv);
        
        assertEquals("<?xml version=\"1.0\"?> <taconite-root xml:space=\"preserve\"> <taconite-redirect targetUrl=\"/ajax/success.page\" parseInBrowser=\"true\"></taconite-redirect> </taconite-root>", 
                httpResponse.getContentAsString());
        
        httpResponse = new MockHttpServletResponse();
        
        httpRequest.setParameter(ajaxInterceptor.getEventParameter(), "submit");
        
        ajaxInterceptor.postHandle(httpRequest, httpResponse, controller, mv);
        
        String response1 = httpResponse.getContentAsString();
        String response2 = new DummySubmitHandler().submit(new AjaxSubmitEventImpl("submit", httpRequest)).getResponse();
        
        assertEquals(response1, response2);
    }
    
    protected String[] getConfigLocations() {
        return new String[]{"testApplicationContext.xml"};
    }
}
