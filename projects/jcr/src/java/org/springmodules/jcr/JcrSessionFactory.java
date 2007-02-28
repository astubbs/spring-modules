package org.springmodules.jcr;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.jcr.Credentials;
import javax.jcr.NamespaceRegistry;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Workspace;
import javax.jcr.observation.ObservationManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springmodules.jcr.support.GenericSessionHolderProvider;

/**
 * Jcr Session Factory. This class is just a simple wrapper around the
 * repository which facilitates session retrieval through a central point. No
 * exception conversion from Jcr Repository exceptions to Spring DAO exceptions
 * is done.
 * 
 * <p/> The session factory is able to add event listener definitions for each
 * session and some utility methods.<br/> Note that for added functionality
 * (like JackRabbit SessionListener) you can use the decorators package
 * (available from JackRabbit).
 * 
 * <p/> This factory beans allows registration for namespaces. By default, newly
 * registered namespaces will <strong>not</strong> be unregistered once the
 * FactoryBean is destroyed and existing namespaces registered under the same
 * suffix will <strong>not</strong> be overwritten since most JCR
 * implementations do not support these features.
 * 
 * One can change this behavior using the
 * {@link #setForceNamespacesRegistration(boolean)},
 * {@link #setKeepRegisteredNamespaces(boolean) and
 * {@link #skipRegisteredNamespace(boolean)} methods.
 * 
 * If 'forceNamespacesRegistration' is true and 'keepNamespaces' false, the
 * overwritten namespaces are registered back when the factory is destroyed.
 * 
 * @author Costin Leau
 * @author Brian Moseley <bcm@osafoundation.org>
 * 
 */
public class JcrSessionFactory implements InitializingBean, DisposableBean, SessionFactory {

	private static final Log log = LogFactory.getLog(JcrSessionFactory.class);

	private Repository repository;

	private String workspaceName;

	private Credentials credentials;

	private EventListenerDefinition eventListeners[] = new EventListenerDefinition[] {};

	private Properties namespaces;

	private Map overwrittenNamespaces;

	private boolean forceNamespacesRegistration = false;

	private boolean keepRegisteredNamespaces = true;

	private boolean skipRegisteredNamespace = true;

	/**
	 * session holder provider manager - optional.
	 */
	private SessionHolderProviderManager sessionHolderProviderManager;

	/**
	 * session holder provider - determined and used internally.
	 */
	private SessionHolderProvider sessionHolderProvider;

	/**
	 * Constructor with all the required fields.
	 * 
	 * @param repository
	 * @param workspaceName
	 * @param credentials
	 */
	public JcrSessionFactory(Repository repository, String workspaceName, Credentials credentials) {
		this(repository, workspaceName, credentials, null);
	}

	/**
	 * Constructor containing all the fields available.
	 * 
	 * @param repository
	 * @param workspaceName
	 * @param credentials
	 * @param sessionHolderProviderManager
	 */
	public JcrSessionFactory(Repository repository, String workspaceName, Credentials credentials,
			SessionHolderProviderManager sessionHolderProviderManager) {
		this.repository = repository;
		this.workspaceName = workspaceName;
		this.credentials = credentials;
		this.sessionHolderProviderManager = sessionHolderProviderManager;

		try {
			afterPropertiesSet();
		}
		catch (RepositoryException ex) {
			// convert the exception
			throw SessionFactoryUtils.translateException(ex);
		}
	}

	/**
	 * Empty constructor.
	 */
	public JcrSessionFactory() {
	}

	public void afterPropertiesSet() throws RepositoryException {
		Assert.notNull(getRepository(), "repository is required");

		if (eventListeners != null && eventListeners.length > 0 && !JcrUtils.supportsObservation(getRepository()))
			throw new IllegalArgumentException("repository " + getRepositoryInfo()
					+ " does NOT support Observation; remove Listener definitions");

		registerNamespaces();

		// determine the session holder provider
		if (sessionHolderProviderManager == null) {
			if (log.isDebugEnabled())
				log.debug("no session holder provider manager set; using the default one");
			sessionHolderProvider = new GenericSessionHolderProvider();
		}
		else
			sessionHolderProvider = sessionHolderProviderManager.getSessionProvider(getRepository());
	}

	/**
	 * Register the namespaces.
	 * 
	 * @param session
	 * @throws RepositoryException
	 */
	protected void registerNamespaces() throws RepositoryException {

		if (namespaces == null || namespaces.isEmpty())
			return;

		if (log.isDebugEnabled())
			log.debug("registering custom namespaces " + namespaces);

		NamespaceRegistry registry = getSession().getWorkspace().getNamespaceRegistry();

		// unregister namespaces if told so
		if (forceNamespacesRegistration) {

			// save the old namespace only if it makes sense
			if (!keepRegisteredNamespaces)
				overwrittenNamespaces = new HashMap(namespaces.size());
			// do the lookup, so we avoid exceptions
			String[] prefixes = registry.getPrefixes();
			// sort the array
			Arrays.sort(prefixes);

			// search occurences
			for (Iterator iter = namespaces.keySet().iterator(); iter.hasNext();) {
				String prefix = (String) iter.next();
				int position = Arrays.binarySearch(prefixes, prefix);
				if (position >= 0) {
					if (log.isDebugEnabled()) {
						log.debug("prefix " + prefix + " was already registered; unregistering it");
					}
					if (!keepRegisteredNamespaces) {
						// save old namespace
						overwrittenNamespaces.put(prefix, registry.getURI(prefix));
					}
					registry.unregisterNamespace(prefix);
					// postpone registration for later
				}
			}
		}

		// do the registration
		for (Iterator iter = namespaces.entrySet().iterator(); iter.hasNext();) {
			Map.Entry namespace = (Map.Entry) iter.next();
			String prefix = (String) namespace.getKey();
			String ns = (String) namespace.getValue();
			if (skipRegisteredNamespace && registry.getURI(prefix) != null) {
				log.debug("namespace already registered under [" + prefix + "]; skipping registration");
			}
			else {
				log.debug("registering namespace [" + ns + "] under [" + prefix + "]");
				registry.registerNamespace(prefix, ns);
			}
		}
	}

	/**
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 */
	public void destroy() throws RepositoryException {
		unregisterNamespaces();
	}

	/**
	 * Removes the namespaces.
	 * 
	 * @param session
	 */
	protected void unregisterNamespaces() throws RepositoryException {

		if (namespaces == null || namespaces.isEmpty() || keepRegisteredNamespaces)
			return;

		if (log.isDebugEnabled())
			log.debug("unregistering custom namespaces " + namespaces);

		NamespaceRegistry registry = getSession().getWorkspace().getNamespaceRegistry();

		for (Iterator iter = namespaces.keySet().iterator(); iter.hasNext();) {
			String prefix = (String) iter.next();
			registry.unregisterNamespace(prefix);
		}

		if (forceNamespacesRegistration) {
			if (log.isDebugEnabled())
				log.debug("reverting back overwritten namespaces " + overwrittenNamespaces);
			if (overwrittenNamespaces != null)
				for (Iterator iter = overwrittenNamespaces.entrySet().iterator(); iter.hasNext();) {
					Map.Entry entry = (Map.Entry) iter.next();
					registry.registerNamespace((String) entry.getKey(), (String) entry.getValue());
				}
		}
	}

	/**
	 * @see org.springmodules.jcr.SessionFactory#getSession()
	 */
	public Session getSession() throws RepositoryException {
		return addListeners(repository.login(credentials, workspaceName));
	}

	/**
	 * @see org.springmodules.jcr.SessionFactory#getSessionHolder(javax.jcr.Session)
	 */
	public SessionHolder getSessionHolder(Session session) {
		return sessionHolderProvider.createSessionHolder(session);
	}

	/**
	 * Hook for adding listeners to the newly returned session. We have to treat
	 * exceptions manually and can't reply on the template.
	 * 
	 * @param session JCR session
	 * @return the listened session
	 */
	protected Session addListeners(Session session) throws RepositoryException {
		if (eventListeners != null && eventListeners.length > 0) {
			Workspace ws = session.getWorkspace();
			ObservationManager manager = ws.getObservationManager();
			if (log.isDebugEnabled())
				log.debug("adding listeners " + Arrays.asList(eventListeners).toString() + " for session " + session);

			for (int i = 0; i < eventListeners.length; i++) {
				manager.addEventListener(eventListeners[i].getListener(), eventListeners[i].getEventTypes(),
						eventListeners[i].getAbsPath(), eventListeners[i].isDeep(), eventListeners[i].getUuid(),
						eventListeners[i].getNodeTypeName(), eventListeners[i].isNoLocal());
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
	 * @param repository The repository to set.
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
	 * @param workspaceName The workspaceName to set.
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
	 * @param credentials The credentials to set.
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
	public EventListenerDefinition[] getEventListeners() {
		return eventListeners;
	}

	/**
	 * @param eventListenerDefinitions The eventListenerDefinitions to set.
	 */
	public void setEventListeners(EventListenerDefinition[] eventListenerDefinitions) {
		this.eventListeners = eventListenerDefinitions;
	}

	/**
	 * A toString representation of the Repository.
	 * 
	 * @return
	 */
	private String getRepositoryInfo() {
		// in case toString() is called before afterPropertiesSet()
		if (getRepository() == null)
			return "<N/A>";

		StringBuffer buffer = new StringBuffer();
		buffer.append(getRepository().getDescriptor(Repository.REP_NAME_DESC));
		buffer.append(" ");
		buffer.append(getRepository().getDescriptor(Repository.REP_VERSION_DESC));
		return buffer.toString();
	}

	/**
	 * @return Returns the namespaces.
	 */
	public Properties getNamespaces() {
		return namespaces;
	}

	/**
	 * @param namespaces The namespaces to set.
	 */
	public void setNamespaces(Properties namespaces) {
		this.namespaces = namespaces;
	}

	/**
	 * Used internally.
	 * 
	 * @return Returns the sessionHolderProvider.
	 */
	protected SessionHolderProvider getSessionHolderProvider() {
		return sessionHolderProvider;
	}

	/**
	 * Used internally.
	 * 
	 * @param sessionHolderProvider The sessionHolderProvider to set.
	 */
	protected void setSessionHolderProvider(SessionHolderProvider sessionHolderProvider) {
		this.sessionHolderProvider = sessionHolderProvider;
	}

	/**
	 * @return Returns the sessionHolderProviderManager.
	 */
	public SessionHolderProviderManager getSessionHolderProviderManager() {
		return sessionHolderProviderManager;
	}

	/**
	 * @param sessionHolderProviderManager The sessionHolderProviderManager to
	 * set.
	 */
	public void setSessionHolderProviderManager(SessionHolderProviderManager sessionHolderProviderManager) {
		this.sessionHolderProviderManager = sessionHolderProviderManager;
	}

	/**
	 * Indicate if the given namespace registrations will be kept (the default)
	 * when the application context closes down or if they will be unregistered.
	 * 
	 * If unregistered, the namespace mappings that were overriden are
	 * registered back to the repository.
	 * 
	 * @see #forceNamespacesRegistration
	 * @param keepNamespaces The keepNamespaces to set.
	 */
	public void setKeepRegisteredNamespaces(boolean keepNamespaces) {
		this.keepRegisteredNamespaces = keepNamespaces;
	}

	/**
	 * Indicate if the given namespace registrations will override the namespace
	 * already registered in the repository under the same prefix. This will
	 * cause unregistration for the namespaces that will be modified.
	 * 
	 * <p/> However, depending on the
	 * {@link #setKeepRegisteredNamespaces(boolean)} setting, the old namespaces
	 * can be registered back once the application context is destroyed.
	 * 
	 * False by default.
	 * 
	 * @param forceNamespacesRegistration The forceNamespacesRegistration to
	 * set.
	 */
	public void setForceNamespacesRegistration(boolean forceNamespacesRegistration) {
		this.forceNamespacesRegistration = forceNamespacesRegistration;
	}

	/**
	 * Indicate if the given namespace registrations will skip already
	 * registered namespaces or not. If true (default), the new namespace will
	 * not be registered and the old namespace kept in place. If not skipped,
	 * registration of new namespaces will fail if there are already namespace
	 * registered under the same prefix.
	 * 
	 * <p/> This flag is required for JCR implementations which do not support
	 * namespace unregistration which render the
	 * {@link #setForceNamespacesRegistration(boolean)} method useless (as
	 * namespace registration cannot be forced).
	 * 
	 * @param skipRegisteredNamespace The skipRegisteredNamespace to set.
	 */
	public void setSkipRegisteredNamespace(boolean skipRegisteredNamespace) {
		this.skipRegisteredNamespace = skipRegisteredNamespace;
	}

}
