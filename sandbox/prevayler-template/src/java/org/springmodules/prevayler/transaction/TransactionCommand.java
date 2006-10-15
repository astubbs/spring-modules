package org.springmodules.prevayler.transaction;

import org.springmodules.prevayler.PrevaylerCallback;
import org.springmodules.prevayler.system.PrevalentSystem;
import java.util.Date;
import org.prevayler.TransactionWithQuery;

/**
 *
 * @author Sergio Bossa
 */
public class TransactionCommand implements TransactionWithQuery {
    
    private static final long serialVersionUID = 476105268506330034L;
    
    private PrevaylerCallback callback;
    
    public TransactionCommand() {
    }
    
    public TransactionCommand(PrevaylerCallback callback) {
        this.callback = callback;
    }
    
    public Object executeAndQuery(Object object, Date date) throws Exception {
        PrevalentSystem system= (PrevalentSystem) object;
        return callback.doInTransaction(system);
    }
}
