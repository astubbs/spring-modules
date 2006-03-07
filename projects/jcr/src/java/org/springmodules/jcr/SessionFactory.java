package org.springmodules.jcr;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

/**
 * Session Factory interface. This interface describes a simplfied contract for retrieving
 * a session and acts as a central point inside Spring Modules JCR support.
 * </p>
 * 
 * @author Costin Leau
 *
 */
public interface SessionFactory {

    /**
     * Returns a JCR Session using the credentials and workspace on this JcrSessionFactory.
     * The session factory doesn't allow specification of a different workspace name because:
     * <p>
     * " Each Session object is associated one-to-one with a Workspace object. The Workspace 
     * object represents a `view` of an actual repository workspace entity as seen through 
     * the authorization settings of its associated Session." (quote from javax.jcr.Session javadoc).
     * </p>
     * 
     * @return the JCR session.
     * @throws RepositoryException
     */
    public Session getSession() throws RepositoryException;
    
    /**
     * Returns a specific SessionHolder for the given Session. The holder provider is used
     * internally by the framework in components such as transaction managers to provide 
     * implementation specific information such as transactional support (if it is available).
     * 
     * @return specific sessionHolder.
     */
    public SessionHolder getSessionHolder(Session session);
}