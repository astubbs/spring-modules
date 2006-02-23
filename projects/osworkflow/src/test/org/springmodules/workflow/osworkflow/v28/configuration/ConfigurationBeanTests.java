/**
 * Created on Feb 18, 2006
 *
 * $Id: ConfigurationBeanTests.java,v 1.1 2006/02/23 13:42:56 costin Exp $
 * $Revision: 1.1 $
 */
package org.springmodules.workflow.osworkflow.v28.configuration;

import junit.framework.TestCase;

import org.springmodules.workflow.osworkflow.v28.configuration.ConfigurationBean;

import com.opensymphony.workflow.util.DefaultVariableResolver;
import com.opensymphony.workflow.util.VariableResolver;


/**
 * @author Costin Leau
 *
 */
public class ConfigurationBeanTests extends TestCase {
	
	public void testVariableResolver() throws Exception {
		ConfigurationBean cfg = new ConfigurationBean();
		VariableResolver defaultResolver = cfg.getVariableResolver();
		assertNotNull(defaultResolver);
		VariableResolver userResolver = new DefaultVariableResolver();
		cfg.setVariableResolver(userResolver);
		assertSame(userResolver, cfg.getVariableResolver());
		cfg.setVariableResolver(null);
		assertSame(defaultResolver, cfg.getVariableResolver());
	}

}
