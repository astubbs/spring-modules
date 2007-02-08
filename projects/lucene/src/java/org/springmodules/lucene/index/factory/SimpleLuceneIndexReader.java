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

/**
 * @author Thierry Templier
 */
public class SimpleLuceneIndexReader implements LuceneIndexReader {
	private IndexReader indexReader;

	public SimpleLuceneIndexReader(IndexReader indexReader) {
		this.indexReader = indexReader;
	}
	
	public Directory directory() {
		return indexReader.directory();
	}

	public int docFreq(Term term) throws IOException {
		return indexReader.docFreq(term);
	}

	public Document document(int n) throws IOException {
		return indexReader.document(n);
	}

	public boolean equals(Object obj) {
		return indexReader.equals(obj);
	}

	public Collection getFieldNames(FieldOption fldOption) {
		return indexReader.getFieldNames(fldOption);
	}

	public TermFreqVector getTermFreqVector(int docNumber, String field) throws IOException {
		return indexReader.getTermFreqVector(docNumber, field);
	}

	public TermFreqVector[] getTermFreqVectors(int docNumber) throws IOException {
		return indexReader.getTermFreqVectors(docNumber);
	}

	public long getVersion() {
		return indexReader.getVersion();
	}

	public boolean hasDeletions() {
		return indexReader.hasDeletions();
	}

	public int hashCode() {
		return indexReader.hashCode();
	}

	public boolean hasNorms(String field) throws IOException {
		return indexReader.hasNorms(field);
	}

	public boolean isCurrent() throws IOException {
		return indexReader.isCurrent();
	}

	public boolean isDeleted(int n) {
		return indexReader.isDeleted(n);
	}

	public int maxDoc() {
		return indexReader.maxDoc();
	}

	public void norms(String field, byte[] bytes, int offset) throws IOException {
		indexReader.norms(field, bytes, offset);
	}

	public byte[] norms(String field) throws IOException {
		return indexReader.norms(field);
	}

	public int numDocs() {
		return indexReader.numDocs();
	}

	public void setNorm(int doc, String field, float value) throws IOException {
		indexReader.setNorm(doc, field, value);
	}

	public TermDocs termDocs() throws IOException {
		return indexReader.termDocs();
	}

	public TermDocs termDocs(Term term) throws IOException {
		return indexReader.termDocs(term);
	}

	public TermPositions termPositions() throws IOException {
		return indexReader.termPositions();
	}

	public TermPositions termPositions(Term term) throws IOException {
		return indexReader.termPositions(term);
	}

	public TermEnum terms() throws IOException {
		return indexReader.terms();
	}

	public TermEnum terms(Term term) throws IOException {
		return indexReader.terms(term);
	}

	public void close() throws IOException {
		indexReader.close();
	}

	public LuceneSearcher createSearcher() {
		//TODO: to be implemented
		//throw new IllegalAccessException("Not implemented!");
		return null;
	}

	public void deleteDocument(int docNum) throws IOException {
		indexReader.deleteDocument(docNum);
	}

	public int deleteDocuments(Term term) throws IOException {
		return indexReader.deleteDocuments(term);
	}

	public void setNorm(int doc, String field, byte value) throws IOException {
		indexReader.setNorm(doc, field, value);
	}

	public void undeleteAll() throws IOException {
		indexReader.undeleteAll();
	}

	public Searcher createNativeSearcher() {
		return new IndexSearcher(indexReader);
	}
}
