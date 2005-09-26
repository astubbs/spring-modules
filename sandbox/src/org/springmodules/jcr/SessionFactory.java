package org.springmodules.jcr;

import javax.jcr.Session;

/**
 * Session Factory interface.
 * 
 * @author Costin Leau
 *
 */
public interface SessionFactory {

    /**
     * Returns a Jcr Session using the credentials on this JcrSessionFactory.
     * 
     * @return
     */
    public Session getSession();

    /**
     * Returns a session connected to the given workspaceName using the
     * credentials on this JcrSessionFactory.
     * 
     * @param workspace
     *            the name of the workspace to login to; if not provided, the
     *            workspace named by the <code>workspace</code> property will
     *            be used. If that property is also <code>null</code>, the
     *            repository's default workspace will be used.
     */
    public Session getSession(String workspace);

}