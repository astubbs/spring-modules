package org.springmodules.prevayler;

import org.apache.log4j.Logger;
import org.prevayler.Prevayler;
import org.springmodules.prevayler.callback.PrevaylerCallback;
import org.springmodules.prevayler.configuration.PrevaylerConfiguration;
import org.springmodules.prevayler.support.PrevaylerTransactionException;
import org.springmodules.prevayler.system.PrevalentSystem;
import org.springmodules.prevayler.system.callback.SystemCallback;
import org.springmodules.prevayler.transaction.TransactionCommand;

/**
 * {@link SimplePersistenceManager} {@link Session} implementation.
 * @author Sergio Bossa
 */
public class SimpleSession implements Session {
    
    private static final Logger logger = Logger.getLogger(SimpleSession.class);
    
    private PrevaylerConfiguration configuration;
    
    public SimpleSession(PrevaylerConfiguration configuration) {
        this.configuration = configuration;
    }
    
    public Object execute(PrevaylerCallback callback) {
        logger.debug("Executing callback into Prevayler.");
        try {
            Prevayler prevayler = this.configuration.getPrevaylerInstance();
            return prevayler.execute(new TransactionCommand(callback));
        } catch (Exception ex) {
            throw new PrevaylerTransactionException("Error while executing callback.", ex);
        }
    }
    
    public Object execute(SystemCallback callback) {
        logger.debug("Executing callback into prevalent system.");
        PrevalentSystem localSystem = (PrevalentSystem) this.configuration.getPrevaylerInstance().prevalentSystem();
        return localSystem.execute(callback);
    }
    
    public void flush(Prevayler prevayler) {
        // Do nothing: all operations are suddenly committed.
    }
}
