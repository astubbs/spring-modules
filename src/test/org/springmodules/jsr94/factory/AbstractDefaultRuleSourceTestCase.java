/**
 * 
 */
package org.springmodules.jsr94.factory;

import java.util.ArrayList;
import java.util.List;

import javax.rules.RuleRuntime;
import javax.rules.StatefulRuleSession;
import javax.rules.StatelessRuleSession;

import org.springmodules.jsr94.rulesource.DefaultRuleSource;

/**
 * Abstract test on DefaultRuleSource. Tests rule session creation and execution.
 * 
 * @see org.springmodules.jsr94.factory.TestRuntimeAndAdministratorDefaultRuleSource
 * @see org.springmodules.jsr94.factory.TestProviderDefaultRuleSource
 * @author janm
 */
public abstract class AbstractDefaultRuleSourceTestCase extends AbstractRuleServiceProviderTestCase {

	protected static final String BIND_URI = "test";
	protected static final String RULES_RESOURCE = "org/springmodules/jsr94/test.drl";
	
	/**
	 * Gets the DefaultRuleSource instance
	 * @return The default RuleSource
	 * @throws Exception If something goes wrong
	 */
	protected abstract void setProperties(DefaultRuleSource ruleSource) throws Exception;

	/**
	 * Test session creation & execution
	 * @throws Exception If anything goes wrong
	 */
	public void testStatelessExecution() throws Exception {
		// create the source
		DefaultRuleSource source = new DefaultRuleSource();
		setProperties(source);
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
	
	/**
	 * Test session creation & execution
	 * @throws Exception If anything goes wrong
	 */
	public void testStatefulExecution() throws Exception {
		// create the source
		DefaultRuleSource source = new DefaultRuleSource();
		setProperties(source);
		source.afterPropertiesSet();

		// get the stateless session
		StatefulRuleSession session = (StatefulRuleSession)source.createSession(BIND_URI, null, RuleRuntime.STATEFUL_SESSION_TYPE);
		
		// execute it
		List facts = new ArrayList();
		facts.add("Gecko");
		session.addObjects(facts);
		session.executeRules();
		facts = session.getObjects();
		
		assertTrue("Facts does not contain Gecko", facts.contains("Gecko"));
		assertTrue("Facts does not contain a:Gecko", facts.contains("a:Gecko"));
		
		session.release();
	}

}
