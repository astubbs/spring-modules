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

package org.springmodules.resource.support;

import org.springframework.core.Ordered;
import org.springmodules.resource.ResourceStatus;

/**
 * Adapter for the ResourceSynchronization interface.
 * Contains empty implementations of all interface methods,
 * for easy overriding of single methods.
 *
 * <p>Also implements the Ordered interface to allow for
 * influencing the execution order of synchronizations.
 * Default is Integer.MAX_VALUE, indicating late execution;
 * return a lower value for earlier execution.
 *
 * @author Juergen Hoeller
 * @author Thierry Templier
 */
public abstract class ResourceSynchronizationAdapter implements ResourceSynchronization, Ordered {

	public int getOrder() {
		return Integer.MAX_VALUE;
	}

	public void beforeClose(ResourceStatus status) {
		System.err.println("beforeClose : "+status);
	}

	public void afterClose(int completionStatus) {
		System.err.println("afterClose : "+completionStatus);
	}

}
