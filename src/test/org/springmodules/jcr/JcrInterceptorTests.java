/**
 * Created on Sep 12, 2005
 *
 * $Id: JcrInterceptorTests.java,v 1.2 2005/11/11 15:47:08 costin Exp $
 * $Revision: 1.2 $
 */
package org.springmodules.jcr;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import junit.framework.TestCase;

import org.aopalliance.intercept.Interceptor;
import org.aopalliance.intercept.Invocation;
import org.aopalliance.intercept.MethodInvocation;
import org.easymock.MockControl;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * @author Costin Leau
 * 
 */
public class JcrInterceptorTests extends TestCase {

    public void testInterceptor() throws RepositoryException {
        MockControl sfControl = MockControl.createControl(SessionFactory.class);
        SessionFactory sf = (SessionFactory) sfControl.getMock();
        MockControl sessionControl = MockControl.createControl(Session.class);
        Session session = (Session) sessionControl.getMock();
        MockControl repositoryControl = MockControl.createNiceControl(Repository.class);
        Repository repo = (Repository) repositoryControl.getMock();

        repositoryControl.replay();
        
        sf.getSession();
        sfControl.setReturnValue(session, 2);
        session.logout();
        sessionControl.setVoidCallable(1);
        sessionControl.expectAndReturn(session.getRepository(), repo);
        sfControl.replay();
        sessionControl.replay();

        JcrInterceptor interceptor = new JcrInterceptor();
        interceptor.setSessionFactory(sf);
        interceptor.afterPropertiesSet();
        try {
            interceptor.invoke(new TestInvocation(sf));
        } catch (Throwable t) {
            fail("Should not have thrown Throwable: " + t);
        }

        sfControl.verify();
        sessionControl.verify();
    }

    public void testInterceptorWithPrebound() {
        MockControl sfControl = MockControl.createControl(SessionFactory.class);
        SessionFactory sf = (SessionFactory) sfControl.getMock();
        MockControl sessionControl = MockControl.createControl(Session.class);
        Session session = (Session) sessionControl.getMock();
        sfControl.replay();
        sessionControl.replay();

        TransactionSynchronizationManager.bindResource(sf, new SessionHolder(session));
        JcrInterceptor interceptor = new JcrInterceptor();
        interceptor.setSessionFactory(sf);
        try {
            interceptor.invoke(new TestInvocation(sf));
        } catch (Throwable t) {
            fail("Should not have thrown Throwable: " + t.getMessage());
        } finally {
            TransactionSynchronizationManager.unbindResource(sf);
        }

        sfControl.verify();
        sessionControl.verify();
    }

    private static class TestInvocation implements MethodInvocation {

        private SessionFactory sessionFactory;

        public TestInvocation(SessionFactory sessionFactory) {
            this.sessionFactory = sessionFactory;
        }

        public Object proceed() throws Throwable {
            if (!TransactionSynchronizationManager.hasResource(this.sessionFactory)) {
                throw new IllegalStateException("Session not bound");
            }
            return null;
        }

        public Object[] getArguments() {
            return null;
        }

        public int getCurrentInterceptorIndex() {
            return 0;
        }

        public int getNumberOfInterceptors() {
            return 0;
        }

        public Interceptor getInterceptor(int i) {
            return null;
        }

        public Method getMethod() {
            return null;
        }

        public AccessibleObject getStaticPart() {
            return getMethod();
        }

        public Object getArgument(int i) {
            return null;
        }

        public void setArgument(int i, Object handler) {
        }

        public int getArgumentCount() {
            return 0;
        }

        public Object getThis() {
            return null;
        }

        public Object getProxy() {
            return null;
        }

        public Invocation cloneInstance() {
            return null;
        }

        public void release() {
        }
    }

}
