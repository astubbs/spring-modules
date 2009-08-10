package org.springmodules.lucene.search.factory;

import java.io.IOException;
import java.util.Iterator;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.Hits;

public class SimpleLuceneHits implements LuceneHits {
	private Hits hits;

	public SimpleLuceneHits(Hits hits) {
		this.hits = hits;
	}
	
	public Document doc(int n) throws IOException {
		return hits.doc(n);
	}

	public int id(int n) throws IOException {
		return hits.id(n);
	}

	public Iterator iterator() {
		return hits.iterator();
	}

	public int length() {
		return hits.length();
	}

	public float score(int n) throws IOException {
		return hits.score(n);
	}

}
