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
	 * Map passed to the localExecutionSetProvider
	 */
	private Map providerProperties;
	
	
	private Map executionSetProperties;

	/* (non-Javadoc)
	 * @see org.springmodules.jsr94.support.AbstractRuleSource#registerRuleExecutionSets()
	 */
	protected void registerRuleExecutionSets() throws RuleExecutionSetCreateException, RemoteException, IOException, RuleExecutionSetRegisterException {
		RuleExecutionSet ruleExecutionSet = ruleAdministrator.getLocalRuleExecutionSetProvider(null).createRuleExecutionSet(source.getInputStream(), null);
		ruleAdministrator.registerRuleExecutionSet(bindUri, ruleExecutionSet, null);		
	}

	/* (non-Javadoc)
	 * @see org.springmodules.jsr94.support.AbstractRuleSource#initRuleSource()
	 */
	protected void initRuleSource() throws Exception {
		if (source == null) throw new IllegalArgumentException("Must set source on " + getClass().getName());
		if (bindUri == null) throw new IllegalArgumentException("Must set bindUri on " + getClass().getName());
	}

	/**
	 * Sets new value for field bindUri
	 * @param bindUri The bindUri to set.
	 */
	public final void setBindUri(String bindUri) {
		this.bindUri = bindUri;
	}

	/**
	 * Sets new value for field source
	 * @param source The source to set.
	 */
	public final void setSource(Resource source) {
		this.source = source;
	}

}
