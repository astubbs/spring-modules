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

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.HitCollector;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopFieldDocs;
import org.springmodules.lucene.index.factory.IndexFactory;
import org.springmodules.lucene.index.factory.SimpleIndexFactory;
import org.springmodules.lucene.search.factory.SearcherFactory;
import org.springmodules.lucene.search.factory.SimpleSearcherFactory;

/**
 * @author Thierry Templier
 */
public class MockSimpleIndexFactory implements IndexFactory,IndexReaderEvent,IndexWriterEvent {
	private SimpleIndexFactory target;

	//Reader state variables
	private boolean indexReaderUndeletedAll=false;
	private int indexReaderDeletedId=-1;
	private boolean indexReaderHasDeletions=false;
	private boolean indexReaderMaxDoc=false;
	private boolean indexReaderNumDocs=false;
	private int indexReaderIsDeleted=-1;
	private boolean readerClosed=false;

	//Writer state variables
	private boolean indexWriterOptimize=false;
	private int indexWriterAddDocuments=0;
	private boolean writerClosed=false;

	public MockSimpleIndexFactory(SimpleIndexFactory indexFactory) {
		this.target=indexFactory;
	}

	private void doInitReaderVariables() {
		indexReaderUndeletedAll=false;
		indexReaderDeletedId=-1;
		indexReaderHasDeletions=false;
		indexReaderMaxDoc=false;
		indexReaderNumDocs=false;
		indexReaderIsDeleted=-1;
	}

	/**
	 * @see org.springmodules.lucene.index.factory.IndexFactory#getIndexReader()
	 */
	public IndexReader getIndexReader() {
		doInitReaderVariables();
		return new MockIndexReader(target.getIndexReader(),this);
	}

	private void doInitWriterVariables() {
		indexWriterOptimize=false;
		indexWriterAddDocuments=0;
		writerClosed=false;
	}

	/**
	 * @see org.springmodules.lucene.index.factory.IndexFactory#getIndexWriter()
	 */
	public IndexWriter getIndexWriter() {
		doInitWriterVariables();
		try {
			return new MockIndexWriter(target.getDirectory(),target.getAnalyzer(),this);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Error during the creation of a TestIndexWriter");
		}
	}

	/**
	 * @see org.springmodules.lucene.index.core.TestIndexEvent#indexReaderClosed()
	 */
	public void indexReaderClosed() {
		readerClosed=true;
	}

	public boolean isReaderClosed() {
		return readerClosed;
	}

	/**
	 * @see org.springmodules.lucene.index.core.TestIndexEvent#indexWriterClosed()
	 */
	public void indexWriterClosed() {
		writerClosed=true;
	}

	public boolean isWriterClosed() {
		return writerClosed;
	}

	/**
	 * @see org.springmodules.lucene.index.core.TestIndexReaderEvent#indexReaderDeleted(int)
	 */
	public void indexReaderDeleted(int id) {
		this.indexReaderDeletedId=id;
	}

	public int getIndexReaderDeletedId() {
		return indexReaderDeletedId;
	}

	/**
	 * @see org.springmodules.lucene.index.core.TestIndexReaderEvent#indexReaderUndeletedAll()
	 */
	public void indexReaderUndeletedAll() {
		this.indexReaderUndeletedAll=true;
		
	}

	public boolean isIndexReaderUndeletedAll() {
		return indexReaderUndeletedAll;
	}

	/**
	 * @see org.springmodules.lucene.index.core.TestIndexReaderEvent#indexReaderHashDeletions()
	 */
	public void indexReaderHasDeletions() {
		indexReaderHasDeletions=true;
	}

	public boolean isIndexReaderHasDeletions() {
		return indexReaderHasDeletions;
	}

	/**
	 * @see org.springmodules.lucene.index.core.TestIndexReaderEvent#indexReaderMaxDoc()
	 */
	public void indexReaderMaxDoc() {
		indexReaderMaxDoc=true;
	}

	public boolean isIndexReaderMaxDoc() {
		return indexReaderMaxDoc;
	}

	/**
	 * @see org.springmodules.lucene.index.core.TestIndexReaderEvent#indexReaderNumDocs()
	 */
	public void indexReaderNumDocs() {
		indexReaderNumDocs=true;
	}

	public boolean isIndexReaderNumDocs() {
		return indexReaderNumDocs;
	}

	/**
	 * @see org.springmodules.lucene.index.core.TestIndexReaderEvent#indexReaderIsDeleted(int)
	 */
	public void indexReaderIsDeleted(int id) {
		indexReaderIsDeleted=id;
	}

	public int getIndexReaderIsDeleted() {
		return indexReaderIsDeleted;
	}

	/**
	 * @see org.springmodules.lucene.index.core.IndexWriterEvent#indexWriterOptimize()
	 */
	public void indexWriterOptimize() {
		indexWriterOptimize=true;
	}

	public boolean isIndexWriterOptimize() {
		return indexWriterOptimize;
	}

	/**
	 * @see org.springmodules.lucene.index.core.IndexWriterEvent#indexWriterAddDocuments()
	 */
	public void indexWriterAddDocument() {
		indexWriterAddDocuments++;
	}


	public int getIndexWriterAddDocuments() {
		return indexWriterAddDocuments;
	}

}
