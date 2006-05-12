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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.RAMDirectory;
import org.springmodules.lucene.index.DocumentHandlerException;
import org.springmodules.lucene.index.LuceneIndexAccessException;
import org.springmodules.lucene.index.factory.SimpleIndexFactory;
import org.springmodules.lucene.index.support.handler.AbstractDocumentHandler;
import org.springmodules.lucene.index.support.handler.DefaultDocumentHandlerManager;
import org.springmodules.lucene.index.support.handler.DocumentHandler;
import org.springmodules.lucene.index.support.handler.IdentityDocumentMatching;
import org.springmodules.lucene.index.support.handler.file.AbstractInputStreamDocumentHandler;
import org.springmodules.lucene.index.support.handler.file.MimeTypeDocumentHandlerManager;
import org.springmodules.lucene.search.core.DefaultLuceneSearchTemplate;
import org.springmodules.lucene.search.core.HitExtractor;
import org.springmodules.lucene.search.core.LuceneSearchTemplate;
import org.springmodules.lucene.search.factory.SearcherFactory;
import org.springmodules.lucene.search.factory.SimpleSearcherFactory;

/**
 * @author Brian McCallister
 * @author Thierry Templier
 */
public class DefaultLuceneIndexTemplateTests extends TestCase {

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
		document1.add(Field.Keyword("id", "1"));
		document1.add(Field.Text("field", "a sample"));
		document1.add(Field.Text("filter", "a sample filter"));
		document1.add(Field.Keyword("sort", "2"));
		writer.addDocument(document1);
		//Adding a document
		Document document2=new Document();
		document2.add(Field.Keyword("id", "2"));
		document2.add(Field.Text("field", "a Lucene support sample"));
		document2.add(Field.Text("filter", "another sample filter"));
		document2.add(Field.Keyword("sort", "3"));
		writer.addDocument(document2);
		//Adding a document
		Document document3=new Document();
		document3.add(Field.Keyword("id", "3"));
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
		LuceneIndexTemplate template=new DefaultLuceneIndexTemplate(indexFactory,analyzer);
		template.deleteDocument(0);

		//Check if a reader has been opened
		assertEquals(indexFactory.getReaderListener().getNumberReadersCreated(),1);
		//Check if the document is marked for deletion
		assertEquals(indexFactory.getReaderListener().getIndexReaderDeletedId(),0);
		//Check if the reader of the template is closed
		assertEquals(indexFactory.getReaderListener().getNumberReadersClosed(),1);
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
		LuceneIndexTemplate template=new DefaultLuceneIndexTemplate(indexFactory,analyzer);
		template.deleteDocuments(new Term("field","lucene"));

		//Check if a reader has been opened
		assertEquals(indexFactory.getReaderListener().getNumberReadersCreated(),1);
		//Check if the document is marked for deletion
		assertEquals(indexFactory.getReaderListener().getIndexReaderDeletedId(),1);
		//Check if the reader of the template is closed
		assertEquals(indexFactory.getReaderListener().getNumberReadersClosed(),1);
	}

	final public void testUndeleteDocuments() throws Exception {
		//Initialization of the index
		SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);

		//Lucene template
		LuceneIndexTemplate template=new DefaultLuceneIndexTemplate(indexFactory,analyzer);
		template.undeleteDocuments();

		//Check if a reader has been opened
		assertEquals(indexFactory.getReaderListener().getNumberReadersCreated(),1);
		//Check if the document is marked for deletion
		assertTrue(indexFactory.getReaderListener().isIndexReaderUndeletedAll());
		//Check if the reader of the template is closed
		assertEquals(indexFactory.getReaderListener().getNumberReadersClosed(),1);
	}

	final public void testIsDeleted() throws Exception {
		//Initialization of the index
		SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);

		//Lucene template
		LuceneIndexTemplate template=new DefaultLuceneIndexTemplate(indexFactory,analyzer);
		template.isDeleted(0);

		//Check if a reader has been opened
		assertEquals(indexFactory.getReaderListener().getNumberReadersCreated(),1);
		//Check if the document is marked for deletion
		assertEquals(indexFactory.getReaderListener().getIndexReaderIsDeleted(),0);
		//Check if the reader of the template is closed
		assertEquals(indexFactory.getReaderListener().getNumberReadersClosed(),1);
	}

	final public void testHasDeletions() throws Exception {
		//Initialization of the index
		SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);

		//Lucene template
		LuceneIndexTemplate template=new DefaultLuceneIndexTemplate(indexFactory,analyzer);
		template.hasDeletions();

		//Check if a reader has been opened
		assertEquals(indexFactory.getReaderListener().getNumberReadersCreated(),1);
		//Check if the document is marked for deletion
		assertTrue(indexFactory.getReaderListener().isIndexReaderHasDeletions());
		//Check if the reader of the template is closed
		assertEquals(indexFactory.getReaderListener().getNumberReadersClosed(),1);
	}

	final public void testGetMaxDoc() throws Exception {
		//Initialization of the index
		SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);

		//Lucene template
		LuceneIndexTemplate template=new DefaultLuceneIndexTemplate(indexFactory,analyzer);
		assertEquals(template.getMaxDoc(),3);

		//Check if a reader has been opened
		assertEquals(indexFactory.getReaderListener().getNumberReadersCreated(),1);
		//Check if the reader calls the maxDoc method
		assertTrue(indexFactory.getReaderListener().isIndexReaderMaxDoc());
		//Check if the reader of the template is closed
		assertEquals(indexFactory.getReaderListener().getNumberReadersClosed(),1);
	}

	final public void testGetNumDocs() throws Exception {
		//Initialization of the index
		SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);

		//Lucene template
		LuceneIndexTemplate template=new DefaultLuceneIndexTemplate(indexFactory,analyzer);
		assertEquals(template.getNumDocs(),3);

		//Check if a reader has been opened
		assertEquals(indexFactory.getReaderListener().getNumberReadersCreated(),1);
		//Check if the reader calls the maxDoc method
		assertTrue(indexFactory.getReaderListener().isIndexReaderNumDocs());
		//Check if the reader of the template is closed
		assertEquals(indexFactory.getReaderListener().getNumberReadersClosed(),1);
	}

	final public void testAddDocument() throws Exception {
		//Initialization of the index
		SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);

		//Lucene template
		LuceneIndexTemplate template=new DefaultLuceneIndexTemplate(indexFactory,analyzer);
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

		//Check if a writer has been opened
		assertEquals(indexFactory.getWriterListener().getNumberWritersCreated(),1);
		//Check if the writer calls the addDocument method
		assertTrue(called[0]);
		//Check if the writer calls the addDocument method
		assertEquals(indexFactory.getWriterListener().getIndexWriterAddDocuments(),1);
		//Check if the writer of the template is closed
		assertEquals(indexFactory.getWriterListener().getNumberWritersClosed(),1);
	}

	private File getFileFromClasspath(String filename) {
		URL url=getClass().getClassLoader().getResource(
					"org/springmodules/lucene/index/object/files/"+filename);
		if( url==null ) {
			return null;
		}

		return new File(url.getFile());
	}

	final public void testAddDocumentWithInputStream() throws Exception {
		//Initialization of the index
		SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);

		//File to index
		final File file=getFileFromClasspath("test.txt");

		//Lucene template
		LuceneIndexTemplate template=new DefaultLuceneIndexTemplate(indexFactory,analyzer);
		final boolean[] called = {false,false};
		template.addDocument(new InputStreamDocumentCreator() {
			public InputStream createInputStream() throws IOException {
				called[0]=true;
				return new FileInputStream(file);
			}

			public Document createDocumentFromInputStream(InputStream inputStream) throws IOException {
				called[1]=true;
				Document document=new Document();
				document.add(Field.Text("field", new InputStreamReader(inputStream)));
				return document;
			}
		});

		//Check if a writer has been opened
		assertEquals(indexFactory.getWriterListener().getNumberWritersCreated(),1);
		//Check if the writer calls the createInputStream and createDocumentFromInputStream methods
		assertTrue(called[0]);
		assertTrue(called[1]);
		//Check if the writer calls the addDocument method
		assertEquals(indexFactory.getWriterListener().getIndexWriterAddDocuments(),1);
		//Check if the writer of the template is closed
		assertEquals(indexFactory.getWriterListener().getNumberWritersClosed(),1);
	}

	final public void testAddDocumentWithInputStreamAndManager() throws Exception {
		//Initialization of the index
		SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);
		MimeTypeDocumentHandlerManager manager=new MimeTypeDocumentHandlerManager();
		manager.registerDefaultHandlers();

		//File to index
		final File file=getFileFromClasspath("test.txt");

		//Lucene template
		LuceneIndexTemplate template=new DefaultLuceneIndexTemplate(indexFactory,analyzer);
		final boolean[] called = {false,false,false};
		template.addDocument(new InputStreamDocumentCreatorWithManager(manager) {
			protected String getResourceName() {
				called[0]=true;
				return file.getPath();
			}

			protected Map getResourceDescription() {
				called[1]=true;
				Map description=new HashMap();
				description.put(AbstractInputStreamDocumentHandler.FILENAME,file.getPath());
				return description;
			}

			public InputStream createInputStream() throws IOException {
				called[2]=true;
				return new FileInputStream(file);
			}
		});

		//Check if a writer has been opened
		assertEquals(indexFactory.getWriterListener().getNumberWritersCreated(),1);
		//Check if the writer calls the getResourceName, getResourceDescription and
		//createInputStream methods
		assertTrue(called[0]);
		assertTrue(called[1]);
		assertTrue(called[2]);
		//Check if the writer calls the addDocument method
		assertEquals(indexFactory.getWriterListener().getIndexWriterAddDocuments(),1);
		//Check if the writer of the template is closed
		assertEquals(indexFactory.getWriterListener().getNumberWritersClosed(),1);
	}

	final public void testAddDocumentWithInputStreamAndManagerError() throws Exception {
		//Initialization of the index
		SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);
		DefaultDocumentHandlerManager manager=new DefaultDocumentHandlerManager();
		manager.registerDefaultHandlers();

		//File to index
		final File file=getFileFromClasspath("test.foo");

		//Lucene template
		LuceneIndexTemplate template=new DefaultLuceneIndexTemplate(indexFactory,analyzer);
		final boolean[] called = {false,false,false};
		try {
			template.addDocument(new InputStreamDocumentCreatorWithManager(manager) {
				protected String getResourceName() {
					called[0]=true;
					return file.getPath();
				}

				protected Map getResourceDescription() {
					called[1]=true;
					Map description=new HashMap();
					description.put(AbstractInputStreamDocumentHandler.FILENAME,file.getPath());
					return description;
				}

				public InputStream createInputStream() throws IOException {
					called[2]=true;
					return new FileInputStream(file);
				}
			});
			fail();
		} catch(DocumentHandlerException ex) {
			//Check if a writer has been opened
			assertEquals(indexFactory.getWriterListener().getNumberWritersCreated(),0);
			//Check if the writer calls the getResourceName, getResourceDescription and
			//createInputStream methods
			assertTrue(called[0]);
			assertFalse(called[1]);
			assertTrue(called[2]);
			//Check if the writer calls the addDocument method
			assertEquals(indexFactory.getWriterListener().getIndexWriterAddDocuments(),0);
			//Check if the writer of the template is closed
			assertEquals(indexFactory.getWriterListener().getNumberWritersClosed(),0);
		}

	}

	final public void testAddDocumentWithManager() throws Exception {
		//Initialization of the index
		SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);
		DefaultDocumentHandlerManager manager=new DefaultDocumentHandlerManager();

		//Object to index
		final String text="a text";

		final boolean[] called = {false,false};
		manager.registerDocumentHandler(new IdentityDocumentMatching("java.lang.String"),new AbstractDocumentHandler() {
			public boolean supports(Class clazz) {
				called[0]=true;
				return true;
			}

			protected Document doGetDocument(Map description, Object object) throws Exception {
				called[1]=true;
				Document document=new Document();
				document.add(Field.Text("text", (String)object));
				return document;
			}
		});
		
		//Lucene template
		LuceneIndexTemplate template=new DefaultLuceneIndexTemplate(indexFactory,analyzer);
		template.addDocument(new DocumentCreatorWithManager(manager,text));

		//Check if a writer has been opened
		assertEquals(indexFactory.getWriterListener().getNumberWritersCreated(),1);
		//Check if the writer calls the getResourceName, getResourceDescription and
		//createInputStream methods
		assertTrue(called[0]);
		assertTrue(called[1]);
		//Check if the writer calls the addDocument method
		assertEquals(indexFactory.getWriterListener().getIndexWriterAddDocuments(),1);
		//Check if the writer of the template is closed
		assertEquals(indexFactory.getWriterListener().getNumberWritersClosed(),1);
	}

	final public void testAddDocumentWithManagerError() throws Exception {
		//Initialization of the index
		SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);
		DefaultDocumentHandlerManager manager=new DefaultDocumentHandlerManager();

		//Object to index
		final String text="a text";

		final boolean[] called = {false,false};
		manager.registerDocumentHandler(new IdentityDocumentMatching("text"),new AbstractDocumentHandler() {
			public boolean supports(Class clazz) {
				called[0]=true;
				return true;
			}

			protected Document doGetDocument(Map description, Object object) throws Exception {
				called[1]=true;
				Document document=new Document();
				document.add(Field.Text("text", (String)object));
				return document;
			}
		});
		
		try {
			//Lucene template
			LuceneIndexTemplate template=new DefaultLuceneIndexTemplate(indexFactory,analyzer);
			template.addDocument(new DocumentCreatorWithManager(manager,text));
			fail();
		} catch(LuceneIndexAccessException ex) {
			//Check if a writer has been opened
			assertEquals(indexFactory.getWriterListener().getNumberWritersCreated(),0);
			//Check if the writer calls the getResourceName, getResourceDescription and
			//createInputStream methods
			assertFalse(called[0]);
			assertFalse(called[1]);
			//Check if the writer calls the addDocument method
			assertEquals(indexFactory.getWriterListener().getIndexWriterAddDocuments(),0);
			//Check if the writer of the template is closed
			assertEquals(indexFactory.getWriterListener().getNumberWritersClosed(),0);
		}
	}

	final public void testAddDocumentWithManagerAndName() throws Exception {
		//Initialization of the index
		SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);
		DefaultDocumentHandlerManager manager=new DefaultDocumentHandlerManager();

		//Object to index
		final String text="a text";

		final boolean[] called = {false,false};
		manager.registerDocumentHandler(new IdentityDocumentMatching("text"),new AbstractDocumentHandler() {
			public boolean supports(Class clazz) {
				called[0]=true;
				return true;
			}

			protected Document doGetDocument(Map description, Object object) throws Exception {
				called[1]=true;
				Document document=new Document();
				document.add(Field.Text("text", (String)object));
				return document;
			}
		});
		
		//Lucene template
		LuceneIndexTemplate template=new DefaultLuceneIndexTemplate(indexFactory,analyzer);
		template.addDocument(new DocumentCreatorWithManager(manager,"text",text));

		//Check if a writer has been opened
		assertEquals(indexFactory.getWriterListener().getNumberWritersCreated(),1);
		//Check if the writer calls the getResourceName, getResourceDescription and
		//createInputStream methods
		assertTrue(called[0]);
		assertTrue(called[1]);
		//Check if the writer calls the addDocument method
		assertEquals(indexFactory.getWriterListener().getIndexWriterAddDocuments(),1);
		//Check if the writer of the template is closed
		assertEquals(indexFactory.getWriterListener().getNumberWritersClosed(),1);

		//fail();
	}

	final public void testAddDocumentWithManagerAndNameError() throws Exception {
		//Initialization of the index
		SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);
		DefaultDocumentHandlerManager manager=new DefaultDocumentHandlerManager();

		//Object to index
		final String text="a text";

		final boolean[] called = {false,false};
		manager.registerDocumentHandler(new IdentityDocumentMatching("text1"),new AbstractDocumentHandler() {
			public boolean supports(Class clazz) {
				called[0]=true;
				return true;
			}

			protected Document doGetDocument(Map description, Object object) throws Exception {
				called[1]=true;
				Document document=new Document();
				document.add(Field.Text("text", (String)object));
				return document;
			}
		});

		try {
			//Lucene template
			LuceneIndexTemplate template=new DefaultLuceneIndexTemplate(indexFactory,analyzer);
			template.addDocument(new DocumentCreatorWithManager(manager,"text",text));
			fail();
		} catch(LuceneIndexAccessException ex) {
			//Check if a writer has been opened
			assertEquals(indexFactory.getWriterListener().getNumberWritersCreated(),0);
			//Check if the writer calls the getResourceName, getResourceDescription and
			//createInputStream methods
			assertFalse(called[0]);
			assertFalse(called[1]);
			//Check if the writer calls the addDocument method
			assertEquals(indexFactory.getWriterListener().getIndexWriterAddDocuments(),0);
			//Check if the writer of the template is closed
			assertEquals(indexFactory.getWriterListener().getNumberWritersClosed(),0);
		}
	}

	final public void testAddDocuments() throws Exception {
		//Initialization of the index
		SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);

		//Lucene template
		LuceneIndexTemplate template=new DefaultLuceneIndexTemplate(indexFactory,analyzer);
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

		//Check if a writer has been opened
		assertEquals(indexFactory.getWriterListener().getNumberWritersCreated(),1);
		//Check if the writer calls the addDocument method
		assertTrue(called[0]);
		//Check if the writer calls the addDocument method
		assertEquals(indexFactory.getWriterListener().getIndexWriterAddDocuments(),2);
		//Check if the writer of the template is closed
		assertEquals(indexFactory.getWriterListener().getNumberWritersClosed(),1);
	}

	final public void testUpdateDocument() throws Exception {
		//Initialization of the index
		SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);

		//Lucene template
		LuceneIndexTemplate template=new DefaultLuceneIndexTemplate(indexFactory,analyzer);
		final boolean[] called = {false,false};
		template.updateDocument(new DocumentModifier() {
			public Document updateDocument(Document document) throws IOException {
				called[0]=true;
				String id=document.get("id");
				String field=document.get("field");
				String filter=document.get("filter");
				String sort=document.get("sort");
				
				Document updatedDocument=new Document();
				updatedDocument.add(Field.Keyword("id", id));
				updatedDocument.add(Field.Text("field", "test"));
				updatedDocument.add(Field.Text("filter", filter));
				updatedDocument.add(Field.Keyword("sort", sort));
				return updatedDocument;
			}
		},new DocumentIdentifier() {
			public Term getIdentifier() {
				called[1]=true;
				return new Term("id","2");
			}
		});

		//Check if a writer has been opened
		assertEquals(indexFactory.getWriterListener().getNumberWritersCreated(),1);
		//Check if a writer has been opened
		assertEquals(indexFactory.getReaderListener().getNumberReadersCreated(),2);

		//Check if the writer calls the updateDocument method
		assertTrue(called[0]);
		//Check if the writer calls the getIdentifier method
		assertTrue(called[1]);

		//Check if the writer calls the addDocument method
		assertEquals(indexFactory.getWriterListener().getIndexWriterAddDocuments(),1);
		//Check if the reader calls the delete method
		assertEquals(indexFactory.getReaderListener().getIndexReaderDeletedId(),1);

		//Check if the writer of the template is closed
		assertEquals(indexFactory.getWriterListener().getNumberWritersClosed(),1);
		//Check if the writer of the template is closed
		assertEquals(indexFactory.getReaderListener().getNumberReadersClosed(),2);

		SearcherFactory searcherFactory=new SimpleSearcherFactory(indexFactory);
		LuceneSearchTemplate searchTemplate=new DefaultLuceneSearchTemplate(searcherFactory,analyzer);
		List fields=searchTemplate.search(new TermQuery(new Term("id","2")),new HitExtractor() {
			public Object mapHit(int id, Document document, float score) {
				return document.get("field");
			}
		});
		assertEquals(fields.size(),1);
		assertEquals((String)fields.get(0),"test");
	}

	final public void testUpdateDocuments() throws Exception {
		//Initialization of the index
		SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);

		//Lucene template
		LuceneIndexTemplate template=new DefaultLuceneIndexTemplate(indexFactory,analyzer);
		final boolean[] called = {false,false};
		template.updateDocuments(new DocumentsModifier() {
			public List updateDocuments(Hits hits) throws IOException {
				called[0]=true;

				List updatedDocuments=new ArrayList();
				for(int cpt=0;cpt<hits.length();cpt++) {
					Document document=hits.doc(cpt);
					String id=document.get("id");
					String field=document.get("field");
					String filter=document.get("filter");
					String sort=document.get("sort");
				
					Document updatedDocument=new Document();
					updatedDocument.add(Field.Keyword("id", id));
					updatedDocument.add(Field.Text("field", "test"));
					updatedDocument.add(Field.Text("filter", filter));
					updatedDocument.add(Field.Keyword("sort", sort));
					updatedDocuments.add(updatedDocument);
				}
				return updatedDocuments;
			}
		},new DocumentsIdentifier() {
			public Term getIdentifier() {
				called[1]=true;
				return new Term("id","2");
			}
		});

		//Check if a writer has been opened
		assertEquals(indexFactory.getWriterListener().getNumberWritersCreated(),1);
		//Check if a writer has been opened
		assertEquals(indexFactory.getReaderListener().getNumberReadersCreated(),2);

		//Check if the writer calls the updateDocument method
		assertTrue(called[0]);
		//Check if the writer calls the getIdentifier method
		assertTrue(called[1]);

		//Check if the writer calls the addDocument method
		assertEquals(indexFactory.getWriterListener().getIndexWriterAddDocuments(),1);
		//Check if the reader calls the delete method
		assertEquals(indexFactory.getReaderListener().getIndexReaderDeletedId(),1);

		//Check if the writer of the template is closed
		assertEquals(indexFactory.getWriterListener().getNumberWritersClosed(),1);
		//Check if the writer of the template is closed
		assertEquals(indexFactory.getReaderListener().getNumberReadersClosed(),2);

		SearcherFactory searcherFactory=new SimpleSearcherFactory(indexFactory);
		LuceneSearchTemplate searchTemplate=new DefaultLuceneSearchTemplate(searcherFactory,analyzer);
		List fields=searchTemplate.search(new TermQuery(new Term("id","2")),new HitExtractor() {
			public Object mapHit(int id, Document document, float score) {
				return document.get("field");
			}
		});
		assertEquals(fields.size(),1);
		assertEquals((String)fields.get(0),"test");
	}

	final public void testOptimize() throws Exception {
		//Initialization of the index
		SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);

		//Lucene template
		LuceneIndexTemplate template=new DefaultLuceneIndexTemplate(indexFactory,analyzer);
		template.optimize();

		//Check if a writer has been opened
		assertEquals(indexFactory.getWriterListener().getNumberWritersCreated(),1);
		//Check if the writer calls the optimize method
		assertTrue(indexFactory.getWriterListener().isIndexWriterOptimize());
		//Check if the writer of the template is closed
		assertEquals(indexFactory.getWriterListener().getNumberWritersClosed(),1);
	}

	final public void testRead() throws Exception {
		//Initialization of the index
		SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);

		//Lucene template
		LuceneIndexTemplate template=new DefaultLuceneIndexTemplate(indexFactory,analyzer);
		final boolean[] called = {false};
		template.read(new ReaderCallback() {
			public Object doWithReader(IndexReader reader) throws IOException {
				called[0] = true;
				assertNotNull(reader);
				return null;
			}
		});

		//Check if a reader has been opened
		assertEquals(indexFactory.getReaderListener().getNumberReadersCreated(),1);
		//Check if doWithReader is called
		assertTrue(called[0]);
		//Check if the reader of the template is closed
		assertEquals(indexFactory.getReaderListener().getNumberReadersClosed(),1);
	}

	final public void testWrite() throws Exception {
		//Initialization of the index
		SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);

		//Lucene template
		LuceneIndexTemplate template=new DefaultLuceneIndexTemplate(indexFactory,analyzer);
		final boolean[] called = {false};
		template.write(new WriterCallback() {
			public Object doWithWriter(IndexWriter writer) throws IOException {
				called[0] = true;
				assertNotNull(writer);
				return null;
			}
		});

		//Check if a writer has been opened
		assertEquals(indexFactory.getWriterListener().getNumberWritersCreated(),1);
		//Check if doWithReader is called
		assertTrue(called[0]);
		//Check if the reader of the template is closed
		assertEquals(indexFactory.getWriterListener().getNumberWritersClosed(),1);
	}

}
