package org.springmodules.prevayler.transaction.callback;

import org.springmodules.prevayler.PrevaylerCallback;
import org.springmodules.prevayler.system.PrevalentSystem;

/**
 *
 * @author Sergio Bossa
 */
public class SaveCallback implements PrevaylerCallback {
    
    private static final long serialVersionUID = 478805268506333743L;
    
    private Object target;
    
    public SaveCallback(Object target) {
        this.target = target;
    }
    
    public Object doInTransaction(PrevalentSystem system) {
        return system.save(target);
    }
}
