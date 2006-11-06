package org.springmodules.prevayler;

import org.springmodules.prevayler.configuration.PrevaylerConfiguration;

/**
 * Persistence manager for transaction demarcation and execution of callbacks into Prevayler and its
 * prevalent system.<br>
 * The transaction manager creates, commits and rollbacks transactions by the use of a {@link Session}
 * object, representing the transaction execution context.
 *
 * @author Sergio Bossa
 */
public interface PersistenceManager {
    
    public Session createTransaction();
    
    public void commitTransaction(Session session);
    
    public void rollbackTransaction(Session session);
    
    public void setPrevaylerConfiguration(PrevaylerConfiguration configuration);
}
