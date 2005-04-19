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

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.index.IndexReader;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springmodules.lucene.index.LuceneCloseIndexException;
import org.springmodules.lucene.search.LuceneCannotGetSearcherException;

/**
 * @author Brian McCallister
 * @author Thierry Templier
 */
public abstract class IndexReaderFactoryUtils {

	private static final Log logger = LogFactory.getLog(IndexReaderFactoryUtils.class);

    /**
	 * @param indexFactory
	 * @return
	 */
	public static IndexReader getIndexReader(IndexFactory indexFactory) {
    	return getIndexReader(indexFactory,true);
    }

	/**
	 * @param indexFactory
	 * @param allowSynchronization
	 * @return
	 */
	public static IndexReader getIndexReader(IndexFactory indexFactory,boolean allowSynchronization) {
		try {
			return doGetIndexReader(indexFactory, allowSynchronization);
		} catch (IOException ex) {
			throw new LuceneCannotGetSearcherException("Could not get Lucene reader", ex);
		}
	}

	/**
	 * @param indexFactory
	 * @return
	 * @throws IOException
	 */
	public static IndexReader doGetIndexReader(IndexFactory indexFactory) throws IOException {
		return doGetIndexReader(indexFactory,true);
	}

	/**
	 * @param indexFactory
	 * @param allowSynchronization
	 * @return
	 * @throws IOException
	 */
	public static IndexReader doGetIndexReader(IndexFactory indexFactory,boolean allowSynchronization) throws IOException {
		IndexReaderHolder indexReaderHolder = (IndexReaderHolder) TransactionSynchronizationManager.getResource(indexFactory);
		if (indexReaderHolder != null) {
			return indexReaderHolder.getIndexReader();
		}
		IndexReader reader = indexFactory.getIndexReader();
		if (allowSynchronization && TransactionSynchronizationManager.isSynchronizationActive()) {
			logger.debug("Registering reader synchronization for Lucene index read");
			indexReaderHolder = new IndexReaderHolder(reader);
			TransactionSynchronizationManager.bindResource(indexFactory, indexReaderHolder);
			TransactionSynchronizationManager.registerSynchronization(new IndexReaderSynchronization(indexReaderHolder, indexFactory));
		}
		return reader;
    }

	/**
	 * @param indexFactory
	 * @param indexReader
	 */
	public static void closeIndexReaderIfNecessary(IndexFactory indexFactory,IndexReader indexReader) {
		try {
			doCloseIndexReaderIfNecessary(indexFactory,indexReader);
		} catch(IOException ex) {
			throw new LuceneCloseIndexException("Unable to close index reader",ex);
		}
	}

	/**
	 * @param indexFactory
	 * @param indexReader
	 * @throws IOException
	 */
	public static void doCloseIndexReaderIfNecessary(IndexFactory indexFactory,IndexReader indexReader) throws IOException {
		if (indexReader == null || TransactionSynchronizationManager.hasResource(indexFactory)) {
			return;
		}

		indexReader.close();
	}

	/**
	 * Callback for resource cleanup at the end of an use of index read.
	 */
	private static class IndexReaderSynchronization extends TransactionSynchronizationAdapter {

		private final IndexReaderHolder indexReaderHolder;

		private final IndexFactory indexFactory;

		public IndexReaderSynchronization(IndexReaderHolder indexReaderHolder, IndexFactory indexFactory) {
			this.indexReaderHolder = indexReaderHolder;
			this.indexFactory = indexFactory;
		}

		public void suspend() {
			TransactionSynchronizationManager.unbindResource(this.indexFactory);
		}

		public void resume() {
			TransactionSynchronizationManager.bindResource(this.indexFactory, this.indexReaderHolder);
		}

		public void beforeCompletion() {
			TransactionSynchronizationManager.unbindResource(this.indexFactory);
			closeIndexReaderIfNecessary(this.indexFactory, this.indexReaderHolder.getIndexReader() );
		}
	}
}