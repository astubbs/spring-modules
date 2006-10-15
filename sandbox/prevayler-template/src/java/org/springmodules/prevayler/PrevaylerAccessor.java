package org.springmodules.prevayler;

import org.springmodules.prevayler.configuration.PrevaylerConfiguration;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.factory.InitializingBean;

/**
 * Base class for prevayler templates, providing access to the {@link org.springmodules.prevayler.configuration.PrevaylerConfiguration}.
 * 
 * @author Sergio Bossa
 */
public class PrevaylerAccessor implements InitializingBean {
    
    private PrevaylerConfiguration configuration;

    /**
     * Get the {@link org.springmodules.prevayler.configuration.PrevaylerConfiguration} to use.
     */
    public PrevaylerConfiguration getConfiguration() {
        return this.configuration;
    }

    /**
     * Set the {@link org.springmodules.prevayler.configuration.PrevaylerConfiguration} to use.
     */
    public void setConfiguration(PrevaylerConfiguration configuration) {
        this.configuration = configuration;
    }
    
    public void afterPropertiesSet() throws Exception {
        if (this.configuration == null) {
            throw new BeanInstantiationException(this.getClass(), "No Prevayler configuration found!");
        }
        if (this.configuration.getPrevalentSystem() == null) {
            throw new BeanInstantiationException(this.getClass(), "No prevalent system found!");
        }
    }
}
