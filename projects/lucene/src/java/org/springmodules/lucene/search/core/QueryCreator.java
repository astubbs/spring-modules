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

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Query;

/**
 * Callback interface for creating a Query for Lucene search.
 *
 * <p>Used for input Query creation in LuceneSearchTemplate. Alternatively,
 * Query instances can be passed into LuceneSearchTemplate's corresponding
 * <code>search</code> methods directly.
 *
 * @author Thierry Templier
 */
public interface QueryCreator {

	/**
	 * Subclasses must implement this method to create a Lucene query
	 * from a string object passed into the <code>search</code> method.
	 * @param textToSearch the passed-in text to construct a query
	 * @return the Query
	 * @throws ParseException if thrown by a Lucene method, to be auto-converted
	 * to a LuceneSearchException
	 */
	public Query createQuery(Analyzer analyzer) throws ParseException;
}
