
package org.springmodules.jsr94.factory;

import javax.rules.RuleServiceProvider;

public class DefaultRuleServiceProviderFactoryBeanTests extends AbstractRuleServiceProviderTests {

	public void testAfterPropertiesSet() throws Exception {
		DefaultRuleServiceProviderFactoryBean bean = new DefaultRuleServiceProviderFactoryBean();
		try {
			bean.afterPropertiesSet();
			fail("No provider or providerClass set");
		}
		catch (IllegalArgumentException ex) {
			// expected
		}
		try {
			bean.setProvider(PROVIDER);
			bean.afterPropertiesSet();
			fail("No providerClass set");
		}
		catch (IllegalArgumentException ex) {
			// expected
		}
		bean.setProviderClass(PROVIDER_CLASS);
		bean.afterPropertiesSet();

		RuleServiceProvider provider = (RuleServiceProvider) bean.getObject();
		assertNotNull("Provider is null", provider);
		assertEquals("Incorrect provider", PROVIDER_CLASS, provider.getClass().getName());

		bean.destroy();
		try {
			bean.getObject();
			fail("Destroyed factory must not return objects");
		}
		catch (IllegalStateException ex) {
			// expected
		}
	}

}
