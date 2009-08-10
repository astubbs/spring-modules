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

package org.springmodules.lucene.index.object.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import junit.framework.TestCase;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.easymock.AbstractMatcher;
import org.easymock.MockControl;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springmodules.lucene.index.document.handler.database.SqlDocumentHandler;
import org.springmodules.lucene.index.document.handler.database.SqlRequest;
import org.springmodules.lucene.index.factory.IndexFactory;
import org.springmodules.lucene.index.factory.LuceneIndexWriter;

/**
 * @author Thierry Templier
 */
public class DatabaseIndexerTests extends TestCase {

	private DriverManagerDataSource dataSource;
	private JdbcTemplate template;

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		//Initialization of the datasource
		this.dataSource = new DriverManagerDataSource();
		this.dataSource.setDriverClassName("org.hsqldb.jdbcDriver");
		this.dataSource.setUrl("jdbc:hsqldb:test");
		this.dataSource.setUsername("sa");
		this.dataSource.setPassword("");
		this.template = new JdbcTemplate(this.dataSource);

		//Creation of the schema
		StringBuffer requestCreate = new StringBuffer();
		requestCreate.append("DROP TABLE TEST IF EXISTS" );
		this.template.execute(requestCreate.toString());
		requestCreate = new StringBuffer();
		requestCreate.append("create table TEST ( TEST_ID INT not null,");
		requestCreate.append(" TEST_NAME VARCHAR(255) not null,");
		requestCreate.append(" constraint PK_TEST primary key (TEST_ID) )");
		this.template.execute(requestCreate.toString());

		//Insertion of tuples
		StringBuffer requestInsertion = new StringBuffer();
		requestInsertion.append("insert into TEST (TEST_ID, TEST_NAME)");
		requestInsertion.append(" values(1, 'this is a test')");
		this.template.execute(requestInsertion.toString());
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		//Destoy the schema
		this.template.execute("drop table TEST");

		//Finalization of the datasource
		this.dataSource = null;
		this.template = null;
	}

	final public void testRegisterDocumentHandler() {
		MockControl indexFactoryControl = MockControl.createStrictControl(IndexFactory.class);
		IndexFactory indexFactory = (IndexFactory)indexFactoryControl.getMock();

		indexFactoryControl.replay();
		
		//Indexer
		DefaultDatabaseIndexer indexer = new DefaultDatabaseIndexer(indexFactory);

		//Register a document handler
		SqlRequest request = new SqlRequest("select * from test");
		assertNull(indexer.getDocumentHandler(request));

		indexer.registerDocumentHandler(request,new SqlDocumentHandler() {
			public Document getDocument(SqlRequest request, ResultSet rs) throws SQLException {
				return null;
			}
		});
		
		Analyzer a = null;
		

		indexFactoryControl.verify();
		assertNotNull(indexer.getDocumentHandler(request));
	}

	final public void testUnregisterDocumentHandler() {
		MockControl indexFactoryControl = MockControl.createStrictControl(IndexFactory.class);
		IndexFactory indexFactory = (IndexFactory)indexFactoryControl.getMock();

		indexFactoryControl.replay();
		
		//Indexer
		DefaultDatabaseIndexer indexer = new DefaultDatabaseIndexer(indexFactory);

		//Register a document handler
		SqlRequest request = new SqlRequest("select * from test");
		indexer.registerDocumentHandler(request, new SqlDocumentHandler() {
			public Document getDocument(SqlRequest request, ResultSet rs) throws SQLException {
				return null;
			}
		});
		assertNotNull(indexer.getDocumentHandler(request));

		//Unregister a document handler
		indexer.unregisterDocumentHandler(request);
		assertNull(indexer.getDocumentHandler(request));

		indexFactoryControl.verify();
	}

	final public void testAddListener() {
		MockControl indexFactoryControl = MockControl.createStrictControl(IndexFactory.class);
		IndexFactory indexFactory = (IndexFactory)indexFactoryControl.getMock();

		indexFactoryControl.replay();
		
		//Indexer
		DefaultDatabaseIndexer indexer = new DefaultDatabaseIndexer(indexFactory);

		//Register a document handler
		assertEquals(indexer.getListeners().size(), 0);

		DatabaseIndexingListener listener = new DatabaseIndexingListenerAdapter();
		indexer.addListener(listener);
		assertEquals(indexer.getListeners().size(), 1);
		DatabaseIndexingListener tmpListener = (DatabaseIndexingListener)indexer.getListeners().get(0);
		assertEquals(listener, tmpListener);
		
		indexFactoryControl.verify();
	}

	final public void testRemoveListener() {
		MockControl indexFactoryControl = MockControl.createStrictControl(IndexFactory.class);
		IndexFactory indexFactory = (IndexFactory)indexFactoryControl.getMock();

		indexFactoryControl.replay();
		
		//Indexer
		DefaultDatabaseIndexer indexer = new DefaultDatabaseIndexer(indexFactory);

		//Register a document handler
		DatabaseIndexingListener listener = new DatabaseIndexingListenerAdapter();
		indexer.addListener(listener);
		assertEquals(indexer.getListeners().size(), 1);
		DatabaseIndexingListener tmpListener = (DatabaseIndexingListener)indexer.getListeners().get(0);
		assertEquals(listener, tmpListener);

		//Unregister a document handler
		indexer.removeListener(listener);
		assertEquals(indexer.getListeners().size(), 0);

		indexFactoryControl.verify();
	}

	final public void testIndexDataSource() throws Exception {
		MockControl indexFactoryControl = MockControl.createStrictControl(IndexFactory.class);
		IndexFactory indexFactory = (IndexFactory)indexFactoryControl.getMock();
		MockControl indexWriterControl = MockControl.createStrictControl(LuceneIndexWriter.class);
		LuceneIndexWriter indexWriter = (LuceneIndexWriter)indexWriterControl.getMock();
		MockControl listenerControl = MockControl.createControl(DatabaseIndexingListener.class);
		DatabaseIndexingListener listener = (DatabaseIndexingListener)listenerControl.getMock();

		//requests
		SqlRequest request1 = new SqlRequest("select * from test");
		SqlRequest request2 = new SqlRequest("select * from test");

		//documents
		final Document document1 = new Document();
		document1.add(new Field("contents", "test_name", Field.Store.NO, Field.Index.TOKENIZED));
		final Document document2 = new Document();
		document2.add(new Field("contents", "test_name", Field.Store.NO, Field.Index.TOKENIZED));

		indexFactory.getIndexWriter();
		indexFactoryControl.setReturnValue(indexWriter, 1);
		
		listener.beforeIndexingRequest(request1);
		listenerControl.setVoidCallable(1);
		
		indexWriter.addDocument(document1);
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

		listener.afterIndexingRequest(request1);
		listenerControl.setVoidCallable(1);

		listener.beforeIndexingRequest(request2);
		listenerControl.setVoidCallable(1);

		indexWriter.addDocument(document2);
		indexWriterControl.setVoidCallable(1);

		listener.afterIndexingRequest(request2);
		listenerControl.setVoidCallable(1);

		indexWriter.close();
		indexWriterControl.setVoidCallable(1);
		
		indexFactoryControl.replay();
		indexWriterControl.replay();
		listenerControl.replay();
		
		//Indexer
		final boolean[] called = new boolean[] { false, false };
		DefaultDatabaseIndexer indexer = new DefaultDatabaseIndexer(indexFactory);
		indexer.registerDocumentHandler(request1, new SqlDocumentHandler() {
			public Document getDocument(SqlRequest request, ResultSet rs) throws SQLException {
				called[0] = true;
				return document1;
			}
		});
		indexer.registerDocumentHandler(request2, new SqlDocumentHandler() {
			public Document getDocument(SqlRequest request, ResultSet rs) throws SQLException {
				called[1] = true;
				return document2;
			}
		});
		indexer.addListener(listener);

		//Index
		indexer.index(this.dataSource);

		assertTrue(called[0]);
		assertTrue(called[1]);

		indexFactoryControl.verify();
		indexWriterControl.verify();
		listenerControl.verify();
	}

	final public void testIndexDataSourceWithSqlError() throws Exception {
		MockControl indexFactoryControl = MockControl.createStrictControl(IndexFactory.class);
		IndexFactory indexFactory = (IndexFactory)indexFactoryControl.getMock();
		MockControl indexWriterControl = MockControl.createStrictControl(LuceneIndexWriter.class);
		LuceneIndexWriter indexWriter = (LuceneIndexWriter)indexWriterControl.getMock();
		MockControl listenerControl = MockControl.createControl(DatabaseIndexingListener.class);
		DatabaseIndexingListener listener = (DatabaseIndexingListener)listenerControl.getMock();
		
		//requests
		SqlRequest request1 = new SqlRequest("select * from test1");

		//documents
		final Document document1 = new Document();
		document1.add(new Field("contents", "test_name", Field.Store.NO, Field.Index.TOKENIZED));
		
		//exception
		BadSqlGrammarException ex = new BadSqlGrammarException("arg1", "arg2", null);

		indexFactory.getIndexWriter();
		indexFactoryControl.setReturnValue(indexWriter, 1);
		
		listener.beforeIndexingRequest(request1);
		listenerControl.setVoidCallable(1);
		
		listener.onErrorIndexingRequest(request1, ex);
		listenerControl.setMatcher(new AbstractMatcher() {
			protected boolean argumentMatches(Object expected, Object actual) {
				if( expected instanceof DataAccessException && actual instanceof DataAccessException ) {
					return true;
				} else {
					return expected.equals(actual);
				}
			}
		});

		listenerControl.setVoidCallable(1);

		indexWriter.close();
		indexWriterControl.setVoidCallable(1);
		
		indexFactoryControl.replay();
		indexWriterControl.replay();
		listenerControl.replay();
		
		//Indexer
		final boolean[] called = new boolean[] { false };
		DefaultDatabaseIndexer indexer = new DefaultDatabaseIndexer(indexFactory);
		indexer.registerDocumentHandler(request1, new SqlDocumentHandler() {
			public Document getDocument(SqlRequest request, ResultSet rs) throws SQLException {
				called[0] = true;
				return document1;
			}
		});
		indexer.addListener(listener);

		//Index
		indexer.index(this.dataSource);

		assertFalse(called[0]);

		indexFactoryControl.verify();
		indexWriterControl.verify();
		listenerControl.verify();
	}

	final public void testIndexDataSourceboolean() throws Exception {
		MockControl indexFactoryControl = MockControl.createStrictControl(IndexFactory.class);
		IndexFactory indexFactory = (IndexFactory)indexFactoryControl.getMock();
		MockControl indexWriterControl = MockControl.createStrictControl(LuceneIndexWriter.class);
		LuceneIndexWriter indexWriter = (LuceneIndexWriter)indexWriterControl.getMock();
		MockControl listenerControl = MockControl.createControl(DatabaseIndexingListener.class);
		DatabaseIndexingListener listener = (DatabaseIndexingListener)listenerControl.getMock();

		//requests
		SqlRequest request1 = new SqlRequest("select * from test");
		SqlRequest request2 = new SqlRequest("select * from test");

		//documents
		final Document document1 = new Document();
		document1.add(new Field("contents", "test_name", Field.Store.NO, Field.Index.TOKENIZED));
		final Document document2 = new Document();
		document2.add(new Field("contents", "test_name", Field.Store.NO, Field.Index.TOKENIZED));

		indexFactory.getIndexWriter();
		indexFactoryControl.setReturnValue(indexWriter, 1);
		
		listener.beforeIndexingRequest(request1);
		listenerControl.setVoidCallable(1);
		
		indexWriter.addDocument(document1);
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

		listener.afterIndexingRequest(request1);
		listenerControl.setVoidCallable(1);

		listener.beforeIndexingRequest(request2);
		listenerControl.setVoidCallable(1);

		indexWriter.addDocument(document2);
		indexWriterControl.setVoidCallable(1);

		listener.afterIndexingRequest(request2);
		listenerControl.setVoidCallable(1);
		
		indexWriter.optimize();
		indexWriterControl.setVoidCallable(1);

		indexWriter.close();
		indexWriterControl.setVoidCallable(1);
		
		indexFactoryControl.replay();
		indexWriterControl.replay();
		listenerControl.replay();
		
		//Indexer
		final boolean[] called = new boolean[] { false, false };
		DefaultDatabaseIndexer indexer = new DefaultDatabaseIndexer(indexFactory);
		indexer.registerDocumentHandler(request1, new SqlDocumentHandler() {
			public Document getDocument(SqlRequest request, ResultSet rs) throws SQLException {
				called[0] = true;
				return document1;
			}
		});
		indexer.registerDocumentHandler(request2, new SqlDocumentHandler() {
			public Document getDocument(SqlRequest request, ResultSet rs) throws SQLException {
				called[1] = true;
				return document2;
			}
		});
		indexer.addListener(listener);

		//Index
		indexer.index(this.dataSource, true);

		assertTrue(called[0]);
		assertTrue(called[1]);

		indexFactoryControl.verify();
		indexWriterControl.verify();
		listenerControl.verify();
	}

}
