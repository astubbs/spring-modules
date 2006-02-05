package org.springmodules.jcr.support;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.support.DaoSupport;
import org.springmodules.jcr.JcrTemplate;
import org.springmodules.jcr.SessionFactory;
import org.springmodules.jcr.SessionFactoryUtils;
import org.springmodules.jcr.SessionHolderProviderManager;

/**
 * Convenient class for accessing Jcr objects.
 * 
 * 
 * @author Costin Leau
 * @author Guillaume Bort <guillaume.bort@zenexity.fr>
 */
public abstract class JcrDaoSupport extends DaoSupport {

	private JcrTemplate jcrTemplate;
	private SessionHolderProviderManager providerManager;
	private SessionFactory sessionFactory;

	/**
	 * Set the JCR SessionFactory to be used by this DAO.
	 * Will automatically create a JcrTemplate for the given SessionFactory.
	 * @see #createJcrTemplate
	 * @see #setJcrTemplate
	 */
	public final void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * Create a JcrTemplate for the given JcrSessionFactory.
	 * Only invoked if populating the DAO with a SessionFactory reference!
	 * <p>Can be overridden in subclasses to provide a JcrTemplate instance
	 * with different configuration, or a custom JcrTemplate subclass.
	 * 
	 * @param sessionFactory the JCR SessionFactory to create a JcrTemplate for
	 * @return the new JcrTemplate instance
	 * @see #setSessionFactory
	 */
	protected JcrTemplate createJcrTemplate(SessionFactory sessionFactory) {
		return new JcrTemplate(sessionFactory);
	}

	/**
	 * Return the Jcr SessionFactory used by this DAO.
	 */
	public final SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * Set the JcrTemplate for this DAO explicitly,
	 * as an alternative to specifying a SessionFactory.
	 * @see #setSessionFactory
	 */
	public final void setJcrTemplate(JcrTemplate jcrTemplate) {
		this.jcrTemplate = jcrTemplate;
	}

	/**
	 * Return the JcrTemplate for this DAO, pre-initialized
	 * with the SessionFactory or set explicitly.
	 */
	public final JcrTemplate getJcrTemplate() {
		return jcrTemplate;
	}

	protected final void checkDaoConfig() {
		if (this.jcrTemplate == null && this.sessionFactory == null) {
			throw new IllegalArgumentException(
					"sessionFactory or jcrTemplate is required");
		}
	}

	/**
	 * Get a JCR Session, either from the current transaction or
	 * a new one. The latter is only allowed if the "allowCreate" setting
	 * of this bean's JcrTemplate is true.
	 * @return the JCR Session
	 * @throws DataAccessResourceFailureException if the Session couldn't be created
	 * @throws IllegalStateException if no thread-bound Session found and allowCreate false
	 * @see org.springmodules.jcr.SessionFactoryUtils#getSession
	 */
	protected final Session getSession() {
		return getSession(this.jcrTemplate.isAllowCreate());
	}

	/**
	 * Get a JCR Session, either from the current transaction or
	 * a new one. The latter is only allowed if "allowCreate" is true.
	 * @param allowCreate if a non-transactional Session should be created
	 * when no transactional Session can be found for the current thread
	 * @return the JCR Session
	 * @throws DataAccessResourceFailureException if the Session couldn't be created
	 * @throws IllegalStateException if no thread-bound Session found and allowCreate false
	 * @see org.springmodules.jcr.SessionFactoryUtils#getSession
	 */
	protected final Session getSession(boolean allowCreate) throws DataAccessResourceFailureException,
			IllegalStateException {

		return SessionFactoryUtils.getSession(getSessionFactory(), allowCreate);
	}

	/**
	 * Convert the given JCRException to an appropriate exception from the
	 * org.springframework.dao hierarchy.
	 * <p>Delegates to the convertJCRAccessException method of this DAO's JCRTemplate.
	 * @param ex JCRException that occured
	 * @return the corresponding DataAccessException instance
	 * @see #setJCRTemplate
	 * @see org.springmodules.jcr.JcrTemplate#convertJCRAccessException
	 */
	protected final DataAccessException convertJCRAccessException(RepositoryException ex) {
		return this.jcrTemplate.convertJcrAccessException(ex);
	}

	/**
	 * Close the given JCR Session, created via this DAO's
	 * SessionFactory, if it isn't bound to the thread.
	 * @param pm Session to close
	 * @see org.springframework.orm.JCR.SessionFactoryUtils#releaseSession
	 */
	protected final void releaseSession(Session session) {
		SessionFactoryUtils.releaseSession(session, getSessionFactory());
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
	 * @see org.springframework.dao.support.DaoSupport#initDao()
	 */
	protected final void initDao() throws Exception {
		if (this.jcrTemplate == null)
			this.jcrTemplate = createJcrTemplate(sessionFactory);
	}

}
