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

import junit.framework.TestCase;

import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.RAMDirectory;
import org.springmodules.lucene.index.LuceneIndexAccessException;

/**
 * @author Thierry Templier
 */
public class SimpleIndexFactoryTests extends TestCase {

	private RAMDirectory directory;

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		//Initialization of the index
		this.directory = new RAMDirectory();
		IndexWriter indexWriter = new IndexWriter(this.directory,new SimpleAnalyzer(),true);
		Document document = new Document();
		document.add(new Field("field", "a sample", Field.Store.YES, Field.Index.TOKENIZED));
		document.add(new Field("filter", "a sample filter", Field.Store.YES, Field.Index.TOKENIZED));
		document.add(new Field("sort", "2", Field.Store.YES, Field.Index.UN_TOKENIZED));
		indexWriter.addDocument(document);
		indexWriter.close();

	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		this.directory=null;
	}

	private void closeIndexReader(LuceneIndexReader indexReader) {
		if( indexReader!=null ) {
			try {
				indexReader.close();
			} catch (IOException ex) {
				fail();
			}
		}
	}

	private void closeIndexWriter(LuceneIndexWriter indexWriter) {
		if( indexWriter!=null ) {
			try {
				indexWriter.close();
			} catch (IOException ex) {
				fail();
			}
		}
	}

	private void closeIndexReader(IndexReader indexReader) {
		if( indexReader!=null ) {
			try {
				indexReader.close();
			} catch (IOException ex) {
				fail();
			}
		}
	}

	private void closeIndexWriter(IndexWriter indexWriter) {
		if( indexWriter!=null ) {
			try {
				indexWriter.close();
			} catch (IOException ex) {
				fail();
			}
		}
	}

	final public void testGetIndexFactoryReader() {
		SimpleIndexFactory indexFactory = new SimpleIndexFactory();

		try {
			indexFactory.getIndexReader();
			fail();
		} catch(LuceneIndexAccessException ex) {}
	}

	final public void testGetIndexFactoryWriter() {
		SimpleIndexFactory indexFactory = new SimpleIndexFactory();

		try {
			indexFactory.getIndexWriter();
			fail();
		} catch(LuceneIndexAccessException ex) {}
	}

	final public void testGetIndexReader() {
		SimpleIndexFactory indexFactory = new SimpleIndexFactory();
		indexFactory.setDirectory(this.directory);

		LuceneIndexReader indexReader = null;
		try {
			indexReader=indexFactory.getIndexReader();
			assertNotNull(indexReader);
			assertFalse(indexReader.hasDeletions());
		} finally {
			closeIndexReader(indexReader);
		}
	}

	final public void testIndexLockingWithReader() throws Exception {
		SimpleIndexFactory indexFactory = new SimpleIndexFactory();
		indexFactory.setDirectory(this.directory);

		IndexWriter indexWriter = null;
		LuceneIndexReader indexReader = null;
		try {
			indexWriter = new IndexWriter(this.directory,new SimpleAnalyzer(),false);

			indexReader = indexFactory.getIndexReader();
			fail();
		} catch(LuceneIndexAccessException ex) {
		} finally {
			closeIndexReader(indexReader);
			closeIndexWriter(indexWriter);
		}
	}

	final public void testIndexLockingResolvingWithReader() throws Exception {
		SimpleIndexFactory indexFactory = new SimpleIndexFactory();
		indexFactory.setDirectory(this.directory);
		indexFactory.setResolveLock(true);

		IndexWriter indexWriter = null;
		LuceneIndexReader indexReader = null;
		try {
			indexWriter = new IndexWriter(this.directory,new SimpleAnalyzer(),false);

			indexReader = indexFactory.getIndexReader();
		} catch(LuceneIndexAccessException ex) {
		} finally {
			closeIndexReader(indexReader);
			closeIndexWriter(indexWriter);
		}
	}

	final public void testGetIndexWriter() throws Exception {
		SimpleIndexFactory indexFactory = new SimpleIndexFactory();
		indexFactory.setDirectory(this.directory);
		indexFactory.setAnalyzer(new SimpleAnalyzer());

		LuceneIndexWriter indexWriter = null;
		try {
			indexWriter = indexFactory.getIndexWriter();
			assertNotNull(indexWriter);
			Document document = new Document();
			document.add(new Field("field", "a sample", Field.Store.YES, Field.Index.TOKENIZED));
			document.add(new Field("filter", "a sample filter", Field.Store.YES, Field.Index.TOKENIZED));
			document.add(new Field("sort", "3", Field.Store.YES, Field.Index.UN_TOKENIZED));
			indexWriter.addDocument(document);
		} finally {
			closeIndexWriter(indexWriter);
		}
	}

	final public void testIndexLockingWithWrier() throws Exception {
		SimpleIndexFactory indexFactory = new SimpleIndexFactory();
		indexFactory.setDirectory(this.directory);

		IndexWriter indexWriter1 = null;
		LuceneIndexWriter indexWriter2 = null;
		try {
			indexWriter1 = new IndexWriter(this.directory,new SimpleAnalyzer(),false);

			indexWriter2 = indexFactory.getIndexWriter();
			fail();
		} catch(LuceneIndexAccessException ex) {
		} finally {
			closeIndexWriter(indexWriter2);
			closeIndexWriter(indexWriter1);
		}
	}

	final public void testIndexLockingResolvingWithWriter() throws Exception {
		SimpleIndexFactory indexFactory = new SimpleIndexFactory();
		indexFactory.setDirectory(this.directory);
		indexFactory.setResolveLock(true);

		IndexWriter indexWriter1 = null;
		LuceneIndexWriter indexWriter2 = null;
		try {
			indexWriter1 = new IndexWriter(this.directory,new SimpleAnalyzer(),false);

			indexWriter2 = indexFactory.getIndexWriter();
		} catch(LuceneIndexAccessException ex) {
		} finally {
			closeIndexWriter(indexWriter2);
			closeIndexWriter(indexWriter1);
		}
	}

}
