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

package org.springmodules.lucene.search.core;

import org.apache.lucene.search.Searcher;
import org.springframework.beans.factory.InitializingBean;
import org.springmodules.lucene.search.factory.LuceneSearcher;
import org.springmodules.lucene.search.factory.SearcherFactory;
import org.springmodules.lucene.search.factory.SearcherFactoryUtils;
import org.springmodules.lucene.search.factory.SearcherHolder;
import org.springmodules.resource.AbstractResourceManager;
import org.springmodules.resource.support.ResourceBindingManager;

/**
 * Dedicated resource manager for SearcherFactory. It allows the
 * application to manage the Lucene Searcher openings and closings.
 * 
 * <p>Searcher is lazily opened at its first uses.
 *  
 * @author Thierry Templier
 * @see org.springmodules.lucene.search.factory.SearcherFactory
 * @see org.springmodules.lucene.search.factory.SearcherFactoryUtils#getSearcher(SearcherFactory)
 * @see org.springmodules.lucene.search.factory.SearcherFactoryUtils#closeSearcherIfNecessary(SearcherFactory, Searcher)
 * @see org.springmodules.resource.support.ResourceBindingManager#getResource(Object)
 * @see org.springmodules.resource.support.ResourceBindingManager#bindResource(Object, Object)
 * @see org.springmodules.resource.support.ResourceBindingManager#unbindResource(Object)
 */
public class LuceneSearcherResourceManager extends AbstractResourceManager implements InitializingBean {

	private SearcherFactory searcherFactory;

	/**
	 * Construct a new LuceneSearcherResourceManager for bean usage.
	 * Note: The SearcherFactory has to be set before using the instance.
	 * This constructor can be used to prepare a LuceneSearchTemplate via a BeanFactory,
	 * typically setting the SearcherFactory via setSearcherFactory.
	 * @see #setSearcherFactory(SearcherFactory)
	 */
	public LuceneSearcherResourceManager() {
	}

	/**
	 * Construct a new LuceneSearcherResourceManager, given an SearcherFactory to manage
	 * as a resource.
	 * @param searcherFactory SearcherFactory to obtain Searcher
	 */
	public LuceneSearcherResourceManager(SearcherFactory searcherFactory) {
		setSearcherFactory(searcherFactory);
		
	}

	/**
	 * Set the SearcherFactory that this instance manages resources for.
	 */
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
	 * Binds an empty SearcherHolder for the configured factory. The
	 * corresponding Searcher resource will
	 * be created lazily by a LuceneSearcherTemplate or a search query. 
	 * @see org.springmodules.resource.ResourceManager#open()
	 */
	public void doOpen() {
		SearcherHolder holder=new SearcherHolder(null);
		ResourceBindingManager.bindResource(this.searcherFactory, holder);
	}

	/**
	 * Closes the opened Searcher, and unbind the
	 * corresponding SearcherHolder.
	 * @see org.springmodules.resource.ResourceManager#close()
	 * @see SearcherFactoryUtils#releaseSearcher(SearcherFactory, Searcher)
	 */
	public void doClose() {
		SearcherHolder holder=(SearcherHolder)ResourceBindingManager.getResource(this.searcherFactory);

		// Remove the resource holder from the thread.
		ResourceBindingManager.unbindResource(this.searcherFactory);

		// Close searcher.
		LuceneSearcher searcher = holder.getSearcher();
		if (logger.isDebugEnabled()) {
			logger.debug("Closing Lucene searcher [" + searcher + "]");
		}
		SearcherFactoryUtils.releaseSearcher(this.searcherFactory,searcher);
	}

	/**
	 * Check if the searcherFactory is set.
	 */
	public void afterPropertiesSet() throws Exception {
		if (this.searcherFactory == null) {
			throw new IllegalArgumentException("searcherFactory is required");
		}
	}

}
