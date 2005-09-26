package org.springmodules.jcr;

import javax.jcr.Credentials;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.springframework.beans.factory.InitializingBean;

/**
 * Jcr Session Factory. Right now this class is just a simple wrapper around the
 * repository but it offers a central point for adding more functionality later.
 * 
 * @author Costin Leau
 * @author Brian Moseley <bcm@osafoundation.org>
 * 
 */
public class JcrSessionFactory implements InitializingBean, SessionFactory {

    private Repository repository;

    private String workspaceName;

    private Credentials credentials;

    /**
     * @param repository
     * @param workspaceName
     */
    public JcrSessionFactory(Repository repository, String workspaceName, Credentials credentials) {
        this.repository = repository;
        this.workspaceName = workspaceName;
        this.credentials = credentials;
        afterPropertiesSet();
        
    }

    public JcrSessionFactory() {
    }

    public void afterPropertiesSet() {

        if (getRepository() == null)
            throw new IllegalArgumentException("repository is required");
    }

    /**
     * @see org.springmodules.jcr.SessionFactory#getSession()
     */
    public Session getSession() {
        return getSession(workspaceName);
    }

    /**
     * @see org.springmodules.jcr.SessionFactory#getSession(java.lang.String)
     */
    public Session getSession(String workspace) {
        try {
            return repository.login(credentials, workspace);
        } catch (RepositoryException e) {
            throw SessionFactoryUtils.translateException(e);
        }
    }

    /**
     * @return Returns the repository.
     */
    public Repository getRepository() {
        return repository;
    }

    /**
     * @param repository
     *            The repository to set.
     */
    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    /**
     * @return Returns the workspaceName.
     */
    public String getWorkspaceName() {
        return workspaceName;
    }

    /**
     * @param workspaceName
     *            The workspaceName to set.
     */
    public void setWorkspaceName(String workspaceName) {
        this.workspaceName = workspaceName;
    }

    /**
     * @return Returns the credentials.
     */
    public Credentials getCredentials() {
        return credentials;
    }

    /**
     * @param credentials
     *            The credentials to set.
     */
    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof JcrSessionFactory)
            return (this.hashCode() == obj.hashCode());
        return false;

    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        int result = 17;
        result = 37 * result + repository.hashCode();
        // add the optional params (can be null)
        if (credentials != null)
            result = 37 * result + credentials.hashCode();
        if (workspaceName != null)
            result = 37 * result + workspaceName.hashCode();

        return result;
    }

}
