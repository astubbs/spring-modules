/**
 * Created on Feb 20, 2006
 *
 * $Id: JbpmTemplate.java,v 1.3 2006/06/10 11:27:46 costin Exp $
 * $Revision: 1.3 $
 */
package org.springmodules.workflow.jbpm31;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.JbpmException;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.def.Transition;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.graph.exe.Token;
import org.jbpm.svc.Services;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * Jbpm 3.1 Template. Requires a jbpmConfiguration and accepts also a
 * hibernateTemplate and processDefinition. Jbpm Persistence Service can be
 * managed by Spring through the given HibernateTemplate, allowing jBPM to work
 * with a user configured session factory and thread-bound session depending on
 * the HibernateTemplate settings. However, due to the nature of jBPM
 * architecture, on each execute the jbpmContext will try to close the user
 * Hibernate session which is undesireable when working with a thread-bound
 * session or a transaction. One can overcome this undesired behavior by setting
 * 'exposeNative' property on the HibernateTemplate to false (default).
 * 
 * @see org.springframework.orm.hibernate3.HibernateTemplate
 * @author Costin Leau
 * 
 */
public class JbpmTemplate extends JbpmAccessor implements JbpmOperations {
	// TODO: persistence is not always required

	/**
	 * Optional process definition.
	 */
	private ProcessDefinition processDefinition;

	/**
	 * Required if jBPM has persistence services.
	 */
	private HibernateTemplate hibernateTemplate;

	/**
	 * Boolean used to determine if the persistence service is used or not. If
	 * so, hibernateTemplate will be required and used internally.
	 */
	private boolean hasPersistenceService;

	/**
	 * jBPM context name. defaults to null which is equivalent to
	 * JbpmContext.DEFAULT_JBPM_CONTEXT_NAME
	 */
	private String contextName = JbpmContext.DEFAULT_JBPM_CONTEXT_NAME;

	/**
	 * Execute the action specified by the given action object within a
	 * JbpmSession.
	 * 
	 * @param callback
	 * @return
	 */
	public Object execute(final JbpmCallback callback) {
		final JbpmContext context = getContext();

		try {
			// use the hibernateTemplate is present and if needed
			if (hibernateTemplate != null && hasPersistenceService) {

				// use hibernate template
				return hibernateTemplate.execute(new HibernateCallback() {
					/**
					 * @see org.springframework.orm.hibernate3.HibernateCallback#doInHibernate(org.hibernate.Session)
					 */
					public Object doInHibernate(Session session) throws HibernateException, SQLException {
						// inject the session in the context
						context.setSession(session);
						return callback.doInJbpm(context);
					}
				});
			}

			// plain callback invocation (no template w/ persistence)
			return callback.doInJbpm(context);

		}
		catch (JbpmException ex) {
			throw convertJbpmException(ex);
		}
		finally {
			releaseContext(context);
		}

	}

	/**
	 * Hook for subclasses for adding custom behavior.
	 * 
	 * @param jbpmContext
	 */
	protected void releaseContext(JbpmContext jbpmContext) {
		jbpmContext.close();
	}

	/**
	 * Hook for subclasses for adding custom behavior.
	 * 
	 * @return created of fetched from the thread jbpm context.
	 */
	protected JbpmContext getContext() {
		JbpmContext context = jbpmConfiguration.createJbpmContext(contextName);

		return context;
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		// see if persistence is required

		// we don't have any other way to get the services then by creating a
		// jbpm context
		// secured in try/finally block
		JbpmContext dummy = getContext();
		try {

			if (dummy.getServiceFactory(Services.SERVICENAME_PERSISTENCE) != null) {
				hasPersistenceService = true;
				logger.debug("jBPM persistence service present");
			}

			if (hibernateTemplate != null)
				logger.debug("hibernateTemplate present - jBPM persistence service will be managed by Spring");
			else {
				if (dummy.getSessionFactory() != null) {
					logger.debug("creating hibernateTemplate based on jBPM SessionFactory");
					hibernateTemplate = new HibernateTemplate(dummy.getSessionFactory());
				}
				else

					logger.debug("hibernateTemplate missing - jBPM will handle its own persistence");
			}

		}
		finally {
			dummy.close();
		}

	}

	public JbpmTemplate() {
	}

	public JbpmTemplate(JbpmConfiguration jbpmConfiguration) {
		this.jbpmConfiguration = jbpmConfiguration;
	}

	public JbpmTemplate(JbpmConfiguration jbpmConfiguration, ProcessDefinition processDefinition) {
		this.jbpmConfiguration = jbpmConfiguration;
		this.processDefinition = processDefinition;
	}

	/**
	 * @return Returns the contextName.
	 */
	public String getContextName() {
		return contextName;
	}

	/**
	 * @param contextName The contextName to set.
	 */
	public void setContextName(String contextName) {
		this.contextName = contextName;
	}

	/**
	 * @return Returns the hibernateTemplate.
	 */
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	/**
	 * @param hibernateTemplate The hibernateTemplate to set.
	 */
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	/**
	 * @return Returns the processDefinition.
	 */
	public ProcessDefinition getProcessDefinition() {
		return processDefinition;
	}

	/**
	 * @param processDefinition The processDefinition to set.
	 */
	public void setProcessDefinition(ProcessDefinition processDefinition) {
		this.processDefinition = processDefinition;
	}

	/**
	 * @see org.springmodules.workflow.jbpm31.JbpmOperations#findProcessInstance(java.lang.Long)
	 */
	public ProcessInstance findProcessInstance(final Long processInstanceId) {
		return (ProcessInstance) execute(new JbpmCallback() {

			public Object doInJbpm(JbpmContext context) {
				return context.getGraphSession().loadProcessInstance(processInstanceId.longValue());
			}
		});
	}

	/**
	 * @see org.springmodules.workflow.jbpm31.JbpmOperations#findProcessInstances()
	 */
	public List findProcessInstances() {
		return (List) execute(new JbpmCallback() {

			public Object doInJbpm(JbpmContext context) {
				return context.getGraphSession().findProcessInstances(processDefinition.getId());
			}
		});
	}

	/**
	 * @see org.springmodules.workflow.jbpm31.JbpmOperations#findPooledTaskInstances(java.lang.String)
	 */
	public List findPooledTaskInstances(final String actorId) {
		return (List) execute(new JbpmCallback() {

			public Object doInJbpm(JbpmContext context) {
				return context.getTaskMgmtSession().findPooledTaskInstances(actorId);
			}
		});
	}

	/**
	 * @see org.springmodules.workflow.jbpm31.JbpmOperations#findPooledTaskInstances(java.util.List)
	 */
	public List findPooledTaskInstances(final List actorIds) {
		return (List) execute(new JbpmCallback() {

			public Object doInJbpm(JbpmContext context) {
				return context.getTaskMgmtSession().findPooledTaskInstances(actorIds);
			}
		});
	}

	/**
	 * @see org.springmodules.workflow.jbpm31.JbpmOperations#findTaskInstances(java.lang.String)
	 */
	public List findTaskInstances(final String actorId) {
		return (List) execute(new JbpmCallback() {

			public Object doInJbpm(JbpmContext context) {
				return context.getTaskMgmtSession().findTaskInstances(actorId);
			}
		});
	}

	/**
	 * @see org.springmodules.workflow.jbpm31.JbpmOperations#findTaskInstances(java.lang.String[])
	 */
	public List findTaskInstances(final String[] actorIds) {
		return (List) execute(new JbpmCallback() {

			public Object doInJbpm(JbpmContext context) {
				return context.getTaskMgmtSession().findTaskInstances(actorIds);
			}
		});
	}

	/**
	 * @see org.springmodules.workflow.jbpm31.JbpmOperations#findTaskInstances(java.util.List)
	 */
	public List findTaskInstances(final List actorIds) {
		return (List) execute(new JbpmCallback() {

			public Object doInJbpm(JbpmContext context) {
				return context.getTaskMgmtSession().findTaskInstances(actorIds);
			}
		});
	}

	/**
	 * @see org.springmodules.workflow.jbpm31.JbpmOperations#findTaskInstancesByToken(org.jbpm.graph.exe.Token)
	 */
	public List findTaskInstancesByToken(Token token) {
		return findTaskInstancesByToken(token.getId());
	}

	/**
	 * @see org.springmodules.workflow.jbpm31.JbpmOperations#findTaskInstancesByToken(long)
	 */
	public List findTaskInstancesByToken(final long tokenId) {
		return (List) execute(new JbpmCallback() {

			public Object doInJbpm(JbpmContext context) {
				return context.getTaskMgmtSession().findTaskInstancesByToken(tokenId);
			}
		});
	}

	/**
	 * @see org.springmodules.workflow.jbpm31.JbpmOperations#signal(org.jbpm.graph.exe.ProcessInstance)
	 */
	public void signal(final ProcessInstance processInstance) {
		execute(new JbpmCallback() {

			public Object doInJbpm(JbpmContext context) {
				processInstance.signal();
				return null;
			}
		});
	}

	/**
	 * @see org.springmodules.workflow.jbpm31.JbpmOperations#saveProcessInstance(org.jbpm.graph.exe.ProcessInstance)
	 */
	public Long saveProcessInstance(final ProcessInstance processInstance) {
		return (Long) execute(new JbpmCallback() {

			public Object doInJbpm(JbpmContext context) {
				context.save(processInstance);
				return new Long(processInstance.getId());
			}
		});
	}

	/**
	 * @see org.springmodules.workflow.jbpm31.JbpmOperations#signal(org.jbpm.graph.exe.ProcessInstance,
	 *      java.lang.String)
	 */
	public void signal(final ProcessInstance processInstance, final String transitionId) {
		execute(new JbpmCallback() {

			public Object doInJbpm(JbpmContext context) {
				processInstance.signal(transitionId);
				return null;
			}
		});
	}

	/**
	 * @see org.springmodules.workflow.jbpm31.JbpmOperations#signal(org.jbpm.graph.exe.ProcessInstance,
	 *      org.jbpm.graph.def.Transition)
	 */
	public void signal(final ProcessInstance processInstance, final Transition transition) {
		execute(new JbpmCallback() {

			public Object doInJbpm(JbpmContext context) {
				processInstance.signal(transition);
				return null;
			}
		});
		throw new UnsupportedOperationException();
	}

}
