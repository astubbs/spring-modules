/**
 * Created on Mar 11, 2006
 *
 * $Id$
 * $Revision$
 */
package org.springmodules.javaspaces.blitz;

import net.jini.space.JavaSpace;

import org.dancres.blitz.config.ConfigurationFactory;
import org.dancres.blitz.remote.LocalSpace;
import org.springframework.core.io.Resource;
import org.springmodules.javaspaces.AbstractJavaSpaceFactoryBean;

/**
 * FactoryBean used for retrieving a Local Blitz Space. The space is created on startup and closed
 * along with Spring application context.
 * 
 * @author Costin Leau
 *
 */
public class LocalSpaceFactoryBean extends AbstractJavaSpaceFactoryBean {

	private LocalSpace space;
	private Resource configuration;

	/**
	 * @see org.springmodules.javaspaces.AbstractJavaSpaceFactoryBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		if (configuration == null)
			throw new IllegalArgumentException("configuration is required");
	}

	/**
	 * @see org.springmodules.javaspaces.AbstractJavaSpaceFactoryBean#createSpace()
	 */
	protected JavaSpace createSpace() throws Exception {
		// static hookup of properties
		ConfigurationFactory.setup(new String[] { configuration.getFile().getAbsolutePath() });

		space = new LocalSpace();
		return space.getJavaSpaceProxy();
	}

	/**
	 * @see org.springmodules.javaspaces.AbstractJavaSpaceFactoryBean#destroy()
	 */
	public void destroy() throws Exception {
		super.destroy();
		space.destroy();
	}

	/**
	 * @return Returns the configuration.
	 */
	public Resource getConfiguration() {
		return configuration;
	}

	/**
	 * @param configuration The configuration to set.
	 */
	public void setConfiguration(Resource configuration) {
		this.configuration = configuration;
	}

}
