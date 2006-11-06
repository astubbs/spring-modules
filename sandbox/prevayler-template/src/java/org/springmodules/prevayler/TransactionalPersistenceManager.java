package org.springmodules.prevayler;

import edu.emory.mathcs.backport.java.util.concurrent.Executors;
import edu.emory.mathcs.backport.java.util.concurrent.Future;
import edu.emory.mathcs.backport.java.util.concurrent.Semaphore;
import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;
import edu.emory.mathcs.backport.java.util.concurrent.locks.Lock;
import edu.emory.mathcs.backport.java.util.concurrent.locks.ReentrantLock;
import org.apache.log4j.Logger;
import org.springmodules.prevayler.configuration.PrevaylerConfiguration;
import org.springmodules.prevayler.support.PrevaylerTransactionException;

/**
 * {@link PersistenceManager} implementation supporting external transaction demarcation.
 *
 * @author Sergio Bossa
 */
public class TransactionalPersistenceManager implements PersistenceManager {
    
    private static final Logger logger = Logger.getLogger(TransactionalPersistenceManager.class);
    
    private static final long DEFAULT_TIMEOUT = 5;
    
    private final Lock endLock = new ReentrantLock();
    private final Semaphore transactionSemaphore = new Semaphore(1, true);
    private Future semaphoreHandler;
    private Session activeSession;
    
    private PrevaylerConfiguration configuration;
    private long secondsTimeout;
    
    public TransactionalPersistenceManager() {
        this.secondsTimeout = DEFAULT_TIMEOUT;
    }
    
    public TransactionalPersistenceManager(PrevaylerConfiguration configuration) {
        this();
        this.configuration = configuration;
    }
    
    public Session createTransaction() {
        logger.debug("Waiting for transaction creation....");
        // Acquire transactionSemaphore:
        this.transactionSemaphore.acquireUninterruptibly();
        
        logger.debug("Creating transaction.");
        // Create transactional session;
        this.activeSession = new TransactionalSession(this.configuration);
        
        // Create a watcher thread for releasing the transactionSemaphore after a given timeout expressed in seconds,
        // and store in a thread local variable a "future" to use for stopping the thread when the transaction
        // completes:
        semaphoreHandler = Executors.newSingleThreadScheduledExecutor().schedule(
                new SemaphoreWatcherThread(), this.secondsTimeout, TimeUnit.SECONDS);
        
        return this.activeSession;
    }
    
    public void commitTransaction(Session session) {
        logger.debug("Committing transaction.");
        this.endLock.lock();
        try {
            if (this.activeSession == null || this.activeSession != session) {
                String msg = "Wrong session: no active session, or session timed out.";
                logger.info(msg);
                throw new PrevaylerTransactionException(msg);
            } else {
                session.flush(this.configuration.getPrevaylerInstance());
            }
        } finally {
            this.releaseAll();
            this.endLock.unlock();
        }
    }
    
    public void rollbackTransaction(Session session) {
        logger.debug("Rolling back transaction.");
        this.endLock.lock();
        try {
            if (this.activeSession == null || this.activeSession != session) {
                String msg = "Wrong session: no active session, or session timed out.";
                logger.info(msg);
                throw new PrevaylerTransactionException(msg);
            }
        } finally {
            this.releaseAll();
            this.endLock.unlock();
        }
    }
    
    public void setPrevaylerConfiguration(PrevaylerConfiguration configuration) {
        this.configuration = configuration;
    }
    
    public void setSecondsTimeout(long secondsTimeout) {
        this.secondsTimeout = secondsTimeout;
    }
    
    /** Class internals **/
    
    private void releaseAll() {
        // Put to null the current active session:
        this.activeSession = null;
        // Stop the watcher thread:
        this.semaphoreHandler.cancel(true);
        // Release the transactionSemaphore:
        this.transactionSemaphore.release();
    }
    
    private class SemaphoreWatcherThread implements Runnable {
        
        public void run() {
            if (TransactionalPersistenceManager.this.endLock.tryLock()) {
                try {
                    logger.info("Timeout: forcing transaction abort.");
                    TransactionalPersistenceManager.this.releaseAll();
                } finally {
                    TransactionalPersistenceManager.this.endLock.unlock();
                }
            }
        }
    }
}
