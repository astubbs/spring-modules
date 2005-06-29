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
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.springmodules.lucene.index.LuceneIndexAccessException;
import org.springmodules.lucene.index.factory.IndexFactory;
import org.springmodules.lucene.index.factory.IndexReaderFactoryUtils;
import org.springmodules.lucene.index.factory.IndexWriterFactoryUtils;
import org.springmodules.lucene.util.IOUtils;

/**
 * <b>This is the central class in the lucene indexing core package.</b>
 * It simplifies the use of lucene to index documents or datas using
 * index reader and writer. It helps to avoid common errors and to
 * manage these resource in a flexible manner.
 * It executes core Lucene workflow, leaving application code to focus on
 * the way to create Lucene documents and make some operations on the
 * index.
 *
 * <p>This class is based on the IndexFactory abstraction which is a
 * factory to create IndexReader and IndexWriter for the configured
 * Directory. So the template doesn't need to always hold resources and
 * this avoids some locking problems on the index. You can too apply
 * different strategies for managing index resources.
 *
 * <p>Can be used within a service implementation via direct instantiation
 * with a IndexFactory reference, or get prepared in an application context
 * and given to services as bean reference. Note: The IndexFactory should
 * always be configured as a bean in the application context, in the first case
 * given to the service directly, in the second case to the prepared template.
 * 
 * <p>You must be aware that the use of some methods (like undeleteDocuments,
 * isDeleted, hasDeletions, flush) have sense only if you share the Lucene
 * underlying resources across several template method calls. As a matter of
 * fact, when IndexReader and IndexWriter are closed every changes deferred
 * until the closing of these resources. Moreover some Lucene operations are
 * incompatible if you share resources across several calls. 
 * 
 * @author Brian McCallister
 * @author Thierry Templier
 * @see DocumentCreator
 * @see DocumentsCreator
 * @see org.springmodules.lucene.index.factory
 */
public class LuceneIndexTemplate implements LuceneOperations {

	private IndexFactory indexFactory;
	private Analyzer analyzer;

	/**
	 * Construct a new LuceneIndexTemplate for bean usage.
	 * Note: The IndexFactory has to be set before using the instance.
	 * This constructor can be used to prepare a LuceneIndexTemplate via a BeanFactory,
	 * typically setting the IndexFactory via setIndexFactory.
	 * @see #setIndexFactory
	 */
	public LuceneIndexTemplate() {
	}

	/**
	 * Construct a new LuceneIndexTemplate, given an IndexFactory to obtain both
	 * IndexReader and IndexWriter, and an Analyzer to be used unless an other
	 * one is specified as method parameter.
	 * @param indexFactory IndexFactory to obtain both IndexReader and IndexWriter
	 * @param analyzer Lucene analyzer to extract tokens out of the text to index
	 */
	public LuceneIndexTemplate(IndexFactory indexFactory,Analyzer analyzer) {
		setIndexFactory(indexFactory);
		setAnalyzer(analyzer);
		afterPropertiesSet();
	}

	/**
	 * Check if the indexFactory is set. The analyzer could be not set.
	 */
	public void afterPropertiesSet() {
		if (getIndexFactory() == null) {
			throw new IllegalArgumentException("indexFactory is required");
		}
	}

	/**
	 * Set the IndexFactory to obtain both IndexReader and IndexWriter.
	 */
	public void setIndexFactory(IndexFactory factory) {
		indexFactory = factory;
	}

	/**
	 * Return the IndexFactory used by this template.
	 */
	public IndexFactory getIndexFactory() {
		return indexFactory;
	}

	/**
	 * Set the default Lucene Analyzer used to extract tokens out of the
	 * text to index.
	 */
	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}

	/**
	 * Return the Lucene Analyzer used by this template.
	 */
	public Analyzer getAnalyzer() {
		return analyzer;
	}

	//-------------------------------------------------------------------------
	// Methods dealing with document deletions
	//-------------------------------------------------------------------------

	public void deleteDocument(int internalDocumentId) {
		IndexReader reader=IndexReaderFactoryUtils.getIndexReader(indexFactory);
		try {
			reader.delete(internalDocumentId);
		} catch(IOException ex) {
			throw new LuceneIndexAccessException("Error during deleting a document.",ex);
		} finally {
			IndexReaderFactoryUtils.releaseIndexReader(indexFactory,reader);
		}
	}

	public void deleteDocuments(Term term) {
		IndexReader reader=IndexReaderFactoryUtils.getIndexReader(indexFactory);
		try {
			reader.delete(term);
		} catch(IOException ex) {
			throw new LuceneIndexAccessException("Error during deleting a document.",ex);
		} finally {
			IndexReaderFactoryUtils.releaseIndexReader(indexFactory,reader);
		}
	}

	public void undeleteDocuments() {
		IndexReader reader=IndexReaderFactoryUtils.getIndexReader(indexFactory);
		try {
			reader.undeleteAll();
		} catch(IOException ex) {
			throw new LuceneIndexAccessException("Error during undeleting all documents.",ex);
		} finally {
			IndexReaderFactoryUtils.releaseIndexReader(indexFactory,reader);
		}
	}

	public boolean isDeleted(int internalDocumentId) {
		IndexReader reader=IndexReaderFactoryUtils.getIndexReader(indexFactory);
		try {
			return reader.isDeleted(internalDocumentId);
		} finally {
			IndexReaderFactoryUtils.releaseIndexReader(indexFactory,reader);
		}
	}

	public boolean hasDeletions() {
		IndexReader reader=IndexReaderFactoryUtils.getIndexReader(indexFactory);
		try {
			return reader.hasDeletions();
		} finally {
			IndexReaderFactoryUtils.releaseIndexReader(indexFactory,reader);
		}
	}

	//-------------------------------------------------------------------------
	// Methods dealing with index informations
	//-------------------------------------------------------------------------

	public int getMaxDoc() {
		IndexReader reader=IndexReaderFactoryUtils.getIndexReader(indexFactory);
		try {
			return reader.maxDoc();
		} finally {
			IndexReaderFactoryUtils.releaseIndexReader(indexFactory,reader);
		}
	}

	public int getNumDocs() {
		IndexReader reader=IndexReaderFactoryUtils.getIndexReader(indexFactory);
		try {
			return reader.numDocs();
		} finally {
			IndexReaderFactoryUtils.releaseIndexReader(indexFactory,reader);
		}
	}

	//-------------------------------------------------------------------------
	// Methods dealing with document creations
	//-------------------------------------------------------------------------

	protected Document createDocument(DocumentCreator documentCreator) {
		try {
			return documentCreator.createDocument();
		} catch (IOException ex) {
			throw new LuceneIndexAccessException("Construction of the desired Document failed", ex);
		}
	}

	protected List createDocuments(DocumentsCreator documentsCreator) {
		try {
			return documentsCreator.createDocuments();
		} catch (IOException ex) {
			throw new LuceneIndexAccessException("Construction of the desired Document failed", ex);
		}
	}

	public void addDocument(Document document) {
		addDocument(document,null);
	}

	public void addDocument(Document document,Analyzer analyzer) {
		IndexWriter writer=IndexWriterFactoryUtils.getIndexWriter(indexFactory);
		try {
			doAddDocument(writer,document,null);
		} catch(IOException ex) {
			throw new LuceneIndexAccessException("Error during adding a document.",ex);
		} finally {
			IndexWriterFactoryUtils.releaseIndexWriter(indexFactory,writer);
		}
	}

	public void addDocument(DocumentCreator creator) {
		addDocument(createDocument(creator),null);
	}

	public void addDocument(DocumentCreator documentCreator,Analyzer analyzer) {
		addDocument(createDocument(documentCreator),analyzer);
	}

	public void addDocument(InputStreamDocumentCreator creator) {
		addDocument(creator,null);
	}

	public void addDocument(InputStreamDocumentCreator documentCreator,Analyzer analyzer) {
		InputStream inputStream=null;
		try {
			inputStream=documentCreator.createInputStream();
			addDocument(documentCreator.createDocumentFromInputStream(inputStream),analyzer);
		} catch(IOException ex) {
			//throw new LuceneInputStreamException("Error during adding a document.",ex);
			throw new RuntimeException("Error during adding a document.",ex);
		} finally {
			IOUtils.closeInputStream(inputStream);
		}
	}

	public void addDocuments(List documents) {
		addDocuments(documents,null);
	}

	public void addDocuments(List documents,Analyzer analyzer) {
		IndexWriter writer=IndexWriterFactoryUtils.getIndexWriter(indexFactory);
		try {
			for(Iterator i=documents.iterator();i.hasNext();) {
				Document document=(Document)i.next();
				doAddDocument(writer,document,analyzer);
			}
		} catch(IOException ex) {
			throw new LuceneIndexAccessException("Error during adding a document.",ex);
		} finally {
			IndexWriterFactoryUtils.releaseIndexWriter(indexFactory,writer);
		}
	}

	public void addDocuments(DocumentsCreator creator) {
		addDocuments(creator,null);
	}

	public void addDocuments(DocumentsCreator creator,Analyzer analyzer) {
		addDocuments(createDocuments(creator),analyzer);
	}

	private void doAddDocument(IndexWriter writer,Document document,
								Analyzer analyzer) throws IOException {
		if( document!=null ) {
			if( analyzer==null ) {
				writer.addDocument(document);
			} else if( getAnalyzer()==null ) {
				writer.addDocument(document);
			} else if( analyzer!=null ) {
				writer.addDocument(document,analyzer);
			} else if( getAnalyzer()!=null ) {
				writer.addDocument(document,getAnalyzer());
			} else {
				writer.addDocument(document);
			}
		} else {
			throw new LuceneIndexAccessException("The document created is null.");
		}
	}

	//-------------------------------------------------------------------------
	// Methods dealing with index insertions
	//-------------------------------------------------------------------------

	public void addIndex(Directory directory) {
		addIndexes(new Directory[] { directory });
	}

	public void addIndexes(Directory[] directories) {
		IndexWriter writer=IndexWriterFactoryUtils.getIndexWriter(indexFactory);
		try {
			writer.addIndexes(directories);
		} catch(IOException ex) {
			throw new LuceneIndexAccessException("Error during adding indexes.",ex);
		} finally {
			IndexWriterFactoryUtils.releaseIndexWriter(indexFactory,writer);
		}
	}

	//-------------------------------------------------------------------------
	// Methods dealing with index optmization
	//-------------------------------------------------------------------------

	public void optimize() {
		IndexWriter writer=IndexWriterFactoryUtils.getIndexWriter(indexFactory);
		try {
			writer.optimize();
		} catch(IOException ex) {
			throw new LuceneIndexAccessException("Error during optimize the index.",ex);
		} finally {
			IndexWriterFactoryUtils.releaseIndexWriter(indexFactory,writer);
		}
	}

	//-------------------------------------------------------------------------
	// Methods dealing with index reader and writer directly
	//-------------------------------------------------------------------------

	public Object read(ReaderCallback callback) {
		IndexReader reader=IndexReaderFactoryUtils.getIndexReader(indexFactory);
		try {
			return callback.doWithReader(reader);
		} catch(IOException ex) {
			throw new LuceneIndexAccessException("Error during using the IndexReader.",ex);
		} finally {
			IndexReaderFactoryUtils.releaseIndexReader(indexFactory,reader);
		}
	}

	public Object write(WriterCallback callback) {
		IndexWriter writer=IndexWriterFactoryUtils.getIndexWriter(indexFactory);
		try {
			return callback.doWithWriter(writer);
		} catch(IOException ex) {
			throw new LuceneIndexAccessException("Error during using the IndexWriter.",ex);
		} finally {
			IndexWriterFactoryUtils.releaseIndexWriter(indexFactory,writer);
		}
	}

}
