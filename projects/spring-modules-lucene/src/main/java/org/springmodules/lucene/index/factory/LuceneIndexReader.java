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
import org.apache.lucene.search.Searcher;
import org.apache.lucene.store.Directory;
import org.springmodules.lucene.search.factory.LuceneSearcher;

/**
 * Interface representing the contract of the Lucene IndexReader class. It
 * allows unit tests with this resource and improves management of this
 * kind of resources.
 *  
 * All the method of the IndexReader class are present in this interface
 * and, so allow to make all the operations of this class. 
 *  
 * @author Thierry Templier
 * @see IndexReader
 */
public interface LuceneIndexReader {
	
	/**
	 * @see org.apache.lucene.index.IndexReader#close()
	 * @throws IOException
	 */
	void close() throws IOException;

	/**
	 * @see org.apache.lucene.index.IndexReader#deleteDocument(int)
	 * @throws IOException
	 */
	void deleteDocument(int docNum) throws IOException;

	/**
	 * @see org.apache.lucene.index.IndexReader#deleteDocuments(Term)
	 * @throws IOException
	 */
	int deleteDocuments(Term term) throws IOException;

	/**
	 * @see org.apache.lucene.index.IndexReader#directory()
	 */
	Directory directory();

	/**
	 * @see org.apache.lucene.index.IndexReader#docFreq(Term)
	 * @throws IOException
	 */
	int docFreq(Term t) throws IOException;

	/**
	 * @see org.apache.lucene.index.IndexReader#document(int)
	 * @throws IOException
	 */
	Document document(int n) throws IOException;

	/**
	 * @see org.apache.lucene.index.IndexReader#getFieldNames(org.apache.lucene.index.org.apache.lucene.index.IndexReader.FieldOption)
	 */
	Collection getFieldNames(IndexReader.FieldOption fldOption);

	/**
	 * @see org.apache.lucene.index.IndexReader#getTermFreqVector(int, String)
	 * @throws IOException
	 */
	TermFreqVector getTermFreqVector(int docNumber, String field) throws IOException;

	/**
	 * @see org.apache.lucene.index.IndexReader#getTermFreqVector(int)
	 * @throws IOException
	 */
	TermFreqVector[] getTermFreqVectors(int docNumber) throws IOException;

	/**
	 * @see org.apache.lucene.index.IndexReader#getVersion()
	 */
	long getVersion();

	/**
	 * @see org.apache.lucene.index.IndexReader#hasDeletions()
	 */
	boolean hasDeletions();

	/**
	 * @see org.apache.lucene.index.IndexReader#hasNorms(String)
	 * @throws IOException
	 */
	boolean hasNorms(String field) throws IOException;

	/**
	 * @see org.apache.lucene.index.IndexReader#isCurrent()
	 * @throws IOException
	 */
	boolean isCurrent() throws IOException;

	/**
	 * @see org.apache.lucene.index.IndexReader#isDeleted(int)
	 */
	boolean isDeleted(int n);

	/**
	 * @see org.apache.lucene.index.IndexReader#maxDoc()
	 */
	int maxDoc();

	/**
	 * @see org.apache.lucene.index.IndexReader#norms(String)
	 * @throws IOException
	 */
	byte[] norms(String field) throws IOException;

	/**
	 * @see org.apache.lucene.index.IndexReader#norms(String, byte[], int)
	 * @throws IOException
	 */
	void norms(String field, byte[] bytes, int offset) throws IOException;

	/**
	 * @see org.apache.lucene.index.IndexReader#numDocs()
	 */
	int numDocs();

	/**
	 * @see org.apache.lucene.index.IndexReader#setNorm(int, String, byte)
	 * @throws IOException
	 */
	void setNorm(int doc, String field, byte value) throws IOException;

	/**
	 * @see org.apache.lucene.index.IndexReader#setNorm(int, String, float)
	 * @throws IOException
	 */
	void setNorm(int doc, String field, float value) throws IOException;

	/**
	 * @see org.apache.lucene.index.IndexReader#termDocs()
	 * @throws IOException
	 */
	TermDocs termDocs() throws IOException;

	/**
	 * @see org.apache.lucene.index.IndexReader#termDocs(Term)
	 * @throws IOException
	 */
	TermDocs termDocs(Term term) throws IOException;

	/**
	 * @see org.apache.lucene.index.IndexReader#termPositions()
	 * @throws IOException
	 */
	TermPositions termPositions() throws IOException;

	/**
	 * @see org.apache.lucene.index.IndexReader#termPositions(Term)
	 * @throws IOException
	 */
	TermPositions termPositions(Term term) throws IOException;

	/**
	 * @see org.apache.lucene.index.IndexReader#terms()
	 * @throws IOException
	 */
	TermEnum terms() throws IOException;

	/**
	 * @see org.apache.lucene.index.IndexReader#terms(Term)
	 * @throws IOException
	 */
	TermEnum terms(Term t) throws IOException;

	/**
	 * @see org.apache.lucene.index.IndexReader#undeleteAll()
	 * @throws IOException
	 */
	void undeleteAll() throws IOException;

	/**
	 * Creates an instance of LuceneSearcher basing on the internal
	 * resources of the implementation of this interface.
	 *  
	 * @return the created LuceneSearcher
	 */
	LuceneSearcher createSearcher();

	/**
	 * Creates an instance of Searcher basing on the internal
	 * resources of the implementation of this interface.
	 * 
	 * @return the created Searcher
	 */
	Searcher createNativeSearcher();
}
