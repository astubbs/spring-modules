/**
 * Created on Feb 13, 2006
 *
 * $Id: CommonsConfigurationFactoryBean.java,v 1.2 2006/12/05 14:33:56 costin Exp $
 * $Revision: 1.2 $
 */
package org.springmodules.commons.configuration;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationConverter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

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
 * @author Costin Leau
 * 
 */
public class CommonsConfigurationFactoryBean implements InitializingBean, FactoryBean {

	private CompositeConfiguration configuration;

	private Configuration[] configurations;

	public CommonsConfigurationFactoryBean() {
	}

	public CommonsConfigurationFactoryBean(CompositeConfiguration configuration) {
		this.configuration = configuration;
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
	 * @return Returns the configurations.
	 */
	public Configuration[] getConfigurations() {
		return configurations;
	}

	/**
	 * Set the configurations objects which will be used as properties.
	 * 
	 * @param configurations
	 */
	public void setConfigurations(Configuration[] configurations) {
		this.configurations = configurations;
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		if (configuration == null && (configurations == null || configurations.length == 0))
			throw new IllegalArgumentException("at least one configuration is required");

		if (configuration == null)
			configuration = new CompositeConfiguration();

		for (int i = 0; i < configurations.length; i++) {
			configuration.addConfiguration(configurations[i]);
		}
	}

}
