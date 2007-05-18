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

package org.springmodules.lucene.index.factory;

import java.io.IOException;
import java.io.PrintStream;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.Similarity;
import org.apache.lucene.store.Directory;

/**
 * Simple implementation of the {@link LuceneIndexWriter} interface
 * in order to wrap an instance of IndexWriter and delegate method
 * calls to it.
 * 
 * @author Thierry Templier
 */
public class SimpleLuceneIndexWriter implements LuceneIndexWriter {
	private IndexWriter indexWriter;

	/**
	 * Create a new SimpleLuceneIndexWriter.
	 * 
	 * @param indexWriter an instance of IndexWriter to wrap
	 */
	public SimpleLuceneIndexWriter(IndexWriter indexWriter) {
		this.indexWriter = indexWriter;
	}

	/**
	 * @see LuceneIndexWriter#addDocument(Document, Analyzer)
	 */
	public void addDocument(Document doc, Analyzer analyzer) throws IOException {
		indexWriter.addDocument(doc, analyzer);
	}

	/**
	 * @see LuceneIndexWriter#addDocument(Document)
	 */
	public void addDocument(Document doc) throws IOException {
		indexWriter.addDocument(doc);
	}

	/**
	 * @see LuceneIndexWriter#addIndexes(Directory[])
	 */
	public void addIndexes(Directory[] dirs) throws IOException {
		indexWriter.addIndexes(dirs);
	}

	/**
	 * @see LuceneIndexWriter#addIndexes(IndexReader[])
	 */
	public void addIndexes(IndexReader[] readers) throws IOException {
		indexWriter.addIndexes(readers);
	}

	/**
	 * @see LuceneIndexWriter#close()
	 */
	public void close() throws IOException {
		indexWriter.close();
	}

	/**
	 * @see LuceneIndexWriter#docCount()
	 */
	public int docCount() {
		return indexWriter.docCount();
	}

	/**
	 * @see LuceneIndexWriter#equals(Object)
	 */
	public boolean equals(Object obj) {
		return indexWriter.equals(obj);
	}

	/**
	 * @see LuceneIndexWriter#getAnalyzer()
	 */
	public Analyzer getAnalyzer() {
		return indexWriter.getAnalyzer();
	}

	/**
	 * @see LuceneIndexWriter#getCommitLockTimeout()
	 */
	public long getCommitLockTimeout() {
		return indexWriter.getCommitLockTimeout();
	}

	/**
	 * @see LuceneIndexWriter#getDirectory()
	 */
	public Directory getDirectory() {
		return indexWriter.getDirectory();
	}

	/**
	 * @see LuceneIndexWriter#getInfoStream()
	 */
	public PrintStream getInfoStream() {
		return indexWriter.getInfoStream();
	}

	/**
	 * @see LuceneIndexWriter#getMaxBufferedDocs()
	 */
	public int getMaxBufferedDocs() {
		return indexWriter.getMaxBufferedDocs();
	}

	/**
	 * @see LuceneIndexWriter#getMaxFieldLength()
	 */
	public int getMaxFieldLength() {
		return indexWriter.getMaxFieldLength();
	}

	/**
	 * @see LuceneIndexWriter#getMaxMergeDocs()
	 */
	public int getMaxMergeDocs() {
		return indexWriter.getMaxMergeDocs();
	}

	/**
	 * @see LuceneIndexWriter#getMergeFactor()
	 */
	public int getMergeFactor() {
		return indexWriter.getMergeFactor();
	}

	/**
	 * @see LuceneIndexWriter#getSimilarity()
	 */
	public Similarity getSimilarity() {
		return indexWriter.getSimilarity();
	}

	/**
	 * @see LuceneIndexWriter#getTermIndexInterval()
	 */
	public int getTermIndexInterval() {
		return indexWriter.getTermIndexInterval();
	}

	/**
	 * @see LuceneIndexWriter#getUseCompoundFile()
	 */
	public boolean getUseCompoundFile() {
		return indexWriter.getUseCompoundFile();
	}

	/**
	 * @see LuceneIndexWriter#getWriteLockTimeout()
	 */
	public long getWriteLockTimeout() {
		return indexWriter.getWriteLockTimeout();
	}

	/**
	 * @see LuceneIndexWriter#hashCode()
	 */
	public int hashCode() {
		return indexWriter.hashCode();
	}

	/**
	 * @see LuceneIndexWriter#optimize()
	 */
	public void optimize() throws IOException {
		indexWriter.optimize();
	}

	/**
	 * @see LuceneIndexWriter#setInfoStream(PrintStream)
	 */
	public void setInfoStream(PrintStream infoStream) {
		indexWriter.setInfoStream(infoStream);
	}

	/**
	 * @see LuceneIndexWriter#setMaxBufferedDocs(int)
	 */
	public void setMaxBufferedDocs(int maxBufferedDocs) {
		indexWriter.setMaxBufferedDocs(maxBufferedDocs);
	}

	/**
	 * @see LuceneIndexWriter#setMaxFieldLength(int)
	 */
	public void setMaxFieldLength(int maxFieldLength) {
		indexWriter.setMaxFieldLength(maxFieldLength);
	}

	/**
	 * @see LuceneIndexWriter#setMaxMergeDocs(int)
	 */
	public void setMaxMergeDocs(int maxMergeDocs) {
		indexWriter.setMaxMergeDocs(maxMergeDocs);
	}

	/**
	 * @see LuceneIndexWriter#setMergeFactor(int)
	 */
	public void setMergeFactor(int mergeFactor) {
		indexWriter.setMergeFactor(mergeFactor);
	}

	/**
	 * @see LuceneIndexWriter#setSimilarity(Similarity)
	 */
	public void setSimilarity(Similarity similarity) {
		indexWriter.setSimilarity(similarity);
	}

	/**
	 * @see LuceneIndexWriter#setTermIndexInterval(int)
	 */
	public void setTermIndexInterval(int interval) {
		indexWriter.setTermIndexInterval(interval);
	}

	/**
	 * @see LuceneIndexWriter#setUseCompoundFile(boolean)
	 */
	public void setUseCompoundFile(boolean value) {
		indexWriter.setUseCompoundFile(value);
	}

	/**
	 * @see LuceneIndexWriter#setWriteLockTimeout(long)
	 */
	public void setWriteLockTimeout(long writeLockTimeout) {
		indexWriter.setWriteLockTimeout(writeLockTimeout);
	}

	/**
	 * @see LuceneIndexWriter#setCommitLockTimeout(long)
	 */
	public void setCommitLockTimeout(long commitLockTimeout) {
		indexWriter.setCommitLockTimeout(commitLockTimeout);
	}
}
