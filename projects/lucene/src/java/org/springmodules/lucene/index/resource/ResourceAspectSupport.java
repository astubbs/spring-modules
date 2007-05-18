/*
 * Copyright 2002-2007 the original author or authors.
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

package org.springmodules.lucene.index.resource;

import java.io.Serializable;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author Thierry Templier
 */
public class ResourceAspectSupport implements InitializingBean, Serializable {
	private ResourceAttributeSource resourceAttributeSource;

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

	public void setResourceAttributes(Properties resourceAttributes) {
		NameMatchResourceAttributeSource ras = new NameMatchResourceAttributeSource();
		ras.setProperties(resourceAttributes);
		this.resourceAttributeSource = ras;
	}
	
	public ResourceAttributeSource getResourceAttributesSource() {
		return this.resourceAttributeSource;
	}

	public void setResourceAttributesSource(ResourceAttributeSource resourceAttributeSource ) {
		this.resourceAttributeSource = resourceAttributeSource;
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