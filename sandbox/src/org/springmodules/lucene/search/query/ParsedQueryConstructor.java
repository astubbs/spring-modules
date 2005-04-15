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
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;

/**
 * This class is the dedicated implements of the QueryConstructor
 * interface for the parsed query using an Lucene analyzer.
 * 
 * @author Thierry Templier
 */
public abstract class ParsedQueryConstructor implements QueryConstructor {

	/**
	 * @see org.springmodules.lucene.search.query.QueryConstructor#constructQuery(org.apache.lucene.analysis.Analyzer)
	 */
	public final Query constructQuery(Analyzer analyzer) throws ParseException {
		QueryParams params=configureQuery();
		String[] tokens=params.getToken();
		Query query=null;
		if( tokens.length==1 ) {
			QueryParser parser=new QueryParser(tokens[0],analyzer);
			query=parser.parse(params.getTextToSearch());
		} else {
			query=MultiFieldQueryParser.parse(params.getTextToSearch(),tokens,analyzer);
		}
		setQueryProperties(query);
		return query;
	}

	/**
	 * This method is used to specify the parameters to use get a query
	 * with a query parser.
	 * 
	 * @return the query parameters
	 */
	public abstract QueryParams configureQuery();

	/**
	 * This method must be used to set properties on the
	 * constructed query.
	 * 
	 * @param query the query
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
