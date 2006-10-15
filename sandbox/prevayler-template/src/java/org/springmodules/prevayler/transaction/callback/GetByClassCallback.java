package org.springmodules.prevayler.transaction.callback;

import org.springmodules.prevayler.PrevaylerCallback;
import org.springmodules.prevayler.system.PrevalentSystem;

/**
 *
 * @author Sergio Bossa
 */
public class GetByClassCallback implements PrevaylerCallback {
    
    private static final long serialVersionUID = 416105268506333743L;
    
    private Class entityClass;
    
    public GetByClassCallback(Class entityClass) {
        this.entityClass = entityClass;
    }
    
    public Object doInTransaction(PrevalentSystem system) {
        return system.get(this.entityClass);
    }
}
