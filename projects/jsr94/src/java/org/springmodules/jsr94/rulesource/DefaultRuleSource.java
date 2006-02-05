/**
 * 
 */

package org.springmodules.jsr94.rulesource;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Map;

import javax.rules.admin.RuleExecutionSet;
import javax.rules.admin.RuleExecutionSetCreateException;
import javax.rules.admin.RuleExecutionSetRegisterException;

import org.springframework.core.io.Resource;

/**
 * This class allows a session to be created from rules loaded from a given Resource.
 *
 * @author janm
 */
public class DefaultRuleSource extends AbstractRuleSource {

	/**
	 * The source
	 */
	private Resource source;

	/**
	 * The bindUri
	 */
	private String bindUri;

	/**
		 * Local Rule execution set provider properties -- passed to the getLocalRuleExecutionSetProvider method.
		 * This field can be null.
		 *
		 * @see javax.rules.admin.RuleAdministrator#getLocalRuleExecutionSetProvider(java.util.Map)
		 */
	private Map providerProperties;

	/**
		 * Local ruleset properties -- passed to the createRuleExecutionSet method
		 * This field can be null.
		 *
		 * @see javax.rules.admin.LocalRuleExecutionSetProvider#createRuleExecutionSet(java.io.InputStream, java.util.Map)
		 */
	private Map rulesetProperties;

	/**
		 * Rule execution set registration properties -- passed to the registerRuleExecutionSet method
		 * This field can be null.
		 *
		 * @see javax.rules.admin.RuleAdministrator#registerRuleExecutionSet(java.lang.String, javax.rules.admin.RuleExecutionSet, java.util.Map)
		 */
	private Map registrationProperties;

	/* (non-Javadoc)
	 * @see org.springmodules.jsr94.support.AbstractRuleSource#registerRuleExecutionSets()
	 */
	protected void registerRuleExecutionSets() throws RuleExecutionSetCreateException, RemoteException, IOException, RuleExecutionSetRegisterException {
		RuleExecutionSet ruleExecutionSet = ruleAdministrator.getLocalRuleExecutionSetProvider(providerProperties).createRuleExecutionSet(source.getInputStream(), rulesetProperties);
		if (bindUri == null) {
			logger.info("Using RuleExecutionSet().getName() as bindUri");
			bindUri = ruleExecutionSet.getName();
		}
		ruleAdministrator.registerRuleExecutionSet(bindUri, ruleExecutionSet, registrationProperties);
	}

	/* (non-Javadoc)
	 * @see org.springmodules.jsr94.support.AbstractRuleSource#initRuleSource()
	 */
	protected void initRuleSource() throws Exception {
		if (source == null) throw new IllegalArgumentException("Must set source on " + getClass().getName());

	}

	/**
		 * Sets new value for field bindUri
		 *
		 * @param bindUri The bindUri to set.
		 */
	public final void setBindUri(String bindUri) {
		this.bindUri = bindUri;
	}

	/**
		 * Sets new value for field source
		 *
		 * @param source The source to set.
		 */
	public final void setSource(Resource source) {
		this.source = source;
	}

	/**
		 * Sets new value for field providerProperties
		 *
		 * @param providerProperties The providerProperties to set.
		 */
	public final void setProviderProperties(Map providerProperties) {
		this.providerProperties = providerProperties;
	}

	/**
		 * Sets new value for field registrationProperties
		 *
		 * @param registrationProperties The registrationProperties to set.
		 */
	public final void setRegistrationProperties(Map registrationProperties) {
		this.registrationProperties = registrationProperties;
	}

	/**
	 * Sets new value for field rulesetProperties
	 * @param rulesetProperties The rulesetProperties to set.
	 */
	public final void setRulesetProperties(Map rulesetProperties) {
		this.rulesetProperties = rulesetProperties;
	}

}
