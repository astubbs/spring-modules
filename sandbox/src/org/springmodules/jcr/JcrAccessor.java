/**
 * Created on Sep 12, 2005
 *
 * $Id: JcrAccessor.java,v 1.1 2005/09/26 10:21:52 costin Exp $
 * $Revision: 1.1 $
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
 * 
 * <p>
 * Not intended to be used directly. See JcrTemplate and JcrInterceptor.
 * 
 * @author Costin Leau
 * @see JcrTemplate
 * @see JcrInterceptor
 */
public abstract class JcrAccessor implements InitializingBean {

    protected final Log logger = LogFactory.getLog(getClass());

    private SessionFactory sessionFactory;

    /**
     * Eagerly initialize the JDO dialect, creating a default one for the
     * specified PersistenceManagerFactory if none set.
     */
    public void afterPropertiesSet() {
        if (getSessionFactory() == null) {
            throw new IllegalArgumentException("jcrSessionFactory is required");
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
}
