/**
 * Created on Feb 21, 2006
 *
 * $Id: JbpmAccessor.java,v 1.1 2006/03/02 14:56:00 costin Exp $
 * $Revision: 1.1 $
 */
package org.springmodules.workflow.jbpm31;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmException;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author Costin Leau
 *
 */
public class JbpmAccessor implements InitializingBean{

	protected final Log logger = LogFactory.getLog(getClass());
	
	/**
	 * LocalJbpmConfigurationFactoryBean used with this JbpmTemplate for creating the context.
	 */
	protected JbpmConfiguration jbpmConfiguration;

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		if (jbpmConfiguration == null)
			throw new IllegalArgumentException("jbpmConfiguration must be set");
	}
	
	/**
	 * Utility method for converting the jbpm exceptions.
	 * 
	 * @param exception
	 * @return
	 */
	public RuntimeException convertJbpmException(JbpmException exception) {
		return JbpmConfigurationUtils.convertJbpmException(exception);
	}
	

	/**
	 * @return Returns the jbpmConfiguration.
	 */
	public JbpmConfiguration getJbpmConfiguration() {
		return jbpmConfiguration;
	}

	/**
	 * @param jbpmConfiguration The jbpmConfiguration to set.
	 */
	public void setJbpmConfiguration(JbpmConfiguration jbpmConfiguration) {
		this.jbpmConfiguration = jbpmConfiguration;
	}

}
