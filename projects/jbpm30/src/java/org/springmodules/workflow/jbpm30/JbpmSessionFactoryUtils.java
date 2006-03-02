/*
 * Copyright 2002-2005 the original author or authors.
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

package org.springmodules.workflow.jbpm30;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.jbpm.db.JbpmSession;
import org.jbpm.db.JbpmSessionFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

/**
 * Helper class featuring methods for jBPM Session handling, allowing for reuse of jBPM Session instances within transactions.
 * As jBPM 3.0.x actually stands on top of Hibernate, this class will delegate as much as possible work to Spring Hibernate SessionFactoryUtils.
 * 
 * @author Rob Harrop
 */
public abstract class JbpmSessionFactoryUtils {

	private static final Log logger = LogFactory.getLog(JbpmSessionFactoryUtils.class);

	/**
	 * Returns a jBPM session. It is aware of and will return the thread-bound session if one is found.
	 * 
	 * @param sessionFactory
	 * @return
	 */
	public static JbpmSession getSession(JbpmSessionFactory sessionFactory) {
		try {
			return doGetSession(sessionFactory, true);
		}
		catch (RuntimeException e) {
			throw new DataAccessResourceFailureException("Could not open jBPM Session", e);
		}
	}

	/**
	 * Returns a jBPM session. It is aware of and will return the thread-bound session if one is found.
	 * jBPM exceptions will not be translated.
	 * 
	 * @param sessionFactory
	 * @param allowCreate
	 * @return
	 */
	public static JbpmSession doGetSession(JbpmSessionFactory sessionFactory, boolean allowCreate) {
		Assert.notNull(sessionFactory, "No JbpmSessionFactory specified");

		JbpmSessionHolder jbpmSessionHolder = (JbpmSessionHolder) TransactionSynchronizationManager.getResource(sessionFactory);

		if (jbpmSessionHolder != null && jbpmSessionHolder.getJbpmSession() != null) {
			return jbpmSessionHolder.getJbpmSession();
		}

		JbpmSession jbpmSession = sessionFactory.openJbpmSession();
		jbpmSessionHolder = new JbpmSessionHolder(jbpmSession);

		if (TransactionSynchronizationManager.isSynchronizationActive()) {
			TransactionSynchronizationManager.registerSynchronization(new SpringJbpmSessionSynchronization(
					jbpmSessionHolder, sessionFactory));
			TransactionSynchronizationManager.bindResource(sessionFactory, jbpmSessionHolder);
		}

		return jbpmSession;
	}

	/**
	 * Releases the jBPM session.
	 * 
	 * @param jbpmSession
	 * @param jbpmSessionFactory
	 */
	public static void releaseSession(JbpmSession jbpmSession, JbpmSessionFactory jbpmSessionFactory) {
		if (jbpmSession == null)
			return;

		if (!isTransactional(jbpmSession, jbpmSessionFactory)) {
			logger.debug("Closing jBPM session");
			jbpmSession.close();
		}

	}

	/**
	 * Return whether the given jBPM Session is transactional, that is,
	 * bound to the current thread by Spring's transaction facilities.
	 * 
	 * @param jbpmSession
	 * @param jbpmSessionFactory
	 * @return
	 */
	public static boolean isTransactional(JbpmSession jbpmSession, JbpmSessionFactory jbpmSessionFactory) {
		if (jbpmSessionFactory == null)
			return false;

		JbpmSessionHolder jbpmSessionHolder = (JbpmSessionHolder) TransactionSynchronizationManager.getResource(jbpmSessionFactory);
		return (jbpmSessionHolder != null && jbpmSessionHolder.getJbpmSession() == jbpmSession);
	}

	/**
	 * Converts Jbpm RuntimeExceptions into Spring specific ones (if possible).
	 * @param ex
	 * @return
	 */
	public static RuntimeException convertJbpmException(RuntimeException ex) {
		// try to decode and translate HibernateExceptions
		if (ex instanceof HibernateException) {
			return SessionFactoryUtils.convertHibernateAccessException((HibernateException) ex);
		}

		if (ex.getCause() instanceof HibernateException) {
			DataAccessException rootCause = SessionFactoryUtils.convertHibernateAccessException((HibernateException) ex.getCause());
			return new NestedDataAccessException(ex.getMessage(), rootCause);
		}

		// cannot convert the exception in any meaningful way
		return ex;
	}

	/**
	 * Callback for resource cleanup at the end of a transaction (e.g.
	 * when participating in a JtaTransactionManager transaction).
	 * 
	 * @see org.springframework.transaction.jta.JtaTransactionManager
	 */
	private static class SpringJbpmSessionSynchronization extends TransactionSynchronizationAdapter {

		private static final Log logger = LogFactory.getLog(SpringJbpmSessionSynchronization.class);
		
		private JbpmSessionHolder jbpmSessionHolder;
		private JbpmSessionFactory jbpmSessionFactory;

		public SpringJbpmSessionSynchronization(JbpmSessionHolder jbpmSessionHolder,
				JbpmSessionFactory jbpmSessionFactory) {
			this.jbpmSessionHolder = jbpmSessionHolder;
			this.jbpmSessionFactory = jbpmSessionFactory;
		}

		public void suspend() {
			if (logger.isDebugEnabled())
				logger.debug("suspend synchronization for jbpmSessionFactory " + System.identityHashCode(jbpmSessionFactory));
			TransactionSynchronizationManager.unbindResource(this.jbpmSessionFactory);
		}

		public void resume() {
			if (logger.isDebugEnabled())
				logger.debug("resume synchronization for jbpmSessionFactory " + System.identityHashCode(jbpmSessionFactory));
			TransactionSynchronizationManager.bindResource(jbpmSessionFactory, jbpmSessionHolder);
		}

		public void beforeCompletion() {
			if (logger.isDebugEnabled())
				logger.debug("beforeCompletion synchronization for jbpmSessionFactory " + System.identityHashCode(jbpmSessionFactory));

			TransactionSynchronizationManager.unbindResource(jbpmSessionFactory);
			releaseSession(this.jbpmSessionHolder.getJbpmSession(), this.jbpmSessionFactory);
			this.jbpmSessionHolder.clear();
			
		}
	}
}
