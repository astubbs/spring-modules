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
public class MockSimpleIndexFactory implements IndexFactory {
	private SimpleIndexFactory target;
	private IndexWriterCallListener writerListener;
	private IndexReaderCallListener readerListener;

	public MockSimpleIndexFactory(SimpleIndexFactory indexFactory) {
		this.target=indexFactory;
		this.writerListener=new SimpleIndexWriterCallListener();
		this.readerListener=new SimpleIndexReaderCallListener();
	}

	/**
	 * @see org.springmodules.lucene.index.factory.IndexFactory#getIndexReader()
	 */
	public IndexReader getIndexReader() {
		readerListener.readerCreated();
		return new MockIndexReader(target.getIndexReader(),readerListener);
	}

	/**
	 * @see org.springmodules.lucene.index.factory.IndexFactory#getIndexWriter()
	 */
	public IndexWriter getIndexWriter() {
		writerListener.writerCreated();
		try {
			return new MockIndexWriter(target.getDirectory(),target.getAnalyzer(),writerListener);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Error during the creation of a TestIndexWriter");
		}
	}

	public IndexReaderCallListener getReaderListener() {
		return readerListener;
	}

	public IndexWriterCallListener getWriterListener() {
		return writerListener;
	}

}
