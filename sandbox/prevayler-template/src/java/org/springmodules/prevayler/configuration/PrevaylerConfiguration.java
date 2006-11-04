package org.springmodules.prevayler.configuration;

import org.prevayler.Clock;
import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;
import org.prevayler.implementation.snapshot.SnapshotManager;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springmodules.prevayler.system.PrevalentSystem;

/**
 * <p>This class configures your prevalent system, providing setter methods for almost all
 * parameters exposed by the {@link org.prevayler.PrevaylerFactory} class.<br>
 * The only <b>mandatory property</b> to set is the {@link org.springmodules.prevayler.system.PrevalentSystem}
 * implementation to use.</p>
 * <p>It also provides <b>methods for initializing and destroying resources</b></p>
 * <p>Once configured, it provides access to the {@link org.prevayler.Prevayler} instance to use in the system
 * (see {@link #getPrevaylerInstance()}).</p>
 *
 * @author Sergio Bossa
 */
public class PrevaylerConfiguration implements InitializingBean, DisposableBean, ResourceLoaderAware {
    
    private ResourceLoader resourceLoader;
    
    private Prevayler prevayler;
    
    private PrevalentSystem prevalentSystem;
    
    private String prevalenceBase;
    private Clock clock;
    private SnapshotManager snapshotManager;
    private boolean transactionFiltering;
    private boolean transientMode;
    private long transactionLogSizeThreshold;
    private long transactionLogAgeThreshold;
    
    /**
     * Get the {@link org.prevayler.Prevayler} instance to use in the system.
     */
    public Prevayler getPrevaylerInstance() {
        return this.prevayler;
    }
    
    /**
     * Set the {@link org.springmodules.prevayler.system.PrevalentSystem} to use. It will manage
     * the persistence of all your business objects.
     */
    public void setPrevalentSystem(PrevalentSystem prevalentSystem) {
        this.prevalentSystem = prevalentSystem;
    }
    
    /**
     * @see org.prevayler.PrevaylerFactory#configurePrevalenceBase()
     */
    public void setPrevalenceBase(String prevalenceBase) {
        this.prevalenceBase = prevalenceBase;
    }
    
    /**
     * @see org.prevayler.PrevaylerFactory#configureClock()
     */
    public void setClock(Clock clock) {
        this.clock = clock;
    }
    
    /**
     * @see org.prevayler.PrevaylerFactory#configureTransactionFiltering()
     */
    public void setTransactionFiltering(boolean transactionFiltering) {
        this.transactionFiltering = transactionFiltering;
    }
    
    /**
     * @see org.prevayler.PrevaylerFactory#configureTransientMode()
     */
    public void setTransientMode(boolean transientMode) {
        this.transientMode = transientMode;
    }
    
    /**
     * @see org.prevayler.PrevaylerFactory#configureSnapshotManager()
     */
    public void setSnapshotManager(SnapshotManager snapshotManager) {
        this.snapshotManager = snapshotManager;
    }
    
    /**
     * @see org.prevayler.PrevaylerFactory#configureTransactionLogSizeThreshold()
     */
    public void setTransactionLogSizeThreshold(long transactionLogSizeThreshold) {
        this.transactionLogSizeThreshold = transactionLogSizeThreshold;
    }
    
    /**
     * @see org.prevayler.PrevaylerFactory#configureTransactionLogAgeThreshold()
     */
    public void setTransactionLogAgeThreshold(long transactionLogAgeThreshold) {
        this.transactionLogAgeThreshold = transactionLogAgeThreshold;
    }
    
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
    
    /**
     * Mandatory initialization method to call after all interesting properties have been set.
     */
    public void afterPropertiesSet() throws Exception {
        if (this.prevalenceBase != null) {
            this.prevalenceBase = this.resourceLoader.getResource(this.prevalenceBase).getFile().getCanonicalPath();
        }
        
        if (this.prevalentSystem == null) {
            throw new BeanInstantiationException(this.getClass(), "No prevalent system found!");
        }
        
        PrevaylerFactory factory = new PrevaylerFactory();
        factory.configureClock(this.clock);
        factory.configurePrevalenceBase(this.prevalenceBase);
        factory.configurePrevalentSystem(this.prevalentSystem);
        factory.configureSnapshotManager(this.snapshotManager);
        factory.configureTransactionFiltering(this.transactionFiltering);
        factory.configureTransactionLogFileAgeThreshold(this.transactionLogAgeThreshold);
        factory.configureTransactionLogFileSizeThreshold(this.transactionLogSizeThreshold);
        factory.configureTransientMode(this.transientMode);
        this.prevayler = factory.create();
    }
    
    /**
     * Destroy method for closing resources.
     */
    public void destroy() throws Exception {
        this.prevayler.close();
    }
}
