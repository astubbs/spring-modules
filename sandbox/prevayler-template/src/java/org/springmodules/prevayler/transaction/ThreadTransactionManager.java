package org.springmodules.prevayler.transaction;

import edu.emory.mathcs.backport.java.util.concurrent.Executors;
import edu.emory.mathcs.backport.java.util.concurrent.Future;
import edu.emory.mathcs.backport.java.util.concurrent.Semaphore;
import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;
import edu.emory.mathcs.backport.java.util.concurrent.locks.ReentrantLock;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import org.apache.log4j.Logger;
import org.prevayler.Prevayler;
import org.springmodules.prevayler.callback.PrevaylerCallback;
import org.springmodules.prevayler.configuration.PrevaylerConfiguration;
import org.springmodules.prevayler.system.PrevalentSystem;
import org.springmodules.prevayler.system.callback.SystemCallback;

/**
 * {@link TransactionManager} implementation supporting thread scoped transaction demarcation.
 *
 * @author Sergio Bossa
 */
public class ThreadTransactionManager implements TransactionManager {
    
    private static final Logger logger = Logger.getLogger(ThreadTransactionManager.class);
    
    private static final long DEFAULT_TIMEOUT = 5;
    
    private static final ThreadLocal transactionSystem = new ThreadLocal();
    private static final ThreadLocal transactionQueue = new ThreadLocal();
    private static final ThreadLocal transactionFlag = new ThreadLocal();
    private static final ThreadLocal semaphoreWatcher = new ThreadLocal();
    
    private static final Semaphore transactionSemaphore = new Semaphore(1, true);
    private static final ReentrantLock activeTransactionLock = new ReentrantLock(true);
    
    private PrevaylerConfiguration configuration;
    private long secondsTimeout;
    
    public ThreadTransactionManager() {
        this.secondsTimeout = DEFAULT_TIMEOUT;
    }
    
    public ThreadTransactionManager(PrevaylerConfiguration configuration) {
        this();
        this.configuration = configuration;
    }
    
    public void begin() {
        logger.debug("Waiting for transaction ....");
        // Acquire transactionSemaphore:
        ThreadTransactionManager.transactionSemaphore.acquireUninterruptibly();
        // Acquire activeTransactionLock:
        ThreadTransactionManager.activeTransactionLock.lock();
        try {
            // Set the thread local transaction flag:
            ThreadTransactionManager.transactionFlag.set(Boolean.TRUE);
            
            // Begin transaction:
            logger.debug("Beginning transaction.");
            Prevayler prevayler = this.configuration.getPrevaylerInstance();
            PrevalentSystem system = (PrevalentSystem) prevayler.prevalentSystem();
            ThreadTransactionManager.transactionSystem.set(this.deepCopy(system));
            ThreadTransactionManager.transactionQueue.set(new LinkedList());
            
            // Create a watcher thread for releasing the transactionSemaphore after a given period of inactivity expressed in seconds,
            // and store in a thread local variable a "future" to use for stopping the thread when the transaction
            // completes successfully:
            ThreadTransactionManager.semaphoreWatcher.set(
                    Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
                    new SemaphoreWatcherThread(), this.secondsTimeout, this.secondsTimeout, TimeUnit.SECONDS));
        } finally {
            ThreadTransactionManager.activeTransactionLock.unlock();
        }
    }
    
    public void commit() {
        logger.debug("Committing transaction.");
        // Acquire activeTransactionLock:
        ThreadTransactionManager.activeTransactionLock.lock();
        try {
            if (ThreadTransactionManager.transactionFlag.get() == null) {
                logger.info("No active transaction: no transaction was started, or the previous transaction timed out.");
                throw new PrevaylerTransactionException("No active transaction found!");
            } else {
                Prevayler prevayler = this.configuration.getPrevaylerInstance();
                LinkedList localQueue = (LinkedList) ThreadTransactionManager.transactionQueue.get();
                CompositeTransactionCommand command = new CompositeTransactionCommand(localQueue);
                prevayler.execute(command);
            }
        } finally {
            this.releaseAll();
            ThreadTransactionManager.activeTransactionLock.unlock();
        }
    }
    
    public void rollback() {
        logger.debug("Rolling back transaction.");
        // Acquire activeTransactionLock:
        ThreadTransactionManager.activeTransactionLock.lock();
        try {
            if (ThreadTransactionManager.transactionFlag.get() == null) {
                logger.info("No active transaction: no transaction was started, or the previous transaction timed out.");
                throw new PrevaylerTransactionException("No active transaction found!");
            }
        } finally {
            this.releaseAll();
            ThreadTransactionManager.activeTransactionLock.unlock();
        }
    }
    
    public Object execute(PrevaylerCallback callback) {
        logger.debug("Executing transactional prevayler callback.");
        // Acquire activeTransactionLock:
        ThreadTransactionManager.activeTransactionLock.lock();
        try {
            if (ThreadTransactionManager.transactionFlag.get() == null) {
                logger.info("No active transaction: no transaction was started, or the previous transaction timed out.");
                throw new PrevaylerTransactionException("No active transaction found!");
            } else {
                // Enqueue a copy of the callback for later execution through prevayler at commit time:
                LinkedList localQueue = (LinkedList) ThreadTransactionManager.transactionQueue.get();
                PrevaylerCallback copiedCallback = (PrevaylerCallback) this.deepCopy(callback);
                localQueue.add(copiedCallback);
                // Locally execute the callback:
                PrevalentSystem localSystem = (PrevalentSystem) ThreadTransactionManager.transactionSystem.get();
                return callback.doInTransaction(localSystem);
            }
        } finally {
            ThreadTransactionManager.activeTransactionLock.unlock();
        }
    }
    
    public Object execute(SystemCallback callback) {
        logger.debug("Executing transactional system callback.");
        // Acquire activeTransactionLock:
        ThreadTransactionManager.activeTransactionLock.lock();
        try {
            if (ThreadTransactionManager.transactionFlag.get() == null) {
                logger.info("No active transaction: no transaction was started, or the previous transaction timed out.");
                throw new PrevaylerTransactionException("No active transaction found!");
            } else {
                // Locally execute the callback:
                PrevalentSystem localSystem = (PrevalentSystem) ThreadTransactionManager.transactionSystem.get();
                return localSystem.execute(callback);
                // No need to enqueue because this is a callback to directly execute into the system.
            }
        } finally {
            ThreadTransactionManager.activeTransactionLock.unlock();
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
        // Release resources:
        ThreadTransactionManager.transactionSystem.set(null);
        ThreadTransactionManager.transactionQueue.set(null);
        ThreadTransactionManager.transactionFlag.set(null);
        
        // Release the transactionSemaphore:
        ThreadTransactionManager.transactionSemaphore.release();
        
        // Stop the watcher thread:
        Future future = (Future) ThreadTransactionManager.semaphoreWatcher.get();
        future.cancel(true);
    }
    
    private Object deepCopy(Object target) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        Object replica = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        try {
            out = new ObjectOutputStream(buffer);
            out.writeObject(target);
            out.flush();
            in = new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
            replica = in.readObject();
        } catch(Exception ex) {
                throw new PrevaylerTransactionException("Internal error in transaction!", ex);
        } finally {
            try {
                if (out != null) out.close();
                if (in != null) in.close();
            } catch (IOException ex) {
                logger.error("Error closing streams!");
            }
        }
        return replica;
    }
    
    private class SemaphoreWatcherThread implements Runnable {
        public void run() {
            if (! ThreadTransactionManager.activeTransactionLock.isLocked()) {
                logger.info("Timeout: forcing resource releasing.");
                ThreadTransactionManager.this.releaseAll();
            }
        }
    }
}
