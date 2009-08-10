/*
 * Copyright 2004-2007 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springmodules.workflow.jbpm31.support;

import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmException;
import org.springframework.dao.support.DaoSupport;
import org.springframework.util.Assert;
import org.springmodules.workflow.jbpm31.JbpmTemplate;

/**
 * Convenient super class for jBPM data access objects.
 * 
 * <p/> Requires a JbpmConfiguration or JbpmTemplate to be set. If customization
 * of the JbpmTemplate is required, opt for creating one separately and then
 * injecting it in this class.
 * 
 * @author Costin Leau
 * @see org.springmodules.workflow.jbpm31.JbpmTemplate
 */
public abstract class JbpmDaoSupport extends DaoSupport {

	private JbpmTemplate template;

	/**
	 * Set the {@link JbpmConfiguration} to be used by this DAO. Will
	 * automatically create a JbpmTemplate for the given JbpmConfiguration.
	 * 
	 * @param jbpmConfiguration the jbpmConfiguration to set
	 */
	public final void setJbpmConfiguration(JbpmConfiguration jbpmConfiguration) {
		template = new JbpmTemplate(jbpmConfiguration);
	}

	/**
	 * Set the JbpmTemplate to be used by this DAO.
	 * 
	 * @param jbpmTemplate the jbpmTemplate to set
	 */
	public final void setTemplate(JbpmTemplate jbpmTemplate) {
		template = jbpmTemplate;
	}
	
	/**
	 * Return the JbpmConfiguration used by this DAO.
	 * @return
	 */
	public final JbpmConfiguration getJbpmConfiguration() {
		return (template != null) ? template.getJbpmConfiguration() : null;
	}


	/**
	 * @see org.springframework.dao.support.DaoSupport#checkDaoConfig()
	 */
	protected final void checkDaoConfig() throws IllegalArgumentException {
		Assert.notNull(template, "jbpmConfiguration or jbpmTemplate is required");
	}

	/**
	 * @return the jbpmTemplate
	 */
	public final JbpmTemplate getTemplate() {
		return template;
	}

	/**
	 * Convert the given JbpmException into a Spring unchecked exception (if
	 * possible). It unwraps internal HibernateExceptions and translates them
	 * into Spring's DAO exceptions.
	 * 
	 * @param ex
	 * @return
	 */
	protected final RuntimeException convertJbpmException(JbpmException ex) {
		return template.convertJbpmException(ex);
	}
}
