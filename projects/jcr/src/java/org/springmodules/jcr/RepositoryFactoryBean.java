/**
 * Created on Sep 23, 2005
 *
 * $Id: RepositoryFactoryBean.java,v 1.1 2005/12/20 17:38:11 costin Exp $
 * $Revision: 1.1 $
 */
package org.springmodules.jcr;

import javax.jcr.Repository;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

/**
 * Base class with common functionality for creating JCR repositories. Subclasses should extend
 * this class for custom configuration. 
 * 
 * @author Costin Leau
 *
 */
public abstract class RepositoryFactoryBean implements InitializingBean, DisposableBean, FactoryBean {

	protected final Log log = LogFactory.getLog(getClass());

	/**
	 * Repository configuration.
	 */
	protected Resource configuration;

	/**
	 * The actual repository.
	 */
	protected Repository repository;

	/**
	 * Subclasses have to implement this method to allow specific JSR-170 implementation repository configuration.
	 *
	 */
	protected abstract void resolveConfigurationResource() throws Exception;

	/**
	 * Subclasses have to implement this method to allow specific JSR-170 implementation repository creation.
	 * 
	 * @return
	 */
	protected abstract Repository createRepository() throws Exception;

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		resolveConfigurationResource();
		repository = createRepository();

	}

	/**
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 */
	public void destroy() throws Exception {
	}

	/**
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	public Object getObject() throws Exception {
		return this.repository;
	}

	/**
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	public Class getObjectType() {
		// the repository is proxied.
		return Repository.class;
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
		return this.configuration;
	}

	/**
	 * @param configuration The configuration to set.
	 */
	public void setConfiguration(Resource configuration) {
		this.configuration = configuration;
	}

}
