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

import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.WorkflowException;

/**
 * Callback interface for OSWorkflow code. To be used with the <code>OsWorkflowTemplate.execute()</code>
 * method, assumably often as anonymous classes within a method implementation.
 *
 * @author Rob Harrop
 */
public interface OsWorkflowCallback {

	/**
	 * Callback method invoked by the <code>OsWorkflowTemplate</code> with a <code>Workflow</code> instance configured the
	 * current caller.
	 *
	 * @param workflow the <code>Workflow</code> instance to use.
	 * @return any return object or <code>null</code>.
	 * @throws WorkflowException in the case of OSWorkflow errors.
	 */
	Object doWithWorkflow(Workflow workflow) throws WorkflowException;
}
