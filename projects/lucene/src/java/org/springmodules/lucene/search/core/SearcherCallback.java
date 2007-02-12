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

import java.io.IOException;

import org.apache.lucene.queryParser.ParseException;
import org.springmodules.lucene.search.factory.LuceneSearcher;

/**
 * Generic callback interface for code that operates on a Lucene Searcher.
 * Allows to execute any number of operations on a single Searcher.
 * 
 * <p>This is particularly useful for delegating to existing data access code
 * that expects an Searcher to work on and throws IOException. For newly
 * written code, it is strongly recommended to use LuceneSearchTemplate's more
 * specific methods.
 *
 * @author Brian McCallister
 * @author Thierry Templier
 * @see org.springmodules.lucene.search.core.DefaultLuceneSearchTemplate
 * @see LuceneSearcher
 */
public interface SearcherCallback {

	/**
	 * Gets called by <code>LuceneSearchTemplate.search</code> with an active Lucene
	 * Searcher. Does not need to care about activating or closing the Searcher.
	 * 
	 * <p>Allows for returning a result object created within the callback, i.e.
	 * a domain object or a collection of domain objects. A thrown
	 * RuntimeException is treated as application exception: it gets propagated
	 * to the caller of the template.
	 * 
	 * @param searcher an LuceneSearcher instance
	 * @return a result object, or null if none
	 * @throws IOException if thrown by a Lucene method, to be auto-converted
	 * to a LuceneSearchException
	 */
    public Object doWithSearcher(LuceneSearcher searcher) throws IOException,ParseException;
}
