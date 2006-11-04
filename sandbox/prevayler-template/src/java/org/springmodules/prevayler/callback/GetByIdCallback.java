package org.springmodules.prevayler.callback;

import org.springmodules.prevayler.system.PrevalentSystem;

/**
 *
 * @author Sergio Bossa
 */
public class GetByIdCallback implements PrevaylerCallback {
    
    private static final long serialVersionUID = 479805268506333743L;
    
    private Class entityClass;
    private Object id;
    
    public GetByIdCallback(Class entityClass, Object id) {
        this.entityClass = entityClass;
        this.id = id;
    }
    
    public Object doInTransaction(PrevalentSystem system) {
        return system.get(this.entityClass, this.id);
    }
}
