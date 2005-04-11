/**
 * 
 */

package org.springmodules.jsr94.factory;

import javax.rules.RuleExecutionSetNotFoundException;
import javax.rules.RuleRuntime;
import javax.rules.StatelessRuleSession;
import javax.rules.admin.RuleAdministrator;

import org.springmodules.jsr94.rulesource.DefaultRuleSource;

import org.springframework.core.io.ClassPathResource;

/**
 * This class tests DefaultRuleSource instantiation and execution using DefaultRuleSource
 * with just the ruleServiceProvider property set. This is typical scenario for local 
 * single-VM deployment.
 * 
 * The ruleAdministrator and ruleRuntime properties be null if ruleServiceProvider is set!
 * 
 * This test represents the following Spring bean configuration:
 * 
 * <code>
 * <pre>
 * &lt;bean id="foo" class="...DefaultRuleSource"&gt;
 *     &lt;property name="source"&gt;&lt;value&gt;foo&lt;/value&gt;&lt;/property&gt;
 *     &lt;property name="ruleServiceProvider"&gt;&lt;ref bean="ruleServiceProvider"/&gt;&lt;/property&gt;
 * &lt;/bean&gt;
 * </pre>
 * </code>
 * 
 * @see org.springmodules.jsr94.factory.TestRuntimeAndAdministratorDefaultRuleSource
 * @author janm
 */
public class ProviderDefaultRuleSourceTests extends AbstractDefaultRuleSourceTestCase {

	private RuleAdministrator ruleAdministrator;

	private RuleRuntime ruleRuntime;

	/**
		 * Create new instance of TestDefaultRuleSource
		 *
		 * @throws Exception If anything goes wrong
		 */
	public ProviderDefaultRuleSourceTests() throws Exception {
		RuleRuntimeFactoryBean ruleRuntimeFactoryBean = new RuleRuntimeFactoryBean();
		ruleRuntimeFactoryBean.setServiceProvider(getProvider());
		ruleRuntimeFactoryBean.afterPropertiesSet();
		RuleAdministratorFactoryBean ruleAdministratorFactoryBean = new RuleAdministratorFactoryBean();
		ruleAdministratorFactoryBean.setServiceProvider(getProvider());
		ruleAdministratorFactoryBean.afterPropertiesSet();

		ruleAdministrator = (RuleAdministrator) ruleAdministratorFactoryBean.getObject();
		ruleRuntime = (RuleRuntime) ruleRuntimeFactoryBean.getObject();
	}

	/**
	 * Tests the getObject() and afterPropertiesSet() methods
	 */
	public void testInitialization() throws Exception {
		DefaultRuleSource source = new DefaultRuleSource();

		// test the afterPropertiesSet() calls
		try {
			source.afterPropertiesSet();
			fail("ruleServiceProvider, bindUri and source not set");
		}
		catch (IllegalArgumentException ex) {
			// expected
		}
		source.setBindUri(BIND_URI);
		try {
			source.afterPropertiesSet();
			fail("ruleAdministrator, ruleRuntime and source not set");
		}
		catch (IllegalArgumentException ex) {
			// expected
		}
		source.setSource(new ClassPathResource(RULES_RESOURCE));
		try {
			source.afterPropertiesSet();
			fail("ruleAdministrator, ruleRuntime not set");
		}
		catch (IllegalArgumentException ex) {
			// expected
		}

		source.setRuleServiceProvider(getProvider());
		source.setRuleAdministrator(ruleAdministrator);
		try {
			source.afterPropertiesSet();
			fail("ruleServiceProvider AND ruleAdministrator set");
		}
		catch (IllegalArgumentException ex) {
			// expected
		}
		source.setRuleAdministrator(null);
		source.setRuleRuntime(ruleRuntime);
		try {
			source.afterPropertiesSet();
			fail("ruleServiceProvider AND ruleRuntime set");
		}
		catch (IllegalArgumentException ex) {
			// expected
		}
		source.setRuleAdministrator(ruleAdministrator);
		try {
			source.afterPropertiesSet();
			fail("ruleServiceProvider AND ruleRuntime and ruleAdministrator set");
		}
		catch (IllegalArgumentException ex) {
			// expected
		}

		source.setRuleAdministrator(null);
		source.setRuleRuntime(null);
		source.afterPropertiesSet();

		try {
			source.createSession(BIND_URI + "-foo", null, RuleRuntime.STATELESS_SESSION_TYPE);
			fail("Ruleset at uri " + BIND_URI + "-foo exists!");
		}
		catch (RuleExecutionSetNotFoundException ex) {
			// expected
		}

		StatelessRuleSession session = (StatelessRuleSession) source.createSession(BIND_URI, null, RuleRuntime.STATELESS_SESSION_TYPE);
		assertNotNull("Created session is null", session);
		session.release();
	}

	/* (non-Javadoc)
	 * @see org.springmodules.jsr94.factory.AbstractDefaultRuleSourceTestCase#setProperties(org.springmodules.jsr94.rulesource.DefaultRuleSource)
	 */
	protected void setProperties(DefaultRuleSource ruleSource) throws Exception {
		ruleSource.setBindUri(BIND_URI);
		ruleSource.setSource(new ClassPathResource(RULES_RESOURCE));
		ruleSource.setRuleAdministrator(null);
		ruleSource.setRuleServiceProvider(getProvider());
		ruleSource.setRuleRuntime(null);
	}

}
