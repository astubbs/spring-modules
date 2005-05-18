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
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.store.RAMDirectory;
import org.easymock.MockControl;
import org.springmodules.lucene.index.core.MockSimpleIndexFactory;
import org.springmodules.lucene.index.factory.SimpleIndexFactory;
import org.springmodules.lucene.index.object.database.DatabaseIndexer;
import org.springmodules.lucene.index.support.file.DocumentExtensionMatching;
import org.springmodules.lucene.index.support.file.DocumentHandler;
import org.springmodules.lucene.index.support.file.DocumentMatching;
import org.springmodules.lucene.index.support.file.handler.TextDocumentHandler;

import junit.framework.TestCase;

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
		DirectoryIndexer indexer=new DirectoryIndexer(indexFactory);

		//Register a document handler
		DocumentMatching matching=new DocumentMatching() {
			public boolean match(String name) {
				return name.equals("test");
			}
		};
		assertNull(indexer.getDocumentHandler("test"));

		indexer.registerDocumentHandler(matching,new DocumentHandler() {
			public Document getDocument(Map description, InputStream inputStream) throws IOException {
				return null;
			}
		});
		assertNotNull(indexer.getDocumentHandler("test"));
	}

	final public void testUnregisterDocumentHandler() {
		//Initialization of the index
		SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);

		//Indexer
		DirectoryIndexer indexer=new DirectoryIndexer(indexFactory);

		//Register a document handler
		DocumentMatching matching=new DocumentMatching() {
			public boolean match(String name) {
				return name.equals("test");
			}
		};
		indexer.registerDocumentHandler(matching,new DocumentHandler() {
			public Document getDocument(Map description, InputStream inputStream) throws IOException {
				return null;
			}
		});
		assertNotNull(indexer.getDocumentHandler("test"));

		//Unregister a document handler
		indexer.unregisterDocumentHandler(matching);
		assertNull(indexer.getDocumentHandler("test"));

	}

	final public void testAddListener() {
		//Initialization of the index
		SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);

		//Indexer
		DirectoryIndexer indexer=new DirectoryIndexer(indexFactory);

		//Register a document handler
		assertEquals(indexer.getListeners().size(),0);

		DocumentIndexingListener listener=new DocumentIndexingListenerAdapter();
		indexer.addListener(listener);
		assertEquals(indexer.getListeners().size(),1);
		DocumentIndexingListener tmpListener=(DocumentIndexingListener)indexer.getListeners().get(0);
		assertEquals(listener,tmpListener);
	}

	final public void testRemoveListener() {
		//Initialization of the index
		SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);

		//Indexer
		DirectoryIndexer indexer=new DirectoryIndexer(indexFactory);

		//Register a document handler
		DocumentIndexingListener listener=new DocumentIndexingListenerAdapter();
		indexer.addListener(listener);
		assertEquals(indexer.getListeners().size(),1);
		DocumentIndexingListener tmpListener=(DocumentIndexingListener)indexer.getListeners().get(0);
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

		MockControl listenerControl=MockControl.createControl(DocumentIndexingListener.class);
		DocumentIndexingListener listener=(DocumentIndexingListener)listenerControl.getMock();

		listener.beforeIndexingDirectory(getBaseDirectoryToIndex());
		listenerControl.setVoidCallable(1);

		listener.beforeIndexingFile(getFileFromClasspath("test.properties"));
		listenerControl.setVoidCallable(1);

		listener.onNotAvailableHandler(getFileFromClasspath("test.properties"));
		listenerControl.setVoidCallable(1);

		listener.beforeIndexingFile(getFileFromClasspath("test.txt"));
		listenerControl.setVoidCallable(1);

		listener.afterIndexingFile(getFileFromClasspath("test.txt"));
		listenerControl.setVoidCallable(1);

		listener.afterIndexingDirectory(getBaseDirectoryToIndex());
		listenerControl.setVoidCallable(1);

		listenerControl.replay();

		//Indexer
		DirectoryIndexer indexer=new DirectoryIndexer(indexFactory);
		indexer.addListener(listener);
		File baseDirectory=getBaseDirectoryToIndex();
		indexer.index(baseDirectory.getAbsolutePath());

		listenerControl.verify();
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

		MockControl listenerControl=MockControl.createControl(DocumentIndexingListener.class);
		DocumentIndexingListener listener=(DocumentIndexingListener)listenerControl.getMock();

		listener.beforeIndexingDirectory(getBaseDirectoryToIndex());
		listenerControl.setVoidCallable(1);

		listener.beforeIndexingFile(getFileFromClasspath("test.properties"));
		listenerControl.setVoidCallable(1);

		listener.afterIndexingFile(getFileFromClasspath("test.properties"));
		listenerControl.setVoidCallable(1);

		listener.beforeIndexingFile(getFileFromClasspath("test.txt"));
		listenerControl.setVoidCallable(1);

		listener.afterIndexingFile(getFileFromClasspath("test.txt"));
		listenerControl.setVoidCallable(1);
		listener.afterIndexingDirectory(getBaseDirectoryToIndex());
		listenerControl.setVoidCallable(1);

		listenerControl.replay();

		//Indexer
		DirectoryIndexer indexer=new DirectoryIndexer(indexFactory);
		indexer.registerDocumentHandler(new DocumentExtensionMatching("properties"),
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
		MockControl listener1Control=MockControl.createControl(DocumentIndexingListener.class);
		DocumentIndexingListener listener1=(DocumentIndexingListener)listener1Control.getMock();

		listener1.beforeIndexingDirectory(getBaseDirectoryToIndex());
		listener1Control.setVoidCallable(1);

		listener1.beforeIndexingFile(getFileFromClasspath("test.properties"));
		listener1Control.setVoidCallable(1);

		listener1.onNotAvailableHandler(getFileFromClasspath("test.properties"));
		listener1Control.setVoidCallable(1);

		listener1.beforeIndexingFile(getFileFromClasspath("test.txt"));
		listener1Control.setVoidCallable(1);

		listener1.afterIndexingFile(getFileFromClasspath("test.txt"));
		listener1Control.setVoidCallable(1);

		listener1.afterIndexingDirectory(getBaseDirectoryToIndex());
		listener1Control.setVoidCallable(1);

		//Listener 2
		MockControl listener2Control=MockControl.createControl(DocumentIndexingListener.class);
		DocumentIndexingListener listener2=(DocumentIndexingListener)listener2Control.getMock();

		listener2.beforeIndexingDirectory(getBaseDirectoryToIndex());
		listener2Control.setVoidCallable(1);

		listener2.beforeIndexingFile(getFileFromClasspath("test.properties"));
		listener2Control.setVoidCallable(1);

		listener2.onNotAvailableHandler(getFileFromClasspath("test.properties"));
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
		DirectoryIndexer indexer=new DirectoryIndexer(indexFactory);
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

		MockControl listenerControl=MockControl.createControl(DocumentIndexingListener.class);
		DocumentIndexingListener listener=(DocumentIndexingListener)listenerControl.getMock();

		listener.beforeIndexingDirectory(getBaseDirectoryToIndex());
		listenerControl.setVoidCallable(1);

		listener.beforeIndexingFile(getFileFromClasspath("test.properties"));
		listenerControl.setVoidCallable(1);

		listener.onNotAvailableHandler(getFileFromClasspath("test.properties"));
		listenerControl.setVoidCallable(1);

		listener.beforeIndexingFile(getFileFromClasspath("test.txt"));
		listenerControl.setVoidCallable(1);

		listener.afterIndexingFile(getFileFromClasspath("test.txt"));
		listenerControl.setVoidCallable(1);

		listener.afterIndexingDirectory(getBaseDirectoryToIndex());
		listenerControl.setVoidCallable(1);

		listenerControl.replay();

		//Indexer
		DirectoryIndexer indexer=new DirectoryIndexer(indexFactory);
		indexer.addListener(listener);
		File baseDirectory=getBaseDirectoryToIndex();
		indexer.index(baseDirectory.getAbsolutePath(),true);

		listenerControl.verify();
		assertTrue(indexFactory.getWriterListener().isIndexWriterOptimize());
	}

}
