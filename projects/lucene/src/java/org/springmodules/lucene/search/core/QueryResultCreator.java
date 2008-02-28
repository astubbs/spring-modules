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

package org.springmodules.lucene.search.core;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.queryParser.ParseException;
import org.springmodules.lucene.search.factory.LuceneHits;

/**
 * Callback interface for creating the result of a Lucene search.
 *
 * <p>Used for creation of the result in LuceneSearchTemplate. When not specified,
 * a default result creator is used. The latter constructs a list with all the documents
 * contained in the result of the Lucene query.
 *
 * @author Thierry Templier
 */
public interface QueryResultCreator {

	/**
	 * Subclasses must implement this method to create a sub list of the
	 * result of a Lucene query.
	 * @param textToSearch the passed-in text to construct a query
	 * @return the Query
	 * @throws ParseException if thrown by a Lucene method, to be auto-converted
	 * to a LuceneSearchException
	 * 
	 * @param hits the hits of the result
	 * @param hitExtractor the hit extractor used
	 * @return a list of objects
	 * @throws ParseException if thrown by a Lucene method, to be auto-converted
	 * to a LuceneSearchException
	 */
	List createResult(LuceneHits hits, HitExtractor hitExtractor) throws IOException;
}
