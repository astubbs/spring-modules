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

import org.apache.lucene.search.Searcher;
import org.springmodules.lucene.search.factory.LuceneSearcher;
import org.springmodules.lucene.search.factory.SearcherFactory;

/**
 * Subinterface of <code>SearcherFactory</code>, to be implemented by
 * special SearcherFactory that return Lucene searchers in an unwrapped fashion.
 * 
 * <p>Useful for example when you use a remote searcher with RMI and
 * you don't want to close it after having used it, and when you want
 * to keep a single instance of a Searcher opened through every calls
 * of an application.
 *
 * <p>Classes using this interface can query whether or not the searcher
 * should be closed after an operation. Spring Modules's SearcherFactoryUtils and
 * LuceneSearchTemplate classes automatically perform such a check.
 * 
 * @author Thierry Templier
 * @see org.springmodules.lucene.search.factory.SearcherFactoryUtils#releaseSearcher(SearcherFactory, Searcher)
 * @see org.springmodules.lucene.search.factory.SingleSearcherFactory
 */
public interface SmartSearcherFactory extends SearcherFactory {

	/**
	 * This method determines if the Searcher should be closed or
	 * not. This check is made by both the Spring Modules's
	 * SearcherFactoryUtils and LuceneSearchTemplate classes
	 * @param searcher the Searcher to check
	 * @return whether the given searcher should be closed
	 */
	public boolean shouldClose(LuceneSearcher searcher);
}
