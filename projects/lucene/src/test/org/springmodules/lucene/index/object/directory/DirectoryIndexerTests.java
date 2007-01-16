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

package org.springmodules.lucene.index.object.directory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.store.RAMDirectory;
import org.easymock.MockControl;
import org.springmodules.lucene.index.LuceneIndexingException;
import org.springmodules.lucene.index.core.MockSimpleIndexFactory;
import org.springmodules.lucene.index.factory.SimpleIndexFactory;
import org.springmodules.lucene.index.support.handler.DocumentHandler;
import org.springmodules.lucene.index.support.handler.DocumentMatching;
import org.springmodules.lucene.index.support.handler.file.ExtensionDocumentMatching;
import org.springmodules.lucene.index.support.handler.file.TextDocumentHandler;

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
		this.directory=new RAMDirectory();
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		this.directory=null;
	}

	final public void testRegisterDocumentHandler() {
		//Initialization of the index
		SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);

		//Indexer
		DefaultDirectoryIndexer indexer=new DefaultDirectoryIndexer(indexFactory);

		//Register a document handler
		DocumentMatching matching=new DocumentMatching() {
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
	}

	final public void testUnregisterDocumentHandler() {
		//Initialization of the index
		SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);

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
	}

	final public void testAddListener() {
		//Initialization of the index
		SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);

		//Indexer
		DefaultDirectoryIndexer indexer=new DefaultDirectoryIndexer(indexFactory);

		//Register a document handler
		assertEquals(indexer.getListeners().size(),0);

		FileDocumentIndexingListener listener=new FileDocumentIndexingListenerAdapter();
		indexer.addListener(listener);
		assertEquals(indexer.getListeners().size(),1);
		FileDocumentIndexingListener tmpListener=(FileDocumentIndexingListener)indexer.getListeners().get(0);
		assertEquals(listener,tmpListener);
	}

	final public void testRemoveListener() {
		//Initialization of the index
		SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);

		//Indexer
		DefaultDirectoryIndexer indexer=new DefaultDirectoryIndexer(indexFactory);

		//Register a document handler
		FileDocumentIndexingListener listener=new FileDocumentIndexingListenerAdapter();
		indexer.addListener(listener);
		assertEquals(indexer.getListeners().size(),1);
		FileDocumentIndexingListener tmpListener=(FileDocumentIndexingListener)indexer.getListeners().get(0);
		assertEquals(listener,tmpListener);

		//Unregister a document handler
		indexer.removeListener(listener);
		assertEquals(indexer.getListeners().size(),0);
	}

	private File getBaseDirectoryToIndex() {
		URL url=getClass().getClassLoader().getResource(
					"org/springmodules/lucene/index/object/files/");
		if( url==null ) {
			return null;
		}

		String filename=url.getFile();
		return new File(filename);
	}

	private File getFileFromClasspath(String filename) {
		URL url=getClass().getClassLoader().getResource(
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
		//Initialization of the index
		SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);

		MockControl listenerControl=MockControl.createControl(FileDocumentIndexingListener.class);
		FileDocumentIndexingListener listener=(FileDocumentIndexingListener)listenerControl.getMock();

		listener.beforeIndexingDirectory(getBaseDirectoryToIndex());
		listenerControl.setVoidCallable(1);

		listener.beforeIndexingFile(getFileFromClasspath("test.foo"));
		listenerControl.setVoidCallable(1);

		listener.onNotAvailableHandler(getFileFromClasspath("test.foo"));
		listenerControl.setVoidCallable(1);

		listener.beforeIndexingFile(getFileFromClasspath("test.txt"));
		listenerControl.setVoidCallable(1);

		listener.afterIndexingFile(getFileFromClasspath("test.txt"));
		listenerControl.setVoidCallable(1);

		listener.afterIndexingDirectory(getBaseDirectoryToIndex());
		listenerControl.setVoidCallable(1);

		listenerControl.replay();

		//Indexer
		DefaultDirectoryIndexer indexer=new DefaultDirectoryIndexer(indexFactory);
		indexer.addListener(listener);
		File baseDirectory=getBaseDirectoryToIndex();
		indexer.index(baseDirectory.getAbsolutePath());

		listenerControl.verify();
		assertEquals(indexFactory.getWriterListener().getNumberWritersCreated(),1);
		assertEquals(indexFactory.getWriterListener().getNumberWritersClosed(),1);
		assertFalse(indexFactory.getWriterListener().isIndexWriterOptimize());
	}

	/*
	 * Test for void index(String) with document handler registered
	 */
	final public void testIndexStringDocumentHandler() throws Exception {
		//Initialization of the index
		SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);

		MockControl listenerControl=MockControl.createControl(FileDocumentIndexingListener.class);
		FileDocumentIndexingListener listener=(FileDocumentIndexingListener)listenerControl.getMock();

		listener.beforeIndexingDirectory(getBaseDirectoryToIndex());
		listenerControl.setVoidCallable(1);

		listener.beforeIndexingFile(getFileFromClasspath("test.foo"));
		listenerControl.setVoidCallable(1);

		listener.afterIndexingFile(getFileFromClasspath("test.foo"));
		listenerControl.setVoidCallable(1);

		listener.beforeIndexingFile(getFileFromClasspath("test.txt"));
		listenerControl.setVoidCallable(1);

		listener.afterIndexingFile(getFileFromClasspath("test.txt"));
		listenerControl.setVoidCallable(1);
		listener.afterIndexingDirectory(getBaseDirectoryToIndex());
		listenerControl.setVoidCallable(1);

		listenerControl.replay();

		//Indexer
		DefaultDirectoryIndexer indexer=new DefaultDirectoryIndexer(indexFactory);
		indexer.registerDocumentHandler(new ExtensionDocumentMatching("foo"),
		                                new TextDocumentHandler());
		indexer.addListener(listener);
		File baseDirectory=getBaseDirectoryToIndex();
		indexer.index(baseDirectory.getAbsolutePath());

		//fail();
		listenerControl.verify();
	}

	/*
	 * Test pour void index(String) with document handler registered
	 */
	final public void testIndexStringListener() throws Exception {
		//Initialization of the index
		SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);

		//Listener 1
		MockControl listener1Control=MockControl.createControl(FileDocumentIndexingListener.class);
		FileDocumentIndexingListener listener1=(FileDocumentIndexingListener)listener1Control.getMock();

		listener1.beforeIndexingDirectory(getBaseDirectoryToIndex());
		listener1Control.setVoidCallable(1);

		listener1.beforeIndexingFile(getFileFromClasspath("test.foo"));
		listener1Control.setVoidCallable(1);

		listener1.onNotAvailableHandler(getFileFromClasspath("test.foo"));
		listener1Control.setVoidCallable(1);

		listener1.beforeIndexingFile(getFileFromClasspath("test.txt"));
		listener1Control.setVoidCallable(1);

		listener1.afterIndexingFile(getFileFromClasspath("test.txt"));
		listener1Control.setVoidCallable(1);

		listener1.afterIndexingDirectory(getBaseDirectoryToIndex());
		listener1Control.setVoidCallable(1);

		//Listener 2
		MockControl listener2Control=MockControl.createControl(FileDocumentIndexingListener.class);
		FileDocumentIndexingListener listener2=(FileDocumentIndexingListener)listener2Control.getMock();

		listener2.beforeIndexingDirectory(getBaseDirectoryToIndex());
		listener2Control.setVoidCallable(1);

		listener2.beforeIndexingFile(getFileFromClasspath("test.foo"));
		listener2Control.setVoidCallable(1);

		listener2.onNotAvailableHandler(getFileFromClasspath("test.foo"));
		listener2Control.setVoidCallable(1);

		listener2.beforeIndexingFile(getFileFromClasspath("test.txt"));
		listener2Control.setVoidCallable(1);

		listener2.afterIndexingFile(getFileFromClasspath("test.txt"));
		listener2Control.setVoidCallable(1);

		listener2.afterIndexingDirectory(getBaseDirectoryToIndex());
		listener2Control.setVoidCallable(1);

		listener1Control.replay();
		listener2Control.replay();

		//Indexer
		DefaultDirectoryIndexer indexer=new DefaultDirectoryIndexer(indexFactory);
		indexer.addListener(listener1);
		indexer.addListener(listener2);
		File baseDirectory=getBaseDirectoryToIndex();
		indexer.index(baseDirectory.getAbsolutePath());

		listener1Control.verify();
		listener2Control.verify();
		assertFalse(indexFactory.getWriterListener().isIndexWriterOptimize());
	}

	/*
	 * Test pour void index(String, boolean)
	 */
	final public void testIndexStringboolean() {
		//Initialization of the index
		SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);

		MockControl listenerControl=MockControl.createControl(FileDocumentIndexingListener.class);
		FileDocumentIndexingListener listener=(FileDocumentIndexingListener)listenerControl.getMock();

		listener.beforeIndexingDirectory(getBaseDirectoryToIndex());
		listenerControl.setVoidCallable(1);

		listener.beforeIndexingFile(getFileFromClasspath("test.foo"));
		listenerControl.setVoidCallable(1);

		listener.onNotAvailableHandler(getFileFromClasspath("test.foo"));
		listenerControl.setVoidCallable(1);

		listener.beforeIndexingFile(getFileFromClasspath("test.txt"));
		listenerControl.setVoidCallable(1);

		listener.afterIndexingFile(getFileFromClasspath("test.txt"));
		listenerControl.setVoidCallable(1);

		listener.afterIndexingDirectory(getBaseDirectoryToIndex());
		listenerControl.setVoidCallable(1);

		listenerControl.replay();

		//Indexer
		DefaultDirectoryIndexer indexer=new DefaultDirectoryIndexer(indexFactory);
		indexer.addListener(listener);
		File baseDirectory=getBaseDirectoryToIndex();
		indexer.index(baseDirectory.getAbsolutePath(),true);

		listenerControl.verify();
		assertTrue(indexFactory.getWriterListener().isIndexWriterOptimize());
	}

	/*
	 * Test pour void index(String)
	 */
	final public void testIndexStringIfDirectoryNotExist() throws Exception {
		//Initialization of the index
		SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);

		//Indexer
		DefaultDirectoryIndexer indexer=new DefaultDirectoryIndexer(indexFactory);
		File baseDirectory=getBaseDirectoryToIndex();
		File wrongBaseDirectory=new File(baseDirectory.getCanonicalPath()+"/test");

		try {
			indexer.index(wrongBaseDirectory.getAbsolutePath());
			fail();
		} catch(LuceneIndexingException ex) {
		}
	}
}
