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
import java.util.Collection;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.index.TermPositions;
import org.apache.lucene.index.IndexReader.FieldOption;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.springmodules.lucene.index.factory.IndexReaderFactoryUtils;
import org.springmodules.lucene.index.factory.LuceneIndexReader;
import org.springmodules.lucene.index.transaction.AbstractTransactionalLuceneIndexReader;
import org.springmodules.lucene.index.transaction.LuceneOperation;
import org.springmodules.lucene.index.transaction.LuceneRollbackSegment;
import org.springmodules.lucene.index.transaction.LuceneTransactionalIndexCache;
import org.springmodules.lucene.index.transaction.operation.LuceneAddOperation;
import org.springmodules.lucene.index.transaction.operation.LuceneDeleteOperation;
import org.springmodules.lucene.index.transaction.operation.LuceneOperationUtils;
import org.springmodules.lucene.search.factory.LuceneHits;
import org.springmodules.lucene.search.factory.LuceneSearcher;
import org.springmodules.lucene.search.factory.SearcherFactoryUtils;

/** 
 * @author Thierry Templier
 */
public class CacheTransactionalLuceneIndexReader extends AbstractTransactionalLuceneIndexReader {
	private LuceneIndexReader delegate;
	private LuceneTransactionalIndexCache transactionalIndexCache;

	public CacheTransactionalLuceneIndexReader(LuceneIndexReader delegate,
						LuceneTransactionalIndexCache transactionalIndexCache,
						LuceneRollbackSegment rollbackSegment) {
		setDelegate(delegate);
		setTransactionalIndexCache(transactionalIndexCache);
		setRollbackSegment(rollbackSegment);
	}

	public void close() throws IOException {
		IndexReaderFactoryUtils.closeIndexReader(delegate);
	}

	public Searcher createNativeSearcher() {
		// TODO Auto-generated method stub
		return null;
	}

	public LuceneSearcher createSearcher() {
		return getDelegate().createSearcher();
	}

	public void deleteDocument(int docNum) throws IOException {
		Document document = getDelegate().document(docNum);
		LuceneOperation operation = LuceneOperationUtils.getDeleteDocumentOperation(document);
		getTransactionalIndexCache().addOperation(operation);
		getRollbackSegment().addCompensateOperation(operation);
	}
	
	public int deleteDocuments(Term term) throws IOException {
		LuceneSearcher searcher = null;
		int nbDocuments = 0;
		try {
			searcher = getDelegate().createSearcher();
			LuceneHits hits = searcher.search(new TermQuery(term));
			nbDocuments = hits.length();
			Document[] documents = new Document[nbDocuments];
			for(int i=0; i<nbDocuments; i++) {
				documents[i] = hits.doc(i);
			}
			LuceneOperation operation = LuceneOperationUtils.getDeleteDocumentsOperation(documents, term);
			System.out.println("documents: "+documents.length);
			getTransactionalIndexCache().addOperation(operation);
			getRollbackSegment().addCompensateOperation(operation);
		} finally {
			SearcherFactoryUtils.closeSearcher(searcher);
		}

		return nbDocuments;
	}

	/**
	 * @see LuceneIndexReader#directory()
	 */
	public Directory directory() {
		return getDelegate().directory();
	}

	public int docFreq(Term t) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	public Document document(int n) throws IOException {
		Document document = getDelegate().document(n);
		if( getTransactionalIndexCache().isDocumentDeleted(document) ) {
			return null;
		} else {
			return document;
		}
	}

	public Collection getFieldNames(FieldOption fldOption) {
		// TODO Auto-generated method stub
		return null;
	}

	public TermFreqVector getTermFreqVector(int docNumber, String field) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public TermFreqVector[] getTermFreqVectors(int docNumber) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see LuceneIndexReader#getVersion()
	 */
	public long getVersion() {
		return getDelegate().getVersion();
	}

	public boolean hasDeletions() {
		List operations = getTransactionalIndexCache().getOperationsByType(LuceneDeleteOperation.class);
		return (operations.size()>0);
	}

	/**
	 * @see LuceneIndexReader#hasNorms(String)
	 */
	public boolean hasNorms(String field) throws IOException {
		return getDelegate().hasNorms(field);
	}

	/**
	 * @see LuceneIndexReader#isCurrent()
	 */
	public boolean isCurrent() throws IOException {
		return getDelegate().isCurrent();
	}

	public boolean isDeleted(int n) {
		try {
			Document document = getDelegate().document(n);
			return (getTransactionalIndexCache().isDocumentDeleted(document));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	public int maxDoc() {
		// TODO Auto-generated method stub
		return 0;
	}

	public byte[] norms(String field) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public void norms(String field, byte[] bytes, int offset) throws IOException {
		// TODO Auto-generated method stub
		
	}

	public int numDocs() {
		int addedDocumentNumber = transactionalIndexCache.getOperationNumberByType(LuceneAddOperation.class);
		int deletedDocumentNumber = transactionalIndexCache.getOperationNumberByType(LuceneDeleteOperation.class);
		return getDelegate().numDocs() + addedDocumentNumber - deletedDocumentNumber;
	}

	public void setNorm(int doc, String field, byte value) throws IOException {
		// TODO Auto-generated method stub
		
	}

	public void setNorm(int doc, String field, float value) throws IOException {
		// TODO Auto-generated method stub
		
	}

	public TermDocs termDocs() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public TermDocs termDocs(Term term) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public TermPositions termPositions() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public TermPositions termPositions(Term term) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public TermEnum terms() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public TermEnum terms(Term t) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public void undeleteAll() throws IOException {
		getTransactionalIndexCache().removeAllDeleteOperations();
	}

	public LuceneTransactionalIndexCache getTransactionalIndexCache() {
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
