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

package org.springmodules.lucene.search.factory;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.search.Searcher;
import org.springmodules.lucene.index.LuceneCloseIndexException;
import org.springmodules.lucene.search.LuceneCannotGetSearcherException;
import org.springmodules.lucene.search.core.SmartSearcher;
import org.springmodules.resource.support.ResourceSynchronizationManager;

/**
 * @author Brian McCallister
 * @author Thierry Templier
 */
public abstract class SearcherFactoryUtils {

	private static final Log logger = LogFactory.getLog(SearcherFactoryUtils.class);

    /**
	 * @param searcherFactory
	 * @return
	 */
	public static Searcher getSearcher(SearcherFactory searcherFactory) {
    	return getSearcher(searcherFactory,true);
    }

	/**
	 * @param searcherFactory
	 * @param allowSynchronization
	 * @return
	 */
	public static Searcher getSearcher(SearcherFactory searcherFactory,boolean allowSynchronization) {
		try {
			return doGetSearcher(searcherFactory, allowSynchronization);
		} catch (IOException ex) {
			throw new LuceneCannotGetSearcherException("Could not get Lucene searcher", ex);
		}
	}

	/**
	 * @param searcherFactory
	 * @return
	 * @throws IOException
	 */
	public static Searcher doGetSearcher(SearcherFactory searcherFactory) throws IOException {
		return doGetSearcher(searcherFactory,true);
	}

	/**
	 * @param searcherFactory
	 * @param allowSynchronization
	 * @return
	 * @throws IOException
	 */
	public static Searcher doGetSearcher(SearcherFactory searcherFactory,boolean allowSynchronization) throws IOException {
		SearcherHolder searcherHolder = (SearcherHolder) ResourceSynchronizationManager.getResource(searcherFactory);
		if (searcherHolder != null && searcherHolder.getSearcher()!=null ) {
			return searcherHolder.getSearcher();
		}

		boolean bindHolder=true;
		if( searcherHolder!=null ) {
			bindHolder=false;
		}

		Searcher searcher = searcherFactory.getSearcher();
		if (allowSynchronization && ResourceSynchronizationManager.isSynchronizationActive()) {
			logger.debug("Registering searcher synchronization for Lucene index search");
			if( searcherHolder==null ) {
				searcherHolder = new SearcherHolder(searcher);
			} else {
				searcherHolder.setSearcher(searcher);
			}

			if( bindHolder ) {
				ResourceSynchronizationManager.bindResource(searcherFactory, searcherHolder);
				ResourceSynchronizationManager.registerSynchronization(new SearcherSynchronization(searcherHolder, searcherFactory));
			}
		}
		return searcher;
     }

	/**
	 * @param searcherFactory
	 * @param searcher
	 */
	public static void closeSearcherIfNecessary(SearcherFactory searcherFactory,Searcher searcher) {
		try {
			doCloseSearcherIfNecessary(searcherFactory,searcher);
		} catch(IOException ex) {
			throw new LuceneCloseIndexException("Unable to close index searcher",ex);
		}
	}

	/**
	 * @param searcherFactory
	 * @param searcher
	 * @throws IOException
	 */
	public static void doCloseSearcherIfNecessary(SearcherFactory searcherFactory,Searcher searcher) throws IOException {
		if (searcher == null || ResourceSynchronizationManager.hasResource(searcherFactory)) {
			return;
		}
		if( searcher instanceof SmartSearcher && !((SmartSearcher)searcher).shouldClose() ) {
			return;
		}

		searcher.close();
	}

}