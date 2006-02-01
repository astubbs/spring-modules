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

package org.springmodules.lucene.index.object.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import junit.framework.TestCase;

import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.store.RAMDirectory;
import org.easymock.MockControl;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springmodules.lucene.index.core.MockSimpleIndexFactory;
import org.springmodules.lucene.index.factory.SimpleIndexFactory;
import org.springmodules.lucene.index.support.handler.database.SqlDocumentHandler;
import org.springmodules.lucene.index.support.handler.database.SqlRequest;

/**
 * @author Thierry Templier
 */
public class DatabaseIndexerTests extends TestCase {

	private RAMDirectory directory;
	private DriverManagerDataSource dataSource;

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		//Initialization of the index
		this.directory=new RAMDirectory();
		//Initialization of the datasource
		this.dataSource=new DriverManagerDataSource();
		this.dataSource.setDriverClassName("org.hsqldb.jdbcDriver");
		this.dataSource.setUrl("jdbc:hsqldb:mem:.");
		this.dataSource.setUsername("sa");
		this.dataSource.setPassword("");
		//Creation of the schema
		StringBuffer requestCreate=new StringBuffer();
		requestCreate.append("create table TEST ( TEST_ID INT not null,");
		requestCreate.append(" TEST_NAME VARCHAR(255) not null,");
		requestCreate.append(" constraint PK_TEST primary key (TEST_ID) );");
		JdbcTemplate template=new JdbcTemplate(this.dataSource);
		template.execute(requestCreate.toString());
		//Insertion of tuples
		StringBuffer requestInsertion=new StringBuffer();
		requestInsertion.append("insert into TEST (TEST_ID,TEST_NAME)");
		requestInsertion.append(" values(1,'this is a test')");
		template.execute(requestInsertion.toString());
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		//Finalization of the index
		this.directory=null;
		//Destoy the schema
		JdbcTemplate template=new JdbcTemplate(this.dataSource);
		template.execute("drop table TEST");
		//Finalization of the datasource
		this.dataSource=null;
	}

	final public void testRegisterDocumentHandler() {
		//Initialization of the index
		SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);

		//Indexer
		DefaultDatabaseIndexer indexer=new DefaultDatabaseIndexer(indexFactory);

		//Register a document handler
		SqlRequest request=new SqlRequest("select * from test");
		assertNull(indexer.getDocumentHandler(request));

		indexer.registerDocumentHandler(request,new SqlDocumentHandler() {
			public Document getDocument(SqlRequest request, ResultSet rs) throws SQLException {
				return null;
			}
		});
		assertNotNull(indexer.getDocumentHandler(request));
	}

	final public void testUnregisterDocumentHandler() {
		//Initialization of the index
		SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);

		//Indexer
		DefaultDatabaseIndexer indexer=new DefaultDatabaseIndexer(indexFactory);

		//Register a document handler
		SqlRequest request=new SqlRequest("select * from test");
		indexer.registerDocumentHandler(request,new SqlDocumentHandler() {
			public Document getDocument(SqlRequest request, ResultSet rs) throws SQLException {
				return null;
			}
		});
		assertNotNull(indexer.getDocumentHandler(request));

		//Unregister a document handler
		indexer.unregisterDocumentHandler(request);
		assertNull(indexer.getDocumentHandler(request));
	}

	final public void testAddListener() {
		//Initialization of the index
		SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);

		//Indexer
		DefaultDatabaseIndexer indexer=new DefaultDatabaseIndexer(indexFactory);

		//Register a document handler
		assertEquals(indexer.getListeners().size(),0);

		DatabaseIndexingListener listener=new DatabaseIndexingListenerAdapter();
		indexer.addListener(listener);
		assertEquals(indexer.getListeners().size(),1);
		DatabaseIndexingListener tmpListener=(DatabaseIndexingListener)indexer.getListeners().get(0);
		assertEquals(listener,tmpListener);
	}

	final public void testRemoveListener() {
		//Initialization of the index
		SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);

		//Indexer
		DefaultDatabaseIndexer indexer=new DefaultDatabaseIndexer(indexFactory);

		//Register a document handler
		DatabaseIndexingListener listener=new DatabaseIndexingListenerAdapter();
		indexer.addListener(listener);
		assertEquals(indexer.getListeners().size(),1);
		DatabaseIndexingListener tmpListener=(DatabaseIndexingListener)indexer.getListeners().get(0);
		assertEquals(listener,tmpListener);

		//Unregister a document handler
		indexer.removeListener(listener);
		assertEquals(indexer.getListeners().size(),0);
	}

	final public void testIndexDataSource() {
		//Initialization of the index
		SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);

		//Indexer
		final boolean[] test=new boolean[] { false,false };
		DefaultDatabaseIndexer indexer=new DefaultDatabaseIndexer(indexFactory);
		SqlRequest request1=new SqlRequest("select * from test");
		indexer.registerDocumentHandler(request1,new SqlDocumentHandler() {
			public Document getDocument(SqlRequest request, ResultSet rs) throws SQLException {
				test[0]=true;
				Document document=new Document();
				document.add(Field.UnStored("contents", rs.getString("test_name")));
				return document;
			}
		});
		SqlRequest request2=new SqlRequest("select * from test");
		indexer.registerDocumentHandler(request2,new SqlDocumentHandler() {
			public Document getDocument(SqlRequest request, ResultSet rs) throws SQLException {
				test[1]=true;
				return null;
			}
		});

		MockControl listenerControl=MockControl.createControl(DatabaseIndexingListener.class);
		DatabaseIndexingListener listener=(DatabaseIndexingListener)listenerControl.getMock();

		indexer.addListener(listener);

		listener.beforeIndexingRequest(request1);
		listenerControl.setVoidCallable(1);

		listener.afterIndexingRequest(request1);
		listenerControl.setVoidCallable(1);

		listener.beforeIndexingRequest(request2);
		listenerControl.setVoidCallable(1);

		listener.afterIndexingRequest(request2);
		listenerControl.setVoidCallable(1);

		listenerControl.replay();

		//Index
		indexer.index(this.dataSource);

		listenerControl.verify();
		assertTrue(test[0]);
		assertTrue(test[1]);
		assertFalse(indexFactory.getWriterListener().isIndexWriterOptimize());
	}

	final public void testIndexDataSourceWithSqlError() {
		//Initialization of the index
		SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);

		//Indexer
		final boolean[] test=new boolean[] { false,false };
		DefaultDatabaseIndexer indexer=new DefaultDatabaseIndexer(indexFactory);
		SqlRequest request1=new SqlRequest("select * from test1");
		indexer.registerDocumentHandler(request1,new SqlDocumentHandler() {
			public Document getDocument(SqlRequest request, ResultSet rs) throws SQLException {
				test[0]=true;
				Document document=new Document();
				document.add(Field.UnStored("contents", rs.getString("test_name")));
				return document;
			}
		});

		MockControl listenerControl=MockControl.createControl(DatabaseIndexingListener.class);
		DatabaseIndexingListener listener=(DatabaseIndexingListener)listenerControl.getMock();

		indexer.addListener(new DatabaseIndexingListenerAdapter() {
			public void onErrorIndexingRequest(SqlRequest request, Exception ex) {
				test[1]=true;
			}
		});

		//Index
		indexer.index(this.dataSource);

		assertFalse(test[0]);
		assertTrue(test[1]);
	}

	final public void testIndexDataSourceboolean() {
		//Initialization of the index
		SimpleAnalyzer analyzer=new SimpleAnalyzer();
		SimpleIndexFactory targetIndexFactory=new SimpleIndexFactory(directory,analyzer);
		MockSimpleIndexFactory indexFactory=new MockSimpleIndexFactory(targetIndexFactory);

		//Indexer
		final boolean[] test=new boolean[] { false,false };
		DefaultDatabaseIndexer indexer=new DefaultDatabaseIndexer(indexFactory);
		SqlRequest request1=new SqlRequest("select * from test");
		indexer.registerDocumentHandler(request1,new SqlDocumentHandler() {
			public Document getDocument(SqlRequest request, ResultSet rs) throws SQLException {
				test[0]=true;
				Document document=new Document();
				document.add(Field.UnStored("contents", rs.getString("test_name")));
				return document;
			}
		});
		SqlRequest request2=new SqlRequest("select * from test");
		indexer.registerDocumentHandler(request2,new SqlDocumentHandler() {
			public Document getDocument(SqlRequest request, ResultSet rs) throws SQLException {
				test[1]=true;
				return null;
			}
		});

		MockControl listenerControl=MockControl.createControl(DatabaseIndexingListener.class);
		DatabaseIndexingListener listener=(DatabaseIndexingListener)listenerControl.getMock();

		indexer.addListener(listener);

		listener.beforeIndexingRequest(request1);
		listenerControl.setVoidCallable(1);

		listener.afterIndexingRequest(request1);
		listenerControl.setVoidCallable(1);

		listener.beforeIndexingRequest(request2);
		listenerControl.setVoidCallable(1);

		listener.afterIndexingRequest(request2);
		listenerControl.setVoidCallable(1);

		listenerControl.replay();

		//Index
		indexer.index(this.dataSource,true);

		listenerControl.verify();
		assertTrue(test[0]);
		assertTrue(test[1]);
		assertTrue(indexFactory.getWriterListener().isIndexWriterOptimize());
	}

}
