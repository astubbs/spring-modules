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

import java.util.HashMap;
import java.util.Map;

/**
 * @author Rob Harrop
 */
public abstract class WorkflowContext {

	private static final String CALLER = "caller";

	private static final String INSTANCE_ID = "instanceId";

	private static final ThreadLocal resources = new ThreadLocal() {
		protected Object initialValue() {
			return new HashMap();
		}
	};

	public static void setCaller(String caller) {
		getResourceMap().put(CALLER, caller);
	}


	public static String getCaller() {
		String caller = (String) getResourceMap().get(CALLER);

		if (caller == null) {
			throw new InvalidWorkflowContextStateException("Workflow caller not bound to workflow context.");
		}
		else {
			return caller;
		}
	}

	public static void setInstanceId(long instanceId) {
		getResourceMap().put(INSTANCE_ID, new Long(instanceId));
	}

	public static long getInstanceId() {
		Long val = (Long) getResourceMap().get(INSTANCE_ID);

		if (val == null) {
			throw new InvalidWorkflowContextStateException("Workflow instance ID not bound to workflow context.");
		}
		else {
			return val.longValue();
		}

	}

	public static boolean hasInstanceId() {
		return getResourceMap().containsKey(INSTANCE_ID);
	}

	private static Map getResourceMap() {
		return ((Map) resources.get());
	}

	public static void clear() {
		getResourceMap().clear();
	}
}
