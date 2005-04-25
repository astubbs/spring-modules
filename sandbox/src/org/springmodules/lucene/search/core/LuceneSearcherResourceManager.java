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

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Searcher;
import org.springframework.beans.factory.InitializingBean;
import org.springmodules.lucene.index.core.AbstractResourceManager;
import org.springmodules.lucene.search.factory.SearcherFactory;
import org.springmodules.lucene.search.factory.SearcherFactoryUtils;
import org.springmodules.lucene.search.factory.SearcherHolder;
import org.springmodules.resource.ResourceException;
import org.springmodules.resource.ResourceStatus;
import org.springmodules.resource.support.ResourceSynchronizationManager;

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
		/*if (indexFactory instanceof ResourceAwareIndexFactoryProxy) {
			// If we got a TransactionAwareDataSourceProxy, we need to perform transactions
			// for its underlying target DataSource, else data access code won't see
			// properly exposed transactions (i.e. transactions for the target DataSource).
			this.indexFactory = ((ResourceAwareIndexFactoryProxy) indexFactory).getTargetIndexFactory();
		}
		else {*/
			this.searcherFactory = searcherFactory;
		//}
	}

	/**
	 * Return the Lucene SearcherFactory that this instance manages resources for.
	 */
	public SearcherFactory getSearcherFactory() {
		return searcherFactory;
	}

	public Object doGetResource() {
		SearcherFactoryResourceObject txObject = new SearcherFactoryResourceObject();
		SearcherHolder searcherHolder =
			(SearcherHolder) ResourceSynchronizationManager.getResource(this.searcherFactory);
		txObject.setSearcherHolder(searcherHolder);
		return txObject;
	}

	/**
	 * @see org.springmodules.resource.ResourceManager#open()
	 */
	public void doOpen(Object resource) throws ResourceException {
		SearcherFactoryResourceObject rscObject = (SearcherFactoryResourceObject)resource;

		//System.err.println("IndexReaderFactoryUtils.getIndexReader");
		IndexSearcher indexSearcher = null;
		/*IndexReader indexReader = IndexReaderFactoryUtils.getIndexReader(this.indexFactory, false);
		if (logger.isDebugEnabled()) {
			logger.debug("Opened indexReader [" + indexReader + "] for Lucene Resource");
		}*/

		rscObject.setSearcherHolder(new SearcherHolder(indexSearcher));
		rscObject.getSearcherHolder().setSynchronizedWithResource(true);

		ResourceSynchronizationManager.bindResource(getSearcherFactory(), rscObject.getSearcherHolder());
	}

	/**
	 * @see org.springmodules.resource.ResourceManager#close()
	 */
	public void doClose(ResourceStatus status) throws ResourceException {
		SearcherFactoryResourceObject rscObject = (SearcherFactoryResourceObject)status.getResource();

		// Remove the resource holder from the thread.
		ResourceSynchronizationManager.unbindResource(this.searcherFactory);

		// Close searcher.
		Searcher searcher = rscObject.getSearcherHolder().getSearcher();
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

	/**
	 * Searcher resource transaction object, representing a SearcherHolder.
	 * Used as ressource object by LuceneSearcherResourceManager.
	 */
	private static class SearcherFactoryResourceObject {

		private SearcherHolder searcherHolder;

		/**
		 * @return
		 */
		public SearcherHolder getSearcherHolder() {
			return searcherHolder;
		}

		/**
		 * @param holder
		 */
		public void setSearcherHolder(SearcherHolder holder) {
			searcherHolder = holder;
		}

	}

}
