package org.springmodules.jcr;

import javax.jcr.AccessDeniedException;
import javax.jcr.InvalidItemStateException;
import javax.jcr.ItemExistsException;
import javax.jcr.ItemNotFoundException;
import javax.jcr.NamespaceException;
import javax.jcr.NoSuchWorkspaceException;
import javax.jcr.PathNotFoundException;
import javax.jcr.ReferentialIntegrityException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.ValueFormatException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.query.InvalidQueryException;
import javax.jcr.version.VersionException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

/**
 * Helper class.
 * 
 * TODO: add JTA lookups/support
 * 
 * @author Costin Leau
 * 
 */
public abstract class SessionFactoryUtils {

    private static final Log logger = LogFactory.getLog(SessionFactoryUtils.class);

    /**
     * Get a JCR Session for the given Repository. Is aware of and will return
     * any existing corresponding Session bound to the current thread, for
     * example when using JcrTransactionManager. Will create a new Session
     * otherwise, if allowCreate is true. This is the getSession method used by
     * typical data access code, in combination with releaseSession called when
     * done with the Session. Note that JcrTemplate allows to write data access
     * code without caring about such resource handling. Supports
     * synchronization with both Spring-managed JTA transactions (i.e.
     * JtaTransactionManager) and non-Spring JTA transactions (i.e. plain JTA or
     * EJB CMT).
     * 
     * @param sessionFactory
     *            JcrSessionFactory to create session with
     * @param allowCreate
     *            if a non-transactional Session should be created when no
     *            transactional Session can be found for the current thread
     * @return
     */
    public static Session getSession(SessionFactory sessionFactory, boolean allowCreate) {
        Assert.notNull(sessionFactory, "No sessionFactory specified");

        // check if there is any transaction going on
        SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager.getResource(sessionFactory);
        if (sessionHolder != null && sessionHolder.getSession() != null)
            return sessionHolder.getSession();

        if (!allowCreate && !TransactionSynchronizationManager.isSynchronizationActive()) {
            throw new IllegalStateException("No session bound to thread, " + "and configuration does not allow creation of non-transactional one here");
        }

        logger.debug("Opening JCR Session");
        Session session;
        session = sessionFactory.getSession();

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            logger.debug("Registering transaction synchronization for JCR session");
            // Use same session for further JCR actions within the transaction
            // thread object will get removed by synchronization at transaction
            // completion.
            sessionHolder = new SessionHolder(session);
            sessionHolder.setSynchronizedWithTransaction(true);
            TransactionSynchronizationManager.registerSynchronization(new JcrSessionSynchronization(sessionHolder, sessionFactory));
            TransactionSynchronizationManager.bindResource(sessionFactory, sessionHolder);
        }

        return session;
    }

    /**
     * Get a new Jcr Session from the given JcrSessionFactory. Will return a new
     * Session even if there already is a pre-bound Session for the given
     * SessionFactory. This method will work only if the existing session is not
     * included in a transaction.
     * 
     * @param sessionFactory
     *            Jcr SessionFactory to create the session with
     * @return the new Session
     */
    public static Session getNewSession(SessionFactory sessionFactory) {

        throw new UnsupportedOperationException();
        
        /**
        Assert.notNull(sessionFactory, "No Jcr sessionFactory specified");

        SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager.getResource(sessionFactory);
        if (sessionHolder != null && sessionHolder.getSession() != null) {
            return sessionHolder.getSession();
        }
        return sessionFactory.getSession();
        */
    }

    /**
     * Return whether the given JCR Session is transactional, that is, bound to
     * the current thread by Spring's transaction facilities.
     * 
     * @param session
     *            the JCR Session to check
     * @param sessionFactory
     *            the JCR SessionFactory that the Session was created with (can
     *            be null)
     * @return whether the Session is transactional
     */
    public static boolean isSessionTransactional(Session session, SessionFactory sessionFactory) {
        if (sessionFactory == null) {
            return false;
        }
        SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager.getResource(sessionFactory);
        return (sessionHolder != null && session == sessionHolder.getSession());
    }

    /**
     * Close the given Session, created via the given repository, if it is not
     * managed externally (i.e. not bound to the thread).
     * 
     * @param session
     *            the Jcr Session to close
     * @param sessionFactory
     *            JcrSessionFactory that the Session was created with (can be
     *            null)
     */
    public static void releaseSession(Session session, SessionFactory sessionFactory) {
        if (session == null) {
            return;
        }
        // Only close non-transactional Sessions.
        if (!isSessionTransactional(session, sessionFactory)) {
            doReleaseSession(session, sessionFactory);
        }
    }

    /**
     * 
     * Actually release a JcrSession for the given factory.
     * 
     * @param session
     * @param sessionFactory
     */
    public static void doReleaseSession(Session session, SessionFactory sessionFactory) {

        if (session == null) {
            return;
        }
        // Only release non-transactional sessions
        if (!isSessionTransactional(session, sessionFactory)) {
            logger.debug("Closing JCR Session");
            session.logout();
        }
    }
    
    /**
     * Jcr exception translator - it converts specific JSR-170 checked exceptions into 
     * unchecked Spring DA exception.
     * 
     * @author Guillaume Bort <guillaume.bort@zenexity.fr>
     * 
     * @param ex
     * @return
     */
    public static DataAccessException translateException(RepositoryException ex) {
        if (ex instanceof AccessDeniedException) {
            return new DataRetrievalFailureException("Access denied to this data", ex);
        }
        if (ex instanceof ConstraintViolationException) {
            return new DataIntegrityViolationException("Constraint has been violated", ex);
        }
        if (ex instanceof InvalidItemStateException) {
            return new ConcurrencyFailureException("Invalid item state", ex);
        }
        if (ex instanceof InvalidQueryException) {
            return new DataRetrievalFailureException("Invalid query", ex);
        }
        if (ex instanceof ItemExistsException) {
            return new DataIntegrityViolationException("An item already exists", ex);
        }
        if (ex instanceof ItemNotFoundException) {
            return new DataRetrievalFailureException("Item not found", ex);
        }
        if (ex instanceof LockException) {
            return new ConcurrencyFailureException("Item is locked", ex);
        }
        if (ex instanceof NamespaceException) {
            return new InvalidDataAccessApiUsageException("Namespace not registred", ex);
        }
        if (ex instanceof NoSuchNodeTypeException) {
            return new InvalidDataAccessApiUsageException("No such node type", ex);
        }
        if (ex instanceof NoSuchWorkspaceException) {
            return new DataAccessResourceFailureException("Workspace not found", ex);
        }
        if (ex instanceof PathNotFoundException) {
            return new DataRetrievalFailureException("Path not found", ex);
        }
        if (ex instanceof ReferentialIntegrityException) {
            return new DataIntegrityViolationException("Referential integrity violated", ex);
        }
        if (ex instanceof UnsupportedRepositoryOperationException) {
            return new InvalidDataAccessApiUsageException("Unsupported operation", ex);
        }
        if (ex instanceof ValueFormatException) {
            return new InvalidDataAccessApiUsageException("Incorrect value format", ex);
        }
        if (ex instanceof VersionException) {
            return new DataIntegrityViolationException("Invalid version graph operation", ex);
        }
        // fallback
        return new JcrSystemException(ex);
    }

    /**
     * Callback for resource cleanup at the end of a non-JCR transaction (e.g.
     * when participating in a JtaTransactionManager transaction).
     * 
     * @see org.springframework.transaction.jta.JtaTransactionManager
     */
    private static class JcrSessionSynchronization extends TransactionSynchronizationAdapter {

        private final SessionHolder sessionHolder;

        private final SessionFactory sessionFactory;

        private boolean holderActive = true;

        /**
         * @param sessionFactory
         * @param holder
         */
        public JcrSessionSynchronization(SessionHolder holder, SessionFactory sessionFactory) {
            this.sessionFactory = sessionFactory;
            sessionHolder = holder;
        }

        public void suspend() {
            if (this.holderActive) {
                TransactionSynchronizationManager.unbindResource(this.sessionFactory);
            }
        }

        public void resume() {
            if (this.holderActive) {
                TransactionSynchronizationManager.bindResource(this.sessionFactory, this.sessionHolder);
            }
        }

        public void beforeCompletion() {
            TransactionSynchronizationManager.unbindResource(this.sessionFactory);
            this.holderActive = false;
            releaseSession(this.sessionHolder.getSession(), this.sessionFactory);
        }
    }

}
