package org.springmodules.jsr94.factory;

import java.util.ArrayList;
import java.util.List;

import javax.rules.RuleExecutionSetNotFoundException;
import javax.rules.RuleRuntime;
import javax.rules.StatelessRuleSession;
import javax.rules.admin.RuleAdministrator;

import org.springframework.core.io.ClassPathResource;
import org.springmodules.jsr94.factory.RuleAdministratorFactoryBean;
import org.springmodules.jsr94.factory.RuleRuntimeFactoryBean;
import org.springmodules.jsr94.rulesource.DefaultRuleSource;

public class TestDefaultRuleSource extends AbstractRuleServiceProviderTestCase {
	
	private RuleAdministrator ruleAdministrator;
	private RuleRuntime ruleRuntime;
	private static final String BIND_URI = "test";
	private static final String RULES_RESOURCE = "org/springmodules/jsr94/test.drl";

	/**
	 * Create new instance of TestDefaultRuleSource
	 * @throws Exception If anything goes wrong
	 */
	public TestDefaultRuleSource() throws Exception {
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
	
	/**
	 * Test session creation & execution
	 * @throws Exception If anything goes wrong
	 */
	public void testStatelessExecution() throws Exception {
		// create the source
		DefaultRuleSource source = new DefaultRuleSource();
		source.setBindUri(BIND_URI);
		source.setSource(new ClassPathResource(RULES_RESOURCE));
		source.setRuleAdministrator(ruleAdministrator);
		source.setRuleRuntime(ruleRuntime);
		source.afterPropertiesSet();

		// get the stateless session
		StatelessRuleSession session = (StatelessRuleSession)source.createSession(BIND_URI, null, RuleRuntime.STATELESS_SESSION_TYPE);
		
		// execute it
		List facts = new ArrayList();
		facts.add("Gecko");
		facts = session.executeRules(facts);
		
		assertTrue("Facts does not contain Gecko", facts.contains("Gecko"));
		assertTrue("Facts does not contain a:Gecko", facts.contains("a:Gecko"));
		
		session.release();
	}
	
}
