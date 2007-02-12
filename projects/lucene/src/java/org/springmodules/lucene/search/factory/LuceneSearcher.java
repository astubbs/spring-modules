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

package org.springmodules.lucene.search.factory;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.HitCollector;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Similarity;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopFieldDocs;
import org.apache.lucene.search.Weight;

/**
 * Interface representing the contract of the Lucene Searcher class. It
 * allows unit tests with this resource and improves management of this
 * kind of resources.
 * 
 * All the method of the Searcher class are present in this interface
 * and, so allow to make all the operations of this class. 
 *  
 * @author Thierry Templier
 * @see Searcher
 */
public interface LuceneSearcher {
	void close() throws IOException;
	Document doc(int i) throws IOException;
	int docFreq(Term term) throws IOException;
	int[] docFreqs(Term[] terms) throws IOException;
	Explanation explain(Query query, int doc) throws IOException;
	Explanation explain(Weight weight, int doc) throws IOException;
	Similarity getSimilarity();
	int maxDoc() throws IOException;
	Query rewrite(Query query) throws IOException;
	LuceneHits search(Query query) throws IOException;
	LuceneHits search(Query query, Filter filter) throws IOException;
	void search(Query query, Filter filter, HitCollector results) throws IOException;
	TopDocs search(Query query, Filter filter, int n) throws IOException;
	TopFieldDocs search(Query query, Filter filter, int n, Sort sort) throws IOException;
	LuceneHits search(Query query, Filter filter, Sort sort) throws IOException;
	void search(Query query, HitCollector results) throws IOException;
	LuceneHits search(Query query, Sort sort) throws IOException;
	void search(Weight weight, Filter filter, HitCollector results) throws IOException;
	TopDocs search(Weight weight, Filter filter, int n) throws IOException;
	TopFieldDocs search(Weight weight, Filter filter, int n, Sort sort) throws IOException;
	void setSimilarity(Similarity similarity);
	IndexReader getIndexReader();
}
