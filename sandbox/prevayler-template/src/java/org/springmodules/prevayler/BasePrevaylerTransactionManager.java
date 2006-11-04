package org.springmodules.prevayler;

import org.apache.log4j.Logger;

import org.prevayler.Prevayler;

import org.springmodules.prevayler.configuration.PrevaylerConfiguration;
import org.springmodules.prevayler.system.PrevalentSystem;
import org.springmodules.prevayler.transaction.TransactionCommand;

/**
 * This {@link TransactionManager} implementation doesn't support transaction demarcation: each execution
 * is directly delegated to Prevayler, following so the base Prevayler transactional behaviour.
 *
 * @author Sergio Bossa
 */
public class BasePrevaylerTransactionManager implements TransactionManager {
    
    private static final Logger logger = Logger.getLogger(BasePrevaylerTransactionManager.class);
    
    private PrevaylerConfiguration configuration;
    
    public BasePrevaylerTransactionManager(PrevaylerConfiguration configuration) {
        this.configuration = configuration;
    }
    
    public BasePrevaylerTransactionManager() {
    }
    
    public void setPrevaylerConfiguration(PrevaylerConfiguration configuration) {
        this.configuration = configuration;
    }
    
    public void begin() {
        throw new UnsupportedOperationException("Transaction demarcation is not supported by this transaction manager!");
    }
    
    public void commit() {
        throw new UnsupportedOperationException("Transaction demarcation is not supported by this transaction manager!");
    }
    
    public void rollback() {
        throw new UnsupportedOperationException("Transaction demarcation is not supported by this transaction manager!");
    }
    
    public Object execute(PrevaylerCallback callback) {
        logger.debug("Executing callback into Prevayler.");
        try {
            Prevayler prevayler = this.configuration.getPrevaylerInstance();
            return prevayler.execute(new TransactionCommand(callback));
        } catch (Exception ex) {
            throw new PrevaylerTransactionException("Error while executing callback.", ex);
        }
    }
    
    public Object execute(SystemCallback callback) {
        logger.debug("Executing callback into prevalent system.");
        PrevalentSystem localSystem = (PrevalentSystem) this.configuration.getPrevaylerInstance().prevalentSystem();
        return localSystem.execute(callback);
    }
}
