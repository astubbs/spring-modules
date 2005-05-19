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

package org.springmodules.samples.lucene.index.console;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springmodules.lucene.index.factory.IndexFactory;
import org.springmodules.lucene.index.object.database.DatabaseIndexer;
import org.springmodules.lucene.index.object.database.DatabaseIndexingListener;
import org.springmodules.lucene.index.support.database.SqlDocumentHandler;
import org.springmodules.lucene.index.support.database.SqlRequest;

/**
 * Main class to index all datas (directories and databases)
 * 
 * @author Thierry Templier
 */
public class SimpleDatabaseIndexingImpl implements DatabaseIndexing,InitializingBean {

	private DataSource dataSource;
	private IndexFactory indexFactory;
	private DatabaseIndexer indexer;

	public SimpleDatabaseIndexingImpl() {
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		if( indexFactory==null ) {
			throw new IllegalArgumentException("indexFactory is required");
		}
		this.indexer=new DatabaseIndexer(indexFactory);
	}

	public void prepareDatabaseHandlers() {
		//Register the request handler for book_page table without parameters
		this.indexer.registerDocumentHandler(new SqlRequest("select book_page_text from book_page"),
		new SqlDocumentHandler() {
			public Document getDocument(SqlRequest request, ResultSet rs) throws SQLException {
				Document document=new Document();
				document.add(Field.Text("contents", rs.getString("book_page_text")));
				document.add(Field.Keyword("request", request.getSql()));
				return document;
			}
		});

		//Register the request handler for book_page table with parameters
		/*this.indexer.registerDocumentHandler(new SqlRequest("select book_page_text from book_page where book_id=?",new Object[] {new Integer(1)},new int[] {Types.INTEGER}),
		new SqlDocumentHandler() {
			public Document getDocument(String sql, ResultSet rs) throws SQLException {
				Document document=new Document();
				document.add(Field.Text("contents", rs.getString("book_page_text")));
				document.add(Field.Keyword("request", sql));
				return document;
			}
		});*/
	}

	/**
	 * 
	 */
	public void indexDatabase() {
		indexer.index(dataSource,true);
	}

	public void prepareListeners() {
		DatabaseIndexingListener listener=new DatabaseIndexingListener() {
			public void beforeIndexingRequest(SqlRequest request) {
				System.out.println("Indexing the request : "+request.getSql()+" ...");
			}

			public void afterIndexingRequest(SqlRequest request) {
				System.out.println(" -> request indexed.");
			}

			public void onErrorIndexingRequest(SqlRequest request, Exception ex) {
				System.out.println(" -> Error during the indexing : "+ex.getMessage());
			}

		};
		indexer.addListener(listener);
	}

	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx=new ClassPathXmlApplicationContext("/applicationContext.xml");
		DatabaseIndexing indexing=(DatabaseIndexing)ctx.getBean("indexingDatabase");
		indexing.prepareDatabaseHandlers();
		indexing.prepareListeners();
		indexing.indexDatabase();
	}

	/**
	 * @return
	 */
	public IndexFactory getIndexFactory() {
		return indexFactory;
	}

	/**
	 * @param factory
	 */
	public void setIndexFactory(IndexFactory factory) {
		indexFactory = factory;
	}

	/**
	 * @return
	 */
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * @param source
	 */
	public void setDataSource(DataSource source) {
		dataSource = source;
	}

}
