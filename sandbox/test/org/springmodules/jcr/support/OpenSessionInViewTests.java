/**
 * Created on Sep 12, 2005
 *
 * $Id: OpenSessionInViewTests.java,v 1.1 2005/09/26 10:21:53 costin Exp $
 * $Revision: 1.1 $
 */
package org.springmodules.jcr.support;

import java.io.IOException;

import javax.jcr.Session;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import junit.framework.TestCase;

import org.easymock.MockControl;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.StaticWebApplicationContext;
import org.springmodules.jcr.SessionFactory;
import org.springmodules.jcr.jackrabbit.support.OpenSessionInViewFilter;
import org.springmodules.jcr.jackrabbit.support.OpenSessionInViewInterceptor;
/**
 * @author Costin Leau
 *
 */
public class OpenSessionInViewTests extends TestCase {

    public void testOpenSessionInViewInterceptor() throws Exception {
        MockControl sfControl = MockControl.createControl(SessionFactory.class);
        SessionFactory sf = (SessionFactory) sfControl.getMock();
        MockControl sessionControl = MockControl.createControl(Session.class);
        Session session = (Session) sessionControl.getMock();

        OpenSessionInViewInterceptor interceptor = new OpenSessionInViewInterceptor();
        interceptor.setSessionFactory(sf);
        MockServletContext sc = new MockServletContext();
        MockHttpServletRequest request = new MockHttpServletRequest(sc);
        MockHttpServletResponse response = new MockHttpServletResponse();

        sf.getSession();
        sfControl.setReturnValue(session, 1);
        sfControl.replay();
        sessionControl.replay();
        interceptor.preHandle(request, response, "handler");
        assertTrue(TransactionSynchronizationManager.hasResource(sf));

        // check that further invocations simply participate
        interceptor.preHandle(request, response, "handler");

        interceptor.preHandle(request, response, "handler");
        interceptor.postHandle(request, response, "handler", null);
        interceptor.afterCompletion(request, response, "handler", null);

        interceptor.postHandle(request, response, "handler", null);
        interceptor.afterCompletion(request, response, "handler", null);

        interceptor.preHandle(request, response, "handler");
        interceptor.postHandle(request, response, "handler", null);
        interceptor.afterCompletion(request, response, "handler", null);

        sfControl.verify();
        sessionControl.verify();

        sfControl.reset();
        sessionControl.reset();
        sfControl.replay();
        sessionControl.replay();
        interceptor.postHandle(request, response, "handler", null);
        assertTrue(TransactionSynchronizationManager.hasResource(sf));
        sfControl.verify();
        sessionControl.verify();

        sfControl.reset();
        sessionControl.reset();
        session.logout();
        sessionControl.setVoidCallable(1);
        sfControl.replay();
        sessionControl.replay();
        interceptor.afterCompletion(request, response, "handler", null);
        assertFalse(TransactionSynchronizationManager.hasResource(sf));
        sfControl.verify();
        sessionControl.verify();
    }

    public void testOpenSessionInViewFilter() throws Exception {
        MockControl sfControl = MockControl.createControl(SessionFactory.class);
        final SessionFactory sf = (SessionFactory) sfControl.getMock();
        MockControl sessionControl = MockControl.createControl(Session.class);
        Session session = (Session) sessionControl.getMock();

        sf.getSession();
        sfControl.setReturnValue(session, 1);
        session.logout();
        sessionControl.setVoidCallable(1);
        sfControl.replay();
        sessionControl.replay();

        MockControl sf2Control = MockControl.createControl(SessionFactory.class);
        final SessionFactory sf2 = (SessionFactory) sf2Control.getMock();
        MockControl session2Control = MockControl.createControl(Session.class);
        Session session2 = (Session) session2Control.getMock();

        sf2.getSession();
        sf2Control.setReturnValue(session2, 1);
        session2.logout();
        session2Control.setVoidCallable(1);
        sf2Control.replay();
        session2Control.replay();

        MockServletContext sc = new MockServletContext();
        StaticWebApplicationContext wac = new StaticWebApplicationContext();
        wac.setServletContext(sc);
        wac.getDefaultListableBeanFactory().registerSingleton("sessionFactory", sf);
        wac.getDefaultListableBeanFactory().registerSingleton("mySessionFactory", sf2);
        wac.refresh();
        sc.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, wac);
        MockHttpServletRequest request = new MockHttpServletRequest(sc);
        MockHttpServletResponse response = new MockHttpServletResponse();

        MockFilterConfig filterConfig = new MockFilterConfig(wac.getServletContext(), "filter");
        MockFilterConfig filterConfig2 = new MockFilterConfig(wac.getServletContext(), "filter2");
        filterConfig2.addInitParameter("sessionFactoryBeanName", "mySessionFactory");

        final OpenSessionInViewFilter filter = new OpenSessionInViewFilter();
        filter.init(filterConfig);
        final OpenSessionInViewFilter filter2 = new OpenSessionInViewFilter();
        filter2.init(filterConfig2);

        final FilterChain filterChain = new FilterChain() {
            public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse)
                throws IOException, ServletException {
                assertTrue(TransactionSynchronizationManager.hasResource(sf));
                servletRequest.setAttribute("invoked", Boolean.TRUE);
            }
        };

        final FilterChain filterChain2 = new FilterChain() {
            public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse)
                throws IOException, ServletException {
                assertTrue(TransactionSynchronizationManager.hasResource(sf2));
                filter.doFilter(servletRequest, servletResponse, filterChain);
            }
        };

        FilterChain filterChain3 = new FilterChain() {
            public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse)
                throws IOException, ServletException {
                filter2.doFilter(servletRequest, servletResponse, filterChain2);
            }
        };

        assertFalse(TransactionSynchronizationManager.hasResource(sf));
        assertFalse(TransactionSynchronizationManager.hasResource(sf2));
        filter2.doFilter(request, response, filterChain3);
        assertFalse(TransactionSynchronizationManager.hasResource(sf));
        assertFalse(TransactionSynchronizationManager.hasResource(sf2));
        assertNotNull(request.getAttribute("invoked"));

        sfControl.verify();
        sessionControl.verify();
        sfControl.verify();
        sessionControl.verify();

        wac.close();
    }


}
