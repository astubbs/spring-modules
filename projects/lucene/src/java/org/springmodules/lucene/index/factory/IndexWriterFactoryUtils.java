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

package org.springmodules.lucene.index.factory;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.index.IndexWriter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springmodules.lucene.index.LuceneIndexAccessException;
import org.springmodules.lucene.index.resource.ResourceBindingManager;
import org.springmodules.lucene.index.resource.ResourceHolder;
import org.springmodules.lucene.index.transaction.IndexHolder;

/**
 * Helper class that provides static methods to obtain Lucene IndexWriter from
 * an IndexFactory, and to close this writer if necessary. Has special support
 * for Spring-managed resources, e.g. for use with LuceneIndexResourceManager.
 *
 * <p>Used internally by LuceneIndexTemplate, the indexer objects and the
 * LuceneIndexResourceManager.
 * Can also be used directly in application code.
 *
 * @author Brian McCallister
 * @author Thierry Templier
 * @see org.springmodules.lucene.index.core.LuceneIndexResourceManager
 */
public abstract class IndexWriterFactoryUtils {

	private static final Log logger = LogFactory.getLog(IndexWriterFactoryUtils.class);

	/**
	 * Get a IndexWriter from the given IndexFactory. Changes any Lucene io exception
	 * into the Spring hierarchy of unchecked lucene index access exceptions, simplifying
	 * calling code and making any exception that is thrown more meaningful.
	 * <p>Is aware of a corresponding IndexWriter bound to the current thread, for example
	 * when using LuceneIndexResourceManager or LuceneIndexTransactionManager. Will set an
	 * IndexWriter on an IndexHolder bound to the thread.
	 * 
	 * @param indexFactory IndexFactory to get IndexReader from
	 * @return a Lucene IndexWriter from the given IndexFactory
	 * @throws LuceneIndexAccessException
	 * if the attempt to get an IndexReader failed
	 * @see #doGetIndexWriter(IndexFactory)
	 * @see org.springmodules.lucene.index.core.LuceneIndexResourceManager
	 */
	public static LuceneIndexWriter getIndexWriter(IndexFactory indexFactory) {
		return getIndexWriter(indexFactory, true);
	}

	/**
	 * Get a IndexWriter from the given IndexFactory. Changes any Lucene io exception
	 * into the Spring hierarchy of unchecked lucene index access exceptions, simplifying
	 * calling code and making any exception that is thrown more meaningful.
	 * <p>Is aware of a corresponding IndexWriter bound to the current thread, for example
	 * when using LuceneIndexResourceManager or LuceneIndexTransactionManager. Will set an
	 * IndexWriter on an IndexHolder bound to the thread.
	 * 
	 * @param indexFactory IndexFactory to get IndexReader from
	 * @param transaction activation of transaction management
	 * @return a Lucene IndexWriter from the given IndexFactory
	 * @throws LuceneIndexAccessException
	 * if the attempt to get an IndexReader failed
	 * @see #doGetIndexWriter(IndexFactory)
	 * @see org.springmodules.lucene.index.core.LuceneIndexResourceManager
	 */
	public static LuceneIndexWriter getIndexWriter(IndexFactory indexFactory, boolean transaction) {
		try {
			return doGetIndexWriter(indexFactory, transaction);
		} catch (IOException ex) {
			throw new LuceneIndexAccessException("Could not get Lucene reader", ex);
		}
	}

	/**
	 * Actually get a Lucene IndexWriter for the given IndexFactory.
	 * Same as getIndexWriter, but throwing the original IOException.
	 * 
	 * <p>Is aware of a corresponding IndexWriter bound to the current thread, for example
	 * when using {@link org.springmodules.lucene.index.transaction.LuceneIndexTransactionManager}
	 * or {@link org.springmodules.lucene.index.resource.ResourceInterceptor}.
	 * 
	 * @param indexFactory IndexFactory to get IndexWriter from
	 * @param transaction activation of transaction management
	 * @return a Lucene IndexWriter from the given IndexFactory
	 * @throws IOException if thrown by Lucene API methods
	 */
	public static LuceneIndexWriter doGetIndexWriter(IndexFactory indexFactory, boolean transaction) throws IOException {
		if( transaction ) {
			IndexHolder indexHolder = (IndexHolder) TransactionSynchronizationManager.getResource(indexFactory);
			if (indexHolder != null) {
				return indexHolder.getIndexWriter();
			}
		}
		
		ResourceHolder resourceHolder = (ResourceHolder) ResourceBindingManager.getResource(indexFactory);
		if (resourceHolder!= null ) {
			return resourceHolder.getIndexWriter();
		}

		LuceneIndexWriter writer = indexFactory.getIndexWriter();

		return writer;
    }

	/**
	 * Close the given IndexWriter if necessary, i.e. if it is not bound to the
	 * thread.
	 * 
	 * @param indexFactory IndexFactory that the IndexReader came from
	 * @param indexWriter IndexWriter to close if necessary
	 * (if this is null, the call will be ignored)
	 * @see #doReleaseIndexWriter(IndexFactory, IndexWriter)
	 */
	public static void releaseIndexWriter(IndexFactory indexFactory, LuceneIndexWriter indexWriter) {
		releaseIndexWriter(indexFactory, indexWriter, true);
	}

	/**
	 * Close the given IndexWriter if necessary, i.e. if it is not bound to the
	 * thread.
	 * 
	 * @param indexFactory IndexFactory that the IndexReader came from
	 * @param indexWriter IndexWriter to close if necessary
	 * @param transaction activation of transaction management
	 * (if this is null, the call will be ignored)
	 * @see #doReleaseIndexWriter(IndexFactory, IndexWriter)
	 */
	public static void releaseIndexWriter(IndexFactory indexFactory,
						LuceneIndexWriter indexWriter, boolean transaction) {
		try {
			doReleaseIndexWriter(indexFactory, indexWriter, transaction);
		} catch(IOException ex) {
			throw new LuceneIndexAccessException("Unable to close index writer",ex);
		}
	}

	/**
	 * Actually close a Lucene IndexWriter for the given IndexFactory.
	 * Same as releaseIndexWriter, but throwing the original IOException.
	 * 
	 * @param indexFactory IndexFactory that the IndexReader came from
	 * @param indexWriter IndexWriter to close if necessary
	 * @param transaction activation of transaction management
	 * @throws IOException if thrown by Lucene methods
	 */
	public static void doReleaseIndexWriter(IndexFactory indexFactory,
					LuceneIndexWriter indexWriter, boolean transaction) throws IOException {
		if( transaction && TransactionSynchronizationManager.hasResource(indexFactory) ) {
			return;
		}

		if( ResourceBindingManager.hasResource(indexFactory) ) {
			return;
		}

		if( logger.isDebugEnabled() ) {
			logger.debug("Closing Lucene Index Writer");
		}
		indexWriter.close();
	}

	/**
	 * Close the given IndexWriter.
	 * 
	 * @param indexWriter IndexWriter to close if necessary
	 * (if this is null, the call will be ignored)
	 */
	public static void closeIndexWriter(LuceneIndexWriter indexWriter) {
		try {
			if( indexWriter!=null ) {
				indexWriter.close();
			}
		} catch(Exception ex) {
			throw new LuceneIndexAccessException("Unable to close index writer", ex);
		}
	}
}