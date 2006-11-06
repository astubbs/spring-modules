package org.springmodules.prevayler;

import org.prevayler.Prevayler;
import org.springmodules.prevayler.callback.PrevaylerCallback;
import org.springmodules.prevayler.system.callback.SystemCallback;

/**
 * The session represents the execution context of a transaction.
 *
 * @author Sergio Bossa
 */
public interface Session {
    
    public Object execute(PrevaylerCallback callback);
    
    public Object execute(SystemCallback callback);
    
    public void flush(Prevayler prevayler);
}
