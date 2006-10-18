package org.springmodules.prevayler;

import java.util.List;
import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springmodules.prevayler.id.DefaultIdMerger;
import org.springmodules.prevayler.id.IdMerger;
import org.springmodules.prevayler.transaction.callback.DeleteByEntityClassCallback;
import org.springmodules.prevayler.transaction.TransactionCommand;
import org.springmodules.prevayler.transaction.callback.GetByClassCallback;
import org.springmodules.prevayler.transaction.callback.SaveCallback;
import org.springmodules.prevayler.transaction.callback.DeleteByEntityCallback;
import org.springmodules.prevayler.transaction.callback.GetByIdCallback;
import org.springmodules.prevayler.transaction.callback.UpdateCallback;
import org.springframework.dao.DataAccessException;

/**
 * <p>The prevayler template simplifies the creation and use of a prevalent system.</p>
 * <p>A prevalent system is a transparent persistence system based on object serialization.</p>
 * <p>For a prevalent system to work correctly, its business objects must follow two very simple rules. They must be:
 * <ul>
 * <li>Serializable - At any point in time, the system might want to persist an object to disk or other non-volatile media.</li>
 * <li>Deterministic - Given some input, the business object's methods must always return the same output.</li>
 * </ul>
 * If these two rules are satisfied, your business objects can be transparently persisted using a prevalent system implementation like Prevayler.
 * </p>
 * <p>The prevayler template provides a simple, common, abstraction over a prevalent system based on Prevayler.</p>
 * <p>The prevayler template provides common data access methods to use for managing the persistence of your business objects. Under the hood,
 * it uses a {@link org.springmodules.prevayler.system.PrevalentSystem} implementation working as a standard Data Access Object.</p>
 * <p>For obtaining transparent persistence using the {@link PrevaylerTemplate} and its {@link org.springmodules.prevayler.system.PrevalentSystem},
 * you just need to configure it by injecting an {@link org.springmodules.prevayler.configuration.PrevaylerConfiguration}.<br>
 * You don't have to implement anything special. Your business objects just need to satisfy the rules above, plus the following:
 * <ul>
 * <li>Every business object must have an <i>id</i> property field, either directly declared or inherited: it is automatically managed by the prevalent system through the
 * {@link org.springmodules.prevayler.id.IdGenerationStrategy} and {@link org.springmodules.prevayler.id.IdNameResolutionStrategy} objects, so 
 * <b>you don't have to care about it</b>.</li>
 * </ul>
 * Please note that the prevayler template is <b>best configured and used</b> through a Spring application context; if you want to use it standalone, you need
 * to manually set the {@link org.springmodules.prevayler.configuration.PrevaylerConfiguration} object, and manually call the (mandatory) {@link #afterPropertiesSet()} initialization
 * method and (optionally but suggested) the {@link #destroy()} closing method.
 * </p> 
 * <p>Take a look at other classes javadocs for additional details.</p>
 *<p>
 * <b>Managing persistent objects.</b><br>
 * It is strongly suggested that every persistent business object you want to modify must be first retrieved from the prevalent system 
 * through the template data access methods.<br> 
 * You have to pass fresh (newly created) business objects instances only when doing a save: the other methods require you 
 * to pass persistent object instances previously retrieved from the prevalent system.<br>
 * Otherwise, you could get unexpected behaviours.
 * <p>
 * <b>Cascade persistence.</b><br>
 *  TODO : Talk about it.
 * </p>
 * <p>
 * <b>Merging identifiers.</b><br>
 *  TODO : Talk about it.
 * </p>
 *
 * @author Sergio Bossa
 */
public class PrevaylerTemplate extends PrevaylerAccessor implements DisposableBean, PrevaylerOperations {
    
    private Prevayler prevayler;
    
    private IdMerger merger;
    private boolean idMerging = true;
    
    /**
     * Mandatory initialization method to call.
     */
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        this.merger = new DefaultIdMerger(this.getConfiguration().getPrevalentSystem().getPrevalenceInfo());
        if (this.prevayler == null) {
            PrevaylerFactory factory = new PrevaylerFactory();
            
            factory.configureClock(this.getConfiguration().getClock());
            factory.configurePrevalenceBase(this.getConfiguration().getPrevalenceBase());
            factory.configurePrevalentSystem(this.getConfiguration().getPrevalentSystem());
            factory.configureSnapshotManager(this.getConfiguration().getSnapshotManager());
            factory.configureTransactionFiltering(this.getConfiguration().getTransactionFiltering());
            factory.configureTransactionLogFileAgeThreshold(this.getConfiguration().getTransactionLogAgeThreshold());
            factory.configureTransactionLogFileSizeThreshold(this.getConfiguration().getTransactionLogSizeThreshold());
            factory.configureTransientMode(this.getConfiguration().getTransientMode());
            
            this.prevayler = factory.create();
        }
    }
    
    /**
     * Destroy method.
     */
    public void destroy() throws Exception {
        this.prevayler.close();
    }
    
    /**
     * Return true if identifiers must be merged into object instances passed to "save" and "update" operations, false otherwise.<br>
     * The default is <i>false</i>, because the id merging consumes more memory and processing resources.
     */
    public boolean getIdMerging() {
        return this.idMerging;
    }
    
    /**
     * Set true if identifiers must be merged into object instances passed to "save" and "update" operations, false otherwise.<br>
     * The default is <i>true</i>, but consider that <b>id merging consumes more memory and processing resources</b>.
     */
    public void setIdMerging(boolean merging) {
        this.idMerging = merging;
    }

    public Object save(Object newEntity) {
        try {
            Object attached = this.prevayler.execute(new TransactionCommand(new SaveCallback(newEntity)));
            if (this.idMerging) {
                this.merger.merge(attached, newEntity);
            }
            return attached;
        }
        catch(DataAccessException ex) {
            throw ex;
        }
        catch(Exception ex) {
            throw new PrevaylerTransactionException("Exception occured while executing Prevayler transaction: " + ex.getMessage(), ex);
        }
    }
    
    public Object update(Object entity) {
        try {
            Object attached = this.prevayler.execute(new TransactionCommand(new UpdateCallback(entity)));
            if (this.idMerging) {
                this.merger.merge(attached, entity);
            }
            return attached;
        } 
        catch(DataAccessException ex) {
            throw ex;
        }
        catch(Exception ex) {
            throw new PrevaylerTransactionException("Exception occured while executing Prevayler transaction: " + ex.getMessage(), ex);
        }
    }
    
    public void delete(Object entity) {
        try {
            this.prevayler.execute(new TransactionCommand(new DeleteByEntityCallback(entity)));
        } 
        catch(DataAccessException ex) {
            throw ex;
        }
        catch(Exception ex) {
            throw new PrevaylerTransactionException("Exception occured while executing Prevayler transaction: " + ex.getMessage(), ex);
        }
    }
    
    public void delete(Class entityClass) {
        try {
            this.prevayler.execute(new TransactionCommand(new DeleteByEntityClassCallback(entityClass)));
        } 
        catch(DataAccessException ex) {
            throw ex;
        }
        catch(Exception ex) {
            throw new PrevaylerTransactionException("Exception occured while executing Prevayler transaction: " + ex.getMessage(), ex);
        }
    }
    
    public Object get(Class entityClass, Object id) {
        try {
            return this.prevayler.execute(new TransactionCommand(new GetByIdCallback(entityClass, id)));
        }
        catch(DataAccessException ex) {
            throw ex;
        }
        catch(Exception ex) {
            throw new PrevaylerTransactionException("Exception occured while executing Prevayler transaction: " + ex.getMessage(), ex);
        }
    }

    public List get(Class entityClass) {
        try {
            return (List) this.prevayler.execute(new TransactionCommand(new GetByClassCallback(entityClass)));
        }
        catch(DataAccessException ex) {
            throw ex;
        }
        catch(Exception ex) {
            throw new PrevaylerTransactionException("Exception occured while executing Prevayler transaction: " + ex.getMessage(), ex);
        }
    }
    
    public Object execute(PrevaylerCallback callback) {
        try {
            return this.prevayler.execute(new TransactionCommand(callback));
        }
        catch(DataAccessException ex) {
            throw ex;
        }
        catch(Exception ex) {
            throw new PrevaylerTransactionException("Exception occured while executing Prevayler transaction: " + ex.getMessage(), ex);
        }
    }
}
