/**
 * Created on Sep 12, 2005
 *
 * $Id: OpenSessionInViewTests.java,v 1.2 2006/03/07 13:09:31 costin Exp $
 * $Revision: 1.2 $
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
import org.springmodules.jcr.SessionHolder;
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
        
        MockServletContext sc = new MockServletContext();
        MockHttpServletRequest request = new MockHttpServletRequest(sc);
        MockHttpServletResponse response = new MockHttpServletResponse();

        sfControl.expectAndReturn(sf.getSession(), session);
        SessionHolder holder = new SessionHolder(session);
        sfControl.expectAndReturn(sf.getSessionHolder(session), holder);
        sfControl.replay();
        sessionControl.replay();
        
        interceptor.setSessionFactory(sf);
        interceptor.afterPropertiesSet();

        interceptor.preHandle(request, response, "handler");
        assertTrue(TransactionSynchronizationManager.hasResource(sf));
        assertSame(holder, TransactionSynchronizationManager.getResource(sf));

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
        assertSame(holder, TransactionSynchronizationManager.getResource(sf));
        
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
        final Session session = (Session) sessionControl.getMock();

        // set up the session factory
        sfControl.expectAndReturn(sf.getSession(), session);
        final SessionHolder holder = new SessionHolder(session);
        sfControl.expectAndReturn(sf.getSessionHolder(session), holder);
        
        session.logout();
        sessionControl.setVoidCallable(1);
        
        sfControl.replay();
        sessionControl.replay();
        

        // set up the second session factory
        MockControl sf2Control = MockControl.createControl(SessionFactory.class);
        final SessionFactory sf2 = (SessionFactory) sf2Control.getMock();
        MockControl session2Control = MockControl.createControl(Session.class);
        final Session session2 = (Session) session2Control.getMock();
        

        sf2Control.expectAndReturn(sf2.getSession(), session2);
        final SessionHolder holder2 = new SessionHolder(session2);
        sf2Control.expectAndReturn(sf2.getSessionHolder(session2), holder2);
        session2.logout();
        session2Control.setVoidCallable(1);
        
        //session2Control.expectAndReturn(session2.getRepository(), repo);
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

        MockFilterConfig filterConfig3 = new MockFilterConfig(wac.getServletContext(), "filter3");

        final OpenSessionInViewFilter filter = new OpenSessionInViewFilter();
        filter.init(filterConfig);
        final OpenSessionInViewFilter filter2 = new OpenSessionInViewFilter();
        filter2.init(filterConfig2);
        final OpenSessionInViewFilter filter3 = new OpenSessionInViewFilter();
        filter3.init(filterConfig3);

        final FilterChain filterChain = new FilterChain() {
            public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse)
                throws IOException, ServletException {
                assertTrue(TransactionSynchronizationManager.hasResource(sf));
                // check sf-related things
                assertSame(holder, TransactionSynchronizationManager.getResource(sf));
                assertSame(session, holder.getSession());
                
                servletRequest.setAttribute("invoked", Boolean.TRUE);
            }
        };

        final FilterChain filterChain2 = new FilterChain() {
            public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse)
                throws IOException, ServletException {
                assertTrue(TransactionSynchronizationManager.hasResource(sf));
                // check sf-related things
                assertSame(holder, TransactionSynchronizationManager.getResource(sf));
                assertSame(session, holder.getSession());
                
                filter3.doFilter(servletRequest, servletResponse, filterChain);
            }
        };

        final FilterChain filterChain3 = new FilterChain() {
            public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse)
                throws IOException, ServletException {
                assertTrue(TransactionSynchronizationManager.hasResource(sf2));
                // check sf2-related things
                assertSame(holder2, TransactionSynchronizationManager.getResource(sf2));
                assertSame(session2, holder2.getSession());
                
                filter.doFilter(servletRequest, servletResponse, filterChain2);
            }
        };

        FilterChain filterChain4 = new FilterChain() {
            public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse)
                throws IOException, ServletException {
                filter2.doFilter(servletRequest, servletResponse, filterChain3);
            }
        };

        assertFalse(TransactionSynchronizationManager.hasResource(sf));
        assertFalse(TransactionSynchronizationManager.hasResource(sf2));
        filter2.doFilter(request, response, filterChain4);
        assertFalse(TransactionSynchronizationManager.hasResource(sf));
        assertFalse(TransactionSynchronizationManager.hasResource(sf2));
        assertNotNull(request.getAttribute("invoked"));

        sfControl.verify();
        sessionControl.verify();
        sf2Control.verify();
        session2Control.verify();

        wac.close();
    }


}
