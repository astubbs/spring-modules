/**
 * Created on Feb 18, 2006
 *
 * $Id: ConfigurationBean.java,v 1.1 2006/02/23 13:42:57 costin Exp $
 * $Revision: 1.1 $
 */
package org.springmodules.workflow.osworkflow.v28.configuration;

import com.opensymphony.workflow.util.VariableResolver;

/**
 * Configuration bean which adds support for VariableTypeResolver available in OsWorkflow 2.8.0
 * 
 * @author Costin Leau
 *
 */
public class ConfigurationBean extends org.springmodules.workflow.osworkflow.configuration.ConfigurationBean {

	private VariableResolver variableResolver;
	
	/**
	 * @see com.opensymphony.workflow.config.DefaultConfiguration#getVariableResolver()
	 */
	public VariableResolver getVariableResolver() {
		return (this.variableResolver == null) ? super.getVariableResolver() : this.variableResolver;
	}

	/**
	 * @param variableResolver The variableResolver to set.
	 */
	public void setVariableResolver(VariableResolver variableResolver) {
		this.variableResolver = variableResolver;
	}
}
