package org.springmodules.prevayler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;
import org.prevayler.Prevayler;
import org.springmodules.prevayler.callback.PrevaylerCallback;
import org.springmodules.prevayler.configuration.PrevaylerConfiguration;
import org.springmodules.prevayler.support.PrevaylerTransactionException;
import org.springmodules.prevayler.system.PrevalentSystem;
import org.springmodules.prevayler.system.callback.SystemCallback;
import org.springmodules.prevayler.transaction.CompositeTransactionCommand;

/**
 * {@link TransactionalPersistenceManager} {@link Session} implementation.
 * @author Sergio Bossa
 */
public class TransactionalSession implements Session {
    
    private static final Logger logger = Logger.getLogger(TransactionalSession.class);
    
    private PrevalentSystem system;
    private List executionQueue;
    private boolean flushed;
    
    public TransactionalSession(PrevaylerConfiguration configuration) {
        Prevayler prevayler = configuration.getPrevaylerInstance();
        this.system = (PrevalentSystem) this.deepCopy(prevayler.prevalentSystem());
        this.executionQueue = new LinkedList();
        this.flushed = false;
    }
    
    public Object execute(PrevaylerCallback callback) {
        if (this.flushed) {
            throw new PrevaylerTransactionException("Error: session already flushed and closed.");
        } else {
            // Enqueue a copy of the callback for later execution through prevayler at commit time:
            PrevaylerCallback copiedCallback = (PrevaylerCallback) this.deepCopy(callback);
            this.executionQueue.add(copiedCallback);
            // Locally execute the callback:
            return callback.doInTransaction(this.system);
        }
    }
    
    public Object execute(SystemCallback callback) {
        if (this.flushed) {
            throw new PrevaylerTransactionException("Error: session already flushed and closed.");
        } else {
            // Directly execute the callback into the system:
            return this.system.execute(callback);
            // No need to enqueue because this is a callback to directly executed into the system.
        }
    }
    
    public void flush(Prevayler prevayler) {
        if (this.flushed) {
            throw new PrevaylerTransactionException("Error: session already flushed and closed.");
        } else {
            CompositeTransactionCommand command = new CompositeTransactionCommand(this.executionQueue);
            prevayler.execute(command);
        }
    }
    
    /** Class internals **/
    
    private Object deepCopy(Object target) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        Object replica = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        try {
            out = new ObjectOutputStream(buffer);
            out.writeObject(target);
            out.flush();
            in = new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
            replica = in.readObject();
        } catch(Exception ex) {
            throw new PrevaylerTransactionException("Internal transaction error!", ex);
        } finally {
            try {
                if (out != null) out.close();
                if (in != null) in.close();
            } catch (IOException ex) {
                logger.error("Error closing streams!");
            }
        }
        return replica;
    }
}
