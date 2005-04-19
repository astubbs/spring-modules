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

package org.springmodules.lucene.search.query;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Query;

/**
 * This is the interface where you must specify the query to
 * pass to search method.
 * 
 * @author Thierry Templier
 */
public interface QueryCreator {

	/**
	 * This method must be implemented to specify the query
	 * to use in order to make a search with Lucene.
	 * 
	 * @param analyzer the analyzer injected
	 * @return the built query
	 * @throws ParseException
	 */
	public Query createQuery(Analyzer analyzer) throws ParseException;
}
