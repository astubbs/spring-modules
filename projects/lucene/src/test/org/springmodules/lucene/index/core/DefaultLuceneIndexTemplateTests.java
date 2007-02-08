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

package org.springmodules.lucene.index.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.TermQuery;
import org.easymock.MockControl;
import org.easymock.classextension.MockClassControl;
import org.springmodules.lucene.AbstractLuceneTestCase;
import org.springmodules.lucene.index.LuceneIndexingException;
import org.springmodules.lucene.index.factory.IndexFactory;
import org.springmodules.lucene.index.factory.LuceneIndexReader;
import org.springmodules.lucene.index.factory.LuceneIndexWriter;
import org.springmodules.lucene.index.support.handler.AbstractDocumentHandler;
import org.springmodules.lucene.index.support.handler.DefaultDocumentHandlerManager;
import org.springmodules.lucene.index.support.handler.DocumentHandler;
import org.springmodules.lucene.index.support.handler.DocumentHandlerManager;
import org.springmodules.lucene.index.support.handler.IdentityDocumentMatching;
import org.springmodules.lucene.index.support.handler.file.AbstractInputStreamDocumentHandler;
import org.springmodules.lucene.search.factory.LuceneHits;
import org.springmodules.lucene.search.factory.LuceneSearcher;

/**
 * @author Brian McCallister
 * @author Thierry Templier
 */
public class DefaultLuceneIndexTemplateTests extends AbstractLuceneTestCase {

	/*
	 * Test for void deleteDocument(int)
	 */
	final public void testDeleteDocumentint() throws Exception {
		SimpleAnalyzer analyzer = new SimpleAnalyzer();
		MockControl indexFactoryControl = MockControl.createStrictControl(IndexFactory.class);
		IndexFactory indexFactory = (IndexFactory)indexFactoryControl.getMock();
		MockControl indexReaderControl = MockControl.createStrictControl(LuceneIndexReader.class);
		LuceneIndexReader indexReader = (LuceneIndexReader)indexReaderControl.getMock();

		indexFactory.getIndexReader();
		indexFactoryControl.setReturnValue(indexReader, 1);
		
		indexReader.deleteDocument(0);
		indexReaderControl.setVoidCallable(1);
		
		indexReader.close();
		indexReaderControl.setVoidCallable(1);
		
		indexFactoryControl.replay();
		indexReaderControl.replay();
		
		//Lucene template
		LuceneIndexTemplate template = new DefaultLuceneIndexTemplate(indexFactory, analyzer);
		template.deleteDocument(0);

		indexFactoryControl.verify();
		indexReaderControl.verify();
	}

	/*
	 * Test for void deleteDocument(Term)
	 */
	final public void testDeleteDocumentTerm() throws Exception {
		SimpleAnalyzer analyzer = new SimpleAnalyzer();
		MockControl indexFactoryControl = MockControl.createStrictControl(IndexFactory.class);
		IndexFactory indexFactory = (IndexFactory)indexFactoryControl.getMock();
		MockControl indexReaderControl = MockControl.createStrictControl(LuceneIndexReader.class);
		LuceneIndexReader indexReader = (LuceneIndexReader)indexReaderControl.getMock();

		indexFactory.getIndexReader();
		indexFactoryControl.setReturnValue(indexReader, 1);
		
		indexReader.deleteDocuments(new Term("field","lucene"));
		indexReaderControl.setReturnValue(1, 1);
		
		indexReader.close();
		indexReaderControl.setVoidCallable(1);
		
		indexFactoryControl.replay();
		indexReaderControl.replay();
		
		//Lucene template
		LuceneIndexTemplate template = new DefaultLuceneIndexTemplate(indexFactory, analyzer);
		template.deleteDocuments(new Term("field","lucene"));

		indexFactoryControl.verify();
		indexReaderControl.verify();
	}

	final public void testUndeleteDocuments() throws Exception {
		SimpleAnalyzer analyzer = new SimpleAnalyzer();
		MockControl indexFactoryControl = MockControl.createStrictControl(IndexFactory.class);
		IndexFactory indexFactory = (IndexFactory)indexFactoryControl.getMock();
		MockControl indexReaderControl = MockControl.createStrictControl(LuceneIndexReader.class);
		LuceneIndexReader indexReader = (LuceneIndexReader)indexReaderControl.getMock();

		indexFactory.getIndexReader();
		indexFactoryControl.setReturnValue(indexReader, 1);
		
		indexReader.undeleteAll();
		indexReaderControl.setVoidCallable(1);
		
		indexReader.close();
		indexReaderControl.setVoidCallable(1);
		
		indexFactoryControl.replay();
		indexReaderControl.replay();
		
		//Lucene template
		LuceneIndexTemplate template = new DefaultLuceneIndexTemplate(indexFactory, analyzer);
		template.undeleteDocuments();

		indexFactoryControl.verify();
		indexReaderControl.verify();
	}

	final public void testIsDeleted() throws Exception {
		SimpleAnalyzer analyzer = new SimpleAnalyzer();
		MockControl indexFactoryControl = MockControl.createStrictControl(IndexFactory.class);
		IndexFactory indexFactory = (IndexFactory)indexFactoryControl.getMock();
		MockControl indexReaderControl = MockControl.createStrictControl(LuceneIndexReader.class);
		LuceneIndexReader indexReader = (LuceneIndexReader)indexReaderControl.getMock();

		indexFactory.getIndexReader();
		indexFactoryControl.setReturnValue(indexReader, 1);
		
		indexReader.isDeleted(0);
		indexReaderControl.setReturnValue(true, 1);
		
		indexReader.close();
		indexReaderControl.setVoidCallable(1);
		
		indexFactoryControl.replay();
		indexReaderControl.replay();
		
		//Lucene template
		LuceneIndexTemplate template = new DefaultLuceneIndexTemplate(indexFactory, analyzer);
		boolean deleted = template.isDeleted(0);

		indexFactoryControl.verify();
		indexReaderControl.verify();
		
		assertTrue(deleted);
	}

	final public void testHasDeletions() throws Exception {
		SimpleAnalyzer analyzer = new SimpleAnalyzer();
		MockControl indexFactoryControl = MockControl.createStrictControl(IndexFactory.class);
		IndexFactory indexFactory = (IndexFactory)indexFactoryControl.getMock();
		MockControl indexReaderControl = MockControl.createStrictControl(LuceneIndexReader.class);
		LuceneIndexReader indexReader = (LuceneIndexReader)indexReaderControl.getMock();

		indexFactory.getIndexReader();
		indexFactoryControl.setReturnValue(indexReader, 1);
		
		indexReader.hasDeletions();
		indexReaderControl.setReturnValue(true, 1);
		
		indexReader.close();
		indexReaderControl.setVoidCallable(1);
		
		indexFactoryControl.replay();
		indexReaderControl.replay();
		
		//Lucene template
		LuceneIndexTemplate template = new DefaultLuceneIndexTemplate(indexFactory, analyzer);
		boolean deletions = template.hasDeletions();

		indexFactoryControl.verify();
		indexReaderControl.verify();
		
		assertTrue(deletions);
	}

	final public void testGetMaxDoc() throws Exception {
		SimpleAnalyzer analyzer = new SimpleAnalyzer();
		MockControl indexFactoryControl = MockControl.createStrictControl(IndexFactory.class);
		IndexFactory indexFactory = (IndexFactory)indexFactoryControl.getMock();
		MockControl indexReaderControl = MockControl.createStrictControl(LuceneIndexReader.class);
		LuceneIndexReader indexReader = (LuceneIndexReader)indexReaderControl.getMock();

		indexFactory.getIndexReader();
		indexFactoryControl.setReturnValue(indexReader, 1);
		
		indexReader.maxDoc();
		indexReaderControl.setReturnValue(3, 1);
		
		indexReader.close();
		indexReaderControl.setVoidCallable(1);
		
		indexFactoryControl.replay();
		indexReaderControl.replay();
		
		//Lucene template
		LuceneIndexTemplate template = new DefaultLuceneIndexTemplate(indexFactory, analyzer);
		int maxDoc = template.getMaxDoc();

		indexFactoryControl.verify();
		indexReaderControl.verify();
		
		assertEquals(maxDoc, 3);
	}

	final public void testGetNumDocs() throws Exception {
		SimpleAnalyzer analyzer = new SimpleAnalyzer();
		MockControl indexFactoryControl = MockControl.createStrictControl(IndexFactory.class);
		IndexFactory indexFactory = (IndexFactory)indexFactoryControl.getMock();
		MockControl indexReaderControl = MockControl.createStrictControl(LuceneIndexReader.class);
		LuceneIndexReader indexReader = (LuceneIndexReader)indexReaderControl.getMock();

		indexFactory.getIndexReader();
		indexFactoryControl.setReturnValue(indexReader, 1);
		
		indexReader.numDocs();
		indexReaderControl.setReturnValue(3, 1);
		
		indexReader.close();
		indexReaderControl.setVoidCallable(1);
		
		indexFactoryControl.replay();
		indexReaderControl.replay();
		
		//Lucene template
		LuceneIndexTemplate template = new DefaultLuceneIndexTemplate(indexFactory, analyzer);
		int numDoc = template.getNumDocs();

		indexFactoryControl.verify();
		indexReaderControl.verify();
		
		assertEquals(numDoc, 3);
	}

	final public void testAddDocument() throws Exception {
		SimpleAnalyzer analyzer = new SimpleAnalyzer();
		MockControl indexFactoryControl = MockControl.createStrictControl(IndexFactory.class);
		IndexFactory indexFactory = (IndexFactory)indexFactoryControl.getMock();
		MockControl indexWriterControl = MockControl.createStrictControl(LuceneIndexWriter.class);
		LuceneIndexWriter indexWriter = (LuceneIndexWriter)indexWriterControl.getMock();
		MockControl documentCreatorControl = MockControl.createStrictControl(DocumentCreator.class);
		DocumentCreator documentCreator = (DocumentCreator)documentCreatorControl.getMock();

		//document
		Document document = new Document();
		document.add(new Field("field", "a sample", Field.Store.YES, Field.Index.TOKENIZED));
		document.add(new Field("filter", "a sample filter", Field.Store.YES, Field.Index.TOKENIZED));
		document.add(new Field("sort", "2", Field.Store.YES, Field.Index.UN_TOKENIZED));
		
		indexFactory.getIndexWriter();
		indexFactoryControl.setReturnValue(indexWriter, 1);
		
		documentCreator.createDocument();
		documentCreatorControl.setReturnValue(document);
		
		indexWriter.addDocument(document);
		indexWriterControl.setVoidCallable(1);
		
		indexWriter.close();
		indexWriterControl.setVoidCallable(1);
		
		indexFactoryControl.replay();
		indexWriterControl.replay();
		documentCreatorControl.replay();
		
		//Lucene template
		LuceneIndexTemplate template = new DefaultLuceneIndexTemplate(indexFactory, analyzer);
		template.addDocument(documentCreator);

		indexFactoryControl.verify();
		indexWriterControl.verify();
		documentCreatorControl.verify();
	}

	final public void testAddDocumentWithInputStream() throws Exception {
		SimpleAnalyzer analyzer = new SimpleAnalyzer();
		MockControl indexFactoryControl = MockControl.createStrictControl(IndexFactory.class);
		IndexFactory indexFactory = (IndexFactory)indexFactoryControl.getMock();
		MockControl indexWriterControl = MockControl.createStrictControl(LuceneIndexWriter.class);
		LuceneIndexWriter indexWriter = (LuceneIndexWriter)indexWriterControl.getMock();
		MockControl documentCreatorControl = MockControl.createStrictControl(InputStreamDocumentCreator.class);
		InputStreamDocumentCreator documentCreator = (InputStreamDocumentCreator)documentCreatorControl.getMock();
		MockControl inputStreamControl = MockClassControl.createStrictControl(InputStream.class);
		InputStream inputStream = (InputStream)inputStreamControl.getMock();

		//document
		Document document = new Document();
		document.add(new Field("field", "a sample", Field.Store.YES, Field.Index.TOKENIZED));
		document.add(new Field("filter", "a sample filter", Field.Store.YES, Field.Index.TOKENIZED));
		document.add(new Field("sort", "2", Field.Store.YES, Field.Index.UN_TOKENIZED));

		indexFactory.getIndexWriter();
		indexFactoryControl.setReturnValue(indexWriter, 1);
		
		documentCreator.createInputStream();
		documentCreatorControl.setReturnValue(inputStream, 1);
		
		documentCreator.createDocumentFromInputStream(inputStream);
		documentCreatorControl.setReturnValue(document, 1);
		
		indexWriter.addDocument(document);
		indexWriterControl.setVoidCallable(1);
		
		inputStream.close();
		inputStreamControl.setVoidCallable(1);
		
		indexWriter.close();
		indexWriterControl.setVoidCallable(1);
		
		indexFactoryControl.replay();
		indexWriterControl.replay();
		documentCreatorControl.replay();
		inputStreamControl.replay();
		
		//Lucene template
		LuceneIndexTemplate template = new DefaultLuceneIndexTemplate(indexFactory, analyzer);
		template.addDocument(documentCreator);

		indexFactoryControl.verify();
		indexWriterControl.verify();
		documentCreatorControl.verify();
		inputStreamControl.verify();
	}

	private File getFileFromClasspath(String filename) {
		URL url = getClass().getClassLoader().getResource(
				"org/springmodules/lucene/index/object/files/"+filename);
		if( url==null ) {
			return null;
		}
		return new File(url.getFile());
	}

	final public void testAddDocumentWithInputStreamAndManager() throws Exception {
		//Initialization of the index
		SimpleAnalyzer analyzer = new SimpleAnalyzer();
		MockControl indexFactoryControl = MockControl.createStrictControl(IndexFactory.class);
		IndexFactory indexFactory = (IndexFactory)indexFactoryControl.getMock();
		MockControl indexWriterControl = MockControl.createStrictControl(LuceneIndexWriter.class);
		LuceneIndexWriter indexWriter = (LuceneIndexWriter)indexWriterControl.getMock();
		MockControl documentHandlerManagerControl = MockControl.createStrictControl(DocumentHandlerManager.class);
		DocumentHandlerManager documentHandlerManager = (DocumentHandlerManager)documentHandlerManagerControl.getMock();
		MockControl documentHandlerControl = MockControl.createStrictControl(DocumentHandler.class);
		DocumentHandler documentHandler = (DocumentHandler)documentHandlerControl.getMock();
		MockControl inputStreamControl = MockClassControl.createStrictControl(InputStream.class);
		final InputStream inputStream = (InputStream)inputStreamControl.getMock();

		//file
		final File file = getFileFromClasspath("test.txt");
		final Map description = new HashMap();
		description.put(AbstractInputStreamDocumentHandler.FILENAME, file.getPath());
		//final FileInputStream inputStream = new FileInputStream(file);

		//document
		Document document=new Document();
		document.add(new Field("field", "a sample", Field.Store.YES, Field.Index.TOKENIZED));
		document.add(new Field("filter", "a sample filter", Field.Store.YES, Field.Index.TOKENIZED));
		document.add(new Field("sort", "2", Field.Store.YES, Field.Index.UN_TOKENIZED));

		indexFactory.getIndexWriter();
		indexFactoryControl.setReturnValue(indexWriter, 1);

		//DocumentHandler documentHandler = documentHandlerManager.getDocumentHandler(getResourceName());
		documentHandlerManager.getDocumentHandler(file.getPath());
		documentHandlerManagerControl.setReturnValue(documentHandler, 1);
		//Document document = documentHandler.getDocument(getResourceDescription(),inputStream);
		documentHandler.getDocument(description, inputStream);
		documentHandlerControl.setReturnValue(document, 1);
		
		inputStream.close();
		inputStreamControl.setVoidCallable(1);
		
		indexWriter.addDocument(document);
		indexWriterControl.setVoidCallable(1);
		
		indexWriter.close();
		indexWriterControl.setVoidCallable(1);
		
		indexFactoryControl.replay();
		indexWriterControl.replay();
		documentHandlerManagerControl.replay();
		documentHandlerControl.replay();
		inputStreamControl.replay();
		
		//Lucene template
		LuceneIndexTemplate template = new DefaultLuceneIndexTemplate(indexFactory,analyzer);
		final boolean[] called = {false, false, false};
		template.addDocument(new InputStreamDocumentCreatorWithManager(documentHandlerManager) {
			protected String getResourceName() {
				called[0] = true;
				return file.getPath();
			}

			protected Map getResourceDescription() {
				called[1] = true;
				return description;
			}

			public InputStream createInputStream() throws IOException {
				called[2] = true;
				return inputStream;
			}
		});

		indexFactoryControl.verify();
		indexWriterControl.verify();
		inputStreamControl.verify();
		documentHandlerManagerControl.verify();
		documentHandlerControl.verify();
		inputStreamControl.verify();
		
		assertTrue(called[0]);
		assertTrue(called[1]);
		assertTrue(called[2]);
	}

	final public void testAddDocumentWithInputStreamAndManagerError() throws Exception {
		SimpleAnalyzer analyzer = new SimpleAnalyzer();
		MockControl indexFactoryControl = MockControl.createStrictControl(IndexFactory.class);
		IndexFactory indexFactory = (IndexFactory)indexFactoryControl.getMock();
		MockControl indexWriterControl = MockControl.createStrictControl(LuceneIndexWriter.class);
		LuceneIndexWriter indexWriter = (LuceneIndexWriter)indexWriterControl.getMock();
		MockControl documentHandlerManagerControl = MockControl.createStrictControl(DocumentHandlerManager.class);
		DocumentHandlerManager documentHandlerManager = (DocumentHandlerManager)documentHandlerManagerControl.getMock();
		MockControl documentHandlerControl = MockControl.createStrictControl(DocumentHandler.class);
		DocumentHandler documentHandler = (DocumentHandler)documentHandlerControl.getMock();
		MockControl inputStreamControl = MockClassControl.createStrictControl(InputStream.class);
		final InputStream inputStream = (InputStream)inputStreamControl.getMock();

		//file
		final File file = getFileFromClasspath("test.foo");
		final Map description = new HashMap();
		description.put(AbstractInputStreamDocumentHandler.FILENAME, file.getPath());
		//final FileInputStream inputStream = new FileInputStream(file);

		//document
		Document document = new Document();
		document.add(new Field("field", "a sample", Field.Store.YES, Field.Index.TOKENIZED));
		document.add(new Field("filter", "a sample filter", Field.Store.YES, Field.Index.TOKENIZED));
		document.add(new Field("sort", "2", Field.Store.YES, Field.Index.UN_TOKENIZED));

		//indexFactory.getIndexWriter();
		//indexFactoryControl.setReturnValue(indexWriter, 1);

		//DocumentHandler documentHandler = documentHandlerManager.getDocumentHandler(getResourceName());
		documentHandlerManager.getDocumentHandler(file.getPath());
		documentHandlerManagerControl.setReturnValue(null, 1);
		//Document document = documentHandler.getDocument(getResourceDescription(),inputStream);
		/*documentHandler.getDocument(description, inputStream);
		documentHandlerControl.setReturnValue(document, 1);*/
		
		/*inputStream.close();
		inputStreamControl.setVoidCallable(1);
		
		indexWriter.addDocument(document);
		indexWriterControl.setVoidCallable(1);
		
		indexWriter.close();
		indexWriterControl.setVoidCallable(1);*/
		
		inputStream.close();
		inputStreamControl.setVoidCallable(1);
		
		indexFactoryControl.replay();
		indexWriterControl.replay();
		documentHandlerManagerControl.replay();
		documentHandlerControl.replay();
		inputStreamControl.replay();
		
		//Lucene template
		final boolean[] called = {false, false, false};
		try {
			LuceneIndexTemplate template = new DefaultLuceneIndexTemplate(indexFactory, analyzer);
			template.addDocument(new InputStreamDocumentCreatorWithManager(documentHandlerManager) {
				protected String getResourceName() {
					called[0] = true;
					return file.getPath();
				}
	
				protected Map getResourceDescription() {
					called[1] = true;
					return description;
				}
	
				public InputStream createInputStream() throws IOException {
					called[2] = true;
					return inputStream;
				}
			});
			fail();
		} catch(LuceneIndexingException ex) {
		}

		indexFactoryControl.verify();
		indexWriterControl.verify();
		inputStreamControl.verify();
		documentHandlerManagerControl.verify();
		documentHandlerControl.verify();
		inputStreamControl.verify();
		
		assertTrue(called[0]);
		assertFalse(called[1]);
		assertTrue(called[2]);
	}

	final public void testAddDocumentWithManager() throws Exception {
		SimpleAnalyzer analyzer = new SimpleAnalyzer();
		MockControl indexFactoryControl = MockControl.createStrictControl(IndexFactory.class);
		IndexFactory indexFactory = (IndexFactory)indexFactoryControl.getMock();
		MockControl indexWriterControl = MockControl.createStrictControl(LuceneIndexWriter.class);
		LuceneIndexWriter indexWriter = (LuceneIndexWriter)indexWriterControl.getMock();

		DefaultDocumentHandlerManager manager = new DefaultDocumentHandlerManager();

		//Object to index
		final String text = "a text";

		//document
		final Document document = new Document();
		document.add(new Field("text", text, Field.Store.YES, Field.Index.TOKENIZED));


		final boolean[] called = {false, false};
		manager.registerDocumentHandler(new IdentityDocumentMatching("java.lang.String"), new AbstractDocumentHandler() {
			public boolean supports(Class clazz) {
				called[0]=true;
				return true;
			}

			protected Document doGetDocument(Map description, Object object) throws Exception {
				called[1]=true;
				return document;
			}
		});
		
		indexFactory.getIndexWriter();
		indexFactoryControl.setReturnValue(indexWriter, 1);

		indexWriter.addDocument(document);
		indexWriterControl.setVoidCallable(1);
		
		indexWriter.close();
		indexWriterControl.setVoidCallable(1);
		
		indexFactoryControl.replay();
		indexWriterControl.replay();
		
		//Lucene template
		LuceneIndexTemplate template=new DefaultLuceneIndexTemplate(indexFactory,analyzer);
		template.addDocument(new DocumentCreatorWithManager(manager, text));

		//Check if a writer has been opened
		//Check if the writer calls the addDocument method
		//Check if the writer of the template is closed
		indexFactoryControl.verify();
		indexWriterControl.verify();
		
		//Check if the writer calls the getResourceName, getResourceDescription and
		//createInputStream methods
		assertTrue(called[0]);
		assertTrue(called[1]);
	}

	final public void testAddDocumentWithManagerError() throws Exception {
		SimpleAnalyzer analyzer = new SimpleAnalyzer();
		MockControl indexFactoryControl = MockControl.createStrictControl(IndexFactory.class);
		IndexFactory indexFactory = (IndexFactory)indexFactoryControl.getMock();
		MockControl indexWriterControl = MockControl.createStrictControl(LuceneIndexWriter.class);
		LuceneIndexWriter indexWriter = (LuceneIndexWriter)indexWriterControl.getMock();

		DefaultDocumentHandlerManager manager=new DefaultDocumentHandlerManager();

		//Object to index
		final String text="a text";

		//document
		final Document document = new Document();
		document.add(new Field("text", text, Field.Store.YES, Field.Index.TOKENIZED));

		final boolean[] called = {false, false};
		manager.registerDocumentHandler(new IdentityDocumentMatching("text"), new AbstractDocumentHandler() {
			public boolean supports(Class clazz) {
				called[0] = true;
				return true;
			}

			protected Document doGetDocument(Map description, Object object) throws Exception {
				called[1] = true;
				return document;
			}
		});
		
		indexFactoryControl.replay();
		indexWriterControl.replay();

		try {
			//Lucene template
			LuceneIndexTemplate template=new DefaultLuceneIndexTemplate(indexFactory,analyzer);
			template.addDocument(new DocumentCreatorWithManager(manager, text));
			fail();
		} catch(Exception ex) {
			//Check if a writer has been opened
			//Check if the writer calls the addDocument method
			//Check if the writer of the template is closed
			indexFactoryControl.verify();
			indexWriterControl.verify();
			
			//Check if the writer calls the getResourceName, getResourceDescription and
			//createInputStream methods
			assertFalse(called[0]);
			assertFalse(called[1]);
		}
	}

	final public void testAddDocumentWithManagerAndName() throws Exception {
		SimpleAnalyzer analyzer = new SimpleAnalyzer();
		MockControl indexFactoryControl = MockControl.createStrictControl(IndexFactory.class);
		IndexFactory indexFactory = (IndexFactory)indexFactoryControl.getMock();
		MockControl indexWriterControl = MockControl.createStrictControl(LuceneIndexWriter.class);
		LuceneIndexWriter indexWriter = (LuceneIndexWriter)indexWriterControl.getMock();

		DefaultDocumentHandlerManager manager=new DefaultDocumentHandlerManager();

		//Object to index
		final String text="a text";

		//document
		final Document document = new Document();
		document.add(new Field("text", text, Field.Store.YES, Field.Index.TOKENIZED));

		final boolean[] called = {false, false};
		manager.registerDocumentHandler(new IdentityDocumentMatching("text"), new AbstractDocumentHandler() {
			public boolean supports(Class clazz) {
				called[0] = true;
				return true;
			}

			protected Document doGetDocument(Map description, Object object) throws Exception {
				called[1] = true;
				return document;
			}
		});
		
		indexFactory.getIndexWriter();
		indexFactoryControl.setReturnValue(indexWriter, 1);

		indexWriter.addDocument(document);
		indexWriterControl.setVoidCallable(1);
		
		indexWriter.close();
		indexWriterControl.setVoidCallable(1);
		
		indexFactoryControl.replay();
		indexWriterControl.replay();
		
		//Lucene template
		LuceneIndexTemplate template = new DefaultLuceneIndexTemplate(indexFactory, analyzer);
		template.addDocument(new DocumentCreatorWithManager(manager, "text", text));

		//Check if a writer has been opened
		//Check if the writer calls the addDocument method
		//Check if the writer of the template is closed
		indexFactoryControl.verify();
		indexWriterControl.verify();
		
		//Check if the writer calls the getResourceName, getResourceDescription and
		//createInputStream methods
		assertTrue(called[0]);
		assertTrue(called[1]);
	}

	final public void testAddDocumentWithManagerAndNameError() throws Exception {
		SimpleAnalyzer analyzer = new SimpleAnalyzer();
		MockControl indexFactoryControl = MockControl.createStrictControl(IndexFactory.class);
		IndexFactory indexFactory = (IndexFactory)indexFactoryControl.getMock();
		MockControl indexWriterControl = MockControl.createStrictControl(LuceneIndexWriter.class);
		LuceneIndexWriter indexWriter = (LuceneIndexWriter)indexWriterControl.getMock();

		DefaultDocumentHandlerManager manager=new DefaultDocumentHandlerManager();

		//Object to index
		final String text="a text";

		//document
		final Document document = new Document();
		document.add(new Field("text", text, Field.Store.YES, Field.Index.TOKENIZED));

		final boolean[] called = {false, false};
		manager.registerDocumentHandler(new IdentityDocumentMatching("text1"), new AbstractDocumentHandler() {
			public boolean supports(Class clazz) {
				called[0] = true;
				return true;
			}

			protected Document doGetDocument(Map description, Object object) throws Exception {
				called[1] = true;
				return document;
			}
		});
		
		indexFactoryControl.replay();
		indexWriterControl.replay();

		try {
			//Lucene template
			LuceneIndexTemplate template = new DefaultLuceneIndexTemplate(indexFactory, analyzer);
			template.addDocument(new DocumentCreatorWithManager(manager, "text", text));
			fail();
		} catch(Exception ex) {
			//Check if a writer has been opened
			//Check if the writer calls the addDocument method
			//Check if the writer of the template is closed
			indexFactoryControl.verify();
			indexWriterControl.verify();
			
			//Check if the writer calls the getResourceName, getResourceDescription and
			//createInputStream methods
			assertFalse(called[0]);
			assertFalse(called[1]);
		}
	}

	final public void testAddDocuments() throws Exception {
		SimpleAnalyzer analyzer = new SimpleAnalyzer();
		MockControl indexFactoryControl = MockControl.createStrictControl(IndexFactory.class);
		IndexFactory indexFactory = (IndexFactory)indexFactoryControl.getMock();
		MockControl indexWriterControl = MockControl.createStrictControl(LuceneIndexWriter.class);
		LuceneIndexWriter indexWriter = (LuceneIndexWriter)indexWriterControl.getMock();
		MockControl documentsCreatorControl = MockControl.createStrictControl(DocumentsCreator.class);
		DocumentsCreator documentsCreator = (DocumentsCreator)documentsCreatorControl.getMock();
		
		//documents
		List documents = new ArrayList();
		Document document1 = new Document();
		document1.add(new Field("field", "a sample", Field.Store.YES, Field.Index.TOKENIZED));
		document1.add(new Field("filter", "a sample filter", Field.Store.YES, Field.Index.TOKENIZED));
		document1.add(new Field("sort", "2", Field.Store.YES, Field.Index.UN_TOKENIZED));
		documents.add(document1);

		indexFactory.getIndexWriter();
		indexFactoryControl.setReturnValue(indexWriter, 1);
		
		documentsCreator.createDocuments();
		documentsCreatorControl.setReturnValue(documents, 1);
		
		indexWriter.addDocument(document1);
		indexWriterControl.setVoidCallable();
		
		indexWriter.close();
		indexWriterControl.setVoidCallable(1);
		
		indexFactoryControl.replay();
		indexWriterControl.replay();
		documentsCreatorControl.replay();
		
		//Lucene template
		LuceneIndexTemplate template = new DefaultLuceneIndexTemplate(indexFactory,analyzer);
		template.addDocuments(documentsCreator);

		indexFactoryControl.verify();
		indexWriterControl.verify();
		documentsCreatorControl.verify();
	}

	final public void testUpdateDocument() throws Exception {
		SimpleAnalyzer analyzer = new SimpleAnalyzer();
		MockControl indexFactoryControl = MockControl.createStrictControl(IndexFactory.class);
		IndexFactory indexFactory = (IndexFactory)indexFactoryControl.getMock();
		MockControl indexWriterControl = MockControl.createStrictControl(LuceneIndexWriter.class);
		LuceneIndexWriter indexWriter = (LuceneIndexWriter)indexWriterControl.getMock();
		MockControl indexReaderControl = MockControl.createStrictControl(LuceneIndexReader.class);
		LuceneIndexReader indexReader = (LuceneIndexReader)indexReaderControl.getMock();
		MockControl searcherControl = MockControl.createStrictControl(LuceneSearcher.class);
		LuceneSearcher searcher = (LuceneSearcher)searcherControl.getMock();
		MockControl documentModifierControl = MockControl.createStrictControl(DocumentModifier.class);
		DocumentModifier documentModifier = (DocumentModifier)documentModifierControl.getMock();
		MockControl hitsControl = MockControl.createStrictControl(LuceneHits.class);
		LuceneHits hits = (LuceneHits)hitsControl.getMock();
		
		//document
		Document document = new Document();
		document.add(new Field("field", "a sample", Field.Store.YES, Field.Index.TOKENIZED));
		document.add(new Field("filter", "a sample filter", Field.Store.YES, Field.Index.TOKENIZED));
		document.add(new Field("sort", "2", Field.Store.YES, Field.Index.UN_TOKENIZED));

		Term term = new Term("id","2"); 
		
		indexFactory.getIndexReader();
		indexFactoryControl.setReturnValue(indexReader, 1);
		
		indexReader.createSearcher();
		indexReaderControl.setReturnValue(searcher);
		
		searcher.search(new TermQuery(term));
		searcherControl.setReturnValue(hits);
		
		hits.length();
		hitsControl.setReturnValue(1, 2);
		
		hits.doc(0);
		hitsControl.setReturnValue(document);
		
		documentModifier.updateDocument(document);
		documentModifierControl.setReturnValue(document, 1);
		
		searcher.close();
		searcherControl.setVoidCallable(1);

		indexReader.close();
		indexReaderControl.setVoidCallable(1);

		indexFactory.getIndexReader();
		indexFactoryControl.setReturnValue(indexReader, 1);
		
		indexReader.deleteDocuments(term);
		indexReaderControl.setReturnValue(1, 1);
		
		indexReader.close();
		indexReaderControl.setVoidCallable(1);
		
		indexFactory.getIndexWriter();
		indexFactoryControl.setReturnValue(indexWriter, 1);
		
		indexWriter.addDocument(document);
		indexWriterControl.setVoidCallable();
		
		indexWriter.close();
		indexWriterControl.setVoidCallable(1);
		
		indexFactoryControl.replay();
		indexReaderControl.replay();
		indexWriterControl.replay();
		searcherControl.replay();
		documentModifierControl.replay();
		hitsControl.replay();
		
		//Lucene template
		LuceneIndexTemplate template = new DefaultLuceneIndexTemplate(indexFactory, analyzer);
		template.updateDocument(term, documentModifier);

		indexFactoryControl.verify();
		indexReaderControl.verify();
		indexWriterControl.verify();
		searcherControl.verify();
		documentModifierControl.verify();
		hitsControl.verify();
	}

	final public void testUpdateDocuments() throws Exception {
		SimpleAnalyzer analyzer = new SimpleAnalyzer();
		MockControl indexFactoryControl = MockControl.createStrictControl(IndexFactory.class);
		IndexFactory indexFactory = (IndexFactory)indexFactoryControl.getMock();
		MockControl indexWriterControl = MockControl.createStrictControl(LuceneIndexWriter.class);
		LuceneIndexWriter indexWriter = (LuceneIndexWriter)indexWriterControl.getMock();
		MockControl indexReaderControl = MockControl.createStrictControl(LuceneIndexReader.class);
		LuceneIndexReader indexReader = (LuceneIndexReader)indexReaderControl.getMock();
		MockControl searcherControl = MockControl.createStrictControl(LuceneSearcher.class);
		LuceneSearcher searcher = (LuceneSearcher)searcherControl.getMock();
		MockControl documentsModifierControl = MockControl.createStrictControl(DocumentsModifier.class);
		DocumentsModifier documentsModifier = (DocumentsModifier)documentsModifierControl.getMock();
		MockControl hitsControl = MockControl.createStrictControl(LuceneHits.class);
		LuceneHits hits = (LuceneHits)hitsControl.getMock();
		
		//document
		Document document = new Document();
		document.add(new Field("field", "a sample", Field.Store.YES, Field.Index.TOKENIZED));
		document.add(new Field("filter", "a sample filter", Field.Store.YES, Field.Index.TOKENIZED));
		document.add(new Field("sort", "2", Field.Store.YES, Field.Index.UN_TOKENIZED));

		Term term = new Term("id","2");
		List documents = new ArrayList();
		
		indexFactory.getIndexReader();
		indexFactoryControl.setReturnValue(indexReader, 1);
		
		indexReader.createSearcher();
		indexReaderControl.setReturnValue(searcher);
		
		searcher.search(new TermQuery(term));
		searcherControl.setReturnValue(hits);
		
		documentsModifier.updateDocuments(hits);
		documentsModifierControl.setReturnValue(documents, 1);
		
		searcher.close();
		searcherControl.setVoidCallable(1);

		indexReader.close();
		indexReaderControl.setVoidCallable(1);

		indexFactory.getIndexReader();
		indexFactoryControl.setReturnValue(indexReader, 1);
		
		indexReader.deleteDocuments(term);
		indexReaderControl.setReturnValue(1, 1);
		
		indexReader.close();
		indexReaderControl.setVoidCallable(1);
		
		indexFactory.getIndexWriter();
		indexFactoryControl.setReturnValue(indexWriter, 1);
		
		indexWriter.addDocument(document);
		indexWriterControl.setVoidCallable();
		
		indexWriter.close();
		indexWriterControl.setVoidCallable(1);
		
		indexFactoryControl.replay();
		indexReaderControl.replay();
		indexWriterControl.replay();
		searcherControl.replay();
		documentsModifierControl.replay();
		hitsControl.replay();
	}

	final public void testOptimize() throws Exception {
		SimpleAnalyzer analyzer = new SimpleAnalyzer();
		MockControl indexFactoryControl = MockControl.createStrictControl(IndexFactory.class);
		IndexFactory indexFactory = (IndexFactory)indexFactoryControl.getMock();
		MockControl indexWriterControl = MockControl.createStrictControl(LuceneIndexWriter.class);
		LuceneIndexWriter indexWriter = (LuceneIndexWriter)indexWriterControl.getMock();
		
		indexFactory.getIndexWriter();
		indexFactoryControl.setReturnValue(indexWriter, 1);
		
		indexWriter.optimize();
		indexWriterControl.setVoidCallable(1);
		
		indexWriter.close();
		indexWriterControl.setVoidCallable(1);
		
		indexFactoryControl.replay();
		indexWriterControl.replay();
		
		//Lucene template
		LuceneIndexTemplate template = new DefaultLuceneIndexTemplate(indexFactory, analyzer);
		template.optimize();

		indexFactoryControl.verify();
		indexWriterControl.verify();
	}

	/*
	 * Test for Object read(ReaderCallback)
	 */
	final public void testRead() throws Exception {
		SimpleAnalyzer analyzer = new SimpleAnalyzer();
		MockControl indexFactoryControl = MockControl.createStrictControl(IndexFactory.class);
		IndexFactory indexFactory = (IndexFactory)indexFactoryControl.getMock();
		MockControl indexReaderControl = MockControl.createStrictControl(LuceneIndexReader.class);
		LuceneIndexReader indexReader = (LuceneIndexReader)indexReaderControl.getMock();
		MockControl readerCallbackControl = MockControl.createStrictControl(ReaderCallback.class);
		ReaderCallback readerCallback = (ReaderCallback)readerCallbackControl.getMock();
		
		indexFactory.getIndexReader();
		indexFactoryControl.setReturnValue(indexReader, 1);
		
		readerCallback.doWithReader(indexReader);
		readerCallbackControl.setReturnValue("return", 1);
		
		indexReader.close();
		indexReaderControl.setVoidCallable(1);
		
		indexFactoryControl.replay();
		indexReaderControl.replay();
		readerCallbackControl.replay();
		
		//Lucene template
		LuceneIndexTemplate template = new DefaultLuceneIndexTemplate(indexFactory, analyzer);
		Object ret = template.read(readerCallback);

		indexFactoryControl.verify();
		indexReaderControl.verify();
		readerCallbackControl.verify();
		
		assertEquals(ret, "return");
	}

	/*
	 * Test for Object write(WriterCallback)
	 */
	final public void testWrite() throws Exception {
		SimpleAnalyzer analyzer = new SimpleAnalyzer();
		MockControl indexFactoryControl = MockControl.createStrictControl(IndexFactory.class);
		IndexFactory indexFactory = (IndexFactory)indexFactoryControl.getMock();
		MockControl indexWriterControl = MockControl.createStrictControl(LuceneIndexWriter.class);
		LuceneIndexWriter indexWriter = (LuceneIndexWriter)indexWriterControl.getMock();
		MockControl writerCallbackControl = MockControl.createStrictControl(WriterCallback.class);
		WriterCallback writerCallback = (WriterCallback)writerCallbackControl.getMock();
		
		indexFactory.getIndexWriter();
		indexFactoryControl.setReturnValue(indexWriter, 1);
		
		writerCallback.doWithWriter(indexWriter);
		writerCallbackControl.setReturnValue("return", 1);
		
		indexWriter.close();
		indexWriterControl.setVoidCallable(1);
		
		indexFactoryControl.replay();
		indexWriterControl.replay();
		writerCallbackControl.replay();
		
		//Lucene template
		LuceneIndexTemplate template = new DefaultLuceneIndexTemplate(indexFactory, analyzer);
		Object ret = template.write(writerCallback);

		indexFactoryControl.verify();
		indexWriterControl.verify();
		writerCallbackControl.verify();
		
		assertEquals(ret, "return");
	}

}
