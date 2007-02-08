package org.springmodules.lucene.search.factory;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.HitCollector;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Similarity;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopFieldDocs;
import org.apache.lucene.search.Weight;

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
