/**
 * Created on Sep 12, 2005
 *
 * $Id: JcrAccessor.java,v 1.4 2006/03/07 14:40:50 costin Exp $
 * $Revision: 1.4 $
 */
package org.springmodules.jcr;

import java.io.IOException;

import javax.jcr.RepositoryException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;

/**
 * Base class for JcrTemplate and JcrInterceptor, defining common properties
 * like JcrSessionFactory.
 * The required property is sessionFactory.
 * 
 * <p>
 * Not intended to be used directly. See JcrTemplate and JcrInterceptor.
 * 
 * @see JcrTemplate
 * @see JcrInterceptor
 * 
 * @author Costin Leau
 */
public abstract class JcrAccessor implements InitializingBean {

	protected final Log logger = LogFactory.getLog(getClass());

	private SessionFactory sessionFactory;

	/**
	 * Eagerly initialize the session holder provider, creating a default one
	 * if one is not set.
	 */
	public void afterPropertiesSet() {
		if (getSessionFactory() == null) {
			throw new IllegalArgumentException("sessionFactory is required");
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
	 * Convert the given IOException to an appropriate exception from
	 * the <code>org.springframework.dao</code> hierarchy.
	 * <p>
	 * May be overridden in subclasses.
	 * 
	 * @param ex
	 *            IOException that occured
	 * @return the corresponding DataAccessException instance	 
	 */
	public DataAccessException convertJcrAccessException(IOException ex) {
		return SessionFactoryUtils.translateException(ex);
	}

	/**
	 * Convert the given RuntimeException to an appropriate exception from
	 * the <code>org.springframework.dao</code> hierarchy.
	 * <p>
	 * May be overridden in subclasses.

	 * @param ex
	 * @return
	 */
	public RuntimeException convertJcrAccessException(RuntimeException ex) {
		return ex;
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
