package org.springmodules.jsr94.factory;

import javax.rules.RuleRuntime;

import org.springmodules.jsr94.factory.RuleRuntimeFactoryBean;

/**
 * @author janm
 *
 */
public class TestRuleRuntimeFactoryBean extends AbstractRuleServiceProviderTestCase {

	/**
	 * Tests the getObject() and afterPropertiesSet() methods
	 */
	public void testGetObject() throws Exception {
		RuleRuntimeFactoryBean bean = new RuleRuntimeFactoryBean();
		try {
			bean.afterPropertiesSet();
			fail("serviceProvider not set");
		} catch (IllegalArgumentException ex) {
			// expected
		}
		bean.setServiceProvider(getProvider());
		bean.afterPropertiesSet();
		
		RuleRuntime runtime = (RuleRuntime)bean.getObject();
		assertNotNull("Created RuleRuntime is null", runtime);
	}
}
