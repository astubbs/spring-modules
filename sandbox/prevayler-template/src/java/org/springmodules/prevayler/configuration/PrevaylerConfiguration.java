package org.springmodules.prevayler.configuration;

import org.springmodules.prevayler.system.PrevalentSystem;
import org.prevayler.Clock;
import org.prevayler.implementation.snapshot.SnapshotManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;

/**
 * PrevaylerConfiguration configure your prevalent system, providing setter methods for almost all
 * parameters exposed by the {@link org.prevayler.PrevaylerFactory} class.<br>
 * The only <b>mandatory property</b> to set is the {@link org.springmodules.prevayler.system.PrevalentSystem}
 * implementation to use.
 * 
 * @author Sergio Bossa
 */
public class PrevaylerConfiguration implements InitializingBean, ResourceLoaderAware {
    
    private ResourceLoader resourceLoader;
    
    private PrevalentSystem prevalentSystem;
    private String prevalenceBase;
    
    private Clock clock;
    
    private boolean transactionFiltering = true;
    
    private boolean transientMode;
    private SnapshotManager snapshotManager;
    
    private long transactionLogSizeThreshold;
    private long transactionLogAgeThreshold;
    
    /**
     * Get the {@link org.springmodules.prevayler.system.PrevalentSystem} to use. It will manage
     * the persistence of all your business objects.
     */
    public PrevalentSystem getPrevalentSystem() {
        return prevalentSystem;
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
    public String getPrevalenceBase() {
        return prevalenceBase;
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
    public Clock getClock() {
        return clock;
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
    public boolean getTransactionFiltering() {
        return transactionFiltering;
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
    public boolean getTransientMode() {
        return transientMode;
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
    public SnapshotManager getSnapshotManager() {
        return snapshotManager;
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
    public long getTransactionLogSizeThreshold() {
        return transactionLogSizeThreshold;
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
    public long getTransactionLogAgeThreshold() {
        return transactionLogAgeThreshold;
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

    public void afterPropertiesSet() throws Exception {
        if (this.prevalenceBase != null) {
            this.prevalenceBase = this.resourceLoader.getResource(this.prevalenceBase).getFile().getCanonicalPath(); 
        }
    }
}
