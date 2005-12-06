/**
 * Created on Sep 12, 2005
 *
 * $Id: JcrAccessor.java,v 1.3 2005/12/06 10:37:00 costin Exp $
 * $Revision: 1.3 $
 */
package org.springmodules.jcr;

import javax.jcr.RepositoryException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;

/**
 * Base class for JcrTemplate and JcrInterceptor, defining common properties
 * like JcrSessionFactory.
 * The required properties are sessionFactory and sessionHolderProviderManager.
 * 
 * <p>
 * Not intended to be used directly. See JcrTemplate and JcrInterceptor.
 * 
 * @see JcrTemplate
 * @see JcrInterceptor
 * @see org.springmodules.jcr.support.ServiceSessionHolderProviderManager
 * 
 * @author Costin Leau
 */
public abstract class JcrAccessor implements InitializingBean {

	protected final Log logger = LogFactory.getLog(getClass());

	private SessionFactory sessionFactory;

	/**
	 * sessionHolder Factory.
	 */
	private SessionHolderProvider sessionHolderProvider;

	/**
	 * 'Detector' for sessionHolderProvider.
	 */
	private SessionHolderProviderManager providerManager;

	/**
	 * Eagerly initialize the session holder provider, creating a default one
	 * if one is not set.
	 */
	public void afterPropertiesSet() {
		if (getSessionFactory() == null) {
			throw new IllegalArgumentException("sessionFactory is required");
		}

		if (getProviderManager() == null) {
			throw new IllegalArgumentException("providerManager is required");
		}

		// use the provider manager
		setSessionHolderProvider(providerManager.getSessionProvider(sessionFactory));
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
	 * By default ServiceSessionHolderProviderManager is used.
	 * 
	 * @param providerManager The providerManager to set.
	 */
	public void setProviderManager(SessionHolderProviderManager providerManager) {
		this.providerManager = providerManager;
	}

	/**
	 * SessionHolderProvider (used internally by TransactionManager).
	 * 
	 * @return Returns the sessionHolderProvider.
	 */
	public SessionHolderProvider getSessionHolderProvider() {
		return sessionHolderProvider;
	}

	/**
	 * Only subclasses should work with it (we minimize the method for this classes). 
	 * 
	 * @param sessionHolderProvider The sessionHolderProvider to set.
	 */
	protected void setSessionHolderProvider(SessionHolderProvider sessionHolderProvider) {
		this.sessionHolderProvider = sessionHolderProvider;
	}
}
