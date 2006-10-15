package org.springmodules.prevayler.transaction.callback;

import org.springmodules.prevayler.PrevaylerCallback;
import org.springmodules.prevayler.system.PrevalentSystem;

/**
 *
 * @author Sergio Bossa
 */
public class DeleteByEntityClassCallback implements PrevaylerCallback {
    
    private static final long serialVersionUID = 476195268506333743L;
    
    private Class entityClass;
    
    public DeleteByEntityClassCallback(Class entityClass) {
        this.entityClass = entityClass;
    }
    
    public Object doInTransaction(PrevalentSystem system) {
        system.delete(this.entityClass);
        return null;
    }
}
