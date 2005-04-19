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
import org.apache.lucene.index.IndexWriter;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springmodules.lucene.index.LuceneCloseIndexException;
import org.springmodules.lucene.search.LuceneCannotGetSearcherException;

/**
 * @author Brian McCallister
 * @author Thierry Templier
 */
public abstract class IndexWriterFactoryUtils {

	private static final Log logger = LogFactory.getLog(IndexWriterFactoryUtils.class);

    /**
	 * @param indexFactory
	 * @return
	 */
	public static IndexWriter getIndexWriter(IndexFactory indexFactory) {
    	return getIndexWriter(indexFactory,true);
    }

	/**
	 * @param indexFactory
	 * @param allowSynchronization
	 * @return
	 */
	public static IndexWriter getIndexWriter(IndexFactory indexFactory,boolean allowSynchronization) {
		try {
			return doGetIndexWriter(indexFactory, allowSynchronization);
		} catch (IOException ex) {
			throw new LuceneCannotGetSearcherException("Could not get Lucene reader", ex);
		}
	}

	/**
	 * @param indexFactory
	 * @return
	 * @throws IOException
	 */
	public static IndexWriter doGetIndexWriter(IndexFactory indexFactory) throws IOException {
		return doGetIndexWriter(indexFactory,true);
	}

	/**
	 * @param indexFactory
	 * @param allowSynchronization
	 * @return
	 * @throws IOException
	 */
	public static IndexWriter doGetIndexWriter(IndexFactory indexFactory,boolean allowSynchronization) throws IOException {
		IndexWriterHolder indexWriterHolder = (IndexWriterHolder) TransactionSynchronizationManager.getResource(indexFactory);
		if (indexWriterHolder != null) {
			return indexWriterHolder.getIndexWriter();
		}
		IndexWriter writer = indexFactory.getIndexWriter();
		if (allowSynchronization && TransactionSynchronizationManager.isSynchronizationActive()) {
			logger.debug("Registering reader synchronization for Lucene index read");
			indexWriterHolder = new IndexWriterHolder(writer);
			TransactionSynchronizationManager.bindResource(indexFactory, indexWriterHolder);
			TransactionSynchronizationManager.registerSynchronization(new IndexWriterSynchronization(indexWriterHolder, indexFactory));
		}
		return writer;
    }

	/**
	 * @param indexFactory
	 * @param indexReader
	 */
	public static void closeIndexWriterIfNecessary(IndexFactory indexFactory,IndexWriter indexReader) {
		try {
			doCloseIndexWriterIfNecessary(indexFactory,indexReader);
		} catch(IOException ex) {
			throw new LuceneCloseIndexException("Unable to close index writer",ex);
		}
	}

	/**
	 * @param indexFactory
	 * @param indexReader
	 * @throws IOException
	 */
	public static void doCloseIndexWriterIfNecessary(IndexFactory indexFactory,IndexWriter indexWriter) throws IOException {
		if (indexWriter == null || TransactionSynchronizationManager.hasResource(indexFactory)) {
			return;
		}

		indexWriter.close();
	}

	/**
	 * Callback for resource cleanup at the end of an use of index read.
	 */
	private static class IndexWriterSynchronization extends TransactionSynchronizationAdapter {

		private final IndexWriterHolder indexWriterHolder;

		private final IndexFactory indexFactory;

		public IndexWriterSynchronization(IndexWriterHolder indexWriterHolder, IndexFactory indexFactory) {
			this.indexWriterHolder = indexWriterHolder;
			this.indexFactory = indexFactory;
		}

		public void suspend() {
			TransactionSynchronizationManager.unbindResource(this.indexFactory);
		}

		public void resume() {
			TransactionSynchronizationManager.bindResource(this.indexFactory, this.indexWriterHolder);
		}

		public void beforeCompletion() {
			TransactionSynchronizationManager.unbindResource(this.indexFactory);
			closeIndexWriterIfNecessary(this.indexFactory, this.indexWriterHolder.getIndexWriter() );
		}
	}
}