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

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.jbpm.db.JbpmSession;
import org.jbpm.db.JbpmSessionFactory;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.def.Transition;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.graph.exe.Token;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * Helper class that simplifies jBPM data access code.
 *   
 * @author Rob Harrop
 * @auhtor Costin Leau
 */
public class JbpmTemplate implements JbpmOperations, InitializingBean {

	private static final Log logger = LogFactory.getLog(JbpmTemplate.class);

	private JbpmSessionFactory jbpmSessionFactory;

	private ProcessDefinition processDefinition;

	private boolean allowCreate = true;

	private HibernateTemplate hibernateTemplate;

	public JbpmTemplate() {
	}

	public JbpmTemplate(JbpmSessionFactory jbpmSessionFactory) {
		this.jbpmSessionFactory = jbpmSessionFactory;
		afterPropertiesSet();
	}

	public JbpmTemplate(JbpmSessionFactory jbpmSessionFactory, ProcessDefinition processDefinition) {
		this.jbpmSessionFactory = jbpmSessionFactory;
		this.processDefinition = processDefinition;
		afterPropertiesSet();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() {
		if (jbpmSessionFactory == null)
			throw new IllegalArgumentException("jbpmSessionFactory must be set");
		// init the hbTemplate that will be used to prepare and handle the HB Session
		hibernateTemplate = new HibernateTemplate(jbpmSessionFactory.getSessionFactory());
		hibernateTemplate.setAllowCreate(allowCreate);
	}

	public void setJbpmSessionFactory(JbpmSessionFactory jbpmSessionFactory) {
		this.jbpmSessionFactory = jbpmSessionFactory;
	}

	/**
	 * Utility method for converting the jbpm exceptions.
	 * 
	 * @param exception
	 * @return
	 */
	public RuntimeException convertJbpmException(RuntimeException exception) {
		return JbpmSessionFactoryUtils.convertJbpmException(exception);
	}

	public Long saveProcessInstance(final ProcessInstance processInstance) {
		return (Long) execute(new JbpmCallback() {

			public Object doInJbpm(JbpmSession session) {
				session.getGraphSession().saveProcessInstance(processInstance);
				return new Long(processInstance.getId());
			}
		});
	}

	public ProcessInstance findProcessInstance(final Long processInstanceId) {
		return (ProcessInstance) execute(new JbpmCallback() {

			public Object doInJbpm(JbpmSession session) {
				return session.getGraphSession().loadProcessInstance(processInstanceId.longValue());
			}
		});
	}

	public List findProcessInstances() {
		return (List) execute(new JbpmCallback() {

			public Object doInJbpm(JbpmSession session) {
				return session.getGraphSession().findProcessInstances(processDefinition.getId());
			}
		});
	}

	public void signal(final ProcessInstance processInstance) {
		execute(new JbpmCallback() {

			public Object doInJbpm(JbpmSession session) {
				processInstance.signal();
				return null;
			}
		});
	}

	public void signal(final ProcessInstance processInstance, final String transitionId) {
		execute(new JbpmCallback() {

			public Object doInJbpm(JbpmSession session) {
				processInstance.signal(transitionId);
				return null;
			}
		});
	}

	public void signal(final ProcessInstance processInstance, final Transition transition) {
		execute(new JbpmCallback() {

			public Object doInJbpm(JbpmSession session) {
				processInstance.signal(transition);
				return null;
			}
		});
		throw new UnsupportedOperationException();
	}

	public List findPooledTaskInstances(final String actorId) {
		return (List) execute(new JbpmCallback() {

			public Object doInJbpm(JbpmSession session) {
				return session.getTaskMgmtSession().findPooledTaskInstances(actorId);
			}
		});
	}

	public List findPooledTaskInstances(final List actorIds) {
		return (List) execute(new JbpmCallback() {

			public Object doInJbpm(JbpmSession session) {
				return session.getTaskMgmtSession().findPooledTaskInstances(actorIds);
			}
		});
	}

	public List findTaskInstances(final String actorId) {
		return (List) execute(new JbpmCallback() {

			public Object doInJbpm(JbpmSession session) {
				return session.getTaskMgmtSession().findTaskInstances(actorId);
			}
		});
	}

	public List findTaskInstances(final String[] actorIds) {
		return (List) execute(new JbpmCallback() {

			public Object doInJbpm(JbpmSession session) {
				return session.getTaskMgmtSession().findTaskInstances(actorIds);
			}
		});
	}

	public List findTaskInstances(final List actorIds) {
		return (List) execute(new JbpmCallback() {

			public Object doInJbpm(JbpmSession session) {
				return session.getTaskMgmtSession().findTaskInstances(actorIds);
			}
		});
	}

	public List findTaskInstancesByToken(Token token) {
		return findTaskInstancesByToken(token.getId());
	}

	public List findTaskInstancesByToken(final long tokenId) {
		return (List) execute(new JbpmCallback() {

			public Object doInJbpm(JbpmSession session) {
				return session.getTaskMgmtSession().findTaskInstancesByToken(tokenId);
			}
		});
	}

	/**
	 * Execute the action specified by the given action object within a JbpmSession.
	 * 
	 * @param callback
	 * @return
	 */
	public Object execute(final JbpmCallback callback) {
		final JbpmSession jbpmSession = getSession();
		boolean existingTransaction = JbpmSessionFactoryUtils.isTransactional(jbpmSession,
				this.jbpmSessionFactory);

		if (existingTransaction) {
			logger.debug("Found thread-bound Session for JbpmTemplate");
		}

		try {
			return hibernateTemplate.execute(new HibernateCallback() {
				/**
				 * @see org.springframework.orm.hibernate3.HibernateCallback#doInHibernate(org.hibernate.Session)
				 */
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					return callback.doInJbpm(jbpmSession);
				}
			}, true);
		}
		catch (RuntimeException ex) {
			throw convertJbpmException(ex);
		}
		finally {
			if (existingTransaction) {
				logger.debug("Not closing pre-bound jBPM Session after JbpmTemplate");
			}
			else {
				releaseSession(jbpmSession);
			}
		}
	}

	protected void releaseSession(JbpmSession jbpmSession) {
		JbpmSessionFactoryUtils.releaseSession(jbpmSession, this.jbpmSessionFactory);
	}

	protected JbpmSession getSession() {
		return JbpmSessionFactoryUtils.getSession(this.jbpmSessionFactory);
	}

	/**
	 * @param allowCreate The allowCreate to set.
	 */
	public void setAllowCreate(boolean allowCreate) {
		this.allowCreate = allowCreate;
	}
}
