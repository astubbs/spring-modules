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

import org.apache.lucene.document.Document;

/**
 * Callback interface for extracting datas from hits of search
 * result.
 * 
 * <p>Used for output search results in the LuceneSearchTemplate. Alternatively,
 * the Lucene HitCollector class can be used directly. 
 * 
 * @author Thierry Templier
 * @see org.springmodules.lucene.search.core.DefaultLuceneSearchTemplate#search(Query, HitExtractor)
 * @see org.springmodules.lucene.search.core.DefaultLuceneSearchTemplate#search(Query, HitExtractor, Filter)
 * @see org.springmodules.lucene.search.core.DefaultLuceneSearchTemplate#search(Query, HitExtractor, Filter, Sort)
 * @see org.springmodules.lucene.search.core.DefaultLuceneSearchTemplate#search(Query, HitExtractor, Sort)
 * @see org.springmodules.lucene.search.core.DefaultLuceneSearchTemplate#search(QueryCreator, HitExtractor)
 * @see org.springmodules.lucene.search.core.DefaultLuceneSearchTemplate#search(QueryCreator, HitExtractor, Filter)
 * @see org.springmodules.lucene.search.core.DefaultLuceneSearchTemplate#search(QueryCreator, HitExtractor, Filter, Sort)
 * @see org.springmodules.lucene.search.core.DefaultLuceneSearchTemplate#search(QueryCreator, HitExtractor, Sort)
 */
public interface HitExtractor {

	/**
	 * Create a Lucene independant result object from a Lucene result.
	 * The callback method has the internal document identifier, the Lucene
	 * document and the score of the document in the search as method
	 * parameters. 
	 * @param id the internal document identifier
	 * @param document the Document instance
	 * @param score the score of the document in the search
	 * @return a result object
	 */
	public Object mapHit(int id, Document document, float score);
}
