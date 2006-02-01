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
import org.springmodules.lucene.search.core.DefaultLuceneSearchTemplate;
import org.springmodules.lucene.search.factory.SearcherFactory;

/**
 * Base class for Lucene query objects that work with the Lucene API.
 * Encapsulates a SearcherFactory and a Lucene Analyzer.
 *
 * <p>Works with a LuceneSearchTemplate instance underneath. Lucene query
 * objects are an alternative to working with a LuceneSearchTemplate
 * directly.
 *
 * @author Thierry Templier
 * @see #setSearcherFactory(SearcherFactory)
 * @see #setAnalyzer(Analyzer)
 */
public abstract class LuceneSearchQuery {
	private DefaultLuceneSearchTemplate template = new DefaultLuceneSearchTemplate();

	/**
	 * Construct a new LuceneSearchQuery, given an SearcherFactory and an Analyzer
	 * to obtain a Searcher and an Analyzer to be used by the query.
	 * @param searcherFactory SearcherFactory to obtain Searcher
	 * @param analyzer Lucene analyzer used by the queries
	 */
	public LuceneSearchQuery(SearcherFactory searcherFactory,Analyzer analyzer) {
		this.template.setSearcherFactory(searcherFactory);
		this.template.setAnalyzer(analyzer);
	}

	/**
	 * @return the LuceneSearchTemplate instance to use.
	 */
	public DefaultLuceneSearchTemplate getTemplate() {
		return template;
	}

	/**
	 * Set the default Lucene Analyzer used by the queries.
	 */
	public void setAnalyzer(Analyzer analyzer) {
		if( template==null ) {
			throw new IllegalArgumentException("template must not be null");
		}
		this.template.setAnalyzer(analyzer);
	}

	/**
	 * Set the SearcherFactory to obtain Searcher.
	 */
	public void setSearcherFactory(SearcherFactory factory) {
		if( template==null ) {
			throw new IllegalArgumentException("template must not be null");
		}
		this.template.setSearcherFactory(factory);
	}

	/**
	 * Subclasses must implement this method to make the search using the
	 * configured SearcherFactory and Analyzer
	 * @param textToSearch the text query to search
	 * @return the list of results
	 */
	public abstract List search(String textToSearch);
}
