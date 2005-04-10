/**
 * 
 */

package org.springmodules.jsr94.factory;

import javax.rules.RuleServiceProvider;
import javax.rules.RuleServiceProviderManager;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author janm
 *
 */
public class DefaultRuleServiceProviderFactoryBean implements FactoryBean, InitializingBean, DisposableBean {

	/**
	 * Provider implementing class
	 */
	private String providerClass;

	/**
	 * Provider URI, such as http://drools.org/
	 */
	private String provider;

	/**
	 * Indicates that this factory has been destoyed
	 */
	private boolean destroyed;

	/**
	 * The instance
	 */
	private RuleServiceProvider instance;

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	public Object getObject() throws Exception {
		if (destroyed) throw new IllegalStateException(getClass().getName() + " has alread been destroyed");
		return instance;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	public final boolean isSingleton() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	public Class getObjectType() {
		return RuleServiceProvider.class;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		// check the arguments
		if (providerClass == null) throw new IllegalArgumentException("Must set providerClass on " + getClass().getName());
		if (provider == null) throw new IllegalArgumentException("Must set provider on " + getClass().getName());

		// load the provider implementation
		Class clazz = Class.forName(providerClass);
		// register the provider
		RuleServiceProviderManager.registerRuleServiceProvider(provider, clazz);
		// create the instance
		instance = RuleServiceProviderManager.getRuleServiceProvider(provider);
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 */
	public void destroy() throws Exception {
		// deregister the RuleServiceProvider and clear the instance
		instance = null;
		RuleServiceProviderManager.deregisterRuleServiceProvider(provider);
		// mark the factory as destroyed
		destroyed = true;
	}

	/**
		 * Sets new value for field provider
		 *
		 * @param provider The provider to set.
		 */
	public final void setProvider(String provider) {
		this.provider = provider;
	}

	/**
	 * Sets new value for field providerClass
	 * @param providerClass The providerClass to set.
	 */
	public final void setProviderClass(String providerClass) {
		this.providerClass = providerClass;
	}
}