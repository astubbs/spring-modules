package org.springmodules.prevayler;

import org.apache.log4j.Logger;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.SmartTransactionObject;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Prevayler {@link org.springframework.transaction.support.AbstractPlatformTransactionManager} implementation
 * based on {@link PersistenceManager}s.
 *
 * @author Sergio Bossa
 */
public class PrevaylerTransactionManager extends AbstractPlatformTransactionManager {
    
    private static final Logger logger = Logger.getLogger(PrevaylerTransactionManager.class);
    
    private PersistenceManager persistenceManager;
    
    public PrevaylerTransactionManager() {}
    
    public PrevaylerTransactionManager(PersistenceManager persistenceManager) {
        this.persistenceManager = persistenceManager;
    }
    
    public PersistenceManager getPersistenceManager() {
        return this.persistenceManager;
    }
    
    public void setPersistenceManager(PersistenceManager persistenceManager) {
        this.persistenceManager = persistenceManager;
    }
    
    protected void doBegin(Object transaction, TransactionDefinition transactionDefinition) throws TransactionException {
        PrevaylerTransactionObject txObject = (PrevaylerTransactionObject) transaction;
        Session session = this.persistenceManager.createTransaction();
        TransactionSynchronizationManager.bindResource(persistenceManager, session);
        txObject.setSession(session);
    }
    
    protected void doCommit(DefaultTransactionStatus defaultTransactionStatus) throws TransactionException {
        PrevaylerTransactionObject txObject = (PrevaylerTransactionObject) defaultTransactionStatus.getTransaction();
        Session session = txObject.getSession();
        if (defaultTransactionStatus.isDebug()) {
            logger.debug("Committing transaction in session: " + session);
        }
        this.persistenceManager.commitTransaction(session);
    }
    
    protected void doRollback(DefaultTransactionStatus defaultTransactionStatus) throws TransactionException {
        PrevaylerTransactionObject txObject = (PrevaylerTransactionObject) defaultTransactionStatus.getTransaction();
        Session session = txObject.getSession();
        if (defaultTransactionStatus.isDebug()) {
            logger.debug("Rolling back transaction in session: " + session);
        }
        this.persistenceManager.rollbackTransaction(session);
    }
    
    protected Object doGetTransaction() throws TransactionException {
        Session session = (Session) TransactionSynchronizationManager.getResource(this.persistenceManager);
        return new PrevaylerTransactionObject(session);
    }
    
    protected void doSetRollbackOnly(DefaultTransactionStatus status) throws TransactionException {
        PrevaylerTransactionObject txObject = (PrevaylerTransactionObject) status.getTransaction();
        txObject.setRollbackOnly(true);
    }
    
    protected boolean isExistingTransaction(Object transaction) throws TransactionException {
        PrevaylerTransactionObject txObject = (PrevaylerTransactionObject) transaction;
        if (txObject.getSession() != null) {
            return true;
        } 
        else {
            return false;
        }
    }
    
    protected void doCleanupAfterCompletion(Object transaction) {
        PrevaylerTransactionObject txObject = (PrevaylerTransactionObject) transaction;
        TransactionSynchronizationManager.unbindResource(this.persistenceManager);
        txObject.clear();
    }
    
    /** Class internals **/
    
    private class PrevaylerTransactionObject implements SmartTransactionObject {
        
        private Session session;
        private boolean rollbackFlag;
        
        public PrevaylerTransactionObject(Session session) {
            this.session = session;
        }
        
        public void setRollbackOnly(boolean rollbackFlag) {
            this.rollbackFlag = rollbackFlag;
        }
        
        public boolean isRollbackOnly() {
            return this.rollbackFlag;
        }
        
        public Session getSession() {
            return this.session;
        }
        
        public void setSession(Session session) {
            this.session = session;
        }
        
        public void clear() {
            this.session = null;
        }
    }
}
