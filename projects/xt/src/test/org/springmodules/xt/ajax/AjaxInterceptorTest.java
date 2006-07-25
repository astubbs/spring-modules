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
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/ajax/test.action");
        MockHttpServletResponse response = new MockHttpServletResponse();
        ModelAndView mv = new ModelAndView();
        SimpleFormController controller = new SimpleFormController();
        AjaxInterceptor ajaxInterceptor = (AjaxInterceptor) this.applicationContext.getBean("ajaxInterceptor");
        
        request.setParameter(ajaxInterceptor.getAjaxParameter(), ajaxInterceptor.AJAX_ACTION_REQUEST);
        request.setParameter(ajaxInterceptor.getEventParameter(), "fail");
        
        try {
            ajaxInterceptor.preHandle(request, response, controller);
            fail();
        }
        catch(Exception ex) {}
        
        request.setParameter(ajaxInterceptor.getEventParameter(), "test");
        
        ajaxInterceptor.preHandle(request, response, controller);
    }

    public void testPostHandle() throws Exception {
        MockServletContext servletContext = new MockServletContext();
        XmlWebApplicationContext springContext = new XmlWebApplicationContext();
        
        springContext.setConfigLocations(this.getConfigLocations());
        springContext.setServletContext(servletContext);
        springContext.refresh();
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, springContext);
        
        MockHttpServletRequest request = new MockHttpServletRequest(servletContext, "POST", "/ajax/test.action");
        MockHttpSession session = new MockHttpSession(servletContext);
        request.setSession(session);
        request.setAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE, springContext);
        
        MockHttpServletResponse response = new MockHttpServletResponse();
        
        ModelAndView mv = new ModelAndView("ajax-redirect:/ajax/success.page");
        SimpleFormController controller = new SimpleFormController();
        
        
        AjaxInterceptor ajaxInterceptor = (AjaxInterceptor) this.applicationContext.getBean("ajaxInterceptor");
        
        request.setParameter(ajaxInterceptor.getAjaxParameter(), ajaxInterceptor.AJAX_SUBMIT_REQUEST);
        request.setParameter(ajaxInterceptor.getEventParameter(), "test");
        
        try {
            ajaxInterceptor.postHandle(request, response, controller, mv);
            fail();
        }
        catch(Exception ex) {}
        
        request.setParameter(ajaxInterceptor.getEventParameter(), "validate");
        
        ajaxInterceptor.postHandle(request, response, controller, mv);
    }
    
    protected String[] getConfigLocations() {
        return new String[]{"testApplicationContext.xml"};
    }
}
