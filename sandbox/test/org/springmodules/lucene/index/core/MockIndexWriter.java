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

package org.springmodules.lucene.index.core;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

/**
 * @author Thierry Templier
 */
public class MockIndexWriter extends IndexWriter {
	private IndexWriterCallListener indexWriterEvent;

	public MockIndexWriter(Directory directory,Analyzer analyzer,IndexWriterCallListener indexWriterEvent) throws IOException {
		super(directory,analyzer,false);
		this.indexWriterEvent=indexWriterEvent;
	}

	/**
	 * @see org.apache.lucene.index.IndexWriter#addDocument(org.apache.lucene.document.Document)
	 */
	public void addDocument(Document arg0) throws IOException {
		super.addDocument(arg0);
	}

	/**
	 * @see org.apache.lucene.index.IndexWriter#addDocument(org.apache.lucene.document.Document, org.apache.lucene.analysis.Analyzer)
	 */
	public void addDocument(Document arg0, Analyzer arg1) throws IOException {
		super.addDocument(arg0, arg1);
		if( indexWriterEvent!=null ) {
			indexWriterEvent.indexWriterAddDocument();
		}
	}

	/**
	 * @see org.apache.lucene.index.IndexWriter#addIndexes(org.apache.lucene.index.IndexReader[])
	 */
	public synchronized void addIndexes(IndexReader[] arg0)
		throws IOException {
		super.addIndexes(arg0);
	}

	/**
	 * @see org.apache.lucene.index.IndexWriter#addIndexes(org.apache.lucene.store.Directory[])
	 */
	public synchronized void addIndexes(Directory[] arg0) throws IOException {
		super.addIndexes(arg0);
	}

	/**
	 * @see org.apache.lucene.index.IndexWriter#close()
	 */
	public synchronized void close() throws IOException {
		super.close();
		if( indexWriterEvent!=null ) {
			indexWriterEvent.indexWriterClosed();
		}
	}

	/**
	 * @see org.apache.lucene.index.IndexWriter#docCount()
	 */
	public synchronized int docCount() {
		return super.docCount();
	}

	/**
	 * @see org.apache.lucene.index.IndexWriter#getAnalyzer()
	 */
	public Analyzer getAnalyzer() {
		return super.getAnalyzer();
	}

	/**
	 * @see org.apache.lucene.index.IndexWriter#getSimilarity()
	 */
	public Similarity getSimilarity() {
		return super.getSimilarity();
	}

	/**
	 * @see org.apache.lucene.index.IndexWriter#getUseCompoundFile()
	 */
	public boolean getUseCompoundFile() {
		return super.getUseCompoundFile();
	}

	/**
	 * @see org.apache.lucene.index.IndexWriter#optimize()
	 */
	public synchronized void optimize() throws IOException {
		super.optimize();
		if( indexWriterEvent!=null ) {
			indexWriterEvent.indexWriterOptimize();
		}
	}

	/**
	 * @see org.apache.lucene.index.IndexWriter#setSimilarity(org.apache.lucene.search.Similarity)
	 */
	public void setSimilarity(Similarity arg0) {
		super.setSimilarity(arg0);
	}

	/**
	 * @see org.apache.lucene.index.IndexWriter#setUseCompoundFile(boolean)
	 */
	public void setUseCompoundFile(boolean arg0) {
		super.setUseCompoundFile(arg0);
	}

}
