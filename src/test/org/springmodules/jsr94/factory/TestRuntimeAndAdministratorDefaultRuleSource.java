package org.springmodules.jsr94.factory;

import javax.rules.RuleExecutionSetNotFoundException;
import javax.rules.RuleRuntime;
import javax.rules.StatelessRuleSession;
import javax.rules.admin.RuleAdministrator;

import org.springframework.core.io.ClassPathResource;
import org.springmodules.jsr94.factory.RuleAdministratorFactoryBean;
import org.springmodules.jsr94.factory.RuleRuntimeFactoryBean;
import org.springmodules.jsr94.rulesource.DefaultRuleSource;

/**
 * This class tests DefaultRuleSource instantiation and execution using DefaultRuleSource
 * with ruleAdministrator and ruleRuntime properties set. This is a typical scenarion in 
 * JNDI deployment, where the ruleRuntime and ruleAdministrator are JNDI-configured resources.
 * 
 * The ruleServiceProvider property must be null!
 * 
 * This test represents the following Spring bean configuration:
 * 
 * <code>
 * <pre>
 * &lt;bean id="foo" class="...DefaultRuleSource"&gt;
 *     &lt;property name="source"&gt;&lt;value&gt;foo&lt;/value&gt;&lt;/property&gt;
 *     &lt;property name="ruleRuntime"&gt;&lt;ref bean="ruleRuntime"/&gt;&lt;/property&gt;
 *     &lt;property name="ruleAdministrator"&gt;&lt;ref bean="ruleAdministrator"/&gt;&lt;/property&gt;
 * &lt;/bean&gt;
 * </pre>
 * </code>
 * 
 * @see org.springmodules.jsr94.factory.TestProviderDefaultRuleSource
 * @author janm
 */
public class TestRuntimeAndAdministratorDefaultRuleSource extends AbstractDefaultRuleSourceTestCase {
	
	private RuleAdministrator ruleAdministrator;
	private RuleRuntime ruleRuntime;

	/**
	 * Create new instance of TestDefaultRuleSource
	 * @throws Exception If anything goes wrong
	 */
	public TestRuntimeAndAdministratorDefaultRuleSource() throws Exception {
		RuleRuntimeFactoryBean ruleRuntimeFactoryBean = new RuleRuntimeFactoryBean();
		ruleRuntimeFactoryBean.setServiceProvider(getProvider());
		ruleRuntimeFactoryBean.afterPropertiesSet();
		RuleAdministratorFactoryBean ruleAdministratorFactoryBean = new RuleAdministratorFactoryBean();
		ruleAdministratorFactoryBean.setServiceProvider(getProvider());
		ruleAdministratorFactoryBean.afterPropertiesSet();
		
		ruleAdministrator = (RuleAdministrator)ruleAdministratorFactoryBean.getObject();
		ruleRuntime = (RuleRuntime)ruleRuntimeFactoryBean.getObject();
	}

	/**
	 * Tests the getObject() and afterPropertiesSet() methods
	 */
	public void testInitialization() throws Exception {
		DefaultRuleSource source = new DefaultRuleSource();

		// test the afterPropertiesSet() calls
		try {
			source.afterPropertiesSet();
			fail("ruleAdministrator, ruleRuntime, bindUri and source not set");
		} catch (IllegalArgumentException ex) {
			// expected
		}
		source.setBindUri(BIND_URI);
		try {
			source.afterPropertiesSet();
			fail("ruleAdministrator, ruleRuntime and source not set");
		} catch (IllegalArgumentException ex) {
			// expected
		}
		source.setSource(new ClassPathResource(RULES_RESOURCE));
		try {
			source.afterPropertiesSet();
			fail("ruleAdministrator, ruleRuntime not set");
		} catch (IllegalArgumentException ex) {
			// expected
		}
		source.setRuleAdministrator(ruleAdministrator);
		try {
			source.afterPropertiesSet();
			fail("ruleRuntime not set");
		} catch (IllegalArgumentException ex) {
			// expected
		}
		source.setRuleRuntime(ruleRuntime);
		source.afterPropertiesSet();
		
		try {
			source.createSession(BIND_URI + "-foo", null, RuleRuntime.STATELESS_SESSION_TYPE);
			fail("Ruleset at uri " + BIND_URI + "-foo exists!");
		} catch (RuleExecutionSetNotFoundException ex) {
			// expected
		}
		
		StatelessRuleSession session = (StatelessRuleSession)source.createSession(BIND_URI, null, RuleRuntime.STATELESS_SESSION_TYPE);
		assertNotNull("Created session is null", session);
		session.release();
	}

	/* (non-Javadoc)
	 * @see org.springmodules.jsr94.factory.AbstractDefaultRuleSourceTestCase#setProperties(org.springmodules.jsr94.rulesource.DefaultRuleSource)
	 */
	protected void setProperties(DefaultRuleSource ruleSource) throws Exception {
		ruleSource.setBindUri(BIND_URI);
		ruleSource.setSource(new ClassPathResource(RULES_RESOURCE));
		ruleSource.setRuleAdministrator(ruleAdministrator);
		ruleSource.setRuleServiceProvider(null);
		ruleSource.setRuleRuntime(ruleRuntime);
	}
		
}
