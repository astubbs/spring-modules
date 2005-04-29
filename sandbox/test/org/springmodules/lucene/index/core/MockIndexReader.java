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

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.index.TermPositions;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

/**
 * @author Thierry Templier
 */
public class MockIndexReader extends IndexReader {
	private IndexReader target;
	private IndexReaderEvent indexReaderEvent;

	public MockIndexReader(IndexReader indexReader,IndexReaderEvent indexReaderEvent) {
		super(new RAMDirectory());
		this.target=indexReader;
		this.indexReaderEvent=indexReaderEvent;
	}

	/**
	 * @see org.apache.lucene.index.IndexReader#directory()
	 */
	public Directory directory() {
		return target.directory();
	}

	/**
	 * @param arg0
	 * @return
	 * @throws java.io.IOException
	 */
	public int docFreq(Term arg0) throws IOException {
		return target.docFreq(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 * @throws java.io.IOException
	 */
	public Document document(int arg0) throws IOException {
		return target.document(arg0);
	}

	/**
	 * @return
	 * @throws java.io.IOException
	 */
	public Collection getFieldNames() throws IOException {
		return target.getFieldNames();
	}

	/**
	 * @param arg0
	 * @return
	 * @throws java.io.IOException
	 */
	public Collection getFieldNames(boolean arg0) throws IOException {
		return target.getFieldNames(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 */
	public Collection getIndexedFieldNames(boolean arg0) {
		return target.getIndexedFieldNames(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws java.io.IOException
	 */
	public TermFreqVector getTermFreqVector(int arg0, String arg1)
		throws IOException {
		return target.getTermFreqVector(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @return
	 * @throws java.io.IOException
	 */
	public TermFreqVector[] getTermFreqVectors(int arg0) throws IOException {
		return target.getTermFreqVectors(arg0);
	}

	/**
	 * @return
	 */
	public boolean hasDeletions() {
		boolean hasDeletions=target.hasDeletions();
		if( indexReaderEvent!=null ) {
			indexReaderEvent.indexReaderHasDeletions();
		}
		return hasDeletions;
	}

	/**
	 * @param arg0
	 * @return
	 */
	public boolean isDeleted(int id) {
		boolean isDeleted=target.isDeleted(id);
		if( indexReaderEvent!=null ) {
			indexReaderEvent.indexReaderIsDeleted(id);
		}
		return isDeleted;
	}

	/**
	 * @return
	 */
	public int maxDoc() {
		int maxDoc=target.maxDoc();
		if( indexReaderEvent!=null ) {
			indexReaderEvent.indexReaderMaxDoc();
		}
		return maxDoc;
	}

	/**
	 * @param arg0
	 * @return
	 * @throws java.io.IOException
	 */
	public byte[] norms(String arg0) throws IOException {
		return target.norms(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @throws java.io.IOException
	 */
	public void norms(String arg0, byte[] arg1, int arg2) throws IOException {
		target.norms(arg0, arg1, arg2);
	}

	/**
	 * @return
	 */
	public int numDocs() {
		int numDocs=target.numDocs();
		if( indexReaderEvent!=null ) {
			indexReaderEvent.indexReaderNumDocs();
		}
		return numDocs;
	}

	/**
	 * @see org.apache.lucene.index.IndexReader#setNorm(int, java.lang.String, float)
	 */
	public void setNorm(int arg0, String arg1, float arg2) throws IOException {
		target.setNorm(arg0, arg1, arg2);
	}

	/**
	 * @return
	 * @throws java.io.IOException
	 */
	public TermDocs termDocs() throws IOException {
		return target.termDocs();
	}

	/**
	 * @see org.apache.lucene.index.IndexReader#termDocs(org.apache.lucene.index.Term)
	 */
	public TermDocs termDocs(Term arg0) throws IOException {
		return target.termDocs(arg0);
	}

	/**
	 * @return
	 * @throws java.io.IOException
	 */
	public TermPositions termPositions() throws IOException {
		return target.termPositions();
	}

	/**
	 * @see org.apache.lucene.index.IndexReader#termPositions(org.apache.lucene.index.Term)
	 */
	public TermPositions termPositions(Term arg0) throws IOException {
		return target.termPositions(arg0);
	}

	/**
	 * @return
	 * @throws java.io.IOException
	 */
	public TermEnum terms() throws IOException {
		return target.terms();
	}

	/**
	 * @param arg0
	 * @return
	 * @throws java.io.IOException
	 */
	public TermEnum terms(Term arg0) throws IOException {
		return target.terms(arg0);
	}

	/**
	 * @see org.apache.lucene.index.IndexReader#doSetNorm(int, java.lang.String, byte)
	 */
	protected void doSetNorm(int arg0, String arg1, byte arg2) throws IOException {
	}

	/**
	 * @see org.apache.lucene.index.IndexReader#doDelete(int)
	 */
	protected void doDelete(int id) throws IOException {
		System.err.println("doDelete : "+id);
		if( indexReaderEvent!=null ) {
			indexReaderEvent.indexReaderDeleted(id);
		}
	}

	/**
	 * @see org.apache.lucene.index.IndexReader#doUndeleteAll()
	 */
	protected void doUndeleteAll() throws IOException {
		if( indexReaderEvent!=null ) {
			indexReaderEvent.indexReaderUndeletedAll();
		}
	}

	/**
	 * @see org.apache.lucene.index.IndexReader#doCommit()
	 */
	protected void doCommit() throws IOException {
	}

	/**
	 * @see org.apache.lucene.index.IndexReader#doClose()
	 */
	protected void doClose() throws IOException {
		target.close();
		if( indexReaderEvent!=null ) {
			indexReaderEvent.indexReaderClosed();
		}
	}

}
