package org.springmodules.prevayler.callback;

import org.springmodules.prevayler.system.PrevalentSystem;

/**
 *
 * @author Sergio Bossa
 */
public class DeleteByEntityCallback implements PrevaylerCallback {
    
    private static final long serialVersionUID = 47610526850633653L;
    
    private Object target;
    
    public DeleteByEntityCallback(Object target) {
        this.target = target;
    }
    
    public Object doInTransaction(PrevalentSystem system) {
        system.delete(this.target);
        return null;
    }
}
