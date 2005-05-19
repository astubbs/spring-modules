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

package org.springmodules.lucene.search.object;

import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.springmodules.lucene.search.core.HitExtractor;
import org.springmodules.lucene.search.core.ParsedQueryCreator;
import org.springmodules.lucene.search.core.ParsedQueryCreator.QueryParams;
import org.springmodules.lucene.search.factory.SearcherFactory;

/** 
 * Lucene query object that expects to create a Lucene query from a
 * string basing a QueryParser and extracts the results from Lucene Hits class.
 *
 * <p>Concrete subclasses must implement the abstract
 * <code>configureSearchQuery(String)</code> and
 * <code>extractResultHit(int , Document , float)</code> methods, to
 * configure an query and extracts datas from the results, respectively.
 *
 * @author Thierry Templier
 * @see #configureSearchQuery(String)
 * @see #extractResultHit(int, Document, float)
 */
public abstract class ParsedLuceneSearchQuery extends LuceneSearchQuery {

	/**
	 * Construct a new ParsedLuceneSearchQuery, given an SearcherFactory and an Analyzer
	 * to obtain a Searcher and an Analyzer to be used by the query.
	 * @param searcherFactory SearcherFactory to obtain Searcher
	 * @param analyzer Lucene analyzer used by the queries
	 */
	public ParsedLuceneSearchQuery(SearcherFactory searcherFactory,Analyzer analyzer) {
		super(searcherFactory,analyzer);
	}

	/**
	 * Subclasses must implement this method to configure a Lucene query
	 * from a string object passed into the <code>search</code> method.
	 * @param textToSearch the passed-in text to construct a query
	 * @return the Queryparams
	 * @see #search(String)
	 * @see ParsedQueryCreator.QueryParams
	 */
	protected abstract QueryParams configureSearchQuery(String textToSearch);

	/**
	 * Subclasses must implement this method to extract datas from the search
	 * results into a result object which will be added to the result list for
	 * the <code>execute</code> method.
	 * @param id the internal document identifier
	 * @param document the document
	 * @param score the score of the document in the search
	 * @return the result object
	 * @see #search(String)
	 */
	protected abstract Object extractResultHit(int id, Document document, float score);

	/**
	 * Execute the search encapsulated by this query object.
	 * @param textToSearch the text to search, to be used to configure a Query constructed
	 * with a QueryParser with the <code>configureSearchQuery</code> method
	 * @return the result datas extracted with the <code>extractResultHit</code> method
	 * @throws LuceneSearchException if there is any problem
	 * @see #constructSearchQuery(String)
	 * @see #extractResultHit(int, Document, float)
	 */
	public final List search(String textToSearch) {
		return getTemplate().search(new ParsedQueryConstructorImpl(textToSearch),
														new HitExtractorImpl());
	}

	/**
	 * Implementation of ParsedQueryCreator that calls the enclosing
	 * class's <code>configureSearchQuery</code> method.
	 */
	protected class ParsedQueryConstructorImpl extends ParsedQueryCreator {
		private String textToSearch;

		public ParsedQueryConstructorImpl(String textToSearch) {
			this.textToSearch=textToSearch;
		}

		public QueryParams configureQuery() {
			return configureSearchQuery(textToSearch);
		}
	}

	/**
	 * Implementation of HitExtractor that calls the enclosing
	 * class's <code>extractResultHit</code> method.
	 */
	protected class HitExtractorImpl implements HitExtractor {
		public Object mapHit(int id, Document document, float score) {
			return extractResultHit(id,document,score);
		}
	}
}
