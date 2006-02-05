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
 * Manages the binding and retrieval of an <code>OsWorkflowContext</code> with the current thread.
 *
 * @author Rob Harrop
 * @see OsWorkflowContext
 */
public abstract class OsWorkflowContextHolder {

	/**
	 * Holds the <code>OsWorkflowContext</code> on a per-thread basis.
	 */
	private static ThreadLocal workflowContextHolder = new ThreadLocal() {
		protected Object initialValue() {
			return new OsWorkflowContext();
		}
	};

	/**
	 * Gets the <code>OsWorkflowContext</code> for the current thread.
	 */
	public static OsWorkflowContext getWorkflowContext() {
		return (OsWorkflowContext) workflowContextHolder.get();
	}

	/**
	 * Binds a new <code>OsWorkflowContext</code> to the current thread.
	 */
	public static void clearWorkflowContext() {
		workflowContextHolder.set(new OsWorkflowContext());
	}
}
