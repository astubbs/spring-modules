package org.springmodules.prevayler;

import org.apache.log4j.Logger;
import org.springmodules.prevayler.configuration.PrevaylerConfiguration;

/**
 * This {@link PersistenceManager} implementation doesn't support external transaction demarcation: each execution
 * is directly delegated to Prevayler, following the base Prevayler transactional behaviour.<br>
 * So, each single operation produces a corresponding transaction. 
 * 
 * @author Sergio Bossa
 */
public class SimplePersistenceManager implements PersistenceManager {
    
    private static final Logger logger = Logger.getLogger(SimplePersistenceManager.class);
    
    private PrevaylerConfiguration configuration;
    
    public SimplePersistenceManager() {}
    
    public SimplePersistenceManager(PrevaylerConfiguration configuration) {
        this.configuration = configuration;
    }
    
    public void setPrevaylerConfiguration(PrevaylerConfiguration configuration) {
        this.configuration = configuration;
    }
    
    public Session createTransaction() {
        return new SimpleSession(this.configuration);
    }

    public void commitTransaction(Session session) {
        // Do nothing: each operation in the context of the given session has been already automatically executed and committed.
    }

    public void rollbackTransaction(Session session) {
        throw new UnsupportedOperationException("Rollback not supported by this persistence manager: all operations are suddenly committed.");
    }
}
