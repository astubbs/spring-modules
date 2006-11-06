package org.springmodules.prevayler;

import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Utility class for managing {@link Session}s. 
 * // FIXME: explain the relation with PlatformTransactionManager. 
 *
 * @author Sergio Bossa
 */
public class PersistenceManagerUtils {
    
    /**
     * Get a Prevayler {@link Session} corresponding to the given {@link PersistenceManager}.<br>
     * It returns the session bound to the current thread, if any, or a new one if <i>allowCreate</i> is true.
     */
    public static Session getSession(PersistenceManager persistenceManager, boolean allowCreate) {
        if (! allowCreate && ! TransactionSynchronizationManager.isSynchronizationActive()) {
            throw new IllegalStateException("No session bound to thread, " +
                    "and configuration does not allow creation of a new one here");
        } else {
            Session session = (Session) TransactionSynchronizationManager.getResource(persistenceManager);
            if (session == null) {
                session = persistenceManager.createTransaction();
                if (TransactionSynchronizationManager.isSynchronizationActive()) {
                    TransactionSynchronizationManager.bindResource(persistenceManager, session);
                }
            }
            return session;
        }
    }
    
    /**
     * Check if the given session, related to the given persistence manager, is boud to the current thread.
     */
    public static boolean isBound(PersistenceManager persistenceManager, Session session) {
        Session boundSession = (Session) TransactionSynchronizationManager.getResource(persistenceManager);
        if (boundSession != null && boundSession == session) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Close the given session related to the given persistence manager.<br>
     * Please not that if the given session is externally managed, that is, bound to the current thread,
     * this method doesn't actually close it.
     */
    /*public static void closeSession(PersistenceManager persistenceManager, Session session) {
        if (session == null) {
            return;
        }
        else {
            Session boundSession = (Session) TransactionSynchronizationManager.getResource(persistenceManager);
            if (boundSession != null && boundSession == session) {
                // It's the transactional session: don't close it, just return:
                return;
            }
            else {
                // Not transactional. close commit session:
                logger.debug("Closing OJB PersistenceBroker");
                pb.close();
            }
        }
    }*/
}
