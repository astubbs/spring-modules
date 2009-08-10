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

/**
 * <p>This is the searcher factory abstraction to get searcher
 * instances to make searches on a Lucene index. These instances can be used
 * with the LuceneSearchTemplate class or with the different query classes.
 * 
 * @author Thierry Templier
 * @see org.springmodules.samples.lucene.searching.console.Searcher
 * @see org.springmodules.lucene.search.core.DefaultLuceneSearchTemplate
 * @see org.springmodules.lucene.search.object.LuceneSearchQuery
 * @see org.springmodules.lucene.search.object.ParsedLuceneSearchQuery
 */
public interface SearcherFactory {

	/**
	 * Construct a Searcher instance. This instance will be used by both
	 * the LuceneSearcherTemplate and every query classes to make searches
	 * on the index.
	 * @return the Searcher instance
	 */
	LuceneSearcher getSearcher() throws IOException;
}
