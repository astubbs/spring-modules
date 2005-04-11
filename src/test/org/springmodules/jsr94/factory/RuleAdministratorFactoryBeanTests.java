
package org.springmodules.jsr94.factory;

import javax.rules.admin.RuleAdministrator;

/**
 * @author janm
 *
 */
public class RuleAdministratorFactoryBeanTests extends AbstractRuleServiceProviderTestCase {

	/**
	 * Tests the getObject() and afterPropertiesSet() methods
	 */
	public void testGetObject() throws Exception {
		RuleAdministratorFactoryBean bean = new RuleAdministratorFactoryBean();
		try {
			bean.afterPropertiesSet();
			fail("serviceProvider not set");
		}
		catch (IllegalArgumentException ex) {
			// expected
		}
		bean.setServiceProvider(getProvider());
		bean.afterPropertiesSet();

		RuleAdministrator administrator = (RuleAdministrator) bean.getObject();
		assertNotNull("Created RuleAdministrator is null", administrator);
	}

}
