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
import org.springmodules.lucene.index.LuceneIndexAccessException;
import org.springmodules.lucene.search.LuceneSearchException;
import org.springmodules.lucene.search.core.SmartSearcherFactory;
import org.springmodules.resource.support.ResourceBindingManager;

/**
 * Helper class that provides static methods to obtain Lucene Searcher from
 * an SearcherFactory, and to close this searcher if necessary. Has special support
 * for Spring-managed resources, e.g. for use with LuceneSearcherResourceManager.
 *
 * <p>Used internally by LuceneSearchTemplate and the LuceneIndexResourceManager.
 * Can also be used directly in application code.
 *
 * @author Brian McCallister
 * @author Thierry Templier
 */
public abstract class SearcherFactoryUtils {

	private static final Log logger = LogFactory.getLog(SearcherFactoryUtils.class);

	/**
	 * Get a Searcher from the given SearcherFactory. Changes any Lucene io exception
	 * into the Spring hierarchy of unchecked lucene search exceptions, simplifying
	 * calling code and making any exception that is thrown more meaningful.
	 * <p>Is aware of a corresponding Searcher bound to the current thread, for example
	 * when using LuceneSearcherResourceManager. Will set an Searcher on an SearcherHolder bound
	 * to the thread.
	 * @param searcherFactory SearcherFactory to get Searcher from
	 * @return a Lucene Searcher from the given SearcherFactory
	 * @throws LuceneSearchException
	 * if the attempt to get an Searcher failed
	 * @see #doGetSearcher(SearcherFactory)
	 * @see org.springmodules.lucene.search.core.LuceneSearcherResourceManager
	 */
	public static Searcher getSearcher(SearcherFactory searcherFactory) {
		try {
			return doGetSearcher(searcherFactory);
		} catch (IOException ex) {
			throw new LuceneSearchException("Could not get Lucene searcher", ex);
		}
	}

	/**
	 * Actually get a Lucene Searcher for the given SearcherFactory.
	 * Same as getSearcher, but throwing the original IOException.
	 * @param searcherFactory SearcherFactory to get Searcher from
	 * @return a Lucene Searcher from the given SearcherFactory
	 * @throws IOException if thrown by Lucene API methods
	 */
	public static Searcher doGetSearcher(SearcherFactory searcherFactory) throws IOException {
		SearcherHolder searcherHolder = (SearcherHolder) ResourceBindingManager.getResource(searcherFactory);
		if (searcherHolder != null && searcherHolder.getSearcher()!=null ) {
			return searcherHolder.getSearcher();
		}

		Searcher searcher = searcherFactory.getSearcher();
		if( searcherHolder!=null ) {
			//Lazily open the search if there is an IndexHolder
			searcherHolder.setSearcher(searcher);
		}

		return searcher;
     }

	/**
	 * Close the given Searcher if necessary, i.e. if it is not bound to the
	 * thread.
	 * @param searcherFactory SearcherFactory that the Searcher came from
	 * @param searcher Searcher to close if necessary
	 * (if this is null, the call will be ignored)
	 * @see #doReleaseSearcher(SearcherFactory, Searcher) 
	 */
	public static void releaseSearcher(SearcherFactory searcherFactory,Searcher searcher) {
		try {
			doReleaseSearcher(searcherFactory,searcher);
		} catch(IOException ex) {
			throw new LuceneIndexAccessException("Unable to close index searcher",ex);
		}
	}

	/**
	 * Actually close a Lucene Searcher for the given SearcherFactory.
	 * Same as releaseSearcher, but throwing the original IOException.
	 * @param searcherFactory SearcherFactory that the Searcher came from
	 * @param searcher Searcher to close if necessary
	 * @throws IOException if thrown by Lucene methods
	 */
	public static void doReleaseSearcher(SearcherFactory searcherFactory,Searcher searcher) throws IOException {
		if (searcher == null || ResourceBindingManager.hasResource(searcherFactory)) {
			return;
		}

		if (!(searcherFactory instanceof SmartSearcherFactory)
				|| ((SmartSearcherFactory) searcherFactory).shouldClose(searcher)) {
			logger.debug("Closing Lucene Searcher");
			searcher.close();
		}
	}

}