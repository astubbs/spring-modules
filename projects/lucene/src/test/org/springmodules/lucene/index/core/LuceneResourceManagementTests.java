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

import junit.framework.TestCase;

import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.jca.cci.core.CciTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springmodules.lucene.index.factory.SimpleIndexFactory;
import org.springmodules.resource.ResourceManager;
import org.springmodules.resource.support.ResourceBindingManager;
import org.springmodules.resource.support.ResourceCallback;
import org.springmodules.resource.support.ResourceTemplate;

/**
 * @author Thierry Templier
 */
public class LuceneResourceManagementTests extends TestCase {

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

	public void testLuceneReaderResourceManagement() {
		//Initialization of the index
		final SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		final MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);

		ResourceManager rm=new LuceneIndexResourceManager(indexFactory);
		ResourceTemplate rt = new ResourceTemplate(rm);

		rt.execute(new ResourceCallback() {
			public Object doWithResource() {
				assertTrue("Has thread indexFactory", ResourceBindingManager.hasResource(indexFactory));

				//Lucene template
				LuceneIndexTemplate template=new DefaultLuceneIndexTemplate(indexFactory,analyzer);
				template.deleteDocument(0);
				template.hasDeletions();
				template.undeleteDocuments();

				return null;
			}
		});

		//Check if a reader has been opened
		assertEquals(indexFactory.getReaderListener().getNumberReadersCreated(),1);
		//Check if the document is marked for deletion
		assertEquals(indexFactory.getReaderListener().getIndexReaderDeletedId(),0);
		//Check if the hasDeletions has been called
		assertTrue(indexFactory.getReaderListener().isIndexReaderHasDeletions());
		//Check if the document is marked for deletion
		assertTrue(indexFactory.getReaderListener().isIndexReaderUndeletedAll());
		//Check if the reader of the template is closed
		assertEquals(indexFactory.getReaderListener().getNumberReadersClosed(),1);

	}

	public void testNoLuceneReaderResourceManagement() {
		//Initialization of the index
		SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);

		assertFalse("Has no thread indexFactory", ResourceBindingManager.hasResource(indexFactory));

		//Lucene template
		LuceneIndexTemplate template=new DefaultLuceneIndexTemplate(indexFactory,analyzer);
		assertFalse("Has no thread indexFactory", ResourceBindingManager.hasResource(indexFactory));
		template.deleteDocument(0);
		assertFalse("Has no thread indexFactory", ResourceBindingManager.hasResource(indexFactory));
		template.hasDeletions();
		assertFalse("Has no thread indexFactory", ResourceBindingManager.hasResource(indexFactory));
		template.undeleteDocuments();
		assertFalse("Has no thread indexFactory", ResourceBindingManager.hasResource(indexFactory));

		//Check if a reader has been opened
		assertEquals(indexFactory.getReaderListener().getNumberReadersCreated(),3);
		//Check if the document is marked for deletion
		assertEquals(indexFactory.getReaderListener().getIndexReaderDeletedId(),0);
		//Check if the hasDeletions has been called
		assertTrue(indexFactory.getReaderListener().isIndexReaderHasDeletions());
		//Check if the document is marked for deletion
		assertTrue(indexFactory.getReaderListener().isIndexReaderUndeletedAll());
		//Check if the reader of the template is closed
		assertEquals(indexFactory.getReaderListener().getNumberReadersClosed(),3);

	}

	public void testLuceneWriterResourceManagement() {
		//Initialization of the index
		final SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		final MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);

		ResourceManager rm=new LuceneIndexResourceManager(indexFactory);
		ResourceTemplate rt = new ResourceTemplate(rm);

		rt.execute(new ResourceCallback() {
			public Object doWithResource() {
				assertTrue("Has thread indexFactory", ResourceBindingManager.hasResource(indexFactory));

				//Lucene template
				LuceneIndexTemplate template=new DefaultLuceneIndexTemplate(indexFactory,analyzer);
				template.addDocument(new DocumentCreator() {
					public Document createDocument() throws IOException {
						Document document=new Document();
						document.add(Field.Text("field", "a Lucene support sample"));
						document.add(Field.Text("filter", "another sample filter"));
						document.add(Field.Keyword("sort", "13"));
						return document;
					}
				});
				template.addDocument(new DocumentCreator() {
					public Document createDocument() throws IOException {
						Document document=new Document();
						document.add(Field.Text("field", "a Lucene support sample"));
						document.add(Field.Text("filter", "another sample filter"));
						document.add(Field.Keyword("sort", "13"));
						return document;
					}
				});

				return null;
			}
		});

		//Check if a reader has been opened
		assertEquals(indexFactory.getWriterListener().getNumberWritersCreated(),1);
		//Check if the number of added documents is correct
		assertEquals(indexFactory.getWriterListener().getIndexWriterAddDocuments(),2);
		//Check if the reader of the template is closed
		assertEquals(indexFactory.getWriterListener().getNumberWritersClosed(),1);

	}

	public void testNoLuceneWriterResourceManagement() {
		//Initialization of the index
		final SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		final MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);

		//Lucene template
		LuceneIndexTemplate template=new DefaultLuceneIndexTemplate(indexFactory,analyzer);
		template.addDocument(new DocumentCreator() {
			public Document createDocument() throws IOException {
				Document document=new Document();
				document.add(Field.Text("field", "a Lucene support sample"));
				document.add(Field.Text("filter", "another sample filter"));
				document.add(Field.Keyword("sort", "13"));
				return document;
			}
		});
		template.addDocument(new DocumentCreator() {
			public Document createDocument() throws IOException {
				Document document=new Document();
				document.add(Field.Text("field", "a Lucene support sample"));
				document.add(Field.Text("filter", "another sample filter"));
				document.add(Field.Keyword("sort", "13"));
				return document;
			}
		});

		//Check if a reader has been opened
		assertEquals(indexFactory.getWriterListener().getNumberWritersCreated(),2);
		//Check if the number of added documents is correct
		assertEquals(indexFactory.getWriterListener().getIndexWriterAddDocuments(),2);
		//Check if the reader of the template is closed
		assertEquals(indexFactory.getWriterListener().getNumberWritersClosed(),2);

	}

}
