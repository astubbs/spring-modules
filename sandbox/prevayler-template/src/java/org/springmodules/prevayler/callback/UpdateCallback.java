package org.springmodules.prevayler.callback;

import org.springmodules.prevayler.system.PrevalentSystem;

/**
 *
 * @author Sergio Bossa
 */
public class UpdateCallback implements PrevaylerCallback {
    
    private static final long serialVersionUID = 476100068506333743L;
    
    private Object target;
    
    public UpdateCallback(Object target) {
        this.target = target;
    }
    
    public Object doInTransaction(PrevalentSystem system) {
        return system.update(this.target);
    }
}
