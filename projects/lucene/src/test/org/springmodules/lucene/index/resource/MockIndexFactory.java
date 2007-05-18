package org.springmodules.lucene.index.resource;

import org.springmodules.lucene.index.factory.DelegatingIndexFactory;
import org.springmodules.lucene.index.factory.LuceneIndexReader;
import org.springmodules.lucene.index.factory.LuceneIndexWriter;

public class MockIndexFactory extends DelegatingIndexFactory {
	private int callNumberReader = 0;
	private int callNumberWriter = 0;
	
	public LuceneIndexReader getIndexReader() {
		System.out.println("############## getIndexReader");
		callNumberReader++;
		return super.getIndexReader();
	}

	public LuceneIndexWriter getIndexWriter() {
		System.out.println("############## getIndexWriter");
		callNumberWriter++;
		return super.getIndexWriter();
	}

	public int getCallNumberReader() {
		return callNumberReader;
	}

	public int getCallNumberWriter() {
		return callNumberWriter;
	}

}
