/**
 * Created on Feb 21, 2006
 *
 * $Id: LocalJbpmConfigurationFactoryBean.java,v 1.1 2006/03/02 14:56:00 costin Exp $
 * $Revision: 1.1 $
 */
package org.springmodules.workflow.jbpm31;

import java.io.InputStream;
import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.configuration.ObjectFactory;
import org.jbpm.configuration.ObjectFactoryParser;
import org.jbpm.graph.def.ProcessDefinition;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

/**
 * FactoryBean which allows customized creation of JbpmConfiguration objects which are binded 
 * to the lifecycle of the bean factory container. A BeanFactory aware ObjectFactory can be used
 * by the resulting object for retrieving beans from the application context, delegating to the
 * default implementation for unresolved names.  
 * It is possible to use an already defined Hibernate SessionFactory by injecting an approapriate
 * HibernateTemplate - if defined, the underlying session factory will be used by jBPM Persistence
 * Service.  
 *    
 * If set to true, createSchema and dropSchema
 * will be executed on factory initialization and destruction, using the contextName property which, by default,
 * is equivalent with JbpmContext.DEFAULT_JBPM_CONTEXT_NAME. 
 *  
 *
 * @see org.jbpm.configuration.ObjectFactory
 * @author Costin Leau
 *
 */
public class LocalJbpmConfigurationFactoryBean implements InitializingBean, DisposableBean, FactoryBean,
		BeanFactoryAware, BeanNameAware {

	private static final Log logger = LogFactory.getLog(LocalJbpmConfigurationFactoryBean.class);

	private JbpmConfiguration jbpmConfiguration;
	private ObjectFactory objectFactory;
	private Resource configuration;
	private boolean createSchema;
	private boolean dropSchema;
	private String contextName = JbpmContext.DEFAULT_JBPM_CONTEXT_NAME;
	private ProcessDefinition[] processDefinitions;
	private SessionFactory sessionFactory;
	/**
	 * FactoryLocator
	 */
	private JbpmFactoryLocator factoryLocator = new JbpmFactoryLocator();

	
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
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 */
	public void destroy() throws Exception {

		if (dropSchema) {
			logger.info("dropping schema");
			jbpmConfiguration.dropSchema(contextName);
		}
	}


	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		if (configuration == null && objectFactory == null)
			throw new IllegalArgumentException("configuration or objectFactory property need to be not null");

		ObjectFactory jbpmObjectFactory;

		// 1. create the configuration from the file
		if (configuration != null) {
			logger.info("creating JbpmConfiguration from resource " + configuration.getDescription());
			InputStream stream = configuration.getInputStream();
			jbpmObjectFactory = ObjectFactoryParser.parseInputStream(stream);
			stream.close();
		}
		else
			jbpmObjectFactory = objectFactory;

		jbpmConfiguration = new JbpmConfiguration(jbpmObjectFactory);

		// 2. inject the HB session factory if it is the case
		if (sessionFactory != null) {
			logger.info("using given Hibernate session factory");
			JbpmContext context = jbpmConfiguration.createJbpmContext(contextName);
			try {
				context.setSessionFactory(sessionFactory);
			}
			finally {
				context.close();
			}
		}

		// 3. execute persistence operations
		if (createSchema) {
			logger.info("creating schema");
			jbpmConfiguration.createSchema(contextName);
		}

		if (processDefinitions != null && processDefinitions.length > 0) {
			//TODO: replace with another faster utility
			String toString = Arrays.asList(processDefinitions).toString();
			logger.info("deploying process definitions:" + toString);
			JbpmContext context = jbpmConfiguration.createJbpmContext(contextName);
			try {
				for (int i = 0; i < processDefinitions.length; i++) {
					context.deployProcessDefinition(processDefinitions[i]);
				}
			}
			finally {
				context.close();
			}
		}
	}

	/**
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	public Object getObject() throws Exception {
		return jbpmConfiguration;
	}

	/**
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	public Class getObjectType() {
		return JbpmConfiguration.class;
	}

	/**
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	public boolean isSingleton() {
		return true;
	}

	/**
	 * @return Returns the configuration.
	 */
	public Resource getConfiguration() {
		return configuration;
	}

	/**
	 * @param configuration The configuration to set.
	 */
	public void setConfiguration(Resource configuration) {
		this.configuration = configuration;
	}

	/**
	 * @return Returns the objectFactory.
	 */
	public ObjectFactory getObjectFactory() {
		return objectFactory;
	}

	/**
	 * @param objectFactory The objectFactory to set.
	 */
	public void setObjectFactory(ObjectFactory objectFactory) {
		this.objectFactory = objectFactory;
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
	 * @return Returns the createSchema.
	 */
	public boolean isCreateSchema() {
		return createSchema;
	}

	/**
	 * @param createSchema The createSchema to set.
	 */
	public void setCreateSchema(boolean createSchema) {
		this.createSchema = createSchema;
	}

	/**
	 * @return Returns the dropSchema.
	 */
	public boolean isDropSchema() {
		return dropSchema;
	}

	/**
	 * @param dropSchema The dropSchema to set.
	 */
	public void setDropSchema(boolean dropSchema) {
		this.dropSchema = dropSchema;
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

	/**
	 * @return Returns the sessionFactory.
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * @param sessionFactory The sessionFactory to set.
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
