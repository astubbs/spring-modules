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

package org.springmodules.lucene.index.transaction.compensation;

import java.io.IOException;
import java.util.Collection;

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
import org.springmodules.lucene.index.core.ReaderCallback;
import org.springmodules.lucene.index.factory.IndexFactory;
import org.springmodules.lucene.index.factory.IndexReaderFactoryUtils;
import org.springmodules.lucene.index.factory.LuceneIndexReader;
import org.springmodules.lucene.index.transaction.AbstractTransactionalLuceneIndexReader;
import org.springmodules.lucene.index.transaction.LuceneOperation;
import org.springmodules.lucene.index.transaction.LuceneRollbackSegment;
import org.springmodules.lucene.index.transaction.operation.LuceneOperationUtils;
import org.springmodules.lucene.search.factory.LuceneHits;
import org.springmodules.lucene.search.factory.LuceneSearcher;
import org.springmodules.lucene.search.factory.SearcherFactoryUtils;

/** 
 * @author Thierry Templier
 */
public class CompensationTransactionalLuceneIndexReader extends AbstractTransactionalLuceneIndexReader {

	private IndexFactory delegate;
	
	public CompensationTransactionalLuceneIndexReader(IndexFactory delegate, LuceneRollbackSegment rollbackSegment) {
		setDelegate(delegate);
		setRollbackSegment(rollbackSegment);
	}

	public void forceClose() {
	}
	
	private Object executeOnReader(ReaderCallback callback) {
		LuceneIndexReader indexReader = null;
		try {
			indexReader = IndexReaderFactoryUtils.getIndexReader(delegate, false);
			return callback.doWithReader(indexReader);
		} catch(Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			IndexReaderFactoryUtils.releaseIndexReader(delegate, indexReader, false);
		}
	}
	
	public void close() throws IOException {
		//Skip this operation
	}

	public Searcher createNativeSearcher() {
		// TODO Auto-generated method stub
		return null;
	}

	public LuceneSearcher createSearcher() {
		// TODO Auto-generated method stub
		return null;
	}

	public void deleteDocument(final int docNum) throws IOException {
		executeOnReader(new ReaderCallback() {
			public Object doWithReader(LuceneIndexReader reader) throws Exception {
				Document document = reader.document(docNum);
				reader.deleteDocument(docNum);
				LuceneOperation operation = LuceneOperationUtils.getDeleteDocumentOperation(document);
				getRollbackSegment().addCompensateOperation(operation);
				return null;
			}
		});
	}

	private Document[] getDeleteDocuments(Term term, LuceneIndexReader indexReader) {
		LuceneSearcher searcher = null;
		try {
			searcher = indexReader.createSearcher();
			LuceneHits hits = searcher.search(new TermQuery(term));
			int nbDocuments = hits.length();
			Document[] documents = new Document[nbDocuments];
			for(int i=0; i<nbDocuments; i++) {
				documents[i] = hits.doc(i);
			}
			return documents;
		} catch(Exception ex) {
			return null;
		} finally {
			SearcherFactoryUtils.closeSearcher(searcher);
		}
	}
	
	public int deleteDocuments(final Term term) throws IOException {
		Integer nb = (Integer)executeOnReader(new ReaderCallback() {
			public Object doWithReader(LuceneIndexReader reader) throws Exception {
				Document[] documents = getDeleteDocuments(term, reader);
				int nbDocuments = reader.deleteDocuments(term);
				LuceneOperation operation = LuceneOperationUtils.getDeleteDocumentsOperation(documents, term);
				getRollbackSegment().addCompensateOperation(operation);
				return new Integer(nbDocuments);
			}
		});
		return nb.intValue();
	}

	public Directory directory() {
		// TODO Auto-generated method stub
		return null;
	}

	public int docFreq(Term t) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	public Document document(final int n) throws IOException {
		return (Document)executeOnReader(new ReaderCallback() {
			public Object doWithReader(LuceneIndexReader reader) throws Exception {
				return reader.document(n);
			}
		});
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

	public long getVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean hasDeletions() {
		Boolean hasDeletions = (Boolean)executeOnReader(new ReaderCallback() {
			public Object doWithReader(LuceneIndexReader reader) throws Exception {
			return new Boolean(reader.hasDeletions());	
			}
		});
		return hasDeletions.booleanValue();
	}

	public boolean hasNorms(String field) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isCurrent() throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isDeleted(final int n) {
		Boolean hasDeletions = (Boolean)executeOnReader(new ReaderCallback() {
			public Object doWithReader(LuceneIndexReader reader) throws Exception {
			return new Boolean(reader.isDeleted(n));	
			}
		});
		return hasDeletions.booleanValue();
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
		// TODO Auto-generated method stub
		return 0;
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
		executeOnReader(new ReaderCallback() {
			public Object doWithReader(LuceneIndexReader reader) throws Exception {
				reader.undeleteAll();
				getRollbackSegment().removeCompensateOperationForDeleteDocuments();
				return null;
			}
		});
	}

	public IndexFactory getDelegate() {
		return delegate;
	}

	public void setDelegate(IndexFactory delegate) {
		this.delegate = delegate;
	}

}
