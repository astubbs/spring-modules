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
package org.springmodules.workflow;

import org.springframework.core.NestedRuntimeException;

/**
 * @author robh
 */
public class WorkflowException extends NestedRuntimeException {

	/**
	 * Construct a <code>WorkflowException</code> with the specified detail message.
	 *
	 * @param msg the detail message
	 */
	public WorkflowException(String msg) {
		super(msg);
	}

	public WorkflowException(String msg, Throwable ex) {
		super(msg, ex);
	}
}
