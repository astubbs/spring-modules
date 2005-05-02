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
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Similarity;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopFieldDocs;

/**
 * @author Thierry Templier
 */
public class MockSearcher extends Searcher {
	private Searcher target;
	private SearcherCallListener searcherEvent;

	public MockSearcher(Searcher searcher,SearcherCallListener searcherEvent) {
		this.target=searcher;
		this.searcherEvent=searcherEvent;
	}

	/**
	 * @throws java.io.IOException
	 */
	public void close() throws IOException {
		target.close();
		if( searcherEvent!=null ) {
			searcherEvent.searcherClosed();
		}
	}

	/**
	 * @param arg0
	 * @return
	 * @throws java.io.IOException
	 */
	public Document doc(int arg0) throws IOException {
		return target.doc(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 * @throws java.io.IOException
	 */
	public int docFreq(Term arg0) throws IOException {
		return target.docFreq(arg0);
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		return target.equals(obj);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws java.io.IOException
	 */
	public Explanation explain(Query arg0, int arg1) throws IOException {
		return target.explain(arg0, arg1);
	}

	/**
	 * @see org.apache.lucene.search.Searcher#getSimilarity()
	 */
	public Similarity getSimilarity() {
		return target.getSimilarity();
	}

	/**
	 * @return
	 * @throws java.io.IOException
	 */
	public int maxDoc() throws IOException {
		return target.maxDoc();
	}

	/**
	 * @param arg0
	 * @return
	 * @throws java.io.IOException
	 */
	public Query rewrite(Query arg0) throws IOException {
		return target.rewrite(arg0);
	}

	/**
	 * @see org.apache.lucene.search.Searcher#search(org.apache.lucene.search.Query, org.apache.lucene.search.Filter)
	 */
	public Hits search(Query arg0, Filter arg1) throws IOException {
		return target.search(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @return
	 * @throws java.io.IOException
	 */
	public TopDocs search(Query arg0, Filter arg1, int arg2)
		throws IOException {
		return target.search(arg0, arg1, arg2);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @return
	 * @throws java.io.IOException
	 */
	public TopFieldDocs search(Query arg0, Filter arg1, int arg2, Sort arg3)
		throws IOException {
		return target.search(arg0, arg1, arg2, arg3);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @throws java.io.IOException
	 */
	public void search(Query arg0, Filter arg1, HitCollector arg2)
		throws IOException {
		target.search(arg0, arg1, arg2);
	}

	/**
	 * @see org.apache.lucene.search.Searcher#search(org.apache.lucene.search.Query, org.apache.lucene.search.Filter, org.apache.lucene.search.Sort)
	 */
	public Hits search(Query arg0, Filter arg1, Sort arg2) throws IOException {
		return target.search(arg0, arg1, arg2);
	}

	/**
	 * @see org.apache.lucene.search.Searcher#search(org.apache.lucene.search.Query, org.apache.lucene.search.HitCollector)
	 */
	public void search(Query arg0, HitCollector arg1) throws IOException {
		target.search(arg0, arg1);
	}

	/**
	 * @see org.apache.lucene.search.Searcher#search(org.apache.lucene.search.Query, org.apache.lucene.search.Sort)
	 */
	public Hits search(Query arg0, Sort arg1) throws IOException {
		return target.search(arg0, arg1);
	}

	/**
	 * @see org.apache.lucene.search.Searcher#setSimilarity(org.apache.lucene.search.Similarity)
	 */
	public void setSimilarity(Similarity arg0) {
		target.setSimilarity(arg0);
	}

}
