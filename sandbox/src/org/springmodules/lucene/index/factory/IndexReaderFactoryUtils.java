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
import org.springmodules.lucene.index.LuceneIndexAccessException;
import org.springmodules.resource.support.ResourceSynchronizationManager;

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
			throw new LuceneIndexAccessException("Could not get Lucene reader", ex);
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
		IndexHolder indexHolder = (IndexHolder) ResourceSynchronizationManager.getResource(indexFactory);
		if (indexHolder != null && indexHolder.getIndexReader()!=null ) {
			return indexHolder.getIndexReader();
		}

		boolean bindHolder=true;
		if( indexHolder!=null ) {
			bindHolder=false;
		}

		IndexReader reader = indexFactory.getIndexReader();
		if (allowSynchronization && ResourceSynchronizationManager.isSynchronizationActive()) {
			logger.debug("Registering reader synchronization for Lucene index read");
			if( indexHolder==null ) {
				indexHolder = new IndexHolder(reader,null);
			} else {
				indexHolder.setIndexReader(reader);
			}

			if( bindHolder ) {
				ResourceSynchronizationManager.bindResource(indexFactory, indexHolder);
				ResourceSynchronizationManager.registerSynchronization(new IndexSynchronization(indexHolder, indexFactory));
			}
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
			throw new LuceneIndexAccessException("Unable to close index reader",ex);
		}
	}

	/**
	 * @param indexFactory
	 * @param indexReader
	 * @throws IOException
	 */
	public static void doCloseIndexReaderIfNecessary(IndexFactory indexFactory,IndexReader indexReader) throws IOException {
		if (indexReader == null || ResourceSynchronizationManager.hasResource(indexFactory)) {
			return;
		}

		indexReader.close();
	}

}