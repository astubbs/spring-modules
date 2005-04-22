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

package org.springmodules.lucene.index.core;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.springframework.beans.factory.InitializingBean;
import org.springmodules.lucene.index.factory.IndexFactory;
import org.springmodules.lucene.index.factory.IndexHolder;
import org.springmodules.lucene.index.factory.IndexReaderFactoryUtils;
import org.springmodules.lucene.index.factory.IndexWriterFactoryUtils;
import org.springmodules.resource.ResourceException;
import org.springmodules.resource.ResourceStatus;
import org.springmodules.resource.support.ResourceSynchronizationManager;

/**
 * @author Thierry Templier
 */
public class LuceneIndexResourceManager extends AbstractResourceManager implements InitializingBean {

	private IndexFactory indexFactory;

	public LuceneIndexResourceManager() {
	}

	public LuceneIndexResourceManager(IndexFactory indexFactory) {
		setIndexFactory(indexFactory);
		
	}

	public void setIndexFactory(IndexFactory indexFactory) {
		if (indexFactory instanceof ResourceAwareIndexFactoryProxy) {
			// If we got a TransactionAwareDataSourceProxy, we need to perform transactions
			// for its underlying target DataSource, else data access code won't see
			// properly exposed transactions (i.e. transactions for the target DataSource).
			this.indexFactory = ((ResourceAwareIndexFactoryProxy) indexFactory).getTargetIndexFactory();
		}
		else {
			this.indexFactory = indexFactory;
		}
	}

	/**
	 * Return the Lucene IndexFactory that this instance manages resources for.
	 */
	public IndexFactory getIndexFactory() {
		return indexFactory;
	}

	public Object doGetResource() {
		IndexFactoryResourceObject txObject = new IndexFactoryResourceObject();
		IndexHolder indexHolder =
			(IndexHolder) ResourceSynchronizationManager.getResource(this.indexFactory);
		txObject.setIndexHolder(indexHolder);
		return txObject;
	}

	/**
	 * @see org.springmodules.resource.ResourceManager#open()
	 */
	public void doOpen(Object resource) throws ResourceException {
		IndexFactoryResourceObject rscObject = (IndexFactoryResourceObject)resource;
		/*IndexHolder indexHolder =
			(IndexHolder) ResourceSynchronizationManager.getResource(this.indexFactory);
		rscObject.setIndexHolder(indexHolder);*/

		//System.err.println("IndexReaderFactoryUtils.getIndexReader");
		IndexReader indexReader = null;
		/*IndexReader indexReader = IndexReaderFactoryUtils.getIndexReader(this.indexFactory, false);
		if (logger.isDebugEnabled()) {
			logger.debug("Opened indexReader [" + indexReader + "] for Lucene Resource");
		}*/

		//The index writer will be open lazily...
		IndexWriter indexWriter = null;
		/*IndexWriter indexWriter = IndexWriterFactoryUtils.getIndexWriter(this.indexFactory, false);
		if (logger.isDebugEnabled()) {
			logger.debug("Opened indexWriter [" + indexWriter + "] for Lucene Resource");
		}*/

		rscObject.setIndexHolder(new IndexHolder(indexReader,indexWriter));
		rscObject.getIndexHolder().setSynchronizedWithResource(true);

		ResourceSynchronizationManager.bindResource(getIndexFactory(), rscObject.getIndexHolder());
	}

	/**
	 * @see org.springmodules.resource.ResourceManager#close()
	 */
	public void doClose(ResourceStatus status) throws ResourceException {
		IndexFactoryResourceObject rscObject = (IndexFactoryResourceObject)status.getResource();

		// Remove the resource holder from the thread.
		ResourceSynchronizationManager.unbindResource(this.indexFactory);

		// Close index.
		IndexReader indexReader = rscObject.getIndexHolder().getIndexReader();
		if (logger.isDebugEnabled()) {
			logger.debug("Closing Lucene indexReader [" + indexReader + "]");
		}
		IndexReaderFactoryUtils.closeIndexReaderIfNecessary(this.indexFactory,indexReader);

		IndexWriter indexWriter = rscObject.getIndexHolder().getIndexWriter();
		if (logger.isDebugEnabled()) {
			logger.debug("Closing Lucene indexWriter [" + indexWriter + "]");
		}
		IndexWriterFactoryUtils.closeIndexWriterIfNecessary(this.indexFactory,indexWriter);
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		if (this.indexFactory == null) {
			throw new IllegalArgumentException("indexFactory is required");
		}
	}

	/**
	 * Index resource object, representing a IndexHolder.
	 * Used as resource object by LuceneIndexTransactionManager.
	 */
	private static class IndexFactoryResourceObject {

		private IndexHolder indexHolder;

		/**
		 * @return
		 */
		public IndexHolder getIndexHolder() {
			return indexHolder;
		}

		/**
		 * @param holder
		 */
		public void setIndexHolder(IndexHolder holder) {
			indexHolder = holder;
		}

	}

}
