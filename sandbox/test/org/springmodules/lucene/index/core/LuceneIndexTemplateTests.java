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
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.RAMDirectory;
import org.easymock.MockControl;
import org.springmodules.lucene.index.factory.SimpleIndexFactory;
import org.springmodules.lucene.search.core.MockSimpleSearcherFactory;

import junit.framework.TestCase;

/**
 * @author Brian McCallister
 * @author Thierry Templier
 */
public class LuceneIndexTemplateTests extends TestCase {

	private RAMDirectory directory;

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		//Initialization of the index
		this.directory=new RAMDirectory();
		IndexWriter writer=new IndexWriter(directory,new SimpleAnalyzer(),true);
		//Adding a document
		Document document1=new Document();
		document1.add(Field.Text("field", "a sample"));
		document1.add(Field.Text("filter", "a sample filter"));
		document1.add(Field.Keyword("sort", "2"));
		writer.addDocument(document1);
		//Adding a document
		Document document2=new Document();
		document2.add(Field.Text("field", "a Lucene support sample"));
		document2.add(Field.Text("filter", "another sample filter"));
		document2.add(Field.Keyword("sort", "3"));
		writer.addDocument(document2);
		//Adding a document
		Document document3=new Document();
		document3.add(Field.Text("field", "a different sample"));
		document3.add(Field.Text("filter", "another sample filter"));
		document3.add(Field.Keyword("sort", "1"));
		writer.addDocument(document3);
		writer.optimize();
		writer.close();
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		this.directory=null;
	}

	/*
	 * Test for void deleteDocument(int)
	 */
	final public void testDeleteDocumentint() throws Exception {
		//Initialization of the index
		SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);

		//Lucene template
		LuceneIndexTemplate template=new LuceneIndexTemplate(indexFactory,analyzer);
		template.deleteDocument(0);

		//Check if the document is marked for deletion
		assertEquals(indexFactory.getIndexReaderDeletedId(),0);
		//Check if the reader of the template is closed
		assertTrue(indexFactory.isReaderClosed());
	}

	/*
	 * Test for void deleteDocument(Term)
	 */
	final public void testDeleteDocumentTerm() throws Exception {
		//Initialization of the index
		SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);

		//Lucene template
		LuceneIndexTemplate template=new LuceneIndexTemplate(indexFactory,analyzer);
		template.deleteDocument(new Term("field","lucene"));

		//Check if the document is marked for deletion
		assertEquals(indexFactory.getIndexReaderDeletedId(),1);
		//Check if the reader of the template is closed
		assertTrue(indexFactory.isReaderClosed());
	}

	final public void testUndeleteDocuments() throws Exception {
		//Initialization of the index
		SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);

		//Lucene template
		LuceneIndexTemplate template=new LuceneIndexTemplate(indexFactory,analyzer);
		template.undeleteDocuments();

		//Check if the document is marked for deletion
		assertTrue(indexFactory.isIndexReaderUndeletedAll());
		//Check if the reader of the template is closed
		assertTrue(indexFactory.isReaderClosed());
	}

	final public void testIsDeleted() throws Exception {
		//Initialization of the index
		SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);

		//Lucene template
		LuceneIndexTemplate template=new LuceneIndexTemplate(indexFactory,analyzer);
		template.isDeleted(0);

		//Check if the document is marked for deletion
		assertEquals(indexFactory.getIndexReaderIsDeleted(),0);
		//Check if the reader of the template is closed
		assertTrue(indexFactory.isReaderClosed());
	}

	final public void testHasDeletions() throws Exception {
		//Initialization of the index
		SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);

		//Lucene template
		LuceneIndexTemplate template=new LuceneIndexTemplate(indexFactory,analyzer);
		template.hasDeletions();

		//Check if the document is marked for deletion
		assertTrue(indexFactory.isIndexReaderHasDeletions());
		//Check if the reader of the template is closed
		assertTrue(indexFactory.isReaderClosed());
	}

	final public void testGetMaxDoc() throws Exception {
		//Initialization of the index
		SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);

		//Lucene template
		LuceneIndexTemplate template=new LuceneIndexTemplate(indexFactory,analyzer);
		assertEquals(template.getMaxDoc(),3);

		//Check if the reader calls the maxDoc method
		assertTrue(indexFactory.isIndexReaderMaxDoc());
		//Check if the reader of the template is closed
		assertTrue(indexFactory.isReaderClosed());
	}

	final public void testGetNumDocs() throws Exception {
		//Initialization of the index
		SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);

		//Lucene template
		LuceneIndexTemplate template=new LuceneIndexTemplate(indexFactory,analyzer);
		assertEquals(template.getNumDocs(),3);

		//Check if the reader calls the maxDoc method
		assertTrue(indexFactory.isIndexReaderNumDocs());
		//Check if the reader of the template is closed
		assertTrue(indexFactory.isReaderClosed());
	}

	final public void testAddDocument() throws Exception {
		//Initialization of the index
		SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);

		//Lucene template
		LuceneIndexTemplate template=new LuceneIndexTemplate(indexFactory,analyzer);
		final boolean[] called = {false};
		template.addDocument(new DocumentCreator() {
			public Document createDocument() throws IOException {
				called[0] = true;
				Document document=new Document();
				document.add(Field.Text("field", "a sample"));
				document.add(Field.Text("filter", "a sample filter"));
				document.add(Field.Keyword("sort", "2"));
				return document;
			}
		});

		//Check if the reader calls the addDocument method
		assertTrue(called[0]);
		//Check if the reader calls the addDocument method
		assertEquals(indexFactory.getIndexWriterAddDocuments(),1);
		//Check if the reader of the template is closed
		assertTrue(indexFactory.isWriterClosed());
	}

	final public void testAddDocuments() throws Exception {
		//Initialization of the index
		SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);

		//Lucene template
		LuceneIndexTemplate template=new LuceneIndexTemplate(indexFactory,analyzer);
		final boolean[] called = {false};
		template.addDocuments(new DocumentsCreator() {
			public List createDocuments() throws IOException {
				called[0] = true;
				List documents=new ArrayList();
				Document document1=new Document();
				document1.add(Field.Text("field", "a sample 1"));
				document1.add(Field.Text("filter", "a sample filter 1"));
				document1.add(Field.Keyword("sort", "1"));
				documents.add(document1);
				Document document2=new Document();
				document2.add(Field.Text("field", "a sample 2"));
				document2.add(Field.Text("filter", "a sample filter 2"));
				document2.add(Field.Keyword("sort", "2"));
				documents.add(document2);
				return documents;
			}
		});

		//Check if the reader calls the addDocument method
		assertTrue(called[0]);
		//Check if the reader calls the addDocument method
		assertEquals(indexFactory.getIndexWriterAddDocuments(),2);
		//Check if the reader of the template is closed
		assertTrue(indexFactory.isWriterClosed());
	}

	final public void testOptimize() throws Exception {
		//Initialization of the index
		SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);

		//Lucene template
		LuceneIndexTemplate template=new LuceneIndexTemplate(indexFactory,analyzer);
		template.optimize();

		//Check if the reader calls the optimize method
		assertTrue(indexFactory.isIndexWriterOptimize());
		//Check if the reader of the template is closed
		assertTrue(indexFactory.isWriterClosed());
	}

	final public void testRead() throws Exception {
		//Initialization of the index
		SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);

		//Lucene template
		LuceneIndexTemplate template=new LuceneIndexTemplate(indexFactory,analyzer);
		final boolean[] called = {false};
		template.read(new ReaderCallback() {
			public Object doWithReader(IndexReader reader) throws IOException {
				called[0] = true;
				assertNotNull(reader);
				return null;
			}
		});

		//Check if doWithReader is called
		assertTrue(called[0]);
		//Check if the reader of the template is closed
		assertTrue(indexFactory.isReaderClosed());
	}

	final public void testWrite() throws Exception {
		//Initialization of the index
		SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);

		//Lucene template
		LuceneIndexTemplate template=new LuceneIndexTemplate(indexFactory,analyzer);
		final boolean[] called = {false};
		template.write(new WriterCallback() {
			public Object doWithWriter(IndexWriter writer) throws IOException {
				called[0] = true;
				assertNotNull(writer);
				return null;
			}
		});

		//Check if doWithReader is called
		assertTrue(called[0]);
		//Check if the reader of the template is closed
		assertTrue(indexFactory.isWriterClosed());
	}

}
