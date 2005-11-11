package org.springmodules.jcr;

import javax.jcr.Credentials;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Workspace;
import javax.jcr.observation.ObservationManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springmodules.util.ArrayUtils;

/**
 * Jcr Session Factory. This class is just a simple wrapper around the
 * repository which facilitates session retrieval through a central point. No exception
 * conversion from Jcr Repository exceptions to Spring DAO exceptions is done.
 * </p>
 * The session factory is able to add event listener definitions for each session and some
 * utility methods.
 * Note that for added functionality (like JackRabbit SessionListener) you can use
 * the decorators package (available from JackRabbit).
 * 
 * @author Costin Leau
 * @author Brian Moseley <bcm@osafoundation.org>
 * 
 */
public class JcrSessionFactory implements InitializingBean, SessionFactory {

	private static final Log log = LogFactory.getLog(JcrSessionFactory.class);

	private Repository repository;

	private String workspaceName;

	private Credentials credentials;

	private EventListenerDefinition eventListenerDefinitions[];

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
		if (!supportsObservation() && eventListenerDefinitions != null)
			throw new IllegalArgumentException("repository " + getRepositoryInfo() + " does NOT support Observation; remove Listener definitions");
	}

	/**
	 * @see org.springmodules.jcr.SessionFactory#getSession()
	 */
	public Session getSession() throws RepositoryException {
		return addListeners(repository.login(credentials, workspaceName));
	}

	/**
	 * Hook for adding listeners to the newly returned session.
	 * We have to treat exceptions manually and can't reply on the template.
	 * 
	 * @param session JCR session
	 * @return the listened session
	 */
	protected Session addListeners(Session session) throws RepositoryException {
		if (eventListenerDefinitions != null) {
			Workspace ws = session.getWorkspace();
			ObservationManager manager = ws.getObservationManager();
			if (log.isDebugEnabled())
				log.debug("adding listeners "
						+ ArrayUtils.toString(eventListenerDefinitions)
						+ " for session "
						+ session);

			for (int i = 0; i < eventListenerDefinitions.length; i++) {
				manager.addEventListener(eventListenerDefinitions[i].getListener(),
						eventListenerDefinitions[i].getEventTypes(),
						eventListenerDefinitions[i].getAbsPath(), eventListenerDefinitions[i].isDeep(),
						eventListenerDefinitions[i].getUuid(), eventListenerDefinitions[i].getNodeTypeName(),
						eventListenerDefinitions[i].isNoLocal());
			}
		}
		return session;

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

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("SessionFactory for ");
		buffer.append(getRepositoryInfo());
		buffer.append("|workspace=");
		buffer.append(getWorkspaceName());
		return buffer.toString();
	}

	/**
	 * @return Returns the eventListenerDefinitions.
	 */
	public EventListenerDefinition[] getEventListenerDefinitions() {
		return eventListenerDefinitions;
	}

	/**
	 * @param eventListenerDefinitions The eventListenerDefinitions to set.
	 */
	public void setEventListenerDefinitions(EventListenerDefinition[] eventListenerDefinitions) {
		this.eventListenerDefinitions = eventListenerDefinitions;
	}

	public boolean supportsLevel2() {
		return "true".equals(getRepository().getDescriptor(Repository.LEVEL_2_SUPPORTED));
	}

	public boolean supportsTransactions() {
		return "true".equals(getRepository().getDescriptor(Repository.OPTION_TRANSACTIONS_SUPPORTED));
	}

	public boolean supportsVersioning() {
		return "true".equals(getRepository().getDescriptor(Repository.OPTION_VERSIONING_SUPPORTED));
	}

	public boolean supportsObservation() {
		return "true".equals(getRepository().getDescriptor(Repository.OPTION_OBSERVATION_SUPPORTED));
	}

	public boolean supportsLocking() {
		return "true".equals(getRepository().getDescriptor(Repository.OPTION_LOCKING_SUPPORTED));
	}

	public boolean supportsSQLQuery() {
		return "true".equals(getRepository().getDescriptor(Repository.OPTION_QUERY_SQL_SUPPORTED));
	}

	public boolean supportsXPathPosIndex() {
		return "true".equals(getRepository().getDescriptor(Repository.QUERY_XPATH_POS_INDEX));
	}

	public boolean supportsXPathDocOrder() {
		return "true".equals(getRepository().getDescriptor(Repository.QUERY_XPATH_DOC_ORDER));
	}

	private String getRepositoryInfo() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(getRepository().getDescriptor(Repository.REP_NAME_DESC));
		buffer.append(" ");
		buffer.append(getRepository().getDescriptor(Repository.REP_VERSION_DESC));
		return buffer.toString();
	}

}
