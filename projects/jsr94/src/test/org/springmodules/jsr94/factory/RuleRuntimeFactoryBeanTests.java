
package org.springmodules.jsr94.factory;

import javax.rules.RuleRuntime;

/**
 * @author janm
 *
 */
public class RuleRuntimeFactoryBeanTests extends AbstractRuleServiceProviderTests {

	/**
	 * Tests the getObject() and afterPropertiesSet() methods
	 */
	public void testGetObject() throws Exception {
		RuleRuntimeFactoryBean bean = new RuleRuntimeFactoryBean();
		try {
			bean.afterPropertiesSet();
			fail("serviceProvider not set");
		}
		catch (IllegalArgumentException ex) {
			// expected
		}
		bean.setServiceProvider(getProvider());
		bean.afterPropertiesSet();

		RuleRuntime runtime = (RuleRuntime) bean.getObject();
		assertNotNull("Created RuleRuntime is null", runtime);
	}
}
