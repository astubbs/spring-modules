package org.springmodules.prevayler.transaction;

import java.util.Date;
import org.prevayler.TransactionWithQuery;
import org.springmodules.prevayler.callback.PrevaylerCallback;
import org.springmodules.prevayler.system.PrevalentSystem;

/**
 * Prevayler transaction command for executing a {@link org.springmodules.prevayler.PrevaylerCallback}.
 * @author Sergio Bossa
 */
public class TransactionCommand implements TransactionWithQuery {
    
    private static final long serialVersionUID = 476105268506330034L;
    
    private PrevaylerCallback callback;
    
    public TransactionCommand(PrevaylerCallback callback) {
        this.callback = callback;
    }
    
    public Object executeAndQuery(Object object, Date date) throws Exception {
        PrevalentSystem system= (PrevalentSystem) object;
        return callback.doInTransaction(system);
    }
}
