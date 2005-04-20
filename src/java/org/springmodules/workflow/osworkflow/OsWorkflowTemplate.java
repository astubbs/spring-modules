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

package org.springmodules.workflow.osworkflow;

import java.util.Map;

import com.opensymphony.workflow.config.Configuration;
import com.opensymphony.workflow.config.DefaultConfiguration;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.basic.BasicWorkflow;
import org.springmodules.workflow.osworkflow.support.WorkflowContextManager;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.StringUtils;

/**
 * @author Rob Harrop
 */
public class OsWorkflowTemplate implements InitializingBean{

	private Configuration configuration = new DefaultConfiguration();

	private Integer initialStep = new Integer(1);

	private String workflowName;

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public void setInitialStep(Integer initialStep) {
		this.initialStep = initialStep;
	}

	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}

	public void afterPropertiesSet() throws Exception {
		if(!StringUtils.hasText(this.workflowName)) {
			throw new FatalBeanException("Property [workflowName] is required.");
		}
	}

	public void initialize() {
		this.initialize(this.initialStep.intValue(), null, getCallerFromWorkflowContext());
	}

	public void initialize(Map params) {
		this.initialize(this.initialStep.intValue(), params, getCallerFromWorkflowContext());
	}

	public void initialize(Map params, String caller) {
		this.initialize(this.initialStep.intValue(), params, caller);
	}

	public void initialize(int initialStep) {
		this.initialize(initialStep, null, getCallerFromWorkflowContext());
	}

	public void initialize(int initialStep, Map params) {
		this.initialize(initialStep, params, getCallerFromWorkflowContext());
	}

	public void initialize(int initialStep, Map params, String caller) {
		Workflow workflow = createWorkflow(caller);
		workflow.setConfiguration(this.configuration);
		try {
			long id = workflow.initialize(caller, initialStep, params);
			bindWorkflowIdToContext(id);
		}
		catch (WorkflowException e) {
			throw new RuntimeException(e);
		}
	}


	protected Workflow createWorkflow(String caller) {
		return new BasicWorkflow(caller);
	}

	private void bindWorkflowIdToContext(long id) {
		WorkflowContextManager.bindResource(this.workflowName, new Long(id));
	}

	private String getCallerFromWorkflowContext() {
		return (String) WorkflowContextManager.getResource("caller");
	}

}
