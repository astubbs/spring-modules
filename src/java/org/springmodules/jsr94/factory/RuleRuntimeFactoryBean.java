/**
 * 
 */

package org.springmodules.jsr94.factory;

import javax.rules.RuleServiceProvider;
import javax.rules.admin.RuleAdministrator;

/**
 * @author janm
 */
public class RuleRuntimeFactoryBean extends AbstractInitializingFactoryBean {

	/**
	 * The serviceProvider
	 */
	private RuleServiceProvider serviceProvider;

	/* (non-Javadoc)
	 * @see org.springmodules.jsr94.AbstractInitializingFactoryBean#createInstance()
	 */
	protected final Object createInstance() throws Exception {
		return serviceProvider.getRuleRuntime();
	}

	/* (non-Javadoc)
	 * @see org.springmodules.jsr94.AbstractInitializingFactoryBean#preInitialize()
	 */
	protected final void preInitialize() throws Exception {
		if (serviceProvider == null) throw new IllegalArgumentException("Must set serviceProvider on " + getClass().getName());
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	public final Class getObjectType() {
		return RuleAdministrator.class;
	}

	/**
		 * Sets new value for field serviceProvider
		 *
		 * @param serviceProvider The serviceProvider to set.
		 */
	public final void setServiceProvider(RuleServiceProvider serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

}
