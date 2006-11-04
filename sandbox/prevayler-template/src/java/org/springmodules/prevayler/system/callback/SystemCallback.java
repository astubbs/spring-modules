package org.springmodules.prevayler.system.callback;

import org.springmodules.prevayler.system.PrevalentSystem;
import java.io.Serializable;

/**
 * <p>Callback interface for directly executing code into a {@link org.springmodules.prevayler.system.PrevalentSystem},
 * without going through Prevayler.</p>
 *
 * @author Sergio Bossa
 */
public interface SystemCallback extends Serializable {
    
    /**
     * Directly execute operations in the given {@link org.springmodules.prevayler.system.PrevalentSystem}.
     *
     * @return An optional result.
     */
    public Object execute(PrevalentSystem system);
}
