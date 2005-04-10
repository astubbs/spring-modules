/**
 * 
 */

package org.springmodules.jsr94.factory;

import javax.rules.RuleServiceProvider;

import junit.framework.TestCase;

/**
 * Abstract TestCase that provides access to the pre-configured RuleServiceProvider.
 * The RuleServiceProvider is a singleton and access to it is thread-safe as it is a static field.
 *
 * @author janm
 */
public abstract class AbstractRuleServiceProviderTestCase extends TestCase {

	/**
	 * Rule provider name
	 */
	protected static final String PROVIDER = "http://drools.org";

	/**
	 * Rule provider implementation
	 */
	protected static final String PROVIDER_CLASS = "org.drools.jsr94.rules.RuleServiceProviderImpl";

	/**
	 * RuleServiceProvider instance
	 */
	private static RuleServiceProvider instance = null;

	/**
		 * Returns RuleServiceProvider instance
		 *
		 * @return The RuleServiceProvider
		 * @throws Exception If something goes wrong
		 */
	protected static RuleServiceProvider getProvider() throws Exception {
		if (instance == null) {
			DefaultRuleServiceProviderFactoryBean bean = new DefaultRuleServiceProviderFactoryBean();
			bean.setProvider(PROVIDER);
			bean.setProviderClass(PROVIDER_CLASS);
			bean.afterPropertiesSet();

			instance = (RuleServiceProvider) bean.getObject();
		}

		return instance;
	}

}
