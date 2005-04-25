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

package org.springmodules.resource.interceptor;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springmodules.resource.ResourceManager;

/**
 * @author Thierry Templier
 */
public class ResourceAspectSupport implements InitializingBean, Serializable {

	/**
	 * Transient to avoid serialization. Not static as we want it
	 * to be the correct logger for subclasses. Reconstituted in
	 * readObject().
	 */
	protected transient Log logger = LogFactory.getLog(getClass());

	/** Delegate used to open and close resources */
	protected ResourceManager resourceManager;


	/**
	 * Set the resource manager. This will perform actual
	 * resource management: This class is just a way of invoking it.
	 */
	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}

	/**
	 * Return the resource manager.
	 */
	public ResourceManager getResourceManager() {
		return resourceManager;
	}

	/**
	 * Check that required properties were set.
	 */
	public void afterPropertiesSet() {
		if (this.resourceManager == null) {
			throw new IllegalArgumentException("resourceManager is required");
		}
	}

}