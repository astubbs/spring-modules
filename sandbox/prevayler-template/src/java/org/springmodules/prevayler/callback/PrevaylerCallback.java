package org.springmodules.prevayler.callback;

import org.springmodules.prevayler.system.PrevalentSystem;
import java.io.Serializable;

/**
 * <p>Callback interface for executing code in a {@link org.springmodules.prevayler.system.PrevalentSystem} by going through Prevayler.<br>
 * Please note that all operations into the callback method <b>are executed in the context of a single transaction</b>.<br>
 * So, the callback can be used also for grouping more operations into a single transaction.</p>
 * <p><b>Important: </b> due to how object serialization and Prevayler work, don't use anonymous inner classes for
 * implementing this interface.</p>
 *
 * @author Sergio Bossa
 */
public interface PrevaylerCallback extends Serializable {
    
    /**
     * Execute operations in the given {@link org.springmodules.prevayler.system.PrevalentSystem}, in the context
     * of a single transaction.
     *
     * @return An optional result.
     */
    public Object doInTransaction(PrevalentSystem system);
}
