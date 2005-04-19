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

import java.io.IOException;
import java.util.Iterator;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.springmodules.lucene.index.LuceneManipulateIndexException;
import org.springmodules.lucene.index.factory.IndexFactory;
import org.springmodules.lucene.index.factory.IndexReaderFactoryUtils;
import org.springmodules.lucene.index.factory.IndexWriterFactoryUtils;

/**
 * @author Brian McCallister
 * @author Thierry Templier
 */
public class LuceneIndexTemplate {

	private IndexFactory indexFactory;
	private Analyzer analyzer;

	/**
	 * Construct a new LuceneIndexTemplate for bean usage.
	 * Note: The IndexFactory has to be set before using the instance.
	 * This constructor can be used to prepare a LuceneIndexTemplate via a BeanFactory,
	 * typically setting the IndexFactory via setIndexFactory.
	 * @see #setSearcherFactory
	 */
	public LuceneIndexTemplate() {
	}

	/**
	 * @param indexFactory
	 * @param analyzer
	 */
	public LuceneIndexTemplate(IndexFactory indexFactory,Analyzer analyzer) {
		setIndexFactory(indexFactory);
		setAnalyzer(analyzer);
		afterPropertiesSet();
	}

	/**
	 * Eagerly initialize the exception translator,
	 * creating a default one for the specified SearcherFactory if none set.
	 */
	public void afterPropertiesSet() {
		if (getIndexFactory() == null) {
			throw new IllegalArgumentException("searcherFactory is required");
		}
	}

	/**
	 * @param internalDocumentId
	 */
	public void deleteDocument(int internalDocumentId) {
		IndexReader reader=IndexReaderFactoryUtils.getIndexReader(indexFactory);
		try {
			reader.delete(internalDocumentId);
		} catch(IOException ex) {
			throw new LuceneManipulateIndexException("Error during deleting a document.",ex);
		} finally {
			IndexReaderFactoryUtils.closeIndexReaderIfNecessary(indexFactory,reader);
		}
	}

	/**
	 * @param term
	 */
	public void deleteDocument(Term term) {
		IndexReader reader=IndexReaderFactoryUtils.getIndexReader(indexFactory);
		try {
			reader.delete(term);
		} catch(IOException ex) {
			throw new LuceneManipulateIndexException("Error during deleting a document.",ex);
		} finally {
			IndexReaderFactoryUtils.closeIndexReaderIfNecessary(indexFactory,reader);
		}
	}

	/**
	 * Be careful to use this method in a correct context.
	 */
	//TODO: make an precise javadoc about the context 
	public void undeleteDocuments() {
		IndexReader reader=IndexReaderFactoryUtils.getIndexReader(indexFactory);
		try {
			reader.undeleteAll();
		} catch(IOException ex) {
			throw new LuceneManipulateIndexException("Error during undeleting all documents.",ex);
		} finally {
			IndexReaderFactoryUtils.closeIndexReaderIfNecessary(indexFactory,reader);
		}
	}

	/**
	 * Be careful to use this method in a correct context.
	 */
	public void isDeleted(int internalDocumentId) {
		IndexReader reader=IndexReaderFactoryUtils.getIndexReader(indexFactory);
		try {
			reader.isDeleted(internalDocumentId);
		} finally {
			IndexReaderFactoryUtils.closeIndexReaderIfNecessary(indexFactory,reader);
		}
	}

	/**
	 * Be careful to use this method in a correct context.
	 */
	public void hasDeletions() {
		IndexReader reader=IndexReaderFactoryUtils.getIndexReader(indexFactory);
		try {
			reader.hasDeletions();
		} finally {
			IndexReaderFactoryUtils.closeIndexReaderIfNecessary(indexFactory,reader);
		}
	}

	/**
	 * Be careful to use this method in a correct context.
	 */
	public int getMaxDoc() {
		IndexReader reader=IndexReaderFactoryUtils.getIndexReader(indexFactory);
		try {
			return reader.maxDoc();
		} finally {
			IndexReaderFactoryUtils.closeIndexReaderIfNecessary(indexFactory,reader);
		}
	}

	/**
	 * Be careful to use this method in a correct context.
	 */
	public int getNumDocs() {
		IndexReader reader=IndexReaderFactoryUtils.getIndexReader(indexFactory);
		try {
			return reader.numDocs();
		} finally {
			IndexReaderFactoryUtils.closeIndexReaderIfNecessary(indexFactory,reader);
		}
	}

	public void addDocument(DocumentCreator creator) {
		IndexWriter writer=IndexWriterFactoryUtils.getIndexWriter(indexFactory);
		try {
			if( getAnalyzer()==null ) {
				writer.addDocument(creator.createDocument());
			} else {
				writer.addDocument(creator.createDocument(),getAnalyzer());
			}
		} catch(IOException ex) {
			throw new LuceneManipulateIndexException("Error during adding a document.",ex);
		} finally {
			IndexWriterFactoryUtils.closeIndexWriterIfNecessary(indexFactory,writer);
		}
	}

	public void addDocuments(DocumentsCreator creator) {
		IndexWriter writer=IndexWriterFactoryUtils.getIndexWriter(indexFactory);
		try {
			if( getAnalyzer()==null ) {
				for(Iterator i=creator.createDocuments().iterator();i.hasNext();) {
					writer.addDocument((Document)i.next());
				}
			} else {
				for(Iterator i=creator.createDocuments().iterator();i.hasNext();) {
					writer.addDocument((Document)i.next(),getAnalyzer());
				}
			}
		} catch(IOException ex) {
			throw new LuceneManipulateIndexException("Error during adding a document.",ex);
		} finally {
			IndexWriterFactoryUtils.closeIndexWriterIfNecessary(indexFactory,writer);
		}
	}

	public void optimize() {
		IndexWriter writer=IndexWriterFactoryUtils.getIndexWriter(indexFactory);
		try {
			writer.optimize();
		} catch(IOException ex) {
			throw new LuceneManipulateIndexException("Error during optimize the index.",ex);
		} finally {
			IndexWriterFactoryUtils.closeIndexWriterIfNecessary(indexFactory,writer);
		}
	}

	public void read(ReaderCallback callback) {
		IndexReader reader=IndexReaderFactoryUtils.getIndexReader(indexFactory);
		try {
			callback.doWithReader(reader);
		} catch(IOException ex) {
			throw new LuceneManipulateIndexException("Error during reading the index.",ex);
		} finally {
			IndexReaderFactoryUtils.closeIndexReaderIfNecessary(indexFactory,reader);
		}
	}

	public void write(WriterCallback callback) {
		IndexWriter writer=IndexWriterFactoryUtils.getIndexWriter(indexFactory);
		try {
			callback.doWithWriter(writer);
		} catch(IOException ex) {
			throw new LuceneManipulateIndexException("Error during writing the index.",ex);
		} finally {
			IndexWriterFactoryUtils.closeIndexWriterIfNecessary(indexFactory,writer);
		}
	}

	/**
	 * @return
	 */
	public Analyzer getAnalyzer() {
		return analyzer;
	}

	/**
	 * @param analyzer
	 */
	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}

	/**
	 * @return
	 */
	public IndexFactory getIndexFactory() {
		return indexFactory;
	}

	/**
	 * @param factory
	 */
	public void setIndexFactory(IndexFactory factory) {
		indexFactory = factory;
	}

}
