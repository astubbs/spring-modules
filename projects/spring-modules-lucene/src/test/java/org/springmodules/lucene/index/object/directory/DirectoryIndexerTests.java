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

package org.springmodules.lucene.index.object.directory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.store.RAMDirectory;
import org.easymock.AbstractMatcher;
import org.easymock.MockControl;
import org.springmodules.lucene.index.LuceneIndexingException;
import org.springmodules.lucene.index.document.handler.DocumentHandler;
import org.springmodules.lucene.index.document.handler.DocumentMatching;
import org.springmodules.lucene.index.document.handler.file.ExtensionDocumentMatching;
import org.springmodules.lucene.index.document.handler.file.TextDocumentHandler;
import org.springmodules.lucene.index.factory.IndexFactory;
import org.springmodules.lucene.index.factory.LuceneIndexWriter;

/**
 * @author Thierry Templier
 */
public class DirectoryIndexerTests extends TestCase {

	private RAMDirectory directory;

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		//Initialization of the index
		this.directory = new RAMDirectory();
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		this.directory = null;
	}

	final public void testRegisterDocumentHandler() {
		SimpleAnalyzer analyzer = new SimpleAnalyzer();
		MockControl indexFactoryControl = MockControl.createControl(IndexFactory.class);
		IndexFactory indexFactory = (IndexFactory)indexFactoryControl.getMock();

		indexFactoryControl.replay();
		
		//Indexer
		DefaultDirectoryIndexer indexer = new DefaultDirectoryIndexer(indexFactory);

		//Register a document handler
		DocumentMatching matching = new DocumentMatching() {
			public boolean match(String name) {
				return name.equals("test");
			}
		};

		//Check that the document handler test is not found
		try {
			assertNull(indexer.getDocumentHandler("test"));
			fail();
		} catch(Exception ex) {}

		//Register a document handler
		indexer.registerDocumentHandler(matching,new DocumentHandler() {
			public Document getDocument(Map description, Object object) throws IOException {
				return null;
			}

			public boolean supports(Class clazz) {
				return true;
			}
		});

		//Check that the document handler test is found now
		try {
			assertNotNull(indexer.getDocumentHandler("test"));
		} catch(Exception ex) {
			fail();
		}

		indexFactoryControl.verify();
	}

	final public void testUnregisterDocumentHandler() {
		SimpleAnalyzer analyzer = new SimpleAnalyzer();
		MockControl indexFactoryControl = MockControl.createControl(IndexFactory.class);
		IndexFactory indexFactory = (IndexFactory)indexFactoryControl.getMock();

		indexFactoryControl.replay();
		
		//Indexer
		DefaultDirectoryIndexer indexer=new DefaultDirectoryIndexer(indexFactory);

		//Register a document handler
		DocumentMatching matching=new DocumentMatching() {
			public boolean match(String name) {
				return name.equals("test");
			}
		};

		//Register a document handler
		indexer.registerDocumentHandler(matching,new DocumentHandler() {
			public Document getDocument(Map description, Object object) throws IOException {
				return null;
			}

			public boolean supports(Class clazz) {
				return true;
			}
		});

		//Check that the document handler test is found
		try {
			assertNotNull(indexer.getDocumentHandler("test"));
		} catch(Exception ex) {
			fail();
		}

		//Unregister a document handler
		indexer.unregisterDocumentHandler(matching);

		//Check that the document handler test is not found now
		try {
			assertNull(indexer.getDocumentHandler("test"));
			fail();
		} catch(Exception ex) {}

		indexFactoryControl.verify();
	}

	final public void testAddListener() {
		SimpleAnalyzer analyzer = new SimpleAnalyzer();
		MockControl indexFactoryControl = MockControl.createControl(IndexFactory.class);
		IndexFactory indexFactory = (IndexFactory)indexFactoryControl.getMock();

		indexFactoryControl.replay();
		
		//Indexer
		DefaultDirectoryIndexer indexer=new DefaultDirectoryIndexer(indexFactory);

		//Register a document handler
		assertEquals(indexer.getListeners().size(), 0);

		FileDocumentIndexingListener listener = new FileDocumentIndexingListenerAdapter();
		indexer.addListener(listener);
		assertEquals(indexer.getListeners().size(), 1);
		FileDocumentIndexingListener tmpListener = (FileDocumentIndexingListener)indexer.getListeners().get(0);
		assertEquals(listener, tmpListener);

		indexFactoryControl.verify();
	}

	final public void testRemoveListener() {
		SimpleAnalyzer analyzer = new SimpleAnalyzer();
		MockControl indexFactoryControl = MockControl.createControl(IndexFactory.class);
		IndexFactory indexFactory = (IndexFactory)indexFactoryControl.getMock();

		indexFactoryControl.replay();
		
		//Indexer
		DefaultDirectoryIndexer indexer = new DefaultDirectoryIndexer(indexFactory);

		//Register a document handler
		FileDocumentIndexingListener listener = new FileDocumentIndexingListenerAdapter();
		indexer.addListener(listener);
		assertEquals(indexer.getListeners().size(), 1);
		FileDocumentIndexingListener tmpListener = (FileDocumentIndexingListener)indexer.getListeners().get(0);
		assertEquals(listener, tmpListener);

		//Unregister a document handler
		indexer.removeListener(listener);
		assertEquals(indexer.getListeners().size(), 0);

		indexFactoryControl.verify();
	}

	private File getBaseDirectoryToIndex() {
		URL url = getClass().getClassLoader().getResource(
					"org/springmodules/lucene/index/object/files/");
		if( url==null ) {
			return null;
		}

		String filename = url.getFile();
		return new File(filename);
	}

	private File getFileFromClasspath(String filename) {
		URL url = getClass().getClassLoader().getResource(
					"org/springmodules/lucene/index/object/files/"+filename);
		if( url==null ) {
			return null;
		}

		return new File(url.getFile());
	}

	/*
	 * Test pour void index(String)
	 */
	final public void testIndexString() throws Exception {
		SimpleAnalyzer analyzer = new SimpleAnalyzer();
		MockControl indexFactoryControl = MockControl.createStrictControl(IndexFactory.class);
		IndexFactory indexFactory = (IndexFactory)indexFactoryControl.getMock(); 
		MockControl indexWriterControl = MockControl.createStrictControl(LuceneIndexWriter.class);
		LuceneIndexWriter indexWriter = (LuceneIndexWriter)indexWriterControl.getMock(); 
		MockControl listenerControl = MockControl.createStrictControl(FileDocumentIndexingListener.class);
		FileDocumentIndexingListener listener = (FileDocumentIndexingListener)listenerControl.getMock();

		//document
		Document document = new Document();
		document.add(new Field("field", "a sample", Field.Store.YES, Field.Index.TOKENIZED));
		document.add(new Field("filter", "a sample filter", Field.Store.YES, Field.Index.TOKENIZED));
		document.add(new Field("sort", "2", Field.Store.YES, Field.Index.UN_TOKENIZED));

		indexFactory.getIndexWriter();
		indexFactoryControl.setReturnValue(indexWriter, 1);
		
		listener.beforeIndexingDirectory(getBaseDirectoryToIndex());
		listenerControl.setVoidCallable(1);

		listener.beforeIndexingFile(getFileFromClasspath("test.foo"));
		listenerControl.setVoidCallable(1);

		listener.onNotAvailableHandler(getFileFromClasspath("test.foo"));
		listenerControl.setVoidCallable(1);

		listener.beforeIndexingFile(getFileFromClasspath("test.txt"));
		listenerControl.setVoidCallable(1);

		indexWriter.addDocument(document);
		indexWriterControl.setMatcher(new AbstractMatcher() {
			protected boolean argumentMatches(Object expected, Object actual) {
				if( expected instanceof Document && actual instanceof Document ) {
					return true;
				} else {
					return expected.equals(actual);
				}
			}
		});
		indexWriterControl.setVoidCallable(1);
		
		listener.afterIndexingFile(getFileFromClasspath("test.txt"));
		listenerControl.setVoidCallable(1);

		listener.afterIndexingDirectory(getBaseDirectoryToIndex());
		listenerControl.setVoidCallable(1);

		indexWriter.close();
		indexWriterControl.setVoidCallable(1);
		
		indexFactoryControl.replay();
		indexWriterControl.replay();
		listenerControl.replay();

		//Indexer
		DefaultDirectoryIndexer indexer = new DefaultDirectoryIndexer(indexFactory);
		indexer.addListener(listener);
		File baseDirectory = getBaseDirectoryToIndex();
		indexer.index(baseDirectory.getAbsolutePath());

		indexFactoryControl.verify();
		indexWriterControl.verify();
		listenerControl.verify();
	}

	/*
	 * Test for void index(String) with document handler registered
	 */
	final public void testIndexStringDocumentHandler() throws Exception {
		SimpleAnalyzer analyzer = new SimpleAnalyzer();
		MockControl indexFactoryControl = MockControl.createStrictControl(IndexFactory.class);
		IndexFactory indexFactory = (IndexFactory)indexFactoryControl.getMock(); 
		MockControl indexWriterControl = MockControl.createStrictControl(LuceneIndexWriter.class);
		LuceneIndexWriter indexWriter = (LuceneIndexWriter)indexWriterControl.getMock(); 
		MockControl listenerControl = MockControl.createStrictControl(FileDocumentIndexingListener.class);
		FileDocumentIndexingListener listener = (FileDocumentIndexingListener)listenerControl.getMock();

		//document
		Document document = new Document();
		document.add(new Field("field", "a sample", Field.Store.YES, Field.Index.TOKENIZED));
		document.add(new Field("filter", "a sample filter", Field.Store.YES, Field.Index.TOKENIZED));
		document.add(new Field("sort", "2", Field.Store.YES, Field.Index.UN_TOKENIZED));

		indexFactory.getIndexWriter();
		indexFactoryControl.setReturnValue(indexWriter, 1);
		
		listener.beforeIndexingDirectory(getBaseDirectoryToIndex());
		listenerControl.setVoidCallable(1);

		listener.beforeIndexingFile(getFileFromClasspath("test.foo"));
		listenerControl.setVoidCallable(1);

		indexWriter.addDocument(document);
		indexWriterControl.setMatcher(new AbstractMatcher() {
			protected boolean argumentMatches(Object expected, Object actual) {
				if( expected instanceof Document && actual instanceof Document ) {
					return true;
				} else {
					return expected.equals(actual);
				}
			}
		});
		indexWriterControl.setVoidCallable(1);
		
		listener.afterIndexingFile(getFileFromClasspath("test.foo"));
		listenerControl.setVoidCallable(1);

		listener.beforeIndexingFile(getFileFromClasspath("test.txt"));
		listenerControl.setVoidCallable(1);

		indexWriter.addDocument(document);
		indexWriterControl.setVoidCallable(1);
		
		listener.afterIndexingFile(getFileFromClasspath("test.txt"));
		listenerControl.setVoidCallable(1);

		listener.afterIndexingDirectory(getBaseDirectoryToIndex());
		listenerControl.setVoidCallable(1);

		indexWriter.close();
		indexWriterControl.setVoidCallable(1);
		
		indexFactoryControl.replay();
		indexWriterControl.replay();
		listenerControl.replay();

		//Indexer
		DefaultDirectoryIndexer indexer = new DefaultDirectoryIndexer(indexFactory);
		indexer.registerDocumentHandler(new ExtensionDocumentMatching("foo"),
										new TextDocumentHandler());
		indexer.addListener(listener);
		File baseDirectory = getBaseDirectoryToIndex();
		indexer.index(baseDirectory.getAbsolutePath());

		indexFactoryControl.verify();
		indexWriterControl.verify();
		listenerControl.verify();
	}

	/*
	 * Test pour void index(String) with document handler registered
	 */
	final public void testIndexStringListener() throws Exception {
		SimpleAnalyzer analyzer = new SimpleAnalyzer();
		MockControl indexFactoryControl = MockControl.createStrictControl(IndexFactory.class);
		IndexFactory indexFactory = (IndexFactory)indexFactoryControl.getMock(); 
		MockControl indexWriterControl = MockControl.createStrictControl(LuceneIndexWriter.class);
		LuceneIndexWriter indexWriter = (LuceneIndexWriter)indexWriterControl.getMock(); 
		MockControl listener1Control = MockControl.createStrictControl(FileDocumentIndexingListener.class);
		FileDocumentIndexingListener listener1 = (FileDocumentIndexingListener)listener1Control.getMock();
		MockControl listener2Control = MockControl.createStrictControl(FileDocumentIndexingListener.class);
		FileDocumentIndexingListener listener2 = (FileDocumentIndexingListener)listener2Control.getMock();

		//document
		Document document = new Document();
		document.add(new Field("field", "a sample", Field.Store.YES, Field.Index.TOKENIZED));
		document.add(new Field("filter", "a sample filter", Field.Store.YES, Field.Index.TOKENIZED));
		document.add(new Field("sort", "2", Field.Store.YES, Field.Index.UN_TOKENIZED));

		indexFactory.getIndexWriter();
		indexFactoryControl.setReturnValue(indexWriter, 1);
		
		listener1.beforeIndexingDirectory(getBaseDirectoryToIndex());
		listener1Control.setVoidCallable(1);

		listener2.beforeIndexingDirectory(getBaseDirectoryToIndex());
		listener2Control.setVoidCallable(1);

		listener1.beforeIndexingFile(getFileFromClasspath("test.foo"));
		listener1Control.setVoidCallable(1);

		listener2.beforeIndexingFile(getFileFromClasspath("test.foo"));
		listener2Control.setVoidCallable(1);

		listener1.onNotAvailableHandler(getFileFromClasspath("test.foo"));
		listener1Control.setVoidCallable(1);

		listener2.onNotAvailableHandler(getFileFromClasspath("test.foo"));
		listener2Control.setVoidCallable(1);

		listener1.beforeIndexingFile(getFileFromClasspath("test.txt"));
		listener1Control.setVoidCallable(1);

		listener2.beforeIndexingFile(getFileFromClasspath("test.txt"));
		listener2Control.setVoidCallable(1);

		indexWriter.addDocument(document);
		indexWriterControl.setMatcher(new AbstractMatcher() {
			protected boolean argumentMatches(Object expected, Object actual) {
				if( expected instanceof Document && actual instanceof Document ) {
					return true;
				} else {
					return expected.equals(actual);
				}
			}
		});
		indexWriterControl.setVoidCallable(1);
		
		listener1.afterIndexingFile(getFileFromClasspath("test.txt"));
		listener1Control.setVoidCallable(1);

		listener2.afterIndexingFile(getFileFromClasspath("test.txt"));
		listener2Control.setVoidCallable(1);

		listener1.afterIndexingDirectory(getBaseDirectoryToIndex());
		listener1Control.setVoidCallable(1);

		listener2.afterIndexingDirectory(getBaseDirectoryToIndex());
		listener2Control.setVoidCallable(1);

		indexWriter.close();
		indexWriterControl.setVoidCallable(1);
		
		indexFactoryControl.replay();
		indexWriterControl.replay();
		listener1Control.replay();
		listener2Control.replay();

		//Indexer
		DefaultDirectoryIndexer indexer = new DefaultDirectoryIndexer(indexFactory);
		indexer.addListener(listener1);
		indexer.addListener(listener2);
		File baseDirectory = getBaseDirectoryToIndex();
		indexer.index(baseDirectory.getAbsolutePath());

		indexFactoryControl.verify();
		indexWriterControl.verify();
		listener1Control.verify();
		listener2Control.verify();
	}

	/*
	 * Test pour void index(String, boolean)
	 */
	final public void testIndexStringboolean() throws Exception {
		SimpleAnalyzer analyzer = new SimpleAnalyzer();
		MockControl indexFactoryControl = MockControl.createNiceControl(IndexFactory.class);
		IndexFactory indexFactory = (IndexFactory)indexFactoryControl.getMock(); 
		MockControl indexWriterControl = MockControl.createNiceControl(LuceneIndexWriter.class);
		LuceneIndexWriter indexWriter = (LuceneIndexWriter)indexWriterControl.getMock(); 
		MockControl listenerControl = MockControl.createNiceControl(FileDocumentIndexingListener.class);
		FileDocumentIndexingListener listener = (FileDocumentIndexingListener)listenerControl.getMock();

		//document
		Document document = new Document();
		document.add(new Field("field", "a sample", Field.Store.YES, Field.Index.TOKENIZED));
		document.add(new Field("filter", "a sample filter", Field.Store.YES, Field.Index.TOKENIZED));
		document.add(new Field("sort", "2", Field.Store.YES, Field.Index.UN_TOKENIZED));

		indexFactory.getIndexWriter();
		indexFactoryControl.setReturnValue(indexWriter, 1);
		
		listener.beforeIndexingDirectory(getBaseDirectoryToIndex());
		listenerControl.setVoidCallable(1);

		listener.beforeIndexingFile(getFileFromClasspath("test.foo"));
		listenerControl.setVoidCallable(1);

		listener.onNotAvailableHandler(getFileFromClasspath("test.foo"));
		listenerControl.setVoidCallable(1);

		listener.beforeIndexingFile(getFileFromClasspath("test.txt"));
		listenerControl.setVoidCallable(1);

		indexWriter.addDocument(document);
		indexWriterControl.setMatcher(new AbstractMatcher() {
			protected boolean argumentMatches(Object expected, Object actual) {
				if( expected instanceof Document && actual instanceof Document ) {
					return true;
				} else {
					return expected.equals(actual);
				}
			}
		});
		indexWriterControl.setVoidCallable(1);
		
		listener.afterIndexingFile(getFileFromClasspath("test.txt"));
		listenerControl.setVoidCallable(1);

		listener.afterIndexingDirectory(getBaseDirectoryToIndex());
		listenerControl.setVoidCallable(1);

		indexWriter.optimize();
		indexWriterControl.setVoidCallable(1);
		
		indexWriter.close();
		indexWriterControl.setVoidCallable(1);
		
		indexFactoryControl.replay();
		indexWriterControl.replay();
		listenerControl.replay();

		//Indexer
		DefaultDirectoryIndexer indexer = new DefaultDirectoryIndexer(indexFactory);
		indexer.addListener(listener);
		File baseDirectory = getBaseDirectoryToIndex();
		indexer.index(baseDirectory.getAbsolutePath(), true);

		indexFactoryControl.verify();
		indexWriterControl.verify();
		listenerControl.verify();
	}

	/*
	 * Test pour void index(String)
	 */
	final public void testIndexStringIfDirectoryNotExist() throws Exception {
		SimpleAnalyzer analyzer = new SimpleAnalyzer();
		MockControl indexFactoryControl = MockControl.createControl(IndexFactory.class);
		IndexFactory indexFactory = (IndexFactory)indexFactoryControl.getMock();

		indexFactoryControl.replay();
		
		//Indexer
		DefaultDirectoryIndexer indexer = new DefaultDirectoryIndexer(indexFactory);
		File baseDirectory = getBaseDirectoryToIndex();
		File wrongBaseDirectory = new File(baseDirectory.getCanonicalPath()+"/test");

		try {
			indexer.index(wrongBaseDirectory.getAbsolutePath());
			fail();
		} catch(LuceneIndexingException ex) {
		}
		
		indexFactoryControl.verify();
	}
}
