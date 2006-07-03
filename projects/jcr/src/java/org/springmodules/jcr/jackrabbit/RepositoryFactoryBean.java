/**
 * Created on Aug 31, 2005
 *
 * $Id: RepositoryFactoryBean.java,v 1.2 2006/07/03 22:07:31 costin Exp $
 * $Revision: 1.2 $
 */
package org.springmodules.jcr.jackrabbit;

import javax.jcr.Repository;

import org.apache.jackrabbit.api.JackrabbitRepository;
import org.apache.jackrabbit.core.RepositoryImpl;
import org.apache.jackrabbit.core.config.RepositoryConfig;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.xml.sax.InputSource;

/**
 * FactoryBean for creating a JackRabbit (JCR-170) repository through Spring
 * configuration files. Use this factory bean when you have to manually
 * configure the repository; for retrieving the repository from JNDI use the
 * JndiObjectFactoryBean {@link org.springframework.jndi.JndiObjectFactoryBean}
 * 
 * 
 * @see org.springframework.jndi.JndiObjectFactoryBean
 * @author Costin Leau
 * 
 */
public class RepositoryFactoryBean extends org.springmodules.jcr.RepositoryFactoryBean {

	/**
	 * Default repository configuration file.
	 */
	private static final String DEFAULT_CONF_FILE = "repository.xml";

	/**
	 * Default repository directory.
	 */
	private static final String DEFAULT_REP_DIR = ".";

	/**
	 * Home directory for the repository.
	 */
	private Resource homeDir;

	/**
	 * Repository configuratin created through Spring.
	 */
	private RepositoryConfig repositoryConfig;

	/**
	 * @see org.springmodules.jcr.RepositoryFactoryBean#createRepository()
	 */
	protected Repository createRepository() throws Exception {
		// return JackRabbit repository.
		return RepositoryImpl.create(repositoryConfig);
	}

	/**
	 * @see org.springmodules.jcr.RepositoryFactoryBean#resolveConfigurationResource()
	 */
	protected void resolveConfigurationResource() throws Exception {
		// read the configuration object
		if (repositoryConfig != null)
			return;

		if (this.configuration == null) {
			if (log.isDebugEnabled())
				log.debug("no configuration resource specified, using the default one:" + DEFAULT_CONF_FILE);
			configuration = new ClassPathResource(DEFAULT_CONF_FILE);
		}

		if (homeDir == null) {
			if (log.isDebugEnabled())
				log.debug("no repository home dir specified, using the default one:" + DEFAULT_REP_DIR);
			homeDir = new FileSystemResource(DEFAULT_REP_DIR);
		}

		repositoryConfig = RepositoryConfig.create(new InputSource(configuration.getInputStream()),
				homeDir.getFile().getAbsolutePath());
	}

	/**
	 * Shutdown method.
	 * 
	 */
	public void destroy() throws Exception {
		// force cast (but use only the interface)
		if (repository instanceof JackrabbitRepository)
			((JackrabbitRepository) repository).shutdown();
	}

	/**
	 * @return Returns the defaultRepDir.
	 */
	public Resource getHomeDir() {
		return this.homeDir;
	}

	/**
	 * @param defaultRepDir The defaultRepDir to set.
	 */
	public void setHomeDir(Resource defaultRepDir) {
		this.homeDir = defaultRepDir;
	}

	/**
	 * @return Returns the repositryConfig.
	 */
	public RepositoryConfig getRepositoryConfig() {
		return this.repositoryConfig;
	}

	/**
	 * @param repositoryConfig The repositryConfig to set.
	 */
	public void setRepositoryConfig(RepositoryConfig repositoryConfig) {
		this.repositoryConfig = repositoryConfig;
	}
}
