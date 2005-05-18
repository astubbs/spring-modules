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
import org.springmodules.resource.AbstractResourceManager;
import org.springmodules.resource.support.ResourceBindingManager;

/**
 * Dedicated resource manager for IndexFactory. It allows the
 * application to manage the Lucene IndexReader and IndexWriter
 * openings and closings. It prevents the application to lock
 * the index during a long time.   
 * 
 * @author Thierry Templier
 * @see org.springmodules.lucene.index.factory.IndexFactory
 * @see org.springmodules.lucene.index.factory.IndexReaderFactoryUtils#closeIndexReaderIfNecessary(IndexFactory, IndexReader)
 * @see org.springmodules.lucene.index.factory.IndexWriterFactoryUtils#closeIndexWriterIfNecessary(IndexFactory, IndexWriter)
 * @see org.springmodules.resource.support.ResourceBindingManager#getResource(Object)
 * @see org.springmodules.resource.support.ResourceBindingManager#bindResource(Object, Object)
 * @see org.springmodules.resource.support.ResourceBindingManager#unbindResource(Object)
 */
public class LuceneIndexResourceManager extends AbstractResourceManager implements InitializingBean {

	private IndexFactory indexFactory;

	/**
	 * Construct a new LuceneIndexResourceManager for bean usage.
	 * Note: The IndexFactory has to be set before using the instance.
	 * This constructor can be used to prepare a LuceneIndexTemplate via a BeanFactory,
	 * typically setting the IndexFactory via setIndexFactory.
	 * @see #setSearcherFactory
	 */
	public LuceneIndexResourceManager() {
	}

	/**
	 * Construct a new LuceneIndexResourceManager, given an IndexFactory to manage
	 * as a resource.
	 * @param indexFactory IndexFactory to obtain both IndexReader and IndexWriter
	 */
	public LuceneIndexResourceManager(IndexFactory indexFactory) {
		setIndexFactory(indexFactory);
	}

	/**
	 * Set the IndexFactory that this instance manages resources for.
	 */
	public void setIndexFactory(IndexFactory factory) {
		indexFactory = factory;
	}

	/**
	 * Return the IndexFactory used by this resource manager.
	 */
	public IndexFactory getIndexFactory() {
		return indexFactory;
	}

	/**
	 * Binds an empty IndexHolder for the configured factory. The
	 * corresponding IndexReader and IndexWriter resources will
	 * be created lazily by a LuceneIndexTemplate, a DirectoryIndexer
	 * or a DatabaseIndexer. It prevents the application to lock
	 * the index during a long time. 
	 * @see org.springmodules.resource.ResourceManager#open()
	 */
	public void doOpen() {
		//The Lucene reader and writer will opened lazily at their first use 
		IndexHolder holder=new IndexHolder(null,null);
		ResourceBindingManager.bindResource(getIndexFactory(), holder);
	}

	/**
	 * Closes the opened IndexReader and IndexWriter, and unbind the
	 * corresponding IndexHolder.
	 * @see org.springmodules.resource.ResourceManager#close()
	 */
	public void doClose() {
		IndexHolder holder=(IndexHolder)ResourceBindingManager.getResource(this.indexFactory);

		// Remove the resource holder from the thread.
		ResourceBindingManager.unbindResource(this.indexFactory);

		// Close index.
		IndexReader indexReader = holder.getIndexReader();
		if (logger.isDebugEnabled()) {
			logger.debug("Closing Lucene indexReader [" + indexReader + "]");
		}
		IndexReaderFactoryUtils.releaseIndexReader(this.indexFactory,indexReader);

		IndexWriter indexWriter = holder.getIndexWriter();
		if (logger.isDebugEnabled()) {
			logger.debug("Closing Lucene indexWriter [" + indexWriter + "]");
		}
		IndexWriterFactoryUtils.releaseIndexWriter(this.indexFactory,indexWriter);
	}

	/**
	 * Check if the indexFactory is set.
	 */
	public void afterPropertiesSet() throws Exception {
		if (this.indexFactory == null) {
			throw new IllegalArgumentException("indexFactory is required");
		}
	}

}
