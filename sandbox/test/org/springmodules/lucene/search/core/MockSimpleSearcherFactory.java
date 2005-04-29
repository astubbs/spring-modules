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

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.HitCollector;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopFieldDocs;
import org.springmodules.lucene.search.factory.SearcherFactory;
import org.springmodules.lucene.search.factory.SimpleSearcherFactory;

/**
 * @author Thierry Templier
 */
public class MockSimpleSearcherFactory implements SearcherFactory,SearcherEvent {
	private SearcherFactory target;
	private boolean searcherClosed=false;

	public MockSimpleSearcherFactory(SimpleSearcherFactory searcherFactory) {
		this.target=searcherFactory;
	}

	public Searcher getSearcher() throws IOException {
		searcherClosed=false;
		return new MockSearcher(target.getSearcher(),this);
	}

	public boolean isSearcherClosed() {
		return searcherClosed;
	}

	public void searcherClosed() {
		searcherClosed=true;
	}
}
