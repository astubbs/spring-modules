/**
 * Tests propagation of registration properties
 */

package org.springmodules.jsr94.factory;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.rules.RuleRuntime;
import javax.rules.admin.LocalRuleExecutionSetProvider;
import javax.rules.admin.RuleAdministrator;

import junit.framework.TestCase;
import org.easymock.MockControl;
import org.springmodules.jsr94.rulesource.DefaultRuleSource;

import org.springframework.core.io.Resource;

/**
 * Tests that the properties are being propagated correctly to the calls to
 * RuleRuntime and RuleAdministrator implementations.
 *
 * @author janm
 */
public class MockRuntimeAndAdministratorRuleSourceTests extends TestCase {

	private MockControl controlRuleExecutionSetProvider;

	private LocalRuleExecutionSetProvider ruleExecutionSetProvider;

	private MockControl controlRuleRuntime;

	private RuleRuntime ruleRuntime;

	private MockControl controlRuleAdministrator;

	private RuleAdministrator ruleAdministrator;

	private MockControl controlSource;

	private Resource source;

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		controlRuleRuntime = MockControl.createStrictControl(RuleRuntime.class);
		controlRuleAdministrator = MockControl.createStrictControl(RuleAdministrator.class);
		controlSource = MockControl.createStrictControl(Resource.class);
		controlRuleExecutionSetProvider = MockControl.createStrictControl(LocalRuleExecutionSetProvider.class);

		ruleRuntime = (RuleRuntime) controlRuleRuntime.getMock();
		ruleAdministrator = (RuleAdministrator) controlRuleAdministrator.getMock();
		source = (Resource) controlSource.getMock();
		ruleExecutionSetProvider = (LocalRuleExecutionSetProvider) controlRuleExecutionSetProvider.getMock();
	}

	/**
		 * Verifies that the providerProperties, rulesetProperties and registrationProperties are
		 * being passed to the implementation correctly.
		 *
		 * @throws Exception
		 */
	public void testPropertyPropagation() throws Exception {
		Map providerProperties = new HashMap();
		Map rulesetProperties = new HashMap();
		Map registrationProperties = new HashMap();

		providerProperties.put("providerProperties", "providerProperties");
		rulesetProperties.put("rulesetProperties", "rulesetProperties");
		registrationProperties.put("registrationProperties", "registrationProperties");

		DefaultRuleSource rs = new DefaultRuleSource();
		rs.setBindUri("foo");
		rs.setRuleAdministrator(ruleAdministrator);
		rs.setRuleRuntime(ruleRuntime);
		rs.setSource(source);
		rs.setRegistrationProperties(registrationProperties);
		rs.setRulesetProperties(rulesetProperties);
		rs.setProviderProperties(providerProperties);

		ruleAdministrator.getLocalRuleExecutionSetProvider(providerProperties);
		controlRuleAdministrator.setReturnValue(ruleExecutionSetProvider);

		source.getInputStream();
		controlSource.setReturnValue(null);

		ruleExecutionSetProvider.createRuleExecutionSet((InputStream) null, rulesetProperties);
		controlRuleExecutionSetProvider.setReturnValue(null);
		ruleAdministrator.registerRuleExecutionSet("foo", null, registrationProperties);

		controlSource.replay();
		controlRuleAdministrator.replay();
		controlRuleExecutionSetProvider.replay();
		controlRuleRuntime.replay();

		rs.afterPropertiesSet();

		controlRuleAdministrator.verify();
	}

}
