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

/**
 * Stores context parameters for use in the <code>OsWorkflowTemplate</code>. Both the workflow instance ID and caller
 * name are stored in the context. An <code>OsWorkflowContext</code> is bound to a thread using the
 * <code>OsWorkflowContextHolder</code>. The intention is for context parameters to be set externally to the
 * <code>OsWorkflowTemplate</code>. Spring Modules provides the <code>AbstractWorkflowContextHandlerInterceptor</code>
 * to allow for the workflow instance ID and caller to be managed transparently in a web application setting.
 *
 * @author Rob Harrop
 * @see OsWorkflowTemplate
 * @see OsWorkflowContextHolder
 */
public class OsWorkflowContext {

	/**
	 * Stores the workflow instance ID.
	 */
	private Long instanceId;

	/**
	 * Stores the caller identity.
	 */
	private String caller;

	/**
	 * Gets the workflow instance ID.
	 */
	public long getInstanceId() {
		return instanceId.longValue();
	}

	/**
	 * Sets the workflow instance ID.
	 */
	public void setInstanceId(long instanceId) {
		this.instanceId = new Long(instanceId);
	}

	/**
	 * Gets the caller identity.
	 */
	public String getCaller() {
		return caller;
	}

	/**
	 * Sets the caller identity.
	 */
	public void setCaller(String caller) {
		this.caller = caller;
	}

	/**
	 * Returns <code>true</code> if a workflow instance ID is currently bound to this context, else returns <code>false</code>.
	 */ 
	public boolean hasInstanceId() {
		return (instanceId != null);
	}
}
