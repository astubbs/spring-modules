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
 * Interface representing the contract of the Lucene IndexWriter class. It
 * allows unit tests with this resource and improves management of this
 * kind of resources.
 * 
 * All the method of the IndexWriter class are present in this interface
 * and, so allow to make all the operations of this class. 
 *  
 * @author Thierry Templier
 * @see IndexWriter
 */
public interface LuceneIndexWriter {

	/**
	 * @see IndexWriter#addDocument(Document)
	 * @throws IOException
	 */
	void addDocument(Document doc) throws IOException;

	/**
	 * @see IndexWriter#addDocument(Document, Analyzer)
	 * @throws IOException
	 */
	void addDocument(Document doc, Analyzer analyzer) throws IOException;

	/**
	 * @see IndexWriter#addIndexes(Directory[])
	 * @throws IOException
	 */
	void addIndexes(Directory[] dirs) throws IOException;

	/**
	 * @see IndexWriter#addIndexes(IndexReader[])
	 * @throws IOException
	 */
	void addIndexes(IndexReader[] readers) throws IOException;

	/**
	 * @see IndexWriter#close()
	 * @throws IOException
	 */
	void close() throws IOException;

	/**
	 * @see IndexWriter#docCount()
	 */
	int docCount();

	/**
	 * @see IndexWriter#getAnalyzer()
	 */
	Analyzer getAnalyzer();

	/**
	 * @see IndexWriter#getCommitLockTimeout()
	 */
	long getCommitLockTimeout();

	/**
	 * @see IndexWriter#getDirectory()
	 */
	Directory getDirectory();

	/**
	 * @see IndexWriter#getInfoStream()
	 */
	PrintStream getInfoStream();

	/**
	 * @see IndexWriter#getMaxBufferedDocs()
	 */
	int getMaxBufferedDocs();

	/**
	 * @see IndexWriter#getMaxFieldLength()
	 */
	int getMaxFieldLength();

	/**
	 * @see IndexWriter#getMaxMergeDocs()
	 */
	int getMaxMergeDocs();

	/**
	 * @see IndexWriter#getMergeFactor()
	 */
	int getMergeFactor();

	/**
	 * @see IndexWriter#getSimilarity()
	 */
	Similarity getSimilarity();
	
	/**
	 * @see IndexWriter#getTermIndexInterval()
	 */
	int getTermIndexInterval();
	
	/**
	 * @see IndexWriter#getUseCompoundFile()
	 */
	boolean getUseCompoundFile();
	
	/**
	 * @see IndexWriter#getWriteLockTimeout()
	 */
	long getWriteLockTimeout();
	
	/**
	 * @see IndexWriter#optimize()
	 * @see IOException
	 */
	void optimize() throws IOException;
	
	/**
	 * @see IndexWriter#setCommitLockTimeout(long)
	 */
	void setCommitLockTimeout(long commitLockTimeout);
	
	/**
	 * @see IndexWriter#setInfoStream(PrintStream)
	 */
	void setInfoStream(PrintStream infoStream);
	
	/**
	 * @see IndexWriter#setMaxBufferedDocs(int)
	 */
	void setMaxBufferedDocs(int maxBufferedDocs);
	
	/**
	 * @see IndexWriter#setMaxFieldLength(int)
	 */
	void setMaxFieldLength(int maxFieldLength);
	
	/**
	 * @see IndexWriter#setMaxMergeDocs(int)
	 */
	void setMaxMergeDocs(int maxMergeDocs);
	
	/**
	 * @see IndexWriter#setMergeFactor(int)
	 */
	void setMergeFactor(int mergeFactor);
	
	/**
	 * @see IndexWriter#setSimilarity(Similarity)
	 */
	void setSimilarity(Similarity similarity);
	
	/**
	 * @see IndexWriter#setTermIndexInterval(Similarity)
	 */
	void setTermIndexInterval(int interval);
	
	/**
	 * @see IndexWriter#setUseCompoundFile(boolean)
	 */
	void setUseCompoundFile(boolean value);
	
	/**
	 * @see IndexWriter#setWriteLockTimeout(long)
	 */
	void setWriteLockTimeout(long writeLockTimeout);
}
