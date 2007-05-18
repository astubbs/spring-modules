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
import java.util.Collection;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.index.TermPositions;
import org.apache.lucene.index.IndexReader.FieldOption;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.store.Directory;
import org.springmodules.lucene.search.factory.LuceneSearcher;
import org.springmodules.lucene.search.factory.SimpleLuceneSearcher;

/**
 * Simple implementation of the {@link LuceneIndexReader} interface
 * in order to wrap an instance of IndexReader and delegate method
 * calls to it.
 * 
 * @author Thierry Templier
 * @see org.apache.lucene.index.IndexReader
 */
public class SimpleLuceneIndexReader implements LuceneIndexReader {
	private IndexReader indexReader;

	/**
	 * Create a new SimpleLuceneIndexReader.
	 * 
	 * @param indexReader an instance of IndexReader to wrap
	 */
	public SimpleLuceneIndexReader(IndexReader indexReader) {
		this.indexReader = indexReader;
	}
	
	/**
	 * @see LuceneIndexReader#directory()
	 */
	public Directory directory() {
		return indexReader.directory();
	}

	/**
	 * @see LuceneIndexReader#docFreq(Term)
	 */
	public int docFreq(Term term) throws IOException {
		return indexReader.docFreq(term);
	}

	/**
	 * @see LuceneIndexReader#document(int)
	 */
	public Document document(int n) throws IOException {
		return indexReader.document(n);
	}

	/**
	 * @see LuceneIndexReader#equals(Object)
	 */
	public boolean equals(Object obj) {
		return indexReader.equals(obj);
	}

	/**
	 * @see LuceneIndexReader#getFieldNames(FieldOption)
	 */
	public Collection getFieldNames(FieldOption fldOption) {
		return indexReader.getFieldNames(fldOption);
	}

	/**
	 * @see LuceneIndexReader#getTermFreqVector(int, String)
	 */
	public TermFreqVector getTermFreqVector(int docNumber, String field) throws IOException {
		return indexReader.getTermFreqVector(docNumber, field);
	}

	/**
	 * @see LuceneIndexReader#getTermFreqVectors(int)
	 */
	public TermFreqVector[] getTermFreqVectors(int docNumber) throws IOException {
		return indexReader.getTermFreqVectors(docNumber);
	}

	/**
	 * @see LuceneIndexReader#getVersion()
	 */
	public long getVersion() {
		return indexReader.getVersion();
	}

	/**
	 * @see LuceneIndexReader#hasDeletions()
	 */
	public boolean hasDeletions() {
		return indexReader.hasDeletions();
	}

	/**
	 * @see LuceneIndexReader#hashCode()
	 */
	public int hashCode() {
		return indexReader.hashCode();
	}

	/**
	 * @see LuceneIndexReader#hasNorms(String)
	 */
	public boolean hasNorms(String field) throws IOException {
		return indexReader.hasNorms(field);
	}

	/**
	 * @see LuceneIndexReader#isCurrent()
	 */
	public boolean isCurrent() throws IOException {
		return indexReader.isCurrent();
	}

	/**
	 * @see LuceneIndexReader#isDeleted(int)
	 */
	public boolean isDeleted(int n) {
		return indexReader.isDeleted(n);
	}

	/**
	 * @see LuceneIndexReader#maxDoc()
	 */
	public int maxDoc() {
		return indexReader.maxDoc();
	}

	/**
	 * @see LuceneIndexReader#norms(String, byte[], int)
	 */
	public void norms(String field, byte[] bytes, int offset) throws IOException {
		indexReader.norms(field, bytes, offset);
	}

	/**
	 * @see LuceneIndexReader#norms(String)
	 */
	public byte[] norms(String field) throws IOException {
		return indexReader.norms(field);
	}

	/**
	 * @see LuceneIndexReader#numDocs()
	 */
	public int numDocs() {
		return indexReader.numDocs();
	}

	/**
	 * @see LuceneIndexReader#setNorm(int, String, float)
	 */
	public void setNorm(int doc, String field, float value) throws IOException {
		indexReader.setNorm(doc, field, value);
	}

	/**
	 * @see LuceneIndexReader#termDocs()
	 */
	public TermDocs termDocs() throws IOException {
		return indexReader.termDocs();
	}

	/**
	 * @see LuceneIndexReader#termDocs()
	 */
	public TermDocs termDocs(Term term) throws IOException {
		return indexReader.termDocs(term);
	}

	/**
	 * @see LuceneIndexReader#termPositions()
	 */
	public TermPositions termPositions() throws IOException {
		return indexReader.termPositions();
	}

	/**
	 * @see LuceneIndexReader#termPositions(Term)
	 */
	public TermPositions termPositions(Term term) throws IOException {
		return indexReader.termPositions(term);
	}

	/**
	 * @see LuceneIndexReader#terms()
	 */
	public TermEnum terms() throws IOException {
		return indexReader.terms();
	}

	/**
	 * @see LuceneIndexReader#terms(Term)
	 */
	public TermEnum terms(Term term) throws IOException {
		return indexReader.terms(term);
	}

	/**
	 * @see LuceneIndexReader#close()
	 */
	public void close() throws IOException {
		indexReader.close();
	}

	/**
	 * @see LuceneIndexReader#createSearcher()
	 */
	public LuceneSearcher createSearcher() {
		//TODO: to be implemented
		//throw new IllegalAccessException("Not implemented!");
		Searcher nativeSearcher = new IndexSearcher(indexReader);
		return new SimpleLuceneSearcher(nativeSearcher);
	}

	/**
	 * @see LuceneIndexReader#deleteDocument(int)
	 */
	public void deleteDocument(int docNum) throws IOException {
		indexReader.deleteDocument(docNum);
	}

	/**
	 * @see LuceneIndexReader#deleteDocuments(Term)
	 */
	public int deleteDocuments(Term term) throws IOException {
		return indexReader.deleteDocuments(term);
	}

	/**
	 * @see LuceneIndexReader#setNorm(int, String, byte)
	 */
	public void setNorm(int doc, String field, byte value) throws IOException {
		indexReader.setNorm(doc, field, value);
	}

	/**
	 * @see LuceneIndexReader#undeleteAll()
	 */
	public void undeleteAll() throws IOException {
		indexReader.undeleteAll();
	}

	/**
	 * @see LuceneIndexReader#createNativeSearcher()
	 */
	public Searcher createNativeSearcher() {
		return new IndexSearcher(indexReader);
	}
}
