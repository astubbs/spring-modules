package org.springmodules.prevayler;

import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.factory.InitializingBean;

/**
 * Base class for prevayler templates, providing access to the {@link org.springmodules.prevayler.TransactionManager}.
 * 
 * @author Sergio Bossa
 */
public class PrevaylerAccessor implements InitializingBean {
    
    private TransactionManager transactionManager;

    /**
     * Give  access to the {@link org.springmodules.prevayler.TransactionManager} to use.
     */
    public TransactionManager accessTransactionManager() {
        return this.transactionManager;
    }

    /**
     * Set the {@link org.springmodules.prevayler.configuration.PrevaylerConfiguration} to use.
     */
    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }
    
    public void afterPropertiesSet() throws Exception {
        if (this.transactionManager == null) {
            throw new BeanInstantiationException(this.getClass(), "No transaction manager found!");
        }
    }
}
