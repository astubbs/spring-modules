/**
 * Created on Sep 12, 2005
 *
 * $Id: JcrAccessor.java,v 1.1 2005/10/21 08:17:02 costin Exp $
 * $Revision: 1.1 $
 */
package org.springmodules.jcr;

import javax.jcr.RepositoryException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springmodules.jcr.support.DefaultSessionHolderProvider;

/**
 * Base class for JcrTemplate and JcrInterceptor, defining common properties
 * like JcrSessionFactory.
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

    /**
     * Eagerly initialize the session holder provider, creating a default one
     * if one is not set.
     */
    public void afterPropertiesSet(){
        if (getSessionFactory() == null) {
            throw new IllegalArgumentException("jcrSessionFactory is required");
        }
        if (getSessionHolderProvider() == null)
           setSessionHolderProvider(new DefaultSessionHolderProvider());
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
     * @return Returns the sessionHolderProvider.
     */
    public SessionHolderProvider getSessionHolderProvider() {
        return sessionHolderProvider;
    }

    /**
     * Not required - by default it uses a default session holder provider.
     * @param sessionHolderProvider The sessionHolderProvider to set.
     */
    public void setSessionHolderProvider(SessionHolderProvider sessionHolderProvider) {
        this.sessionHolderProvider = sessionHolderProvider;
    }
}
