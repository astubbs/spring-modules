/*
 * Copyright 2002-2005 the original author or authors.
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

package org.springmodules.workflow.osworkflow.configuration;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.opensymphony.workflow.FactoryException;
import com.opensymphony.workflow.InvalidWorkflowDescriptorException;
import com.opensymphony.workflow.StoreException;
import com.opensymphony.workflow.config.DefaultConfiguration;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.workflow.loader.WorkflowLoader;
import com.opensymphony.workflow.spi.WorkflowStore;
import com.opensymphony.workflow.spi.memory.MemoryWorkflowStore;
import com.opensymphony.workflow.util.VariableResolver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

import org.springframework.beans.FatalBeanException;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;

/**
 * Supports Spring-style configuration of OSWorkflow resources.
 * <p/>
 * Workflow descriptor resources are configured through the <code>workflowLocations</code> property. This property
 * accepts a <code>Properties</code> instance and treats the key of each entry as the workflow name and the value
 * as the resource path. All standard Spring resource paths are supported including <code>classpath:</code> style
 * resources.
 * <p/>
 * By default <code>MemoryWorkflowStore</code> is used as the persistence class. However it is possible
 * to use an already configured <code>WorkflowStore</code> by calling #setWorkflowStore.
 * <p/>
 *
 * @author Rob Harrop
 */
public class ConfigurationBean extends DefaultConfiguration {
	/**
	 * <code>Log</code> instance for this class.
	 */
	private static final Log logger = LogFactory.getLog(ConfigurationBean.class);

	/**
	 * Spring <code>ResourceLoader</code> used to load workflow <code>Resource</code>s.
	 * 
	 */
	protected ResourceLoader resourceLoader = new DefaultResourceLoader();

	/**
	 * Stores any arguments that are to be passed to
	 */
	private Map persistenceArgs = new HashMap();

	/**
	 * Stores loaded workflows
	 */
	private Map workflows = new HashMap();

	/**
	 * Indicates whether this instance is initialized or not
	 */
	private boolean initialized;

	/**
	 * User defined store - can be null.
	 */
	private WorkflowStore workflowStore;

	/**
	 * User defined resolver - if null the default one will be used.
	 */
	private VariableResolver variableResolver;
	/**
	 * Creates a new <code>ConfigurationBean</code> with <code>MemoryWorkflowStore</code>
	 * as the persistence class.
	 */
	public ConfigurationBean() {
		setPersistence(MemoryWorkflowStore.class.getName());
	}

	/**
	 * Gets the <code>Map</code> of arguments to be passed to the persistence object
	 */
	public Map getPersistenceArgs() {
		return this.persistenceArgs;
	}

	/**
	 * Sets the arguments to be passed to the persistence object.
	 */
	public void setPersistenceArgs(Map persistenceArgs) {
		Assert.notEmpty(persistenceArgs, "persistenceArgs cannot be null or empty");
		this.persistenceArgs = persistenceArgs;
	}

	/**
	 * Sets the locations of the workflow definition files as a <code>Properties</code> instance.
	 * The key of each entry corresponds to the logical name for the workflow definition and
	 * the value of each entry is the location of the definition file.
	 * <p/>
	 * Locations are specified as Spring-style resource paths and classpath: resources
	 * are fully supported.
	 */
	public void setWorkflowLocations(Properties workflowLocations) {
		Assert.notNull(workflowLocations, "workflowLocations cannot be null");

		Enumeration workflowNames = workflowLocations.propertyNames();
		while (workflowNames.hasMoreElements()) {
			String name = (String) workflowNames.nextElement();
			String resourceLocation = workflowLocations.getProperty(name);

			if (logger.isInfoEnabled()) {
				logger.info("Loading workflow [" + name + "] from [" + resourceLocation + "].");
			}

			workflows.put(name, loadWorkflowDescriptor(resourceLocation, name));
		}

		this.initialized = true;
	}

	/**
	 * Gets a <code>WorkflowDescriptor</code> by name.
	 */
	public WorkflowDescriptor getWorkflow(String name) throws FactoryException {
		WorkflowDescriptor wd = (WorkflowDescriptor) this.workflows.get(name);

		if (wd == null) {
			throw new FactoryException("Unknown workflow name [" + name + "].");
		}

		return wd;
	}

	/**
	 * Gets the names of all configured workflows.
	 */
	public String[] getWorkflowNames() throws FactoryException {
		Set names = this.workflows.keySet();
		return (String[]) names.toArray(new String[names.size()]);
	}

	/**
	 * Indicates whether this instance has been initialized or not.
	 */
	public boolean isInitialized() {
		return this.initialized;
	}

	/**
	 * Unsupported operation.
	 */
	public boolean removeWorkflow(String string) throws FactoryException {
		throw new UnsupportedOperationException("Operation removeWorkflow(String) not supported.");
	}

	/**
	 * Loads a <code>WorkflowDescriptor</code> from the specified location using the <code>WorkflowLoader</code> class.
	 */
	protected WorkflowDescriptor loadWorkflowDescriptor(String resourceLocation, String name) {
		Resource resource = this.resourceLoader.getResource(resourceLocation);
		WorkflowDescriptor workflowDescriptor = null;
		try {
			//workflowDescriptor = WorkflowLoader.load(resource.getURL(), true);
			workflowDescriptor = WorkflowLoader.load(resource.getURL(), false);
		}
		catch (IOException ex) {
			throw new FatalBeanException("Unable to load workflow resource [" + resourceLocation + "].", ex);
		}
		catch (InvalidWorkflowDescriptorException ex) {
			throw new FatalBeanException("Descriptor for workflow ["
					+ name
					+ "] in ["
					+ resourceLocation
					+ "] is invalid.", ex);
		}
		catch (SAXException ex) {
			throw new FatalBeanException("XML in descriptorfor workflow ["
					+ name
					+ "] in ["
					+ resourceLocation
					+ "] is invalid.", ex);
		}
		return workflowDescriptor;
	}

	/**
	 * @see com.opensymphony.workflow.config.DefaultConfiguration#getWorkflowStore()
	 */
	public WorkflowStore getWorkflowStore() throws StoreException {
		// default behavior
		if (workflowStore == null)
			return super.getWorkflowStore();
		return workflowStore;
	}

	/**
	 * @param workflowStore The workflowStore to set.
	 */
	public void setWorkflowStore(WorkflowStore workflowStore) {
		this.workflowStore = workflowStore;
	}

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
