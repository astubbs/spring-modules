/**
 * Created on Sep 12, 2005
 *
 * $Id: OpenSessionInViewTests.java,v 1.1 2005/12/20 17:38:24 costin Exp $
 * $Revision: 1.1 $
 */
package org.springmodules.jcr.support;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.jcr.Repository;
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
import org.springmodules.jcr.SessionHolderProvider;
import org.springmodules.jcr.SessionHolderProviderManager;
import org.springmodules.jcr.jackrabbit.support.JackRabbitSessionHolderProvider;
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

        MockControl repoCtrl = MockControl.createNiceControl(Repository.class);
        Repository repo = (Repository) repoCtrl.getMock();
        
        SessionHolderProviderManager providerManager = new ListSessionHolderProviderManager();
        OpenSessionInViewInterceptor interceptor = new OpenSessionInViewInterceptor();
        
        MockServletContext sc = new MockServletContext();
        MockHttpServletRequest request = new MockHttpServletRequest(sc);
        MockHttpServletResponse response = new MockHttpServletResponse();

        sessionControl.expectAndReturn(session.getRepository(), repo);
        
        sf.getSession();
        sfControl.setReturnValue(session, 2);
        sfControl.replay();
        sessionControl.replay();
        repoCtrl.replay();
        
        interceptor.setSessionFactory(sf);
        interceptor.setProviderManager(providerManager);
        interceptor.afterPropertiesSet();

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
        final Session session = (Session) sessionControl.getMock();
        MockControl repoCtrl = MockControl.createControl(Repository.class);
        Repository repo = (Repository) repoCtrl.getMock();

        SessionHolderProvider jrProvider = new JackRabbitSessionHolderProvider();
        List list = new ArrayList();
        list.add(jrProvider);
        
        final ListSessionHolderProviderManager providerManager = new ListSessionHolderProviderManager();
        providerManager.setProviders(list);
        
        sf.getSession();
        sfControl.setReturnValue(session, 2);
        session.logout();
        sessionControl.setVoidCallable(1);
        // return bogus info
        repoCtrl.expectAndReturn(repo.getDescriptor(Repository.REP_NAME_DESC), "hocus-pocus");
        
        sessionControl.expectAndReturn(session.getRepository(), repo);
        
        sfControl.replay();
        sessionControl.replay();
        

        MockControl sf2Control = MockControl.createControl(SessionFactory.class);
        final SessionFactory sf2 = (SessionFactory) sf2Control.getMock();
        MockControl session2Control = MockControl.createControl(Session.class);
        final Session session2 = (Session) session2Control.getMock();
        

        sf2.getSession();
        sf2Control.setReturnValue(session2, 2);
        session2.logout();
        session2Control.setVoidCallable(1);
        // return bogus info
        repoCtrl.expectAndReturn(repo.getDescriptor(Repository.REP_NAME_DESC), "blabla");
        
        session2Control.expectAndReturn(session2.getRepository(), repo);
        sf2Control.replay();
        session2Control.replay();
        repoCtrl.replay();

        MockServletContext sc = new MockServletContext();
        StaticWebApplicationContext wac = new StaticWebApplicationContext();
        wac.setServletContext(sc);
        wac.getDefaultListableBeanFactory().registerSingleton("sessionFactory", sf);
        wac.getDefaultListableBeanFactory().registerSingleton("mySessionFactory", sf2);
        wac.getDefaultListableBeanFactory().registerSingleton("providerManager", providerManager);
        wac.refresh();
        sc.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, wac);
        MockHttpServletRequest request = new MockHttpServletRequest(sc);
        MockHttpServletResponse response = new MockHttpServletResponse();

        MockFilterConfig filterConfig = new MockFilterConfig(wac.getServletContext(), "filter");
        filterConfig.addInitParameter("sessionHolderProviderManagerBeanName", "providerManager");
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
                Object obj = TransactionSynchronizationManager.getResource(sf);
                assertTrue(obj instanceof SessionHolder);
                assertSame(session, ((SessionHolder)obj).getSession());
                
                servletRequest.setAttribute("invoked", Boolean.TRUE);
            }
        };

        final FilterChain filterChain2 = new FilterChain() {
            public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse)
                throws IOException, ServletException {
                assertTrue(TransactionSynchronizationManager.hasResource(sf));
                // check sf-related things
                Object obj = TransactionSynchronizationManager.getResource(sf);
                assertTrue(obj instanceof SessionHolder);
                assertSame(session, ((SessionHolder)obj).getSession());
                
                filter3.doFilter(servletRequest, servletResponse, filterChain);
            }
        };

        final FilterChain filterChain3 = new FilterChain() {
            public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse)
                throws IOException, ServletException {
                assertTrue(TransactionSynchronizationManager.hasResource(sf2));
                // check sf2-related things
                Object obj = TransactionSynchronizationManager.getResource(sf2);
                assertTrue(obj instanceof SessionHolder);
                assertSame(session2, ((SessionHolder)obj).getSession());
                
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
        sfControl.verify();
        sessionControl.verify();
        //providerManagerCtrl.verify();

        wac.close();
    }


}
