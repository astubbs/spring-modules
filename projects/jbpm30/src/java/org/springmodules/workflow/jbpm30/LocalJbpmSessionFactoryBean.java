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

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.jbpm.db.JbpmSession;
import org.jbpm.db.JbpmSessionFactory;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.jpdl.par.ProcessArchiveDeployer;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

/**
 * FactoryBean that creates a local jBPM SessionFactory instance. Behaves like a SessionFactory instance when used as bean reference, 
 * e.g. for JbpmTemplate's "sessionFactory" property. Note that switching to JndiObjectFactoryBean is just a matter of configuration!
 * <p/>
 * Due to the nature of jbpm 3.0.x architecture and the lack of interfaces for core components, this factoryBean will create subclasses of jbpmSessionFactory
 * and jbpmSession in order to allow Spring transaction and session management. 
 * 
 * @author Rob Harrop
 * @author Costin Leau
 */
public class LocalJbpmSessionFactoryBean implements FactoryBean, InitializingBean, BeanFactoryAware,
		BeanNameAware {

	private static final Log logger = LogFactory.getLog(LocalJbpmSessionFactoryBean.class);
	
	// internal factory locator
	private JbpmFactoryLocator factoryLocator = new JbpmFactoryLocator();

	private JbpmSessionFactory sessionFactory;

	private SessionFactory hibernateSessionFactory;

	private Configuration hibernateConfiguration;
	
	private ProcessDefinition[] processDefinitions;

	public void setHibernateSessionFactory(SessionFactory hibernateSessionFactory) {
		this.hibernateSessionFactory = hibernateSessionFactory;
	}

	public void setHibernateConfiguration(Configuration hibernateConfiguration) {
		this.hibernateConfiguration = hibernateConfiguration;
	}

	public void afterPropertiesSet() throws Exception {
		if (this.hibernateConfiguration == null) {
			throw new FatalBeanException("Property [hibernateConfiguration] of ["
					+ LocalJbpmSessionFactoryBean.class
					+ "] is required.");
		}

		if (this.hibernateSessionFactory == null) {
			throw new FatalBeanException("Property [hibernateSessionFactory] of ["
					+ LocalJbpmSessionFactoryBean.class
					+ "] is required.");
		}

		this.sessionFactory = new SpringJbpmSessionFactory(this.hibernateConfiguration,
				this.hibernateSessionFactory);
		
		// deploy process definitions if there are any
		if (processDefinitions != null && processDefinitions.length > 0) {
			//TODO: replace with another faster utility
			String toString = Arrays.asList(processDefinitions).toString();
			logger.info("deploying process definitions:" + toString);
				for (int i = 0; i < processDefinitions.length; i++) {
					ProcessArchiveDeployer.deployProcessDefinition(processDefinitions[i], this.sessionFactory);
				}
		}
	}

	public Object getObject() throws Exception {
		return this.sessionFactory;
	}

	public Class getObjectType() {
		return (this.sessionFactory == null) ? JbpmSessionFactory.class : this.sessionFactory.getClass();
	}

	public boolean isSingleton() {
		return true;
	}

	/**
	 * @see org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory)
	 */
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		factoryLocator.setBeanFactory(beanFactory);
	}

	/**
	 * @see org.springframework.beans.factory.BeanNameAware#setBeanName(java.lang.String)
	 */
	public void setBeanName(String name) {
		factoryLocator.setBeanName(name);
	}

	/**
	 * Subclass for JbpmSessionFactory that hooks the Spring jbpm session factory.
	 *
	 */
	private static class SpringJbpmSessionFactory extends JbpmSessionFactory {
		private SessionFactory sessionFactory;

		public SpringJbpmSessionFactory(Configuration configuration, SessionFactory sessionFactory) {
			
			super(configuration, sessionFactory);
			this.sessionFactory = sessionFactory;
		}

		public JbpmSession openJbpmSession(Connection jdbcConnection) {
			if (jdbcConnection != null) {
				throw new UnsupportedOperationException(
						"Cannot start a new Hibernate Session using supplied JDBC connection");
			}

			// TODO: should this always be on?
			Session session = SessionFactoryUtils.getSession(this.sessionFactory, true);
			return new SpringJbpmSession(this, session, this.sessionFactory);
		}
	}

	/**
	 * Extension of JbpmSession factory that hooks in Spring resource (transaction/session) management. 
	 *
	 */
	private static class SpringJbpmSession extends JbpmSession {

		private SessionFactory sessionFactory;
		// determine the field at start time.
		private static final Field sessionField;
		private static final String SESSION_FIELD_NAME = "session";
		private static final Field transactionField;
		private static final String TRANSACTION_FIELD_NAME = "transaction";

		static {
			try {
				// do the 'reflection' magic only once to avoid security checks
				// on each close check
				sessionField = JbpmSession.class.getDeclaredField(SESSION_FIELD_NAME);
				sessionField.setAccessible(true);
				transactionField = JbpmSession.class.getDeclaredField(TRANSACTION_FIELD_NAME);
				transactionField.setAccessible(true);

			}
			catch (NoSuchFieldException e) {
				throw new RuntimeException("can't find field "
						+ SESSION_FIELD_NAME
						+ "/"
						+ TRANSACTION_FIELD_NAME
						+ " for introspection on class "
						+ JbpmSession.class, e);
			}
		}

		public SpringJbpmSession(JbpmSessionFactory jbpmSessionFactory, Session session,
				SessionFactory sessionFactory) {
			super(jbpmSessionFactory, session);
			this.sessionFactory = sessionFactory;

		}

		public void beginTransaction() {
			if (isSpringManagedTransaction()) {
				if (logger.isDebugEnabled())
					logger.debug("begin transaction -> handled through Spring");
				return;
			}

			if (logger.isDebugEnabled())
				logger.debug("begin transaction -> handled through jBPM");

			super.beginTransaction();
		}

		public void commitTransaction() {
			if (isSpringManagedTransaction()) {
				if (logger.isDebugEnabled())
					logger.debug("commit transaction -> handled through Spring");
				return;
			}
			if (logger.isDebugEnabled())
				logger.debug("commit transaction -> handled through jBPM");

			super.commitTransaction();
		}

		public void rollbackTransaction() {
			if (isSpringManagedTransaction()) {
				if (logger.isDebugEnabled())
					logger.debug("rollback transaction -> handled through Spring");
				return;
			}

			if (logger.isDebugEnabled())
				logger.debug("rollback transaction -> handled through jBPM");

			super.rollbackTransaction();
		}

		// TODO: thoroughly test these
		// TODO: consider the effect of deferred close of these methods
		// TODO: check out transaction management

		public void commitTransactionAndClose() {
			if (isSpringManagedTransaction()) {
				if (logger.isDebugEnabled())
					logger.debug("commit and close transaction -> handled through Spring");
				
				nullifySuperField(transactionField);
				close();
			}

			if (logger.isDebugEnabled())
				logger.debug("commit and close transaction -> handled through jBPM");

			super.commitTransactionAndClose();
		}

		public void rollbackTransactionAndClose() {
			if (isSpringManagedTransaction()) {
				if (logger.isDebugEnabled())
					logger.debug("rollback and close transaction -> handled through Spring");
				
				nullifySuperField(transactionField);
				close();
			}

			if (logger.isDebugEnabled())
				logger.debug("rollback and close transaction -> handled through jBPM");

			super.rollbackTransactionAndClose();
		}

		public void close() {
			if (logger.isDebugEnabled())
				logger.debug("closing jbpm session");
			
			// should remove the session reference in a nice way (let spring do the handling)

			// a. first set through introspection the session to null to prevent any trouble or
			// closing
			if (isSpringManagedTransaction()) {
				nullifySuperField(sessionField);
			}

			// no matter what let jbpm release its resources
			super.close();
		}

		/**
		 * Utility method for nullified a super field.
		 * @param field
		 */
		protected void nullifySuperField(Field field) {
			if (logger.isDebugEnabled())
				logger.debug("nullfying field " + field.getName());
			try {
				field.set(this, null);
			}
			catch (IllegalAccessException e) {
				throw new UnsupportedOperationException("can not set private field=" + field);
			}
		}
		/**
		 * Check if the session is transaction (was binded to thread).
		 * @return
		 */
		private boolean isSpringManagedTransaction() {
			return SessionFactoryUtils.isSessionTransactional(getSession(), this.sessionFactory);
		}
	}

	/**
	 * @return Returns the processDefinitions.
	 */
	public ProcessDefinition[] getProcessDefinitions() {
		return processDefinitions;
	}

	/**
	 * @param processDefinitions The processDefinitions to set.
	 */
	public void setProcessDefinitions(ProcessDefinition[] processDefinitions) {
		this.processDefinitions = processDefinitions;
	}

}
