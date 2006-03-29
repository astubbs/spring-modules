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

package org.springmodules.resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Thierry Templier
 */
public abstract class AbstractResourceManager implements ResourceManager {

	/** Transient to optimize serialization */
	protected transient Log logger = LogFactory.getLog(getClass());

	/**
	 * @see org.springmodules.resource.ResourceManager#open()
	 */
	public final void open() {
		if( logger.isDebugEnabled() ) {
			logger.debug("Opening a new resource for the thread");
		}

		doOpen();
	}

	protected abstract void doOpen();

	/**
	 * @see org.springmodules.resource.ResourceManager#close()
	 */
	public final void close() {
		if( logger.isDebugEnabled() ) {
			logger.debug("Closing the used resource for the thread");
		}

		doClose();
	}

	protected abstract void doClose();
}
