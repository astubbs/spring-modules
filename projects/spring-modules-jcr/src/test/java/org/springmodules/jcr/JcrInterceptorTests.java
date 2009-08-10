/**
 * Created on Sep 12, 2005
 *
 * $Id: JcrInterceptorTests.java,v 1.2 2006/03/07 13:09:31 costin Exp $
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
		final MockControl sfControl = MockControl.createControl(SessionFactory.class);
		final SessionFactory sf = (SessionFactory) sfControl.getMock();
		final MockControl sessionControl = MockControl.createControl(Session.class);
		final Session session = (Session) sessionControl.getMock();

		sfControl.expectAndReturn(sf.getSession(), session);
		session.logout();
		sessionControl.setVoidCallable(1);
		sfControl.expectAndReturn(sf.getSessionHolder(session), new SessionHolder(session));
		sfControl.replay();
		sessionControl.replay();

		final JcrInterceptor interceptor = new JcrInterceptor();
		interceptor.setSessionFactory(sf);
		interceptor.afterPropertiesSet();
		try {
			interceptor.invoke(new TestInvocation(sf));
		} catch (final Throwable t) {
			fail("Should not have thrown Throwable: " + t);
		}

		sfControl.verify();
		sessionControl.verify();
	}

	public void testInterceptorWithPrebound() {
		final MockControl sfControl = MockControl.createControl(SessionFactory.class);
		final SessionFactory sf = (SessionFactory) sfControl.getMock();
		final MockControl sessionControl = MockControl.createControl(Session.class);
		final Session session = (Session) sessionControl.getMock();
		final MockControl repoControl = MockControl.createNiceControl(Repository.class);

		sfControl.replay();
		sessionControl.replay();
		repoControl.replay();

		TransactionSynchronizationManager.bindResource(sf, new SessionHolder(session));
		final JcrInterceptor interceptor = new JcrInterceptor();
		interceptor.setSessionFactory(sf);
		interceptor.afterPropertiesSet();
		try {
			interceptor.invoke(new TestInvocation(sf));
		} catch (final Throwable t) {
			fail("Should not have thrown Throwable: " + t.getMessage());
		} finally {
			TransactionSynchronizationManager.unbindResource(sf);
		}

		sfControl.verify();
		sessionControl.verify();
	}

	private static class TestInvocation implements MethodInvocation {

		private SessionFactory sessionFactory;

		public TestInvocation(final SessionFactory sessionFactory) {
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

		public Interceptor getInterceptor(final int i) {
			return null;
		}

		public Method getMethod() {
			return null;
		}

		public AccessibleObject getStaticPart() {
			return getMethod();
		}

		public Object getArgument(final int i) {
			return null;
		}

		public void setArgument(final int i, final Object handler) {
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
