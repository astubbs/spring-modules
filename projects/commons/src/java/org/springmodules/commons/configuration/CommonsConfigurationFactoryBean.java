/**
 * Created on Feb 13, 2006
 *
 * $Id: CommonsConfigurationFactoryBean.java,v 1.3 2006/12/05 16:20:14 costin Exp $
 * $Revision: 1.3 $
 */
package org.springmodules.commons.configuration;

import java.net.URL;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationConverter;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * FactoryBean which wraps a Commons CompositeConfiguration object for usage
 * with PropertiesLoaderSupport. This allows the configuration object to behave
 * like a normal java.util.Properties object which can be passed on to
 * setProperties() method allowing PropertyOverrideConfigurer and
 * PropertyPlaceholderConfigurer to take advantage of Commons Configuration.
 * <p/> Internally a CompositeConfiguration object is used for merging multiple
 * Configuration objects.
 * 
 * @see java.util.Properties
 * @see org.springframework.core.io.support.PropertiesLoaderSupport
 * 
 * @author Costin Leau
 * 
 */
public class CommonsConfigurationFactoryBean implements InitializingBean, FactoryBean {

	private CompositeConfiguration configuration;

	private Configuration[] configurations;

	private Resource[] locations;

	private boolean throwExceptionOnMissing = true;

	public CommonsConfigurationFactoryBean() {
	}

	public CommonsConfigurationFactoryBean(Configuration configuration) {
		Assert.notNull(configuration);
		this.configuration = new CompositeConfiguration(configuration);
	}

	/**
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	public Object getObject() throws Exception {
		return (configuration != null) ? ConfigurationConverter.getProperties(configuration) : null;
	}

	/**
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	public Class getObjectType() {
		return java.util.Properties.class;
	}

	/**
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	public boolean isSingleton() {
		return true;
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		if (configuration == null && (configurations == null || configurations.length == 0)
				&& (locations == null || locations.length == 0))
			throw new IllegalArgumentException("no configuration object or location specified");

		if (configuration == null)
			configuration = new CompositeConfiguration();

		configuration.setThrowExceptionOnMissing(throwExceptionOnMissing);

		if (configurations != null) {
			for (int i = 0; i < configurations.length; i++) {
				configuration.addConfiguration(configurations[i]);
			}
		}
		
		if (locations != null) {
			for (int i = 0; i < locations.length; i++) {
				URL url = locations[i].getURL();
				Configuration props = new PropertiesConfiguration(url);
				configuration.addConfiguration(props);
			}
		}
	}

	/**
	 * @return Returns the configurations.
	 */
	public Configuration[] getConfigurations() {
		return configurations;
	}

	/**
	 * Set the commons configurations objects which will be used as properties.
	 * 
	 * @param configurations
	 */
	public void setConfigurations(Configuration[] configurations) {
		this.configurations = configurations;
	}

	public Resource[] getLocations() {
		return locations;
	}

	/**
	 * Shortcut for loading configuration from Spring resources. It will
	 * internally create a PropertiesConfiguration object based on the URL
	 * retrieved from the given Resources.
	 * 
	 * @param locations
	 */
	public void setLocations(Resource[] locations) {
		this.locations = locations;
	}

	public boolean isThrowExceptionOnMissing() {
		return throwExceptionOnMissing;
	}

	/**
	 * Set the underlying Commons CompositeConfiguration throwExceptionOnMissing
	 * flag.
	 * @param throwExceptionOnMissing
	 */
	public void setThrowExceptionOnMissing(boolean throwExceptionOnMissing) {
		this.throwExceptionOnMissing = throwExceptionOnMissing;
	}

	/**
	 * Getter for the underlying CompositeConfiguration object.
	 * 
	 * @return
	 */
	public CompositeConfiguration getConfiguration() {
		return configuration;
	}

}
