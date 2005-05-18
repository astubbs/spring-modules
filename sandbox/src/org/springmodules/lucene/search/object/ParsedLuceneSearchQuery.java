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
import org.springmodules.lucene.search.factory.SearcherFactory;
import org.springmodules.lucene.search.query.ParsedQueryCreator;
import org.springmodules.lucene.search.query.ParsedQueryCreator.QueryParams;

/**
 * @author Thierry Templier
 */
public abstract class ParsedLuceneSearchQuery extends LuceneSearchQuery {

	public ParsedLuceneSearchQuery(SearcherFactory searcherFactory,Analyzer analyzer) {
		super(searcherFactory,analyzer);
	}

	/**
	 * @return
	 */
	protected abstract QueryParams configureSearchQuery(String textToSearch);

	/**
	 * @param id
	 * @param document
	 * @param score
	 * @return
	 */
	protected abstract Object extractResultHit(int id, Document document, float score);

	/**
	 * @see org.springmodules.lucene.search.object.LuceneSearchQuery#search(java.lang.String)
	 */
	public final List search(String textToSearch) {
		return getTemplate().search(new ParsedQueryConstructorImpl(textToSearch),
														new HitExtractorImpl());
	}

	protected class ParsedQueryConstructorImpl extends ParsedQueryCreator {
		private String textToSearch;

		public ParsedQueryConstructorImpl(String textToSearch) {
			this.textToSearch=textToSearch;
		}

		public QueryParams configureQuery() {
			return configureSearchQuery(textToSearch);
		}
	}

	protected class HitExtractorImpl implements HitExtractor {
		public Object mapHit(int id, Document document, float score) {
			return extractResultHit(id,document,score);
		}
	}
}
