package org.springmodules.prevayler.transaction;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.prevayler.Transaction;
import org.springmodules.prevayler.PrevaylerCallback;
import org.springmodules.prevayler.system.PrevalentSystem;

/**
 * Composite Prevayler transaction command for executing a list of {@link org.springmodules.prevayler.PrevaylerCallback}s.
 *
 * @author Sergio Bossa
 */
public class CompositeTransactionCommand implements Transaction {
    
    private static final long serialVersionUID = 476105999506330034L;
    
    private List callbacks;
    
    public CompositeTransactionCommand(List callbacks) {
        this.callbacks = callbacks;
    }
    
    public void executeOn(Object object, Date date) {
        PrevalentSystem system= (PrevalentSystem) object;
        Iterator it = this.callbacks.iterator();
        while (it.hasNext()) {
            PrevaylerCallback callback = (PrevaylerCallback) it.next();
            callback.doInTransaction(system);
        }
    }
}
