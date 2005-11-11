/**
 * Created on Sep 12, 2005
 *
 * $Id: JcrAccessor.java,v 1.2 2005/11/11 15:47:11 costin Exp $
 * $Revision: 1.2 $
 */
package org.springmodules.jcr;

import javax.jcr.RepositoryException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springmodules.jcr.support.ServiceSessionHolderProviderManager;

/**
 * Base class for JcrTemplate and JcrInterceptor, defining common properties
 * like JcrSessionFactory.
 * The required properties are sessionFactory.
 * If the sessionHolderProvider is not set the sessionProviderManager property is used.
 * If it's null the ServiceSessionHolderProviderManager will be used by default.
 * 
 * <p>
 * Not intended to be used directly. See JcrTemplate and JcrInterceptor.
 * 
 * @see JcrTemplate
 * @see JcrInterceptor
 * @author Costin Leau
 */
public abstract class JcrAccessor implements InitializingBean {

	protected final Log logger = LogFactory.getLog(getClass());

	private SessionFactory sessionFactory;

	private SessionHolderProvider sessionHolderProvider;

	private SessionHolderProviderManager providerManager;

	/**
	 * Eagerly initialize the session holder provider, creating a default one
	 * if one is not set.
	 */
	public void afterPropertiesSet() {
		if (getSessionFactory() == null) {
			throw new IllegalArgumentException("jcrSessionFactory is required");
		}
		if (getSessionHolderProvider() == null) {
			if (providerManager == null)
				providerManager = new ServiceSessionHolderProviderManager();
			// use the provider manager
			setSessionHolderProvider(providerManager.getSessionProvider(sessionFactory));
		}
	}

	/**
	 * Convert the given RepositoryException to an appropriate exception from
	 * the <code>org.springframework.dao</code> hierarchy.
	 * <p>
	 * May be overridden in subclasses.
	 * 
	 * @param ex
	 *            RepositoryException that occured
	 * @return the corresponding DataAccessException instance
	 */
	public DataAccessException convertJcrAccessException(RepositoryException ex) {
		return SessionFactoryUtils.translateException(ex);
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

	/**
	 * @return Returns the providerManager.
	 */
	public SessionHolderProviderManager getProviderManager() {
		return providerManager;
	}

	/**
	 * @param providerManager The providerManager to set.
	 */
	public void setProviderManager(SessionHolderProviderManager providerManager) {
		this.providerManager = providerManager;
	}

	/**
	 * @return Returns the sessionHolderProvider.
	 */
	public SessionHolderProvider getSessionHolderProvider() {
		return sessionHolderProvider;
	}

	/**
	 * @param sessionHolderProvider The sessionHolderProvider to set.
	 */
	public void setSessionHolderProvider(SessionHolderProvider sessionHolderProvider) {
		this.sessionHolderProvider = sessionHolderProvider;
	}
}
