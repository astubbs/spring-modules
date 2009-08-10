package org.springmodules.lucene.search.factory;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
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
import org.apache.lucene.search.Weight;

public class SimpleLuceneSearcher implements LuceneSearcher {
	private Searcher searcher;

	public SimpleLuceneSearcher(Searcher searcher) {
		this.searcher = searcher;
	}

	public void close() throws IOException {
		searcher.close();
	}

	public Document doc(int i) throws IOException {
		return searcher.doc(i);
	}

	public int docFreq(Term term) throws IOException {
		return searcher.docFreq(term);
	}

	public int[] docFreqs(Term[] terms) throws IOException {
		return searcher.docFreqs(terms);
	}

	public Explanation explain(Query query, int doc) throws IOException {
		return searcher.explain(query, doc);
	}

	public Explanation explain(Weight weight, int doc) throws IOException {
		return searcher.explain(weight, doc);
	}

	public IndexReader getIndexReader() {
		// TODO: to be implemented
		return null;
	}

	public Similarity getSimilarity() {
		return searcher.getSimilarity();
	}

	public int maxDoc() throws IOException {
		return searcher.maxDoc();
	}

	public Query rewrite(Query query) throws IOException {
		return searcher.rewrite(query);
	}

	public LuceneHits search(Query query) throws IOException {
		Hits hits = searcher.search(query);
		return new SimpleLuceneHits(hits);
	}

	public LuceneHits search(Query query, Filter filter) throws IOException {
		Hits hits = searcher.search(query, filter);
		return new SimpleLuceneHits(hits);
	}

	public void search(Query query, Filter filter, HitCollector results) throws IOException {
		searcher.search(query, filter, results);
	}

	public TopDocs search(Query query, Filter filter, int n) throws IOException {
		return searcher.search(query, filter, n);
	}

	public TopFieldDocs search(Query query, Filter filter, int n, Sort sort) throws IOException {
		return searcher.search(query, filter, n, sort);
	}

	public LuceneHits search(Query query, Filter filter, Sort sort) throws IOException {
		Hits hits = searcher.search(query, filter, sort);
		return new SimpleLuceneHits(hits);
	}

	public void search(Query query, HitCollector results) throws IOException {
		searcher.search(query, results);
	}

	public LuceneHits search(Query query, Sort sort) throws IOException {
		Hits hits = searcher.search(query, sort);
		return new SimpleLuceneHits(hits);
	}

	public void search(Weight weight, Filter filter, HitCollector results) throws IOException {
		searcher.search(weight, filter, results);
	}

	public TopDocs search(Weight weight, Filter filter, int n) throws IOException {
		return searcher.search(weight, filter, n);
	}

	public TopFieldDocs search(Weight weight, Filter filter, int n, Sort sort) throws IOException {
		return searcher.search(weight, filter, n, sort);
	}

	public void setSimilarity(Similarity similarity) {
		searcher.setSimilarity(similarity);
	}
}
