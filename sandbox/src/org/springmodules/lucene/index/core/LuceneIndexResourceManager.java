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
		this.indexFactory = indexFactory;
	}

	/**
	 * Return the Lucene IndexFactory that this instance manages resources for.
	 */
	public IndexFactory getIndexFactory() {
		return indexFactory;
	}

	/**
	 * @see org.springmodules.resource.ResourceManager#open()
	 */
	public void doOpen() {
		//The Lucene reader and writer will opened lazily at their first use 
		IndexHolder holder=new IndexHolder(null,null);
		ResourceBindingManager.bindResource(getIndexFactory(), holder);
	}

	/**
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
		IndexReaderFactoryUtils.closeIndexReaderIfNecessary(this.indexFactory,indexReader);

		IndexWriter indexWriter = holder.getIndexWriter();
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

}
