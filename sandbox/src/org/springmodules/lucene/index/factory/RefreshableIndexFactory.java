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

package org.springmodules.lucene.index.factory;

/**
 * <p>This is the index factory abstraction to allow to refresh
 * its internal state. It is useful for facories which cache index
 * instances.
 * 
 * @author Thierry Templier
 * @see org.springmodules.lucene.index.factory.IndexFactory
 */
public interface RefreshableIndexFactory extends IndexFactory {

	/**
	 * Refresh the factory. This is useful when you want to cache
	 * instances on the index and this one is refresh from another
	 * location. 
	 */
	void refresh();

}
