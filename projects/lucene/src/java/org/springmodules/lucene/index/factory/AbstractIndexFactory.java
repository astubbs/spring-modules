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

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;

/**
 * <p>This abstract index factory allows every sub classes to set the tuning
 * parameters of an IndexWriter instance.
 * 
 * <p>Before return the IndexWriter instance, the getWriter method of the factory
 * must call the setIndexWriterParameters method in order to set the tuning
 * parameters on the instance.
 * 
 * <p>The tuning parameters are set to default values defined as constants in the
 * class (DEFAULT_MAX_BUFFERED_DOCS, DEFAULT_MAX_FIELD_LENGTH, DEFAULT_MAX_MERGE_DOCS,
 * DEFAULT_MERGE_FACTOR, DEFAULT_TERM_INDEX_INTERVAL and DEFAULT_WRITE_LOCK_TIMEOUT).
 * 
 * @author Thierry Templier
 */
public abstract class AbstractIndexFactory {
	public final static int DEFAULT_MAX_BUFFERED_DOCS = 10;
	public final static int DEFAULT_MAX_FIELD_LENGTH = 10000;
	public final static int DEFAULT_MAX_MERGE_DOCS = Integer.MAX_VALUE;
	public final static int DEFAULT_MERGE_FACTOR = 10;
	public final static int DEFAULT_TERM_INDEX_INTERVAL = 128; 
	public final static int DEFAULT_WRITE_LOCK_TIMEOUT = 1000; 

	private Directory directory;
	private Analyzer analyzer;

	private boolean useCompoundFile = false;
	private int maxBufferedDocs = DEFAULT_MAX_BUFFERED_DOCS;
	private int maxFieldLength = DEFAULT_MAX_FIELD_LENGTH;
	private int maxMergeDocs = DEFAULT_MAX_MERGE_DOCS;
	private int mergeFactor = DEFAULT_MERGE_FACTOR;
	private int termIndexInterval = DEFAULT_TERM_INDEX_INTERVAL;
	private int writeLockTimeout = DEFAULT_WRITE_LOCK_TIMEOUT;

	/**
	 * Set the Lucene Directory to be used.
	 */
	public void setDirectory(Directory directory) {
		this.directory = directory;
	}

	/**
	 * Return the Lucene Directory used by IndexFactory.
	 */
	public Directory getDirectory() {
		return directory;
	}

	/**
	 * Set the Lucene Analyzer to be used.
	 */
	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}

	/**
	 * Return the Lucene Analyzer used by index factories.
	 */
	public Analyzer getAnalyzer() {
		return analyzer;
	}

	/**
	 * Sets all the values of parameters specified on the IndexWriter
	 * specified. This method must be called by all concrete IndexFactory
	 * before to return instance of the LuceneIndexWriter using the
	 * {@link IndexFactory#getIndexWriter()} method.  
	 * 
	 * @param writer the IndexWriter to configure
	 * @see IndexFactory#getIndexWriter()
	 * @see SimpleIndexFactory#getIndexWriter()
	 */
	protected void setIndexWriterParameters(IndexWriter writer) {
		writer.setUseCompoundFile(useCompoundFile);
		writer.setMaxBufferedDocs(maxBufferedDocs);
		writer.setMaxFieldLength(maxFieldLength);
		writer.setMaxMergeDocs(maxMergeDocs);
		writer.setMergeFactor(mergeFactor);
		writer.setTermIndexInterval(termIndexInterval);
		writer.setWriteLockTimeout(writeLockTimeout);
	}

	/**
	 * Return the value of maxBufferedDocs parameter by the IndexWriter.
	 */
	public int getMaxBufferedDocs() {
		return maxBufferedDocs;
	}

	/**
	 * Return the value of maxFieldLength parameter by the IndexWriter.
	 */
	public int getMaxFieldLength() {
		return maxFieldLength;
	}

	/**
	 * Return the value of maxMergeDocs parameter by the IndexWriter.
	 */
	public int getMaxMergeDocs() {
		return maxMergeDocs;
	}

	/**
	 * Return the value of mergeFactor parameter by the IndexWriter.
	 */
	public int getMergeFactor() {
		return mergeFactor;
	}

	/**
	 * Return the value of termIndexInterval parameter by the IndexWriter.
	 */
	public int getTermIndexInterval() {
		return termIndexInterval;
	}

	/**
	 * Return the value of writeLockTimeout parameter by the IndexWriter.
	 */
	public int getWriteLockTimeout() {
		return writeLockTimeout;
	}

	/**
	 * Return the value of maxBufferedDocs parameter by the IndexWriter.
	 */
	public boolean isUseCompoundFile() {
		return useCompoundFile;
	}

	/**
	 * Set the value of maxBufferedDocs parameter to use with the IndexWriter.
	 */
	public void setMaxBufferedDocs(int i) {
		maxBufferedDocs = i;
	}
	
	/**
	 * Set the value of maxFieldLength parameter to use with the IndexWriter.
	 */
	public void setMaxFieldLength(int i) {
		maxFieldLength = i;
	}

	/**
	 * Set the value of maxMergeDocs parameter to use with the IndexWriter.
	 */
	public void setMaxMergeDocs(int i) {
		maxMergeDocs = i;
	}

	/**
	 * Set the value of mergeFactor parameter to use with the IndexWriter.
	 */
	public void setMergeFactor(int i) {
		mergeFactor = i;
	}

	/**
	 * Set the value of termIndexInterval parameter to use with the IndexWriter.
	 */
	public void setTermIndexInterval(int i) {
		termIndexInterval = i;
	}

	/**
	 * Set the value of writeLockTimeout parameter to use with the IndexWriter.
	 */
	public void setWriteLockTimeout(int i) {
		writeLockTimeout = i;
	}

	/**
	 * Set the value of useCompoundFile parameter to use with the IndexWriter.
	 */
	public void setUseCompoundFile(boolean b) {
		useCompoundFile = b;
	}

}
