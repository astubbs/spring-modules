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

package org.springmodules.lucene.index.transaction.cache;

import java.io.IOException;
import java.io.PrintStream;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Similarity;
import org.apache.lucene.store.Directory;
import org.springmodules.lucene.index.factory.LuceneIndexReader;
import org.springmodules.lucene.index.transaction.AbstractTransactionalLuceneIndexWriter;
import org.springmodules.lucene.index.transaction.LuceneRollbackSegment;
import org.springmodules.lucene.index.transaction.LuceneTransactionalIndexCache;
import org.springmodules.lucene.index.transaction.operation.LuceneAddOperation;

/** 
 * @author Thierry Templier
 */
public class CacheTransactionalLuceneIndexWriter extends AbstractTransactionalLuceneIndexWriter {
	
	private LuceneIndexReader delegate;
	private LuceneTransactionalIndexCache transactionalIndexCache;

	public CacheTransactionalLuceneIndexWriter(LuceneTransactionalIndexCache transactionalIndexCache,
													LuceneRollbackSegment rollbackSegment) {
		setTransactionalIndexCache(transactionalIndexCache);
		setRollbackSegment(rollbackSegment);
	}

	public void close() throws IOException {
	}

	public void addDocument(Document doc) throws IOException {
		System.out.println("> TransactionalLuceneIndexWriter.addDocument");
		addDocument(doc, null);
	}

	public void addDocument(Document doc, Analyzer analyzer) throws IOException {
		LuceneAddOperation operation = new LuceneAddOperation();
		operation.setDocuments(new Document[] { doc });
		operation.setAnalyzer(analyzer);
		getTransactionalIndexCache().addOperation(operation);
		getRollbackSegment().addCompensateOperation(operation);
	}

	public void addIndexes(Directory[] dirs) throws IOException {
		// TODO Auto-generated method stub
		
	}

	public void addIndexes(IndexReader[] readers) throws IOException {
		// TODO Auto-generated method stub
		
	}

	public int docCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Analyzer getAnalyzer() {
		// TODO Auto-generated method stub
		return null;
	}

	public long getCommitLockTimeout() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Directory getDirectory() {
		// TODO Auto-generated method stub
		return null;
	}

	public PrintStream getInfoStream() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getMaxBufferedDocs() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getMaxFieldLength() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getMaxMergeDocs() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getMergeFactor() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Similarity getSimilarity() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getTermIndexInterval() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean getUseCompoundFile() {
		// TODO Auto-generated method stub
		return false;
	}

	public long getWriteLockTimeout() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void optimize() throws IOException {
		// TODO Auto-generated method stub
		
	}

	public void setCommitLockTimeout(long commitLockTimeout) {
		// TODO Auto-generated method stub
		
	}

	public void setInfoStream(PrintStream infoStream) {
		// TODO Auto-generated method stub
		
	}

	public void setMaxBufferedDocs(int maxBufferedDocs) {
		// TODO Auto-generated method stub
		
	}

	public void setMaxFieldLength(int maxFieldLength) {
		// TODO Auto-generated method stub
		
	}

	public void setMaxMergeDocs(int maxMergeDocs) {
		// TODO Auto-generated method stub
		
	}

	public void setMergeFactor(int mergeFactor) {
		// TODO Auto-generated method stub
		
	}

	public void setSimilarity(Similarity similarity) {
		// TODO Auto-generated method stub
		
	}

	public void setTermIndexInterval(int interval) {
		// TODO Auto-generated method stub
		
	}

	public void setUseCompoundFile(boolean value) {
		// TODO Auto-generated method stub
		
	}

	public void setWriteLockTimeout(long writeLockTimeout) {
		// TODO Auto-generated method stub
		
	}

	protected LuceneTransactionalIndexCache getTransactionalIndexCache() {
		return transactionalIndexCache;
	}

	public void setTransactionalIndexCache(
			LuceneTransactionalIndexCache transactionalIndexCache) {
		this.transactionalIndexCache = transactionalIndexCache;
	}

	public LuceneIndexReader getDelegate() {
		return delegate;
	}

	public void setDelegate(LuceneIndexReader delegate) {
		this.delegate = delegate;
	}

}
