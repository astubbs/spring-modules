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

package org.springmodules.lucene.search.core;

import org.apache.lucene.search.Searcher;
import org.springframework.beans.factory.InitializingBean;
import org.springmodules.lucene.search.factory.SearcherFactory;
import org.springmodules.lucene.search.factory.SearcherFactoryUtils;
import org.springmodules.lucene.search.factory.SearcherHolder;
import org.springmodules.resource.AbstractResourceManager;
import org.springmodules.resource.support.ResourceBindingManager;

/**
 * @author Thierry Templier
 */
public class LuceneSearcherResourceManager extends AbstractResourceManager implements InitializingBean {

	private SearcherFactory searcherFactory;

	public LuceneSearcherResourceManager() {
	}

	public LuceneSearcherResourceManager(SearcherFactory searcherFactory) {
		setSearcherFactory(searcherFactory);
		
	}

	public void setSearcherFactory(SearcherFactory searcherFactory) {
		this.searcherFactory = searcherFactory;
	}

	/**
	 * Return the Lucene SearcherFactory that this instance manages resources for.
	 */
	public SearcherFactory getSearcherFactory() {
		return searcherFactory;
	}

	/**
	 * @see org.springmodules.resource.ResourceManager#open()
	 */
	public void doOpen() {
		SearcherHolder holder=new SearcherHolder(null);
		ResourceBindingManager.bindResource(this.searcherFactory, holder);
	}

	/**
	 * @see org.springmodules.resource.ResourceManager#close()
	 */
	public void doClose() {
		SearcherHolder holder=(SearcherHolder)ResourceBindingManager.getResource(this.searcherFactory);

		// Remove the resource holder from the thread.
		ResourceBindingManager.unbindResource(this.searcherFactory);

		// Close searcher.
		Searcher searcher = holder.getSearcher();
		if (logger.isDebugEnabled()) {
			logger.debug("Closing Lucene searcher [" + searcher + "]");
		}
		SearcherFactoryUtils.closeSearcherIfNecessary(this.searcherFactory,searcher);
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		if (this.searcherFactory == null) {
			throw new IllegalArgumentException("searcherFactory is required");
		}
	}

}
