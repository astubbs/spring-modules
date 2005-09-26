/**
 * Created on Sep 23, 2005
 *
 * $Id: RepositoryFactoryBean.java,v 1.1 2005/09/26 10:21:56 costin Exp $
 * $Revision: 1.1 $
 */
package org.springmodules.jcr.jeceira;

import javax.jcr.Repository;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;

import com.jeceira.config.ConfigManager;
import com.jeceira.config.DefaultConfigManager;
import com.jeceira.repository.RepositoryFactory;

/**
 * FactoryBean for creating a Jeceira (JCR-170) repository through Spring
 * configuration files.
 * 
 * @author Costin Leau
 * 
 */
public class RepositoryFactoryBean extends org.springmodules.jcr.RepositoryFactoryBean {

    /**
     * Logger for this class
     */
    private static final Log log = LogFactory.getLog(RepositoryFactoryBean.class);

    /**
     * Default repository configuration file.
     */
    private static final String DEFAULT_CONF_FILE = "jeceira.xml";

    private String repositoryName;

    /**
     * Used if the user wants to supply his own configuration manager
     * (configuration created dynamically).
     */
    private ConfigManager configManager;

    /**
     * @see org.springmodules.jcr.RepositoryFactoryBean#createRepository()
     */
    protected Repository createRepository() throws Exception {
        RepositoryFactory factory = RepositoryFactory.getInstance(configManager);
        return factory.getRepository(repositoryName);
    }

    /**
     * @see org.springmodules.jcr.RepositoryFactoryBean#resolveConfigurationResource()
     */
    protected void resolveConfigurationResource() throws Exception {

        if (repositoryName == null)
            throw new IllegalArgumentException("repositoryName is required");

        if (configManager == null)
        {
            // resolve the configuration
            if (configuration == null) {
                if (log.isDebugEnabled())
                    log.debug("no configuration resource specified, using the default one:" + DEFAULT_CONF_FILE);
                configuration = new ClassPathResource(DEFAULT_CONF_FILE);
            }
            configManager = new DefaultConfigManager(configuration.getInputStream());
        }
    }

    /**
     * @return Returns the repositoryName.
     */
    public String getRepositoryName() {
        return repositoryName;
    }

    /**
     * @param repositoryName
     *            The repositoryName to set.
     */
    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    /**
     * @return Returns the configManager.
     */
    public ConfigManager getConfigManager() {
        return configManager;
    }

    /**
     * @param configManager
     *            The configManager to set.
     */
    public void setConfigManager(ConfigManager configManager) {
        this.configManager = configManager;
    }

}
