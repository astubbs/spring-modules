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

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;

/**
 * Callback class for creating a Query for Lucene search basing the
 * Lucene QueryParser and MultiFieldQueryParser classes.
 *
 * <p>Used for input Query creation in LuceneSearchTemplate. Alternatively,
 * Query instances can be passed into LuceneSearchTemplate's corresponding
 * <code>search</code> methods directly.
 *
 * @author Thierry Templier
 * @see org.springmodules.lucene.search.query.QueryCreator
 */
public abstract class ParsedQueryCreator implements QueryCreator {

	/**
	 * This method constructs a Lucene query using the Lucene
	 * Analyzer class and the Lucene QueryParser or MultiFieldQueryParser
	 * class according the params of the query returned by the configureQuery
	 * method.
	 * @param analyzer the Lucene Analyzer to use
	 * @return the constructed Query
	 * @throws ParseException if thrown by a Lucene method, to be auto-converted
	 * to a LuceneSearchException
	 * @see #configureQuery()
	 */
	public final Query createQuery(Analyzer analyzer) throws ParseException {
		QueryParams params = configureQuery();
		String[] tokens = params.getToken();
		Query query = null;
		if( tokens.length==1 ) {
			QueryParser parser = new QueryParser(tokens[0],analyzer);
			query = parser.parse(params.getTextToSearch());
		} else {
			query = MultiFieldQueryParser.parse(new String[] { params.getTextToSearch() }, tokens, analyzer);
		}
		setQueryProperties(query);
		return query;
	}

	/**
	 * Subclasses must implement this method to configure with an instance
	 * of QueryParams which must contains informations about the tokens
	 * and the text to search.
	 * @return the parameters to construct a Query
	 */
	public abstract QueryParams configureQuery();

	/**
	 * Subclasses must overwrite this method to specify properties on the
	 * constructed query.
	 * @param query the constructed Query
	 */
	protected void setQueryProperties(Query query) {
		
	}

	/**
	 * This bean is used to contain the parameters needed to construct
	 * a query with a query parser.
	 */
	public static class QueryParams {
		private String[] token;
		private String textToSearch;

		public QueryParams(String token,String textToSearch) {
			this.token=new String[1];
			this.token[0]=token;
			this.textToSearch=textToSearch;
		}

		public QueryParams(String[] token,String textToSearch) {
			this.token=token;
			this.textToSearch=textToSearch;
		}

		public String[] getToken() {
			return token;
		}

		public String getTextToSearch() {
			return textToSearch;
		}
	}
}
