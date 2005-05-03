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

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springmodules.lucene.index.LuceneWriteIndexException;
import org.springmodules.lucene.index.factory.IndexFactory;
import org.springmodules.lucene.index.factory.IndexWriterFactoryUtils;
import org.springmodules.lucene.index.object.AbstractIndexer;

/**
 * @author Thierry Templier
 */
public class DatabaseIndexer extends AbstractIndexer {
	private Map requestDocumentHandlers;
	private List listeners;

	public DatabaseIndexer(IndexFactory indexFactory) {
		setIndexFactory(indexFactory);
		requestDocumentHandlers=new HashMap();
		listeners=new ArrayList();
		registerDefautHandlers();
	}

	protected void registerDefautHandlers() {
	}

	public void registerDocumentHandler(SqlRequest sqlRequest,SqlDocumentHandler handler) {
		if( sqlRequest!=null && handler!=null ) {
			requestDocumentHandlers.put(sqlRequest,handler);
		}
	}

	public void unregisterDocumentHandler(SqlRequest sqlRequest) {
		if( sqlRequest!=null ) {
			requestDocumentHandlers.remove(sqlRequest);
		}
	}

	public void addListener(DatabaseIndexingListener listener) {
		if( listener!=null ) {
			listeners.add(listener);
		}
	}

	public void removeListener(DatabaseIndexingListener listener) {
		if( listener!=null ) {
			listeners.remove(listener);
		}
	}

	protected void fireListenersOnBeforeRequest(SqlRequest request) {
		for(Iterator i=listeners.iterator();i.hasNext();) {
			DatabaseIndexingListener listener=(DatabaseIndexingListener)i.next();
			listener.beforeIndexingRequest(request);
		}
	}

	protected void fireListenersOnAfterRequest(SqlRequest request) {
		for(Iterator i=listeners.iterator();i.hasNext();) {
			DatabaseIndexingListener listener=(DatabaseIndexingListener)i.next();
			listener.afterIndexingRequest(request);
		}
	}

	protected void fireListenersOnErrorRequest(SqlRequest request,Exception ex) {
		for(Iterator i=listeners.iterator();i.hasNext();) {
			DatabaseIndexingListener listener=(DatabaseIndexingListener)i.next();
			listener.onErrorIndexingRequest(request,ex);
		}
	}

	private List indexResultSql(DataSource dataSource,SqlRequest request,SqlDocumentHandler handler) {
		IndexingMappingQuery query=new IndexingMappingQuery(dataSource,request,handler);
		if( request.getParams()!=null ) {
			return query.execute(request.getParams());
		} else {
			return query.execute();
		}
	}

	/**
	 * @param writer
	 * @param documents
	 */
	private void addDocumentsInIndex(IndexWriter writer, List documents) throws IOException{
		for(Iterator i=documents.iterator();i.hasNext();) {
			Document document=(Document)i.next();
			if( document!=null ) {
				writer.addDocument(document);
			}
		}
		
	}

	private void doHandleRequest(DataSource dataSource,SqlRequest request,
									SqlDocumentHandler handler,boolean optimizeIndex) {
		IndexWriter writer = IndexWriterFactoryUtils.getIndexWriter(getIndexFactory());
		try {
			fireListenersOnBeforeRequest(request);
			List documents=indexResultSql(dataSource,request,handler);
			if( documents!=null ) {
				addDocumentsInIndex(writer,documents);
			}

			//Optimize the index
			if( optimizeIndex ) {
				writer.optimize();
			}

			fireListenersOnAfterRequest(request);
		} catch(IOException ex) {
			logger.error("Error during indexing the request",ex);
			fireListenersOnErrorRequest(request,ex);
			throw new LuceneWriteIndexException("Error during indexing the database",ex);
		} finally {
			IndexWriterFactoryUtils.closeIndexWriterIfNecessary(getIndexFactory(),writer);
		}
	}

	public void index(DataSource dataSource,boolean optimizeIndex) {
		Set requests=requestDocumentHandlers.keySet();
		for(Iterator i=requests.iterator();i.hasNext();) {
			SqlRequest request=(SqlRequest)i.next();
			SqlDocumentHandler handler=(SqlDocumentHandler)requestDocumentHandlers.get(request);
			doHandleRequest(dataSource,request,handler,optimizeIndex);
		}
	}

	private static class IndexingMappingQuery extends MappingSqlQuery {

		private SqlRequest request;
		private SqlDocumentHandler handler;

		public IndexingMappingQuery(DataSource ds,SqlRequest request,SqlDocumentHandler handler) {
			super(ds, request.getSql());
			this.request=request;
			this.handler=handler;
			int[] types=request.getTypes();
			if( types!=null ) {
				for(int cpt=0;cpt<types.length;cpt++) {
					super.declareParameter(new SqlParameter(types[cpt]));
				}
			}
			compile();
		}

		public Object mapRow(ResultSet rs, int rowNumber) throws SQLException {
			return handler.getDocument(request,rs);
		} 
	}
}
