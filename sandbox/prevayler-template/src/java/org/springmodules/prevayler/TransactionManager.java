package org.springmodules.prevayler;

import org.springmodules.prevayler.configuration.PrevaylerConfiguration;

/**
 * Transaction manager for external transaction demarcation and execution of callbacks into Prevayler and its
 * prevalent system.
 *
 * @author Sergio Bossa
 */
public interface TransactionManager {
    
    public void begin();
    
    public void commit();
    
    public void rollback();
    
    public Object execute(PrevaylerCallback callback);
    
    public Object execute(SystemCallback callback);
    
    public void setPrevaylerConfiguration(PrevaylerConfiguration configuration);
}
