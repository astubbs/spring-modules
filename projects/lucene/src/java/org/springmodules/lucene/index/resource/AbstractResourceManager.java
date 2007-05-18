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

import org.springmodules.lucene.index.factory.IndexFactory;

/**
 * Root class for all implementation of the ResourceManager interface. It
 * allows the injection of an IndexFactory instance and provides a default
 * implementation of the initializeResources and releaseResources in order
 * to automatically bind resources to the thread local.
 * 
 * Sub classes must implement the doInitializeResources method in order
 * to acually initialize and release resources.
 * 
 * @author Thierry Templier
 */
public abstract class AbstractResourceManager implements ResourceManager {
	private IndexFactory indexFactory;

	/**
	 * Use the {@link #doInitializeResources(ResourceManagementDefinition)} method
	 * to get resources and bind them to the thread local using the {@link ResourceBindingManager}
	 * class.
	 * 
	 * @see ResourceManager#initializeResources(ResourceManagementDefinition)
	 * @see #doInitializeResources(ResourceManagementDefinition)
	 * @see ResourceBindingManager#bindResource(Object, Object)
	 */
	public final void initializeResources(ResourceManagementDefinition definition) {
		ResourceHolder holder = doInitializeResources(definition);
		ResourceBindingManager.bindResource(indexFactory, holder);
	}

	/**
	 * Actually initialize the resources to bind to the thread local.
	 * 
	 * @param definition a definition of the resources to initialize
	 * @return a resource holder containing the initialized resources
	 */
	protected abstract ResourceHolder doInitializeResources(ResourceManagementDefinition definition);

	/**
	 * Get the resources from the thread local using the {@link ResourceBindingManager}
	 * class and then release them with the {@link #doReleaseResources(ResourceHolder)} method.
	 * 
	 * @see ResourceManager#releaseResources()
	 * @see #doReleaseResources(ResourceHolder)
	 * @see ResourceBindingManager#unbindResource(Object)
	 */
	public final void releaseResources() {
		ResourceHolder holder = (ResourceHolder)ResourceBindingManager.getResource(indexFactory);
		if( holder!=null ) {
			ResourceBindingManager.unbindResource(indexFactory);
			doReleaseResources(holder);
		}
	}

	/**
	 * Actually release the resources bound to the thread local.
	 * 
	 * @param holder a resource holder containing the initialized resources
	 */
	protected abstract void doReleaseResources(ResourceHolder holder);

	/**
	 * Return the IndexFactory used by the resource manager.
	 */
	public IndexFactory getIndexFactory() {
		return indexFactory;
	}

	/**
	 * Set the IndexFactory to obtain both IndexReader and IndexWriter.
	 */
	public void setIndexFactory(IndexFactory indexFactory) {
		this.indexFactory = indexFactory;
	}

}
