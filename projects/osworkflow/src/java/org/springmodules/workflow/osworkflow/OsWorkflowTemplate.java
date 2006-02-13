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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.TypeResolver;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.basic.BasicWorkflow;
import com.opensymphony.workflow.config.Configuration;
import com.opensymphony.workflow.config.DefaultConfiguration;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.workflow.query.WorkflowExpressionQuery;
import com.opensymphony.workflow.spi.Step;

/**
 * Template class that simplifies interaction with the <a href="http://www.opensymphony.com/osworkflow">OSWorkflow</a>
 * workflow engine by hiding the management of context parameters such as the caller and workflow ID.
 * <p/>
 * Translates all checked <code>com.opensymphony.workflow.WorkflowException</code>s into unchecked
 * <code>org.springmodules.workflow.WorkflowException</code>s.
 * <p/>
 * Most operations on the <code>Workflow</code> instance are mirrored here, but for operations that require
 * lengthy interaction with a <code>Workflow</code> a custom <code>OsWorkflowCallback</code> can be used in conjunction
 * with the <code>execute(OsWorkflowCallback)</code> method.
 * <p/>
 * It is intended that a single <code>OsWorkflowTemplate</code> will be used to manage all instances of a single workflow
 * within your application. Because of this, the workflow name is not a parameter on any of operations exposed by this
 * class. Instead, the workflow name is set via the required <code>workflowName</code> property. The same workflow name
 * is used for all operations.
 * <p/>
 * Workflow context parameters such as the caller name and current instance ID are stored in an <code>OsWorkflowContext</code>
 * which is bound to the current thread using the <code>OsWorkflowContextHolder</code>. It is intended that context
 * information be set externally to the core application code. For this purpose, Spring Modules provides the
 * <code>AbstractWorkflowContextHandlerInterceptor</code> (and its implementations) which allows for context information
 * to be managed transparently in a web environment.
 * <p/>
 * Both the <code>workflowName</code> and <code>contextManager</code> parameters are required.
 * <p/>
 * It is recommended to use SpringTypeResolver, available inside the official OsWorkflow 2.8 distribution, which allows osworkflow 
 * to obtain business logic components (conditions, functions, and so on) from the ApplicationContext. 
 * 
 * @author Rob Harrop
 * @see WorkflowException
 * @see org.springmodules.workflow.WorkflowException
 * @see #execute(OsWorkflowCallback)
 * @see OsWorkflowContext
 * @see OsWorkflowContextHolder
 * @see org.springmodules.workflow.osworkflow.web.AbstractWorkflowContextHandlerInterceptor
 * @see #setWorkflowName(String)
 * @since 0.2
 */
public class OsWorkflowTemplate implements InitializingBean {

	/**
	 * The <code>Configuration</code> used to load workflow definitions. Uses the OSWorkflow <code>DefaultConfiguration</code>
	 * class by default.
	 */
	private Configuration configuration = new DefaultConfiguration();

	/**
	 * The ID of the initial action to call when initializing a workflow instance. Defaults to <code>0</code>.
	 */
	private Integer initialAction = new Integer(0);

	/**
	 * The name of the workflow definition to use.
	 */
	private String workflowName;

	/**
	 * OsWorkflow Type typeResolver.
	 */
	private TypeResolver typeResolver;

	/**
	 * @return Returns the typeResolver.
	 */
	public TypeResolver getTypeResolver() {
		return typeResolver;
	}

	/**
	 * @param typeResolver The typeResolver to set.
	 */
	public void setTypeResolver(TypeResolver resolver) {
		this.typeResolver = resolver;
	}

	/**
	 * Sets the <code>Configuration<code> used to load workflow definitions.
	 */
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	/**
	 * Sets the inital action used when initializing a workflow instance.
	 */
	public void setInitialAction(Integer initialAction) {
		this.initialAction = initialAction;
	}

	/**
	 * Sets the name of the workflow definition to use. Required.
	 */
	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}

	/**
	 * Gets the workflow name.
	 */
	public String getWorkflowName() {
		return this.workflowName;
	}

	/**
	 * Checks that both the <code>workflowName</code> and <code>contextManager</code> properties have both been specified.
	 *
	 * @throws FatalBeanException if any of the required properties is not set.
	 */
	public void afterPropertiesSet() throws Exception {
		if (!StringUtils.hasText(this.workflowName)) {
			throw new FatalBeanException("Property [workflowName] is required.");
		}
	}

	/**
	 * Initialize a workflow instance using the default initial action.
	 *
	 * @see #setInitialAction(Integer)
	 * @see #initialize(int, java.util.Map)
	 */
	public void initialize() {
		this.initialize(this.initialAction.intValue(), null);
	}

	/**
	 * Initialize a workflow instance using the default initial action and the supplied inputs.
	 *
	 * @see #setInitialAction(Integer)
	 * @see #initialize(int, java.util.Map)
	 */
	public void initialize(final Map inputs) {
		this.initialize(this.initialAction.intValue(), inputs);
	}

	/**
	 * Initialize a workflow instance using the supplied inital action.
	 *
	 * @see #initialize(int, java.util.Map)
	 */
	public void initialize(final int initialAction) {
		this.initialize(initialAction, null);
	}

	/**
	 * Initialize a workflow instance using the supplied initial action and inputs. The caller's identity is retreived
	 * from the <code>WorkflowContextManager</code>. The resulting workflow instance ID is stored using the
	 * <code>WorkflowContextManager</code>.
	 *
	 * @see OsWorkflowContext#getCaller()
	 * @see OsWorkflowContext#setInstanceId(long)
	 */
	public void initialize(final int initialAction, final Map inputs) {
		this.execute(new OsWorkflowCallback() {
			public Object doWithWorkflow(Workflow workflow) throws WorkflowException {
				long id = workflow.initialize(OsWorkflowTemplate.this.workflowName, initialAction, inputs);
				bindInstanceIdToWorkflowContext(id);
				return null;
			}
		});
	}

	/**
	 * Do the workflow action specified by the given action ID.
	 *
	 * @see #doAction(int, java.util.Map)
	 */
	public void doAction(final int actionId) {
		this.doAction(actionId, null);
	}

	/**
	 * Do the workflow action specified by the given action ID. Passes in a single input under the specified key.
	 *
	 * @param actionId the ID of the action to execute
	 * @param inputKey the key of the input
	 * @param inputVal the value of the input
	 * @see #doAction(int, java.util.Map)
	 */
	public void doAction(final int actionId, Object inputKey, Object inputVal) {
		Map inputs = new HashMap();
		inputs.put(inputKey, inputVal);
		this.doAction(actionId, inputs);
	}

	/**
	 * Do the workflow action specified by the given action ID passing in the supplied inputs. The workflow instance ID
	 * to execute the action against is retreived from the <code>WorkflowContextManager</code>.
	 *
	 * @see OsWorkflowContext#getInstanceId()
	 */
	public void doAction(final int actionId, final Map inputs) {
		this.execute(new OsWorkflowCallback() {
			public Object doWithWorkflow(Workflow workflow) throws WorkflowException {
				workflow.doAction(getInstanceId(), actionId, inputs);
				return null;
			}
		});
	}

	/**
	 * Gets the <code>WorkflowDescriptor</code> for the configured workflow.
	 *
	 * @see #getWorkflowName()
	 * @see #setWorkflowName(String)
	 */
	public WorkflowDescriptor getWorkflowDescriptor() {
		return (WorkflowDescriptor) this.execute(new OsWorkflowCallback() {
			public Object doWithWorkflow(Workflow workflow) throws WorkflowException {
				return workflow.getWorkflowDescriptor(OsWorkflowTemplate.this.workflowName);
			}
		});
	}

	/**
	 * Gets the <code>List</code> of history <code>Step</code>s for the current workflow instance.
	 */
	public List getHistorySteps() {
		return (List) this.execute(new OsWorkflowCallback() {
			public Object doWithWorkflow(Workflow workflow) throws WorkflowException {
				return workflow.getHistorySteps(getInstanceId());
			}
		});
	}

	/**
	 * Gets the <code>List</code> of current <code>Step</code>s for the current workflow instance.
	 */
	public List getCurrentSteps() {
		return (List) this.execute(new OsWorkflowCallback() {
			public Object doWithWorkflow(Workflow workflow) throws WorkflowException {
				return workflow.getCurrentSteps(getInstanceId());
			}
		});
	}

	/**
	 * Gets the <code>List</code> of history <code>StepDescriptor</code>s for the current workflow instance.
	 */
	public List getHistoryStepDescriptors() {
		return (List) this.execute(new OsWorkflowCallback() {
			public Object doWithWorkflow(Workflow workflow) throws WorkflowException {
				List steps = workflow.getHistorySteps(getInstanceId());
				return convertStepsToStepDescriptors(steps, workflow);
			}
		});
	}

	/**
	 * Gets the <code>List</code> of current <code>StepDescriptor</code>s for the current workflow instance.
	 */
	public List getCurrentStepDescriptors() {
		return (List) this.execute(new OsWorkflowCallback() {
			public Object doWithWorkflow(Workflow workflow) throws WorkflowException {
				List steps = workflow.getCurrentSteps(getInstanceId());
				return convertStepsToStepDescriptors(steps, workflow);
			}
		});
	}

	/**
	 * Gets the IDs of actions available to the caller on the current workflow instance.
	 */
	public int[] getAvailableActions() {
		return this.getAvailableActions(null);
	}

	/**
	 * Gets the IDs of actions available to the caller that match the supplied inputs on the current workflow instance.
	 */
	public int[] getAvailableActions(final Map inputs) {
		return (int[]) this.execute(new OsWorkflowCallback() {
			public Object doWithWorkflow(Workflow workflow) throws WorkflowException {
				return workflow.getAvailableActions(getInstanceId(), inputs);
			}
		});
	}

	/**
	 * Gets a <code>List</code> of the <code>ActionDescriptor</code>s available to the caller on the current workflow instance.
	 */
	public List getAvailableActionDescriptors() {
		return this.getAvailableActionDescriptors(null);
	}

	/**
	 * Gets a <code>List</code> of the <code>ActionDescriptor</code>s available to the caller that match the supplied
	 * inputs on the current workflow instance.
	 */
	public List getAvailableActionDescriptors(final Map inputs) {
		return (List) this.execute(new OsWorkflowCallback() {
			public Object doWithWorkflow(Workflow workflow) throws WorkflowException {
				WorkflowDescriptor descriptor = workflow.getWorkflowDescriptor(OsWorkflowTemplate.this.workflowName);

				int[] availableActions = workflow.getAvailableActions(getInstanceId(), inputs);
				List actionDescriptors = new ArrayList(availableActions.length);

				for (int i = 0; i < availableActions.length; i++) {
					actionDescriptors.add(descriptor.getAction(availableActions[i]));
				}

				return actionDescriptors;
			}
		});
	}

	/**
	 * Gets the entry state for the current workflow instance.
	 */
	public int getEntryState() {
		Integer state = (Integer) this.execute(new OsWorkflowCallback() {
			public Object doWithWorkflow(Workflow workflow) throws WorkflowException {
				return new Integer(workflow.getEntryState(getInstanceId()));
			}
		});

		return state.intValue();
	}

	/**
	 * Gets the <code>PropertySet</code> for the current workflow instance.
	 *
	 * @return
	 */
	public PropertySet getPropertySet() {
		return (PropertySet) this.execute(new OsWorkflowCallback() {
			public Object doWithWorkflow(Workflow workflow) throws WorkflowException {
				return workflow.getPropertySet(getInstanceId());
			}
		});
	}

	/**
	 * Returns <code>true</code> if the configured workflow can be configured with the supplied initial step.
	 *
	 * @see #setWorkflowName(String)
	 * @see #getWorkflowName()
	 */
	public boolean canInitialize(final int initialStep) {
		return this.canInitialize(initialStep, null);
	}

	/**
	 * Returns <code>true</code> if the configured workflow can be configured with the supplied initial step and inputs.
	 *
	 * @see #setWorkflowName(String)
	 * @see #getWorkflowName()
	 */
	public boolean canInitialize(final int initialStep, final Map inputs) {
		Boolean returnValue = (Boolean) this.execute(new OsWorkflowCallback() {
			public Object doWithWorkflow(Workflow workflow) throws WorkflowException {
				boolean canInit = workflow.canInitialize(OsWorkflowTemplate.this.workflowName, initialStep,
						inputs);
				return (canInit) ? Boolean.TRUE : Boolean.FALSE;
			}
		});
		return returnValue.booleanValue();
	}

	/**
	 * Returns <code>true</code> if the entry state of the current workflow can be modified to the supplied state.
	 *
	 * @see #changeEntryState(int)
	 */
	public boolean canModifyEntryState(final int newState) {
		Boolean returnValue = (Boolean) this.execute(new OsWorkflowCallback() {
			public Object doWithWorkflow(Workflow workflow) throws WorkflowException {
				boolean canModify = workflow.canModifyEntryState(getInstanceId(), newState);
				return (canModify) ? Boolean.TRUE : Boolean.FALSE;
			}
		});

		return returnValue.booleanValue();
	}

	/**
	 * Changes the entry state of the current workflow to the supplied state.
	 *
	 * @see #canModifyEntryState(int)
	 */
	public void changeEntryState(final int newState) {
		this.execute(new OsWorkflowCallback() {
			public Object doWithWorkflow(Workflow workflow) throws WorkflowException {
				workflow.changeEntryState(getInstanceId(), newState);
				return null;
			}
		});
	}

	/**
	 * Executes the supplied <code>WorkflowExpressionQuery</code> and returns a <code>List</code> of IDs that match the
	 * query criteria.
	 *
	 * @see com.opensymphony.workflow.query.WorkflowExpressionQuery
	 * @see com.opensymphony.workflow.Workflow#query(com.opensymphony.workflow.query.WorkflowExpressionQuery)
	 */
	public List query(final WorkflowExpressionQuery query) {
		return (List) this.execute(new OsWorkflowCallback() {
			public Object doWithWorkflow(Workflow workflow) throws WorkflowException {
				return workflow.query(query);
			}
		});
	}

	/**
	 * Executes the supplied <code>OsWorkflowCallback</code> against the current workflow instance as the caller bound
	 * to the current <code>OsWorkflowContext</code>.
	 */
	public Object execute(OsWorkflowCallback callback) {
		try {
			Workflow workflow = createWorkflow(OsWorkflowContextHolder.getWorkflowContext().getCaller());
			workflow.setConfiguration(this.configuration);
			return callback.doWithWorkflow(workflow);
		}
		catch (WorkflowException ex) {
			// TODO: proper exception translation
			throw new org.springmodules.workflow.WorkflowException("", ex);
		}
	}

	/**
	 * Creates a <code>Workflow</code> for the supplied caller.
	 */
	protected Workflow createWorkflow(String caller) throws WorkflowException {
		BasicWorkflow workflow = new BasicWorkflow(caller);
		// inject the type resolver if there is such a case
		if (typeResolver != null)
			workflow.setResolver(typeResolver);
		return workflow;
	}

	/**
	 * Binds the supplied instance ID to the current <code>OsWorkflowContext</code>.
	 */
	protected void bindInstanceIdToWorkflowContext(long id) {
		OsWorkflowContextHolder.getWorkflowContext().setInstanceId(id);
	}

	/**
	 * Retrieves the instance ID bound to the current <code>OsWorkflowContext</code>.
	 */
	protected long getInstanceId() {
		return OsWorkflowContextHolder.getWorkflowContext().getInstanceId();
	}

	/**
	 * Converts a <code>List</code> of <code>Step</code>s to a <code>List</code> of <code>StepDescriptor</code>s.
	 */
	private List convertStepsToStepDescriptors(List steps, Workflow workflow) {
		WorkflowDescriptor descriptor = workflow.getWorkflowDescriptor(OsWorkflowTemplate.this.workflowName);

		List stepDescriptors = new ArrayList();

		for (int i = 0; i < steps.size(); i++) {
			Step step = (Step) steps.get(i);
			stepDescriptors.add(descriptor.getStep(step.getStepId()));
		}

		return Collections.unmodifiableList(stepDescriptors);
	}
}
