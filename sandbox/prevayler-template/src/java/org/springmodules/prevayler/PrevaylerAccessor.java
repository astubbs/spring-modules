package org.springmodules.prevayler;

import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.factory.InitializingBean;

/**
 * Base class for prevayler templates.
 * 
 * @author Sergio Bossa
 */
public class PrevaylerAccessor implements InitializingBean {
    
    private PersistenceManager persistenceManager;

    /**
     * Give access to the {@link PersistenceManager}.
     */
    public PersistenceManager getPersistenceManager() {
        return this.persistenceManager;
    }

    /**
     * Set the {@link PersistenceManager} to use.
     */
    public void setPersistenceManager(PersistenceManager persistenceManager) {
        this.persistenceManager = persistenceManager;
    }
    
    public void afterPropertiesSet() throws Exception {
        if (this.persistenceManager == null) {
            throw new BeanInstantiationException(this.getClass(), "No persistence manager found!");
        }
    }
}
