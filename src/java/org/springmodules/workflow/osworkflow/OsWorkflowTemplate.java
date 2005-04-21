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

import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.basic.BasicWorkflow;
import com.opensymphony.workflow.config.Configuration;
import com.opensymphony.workflow.config.DefaultConfiguration;
import org.springmodules.workflow.osworkflow.support.WorkflowContext;

import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

/**
 * @author Rob Harrop
 */
public class OsWorkflowTemplate implements InitializingBean {

	private Configuration configuration = new DefaultConfiguration();

	private Integer initialAction = new Integer(0);

	private String workflowName;

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public void setInitialAction(Integer initialAction) {
		this.initialAction = initialAction;
	}

	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}

	public void afterPropertiesSet() throws Exception {
		if (!StringUtils.hasText(this.workflowName)) {
			throw new FatalBeanException("Property [workflowName] is required.");
		}
	}

	public void initialize() {
		this.initialize(this.initialAction.intValue(), null, WorkflowContext.getCaller());
	}

	public void initialize(final Map inputs) {
		this.initialize(this.initialAction.intValue(), inputs, WorkflowContext.getCaller());
	}

	public void initialize(final Map inputs, final String caller) {
		this.initialize(this.initialAction.intValue(), inputs, caller);
	}

	public void initialize(final int initialAction) {
		this.initialize(initialAction, null, WorkflowContext.getCaller());
	}

	public void initialize(final int initialAction, final Map inputs) {
		this.initialize(initialAction, inputs, WorkflowContext.getCaller());
	}

	public void initialize(final int initialAction, final Map inputs, final String caller) {
		execute(caller, new OsWorkflowCallback(){
			public Object doWithWorkflow(Workflow workflow) throws WorkflowException {
				long id = workflow.initialize(OsWorkflowTemplate.this.workflowName, initialAction, inputs);
				WorkflowContext.setInstanceId(id);
				return null;
			}
		});
	}

	public Object execute(String caller, OsWorkflowCallback callback) {
		Workflow workflow = createWorkflow(caller);
		workflow.setConfiguration(this.configuration);
		try {
       return callback.doWithWorkflow(workflow);
		} catch(WorkflowException ex) {
			// TODO: proper exception translation
			throw new org.springmodules.workflow.WorkflowException("", ex);
		}
	}


	protected Workflow createWorkflow(String caller) {
		return new BasicWorkflow(caller);
	}

}
