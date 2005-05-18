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
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springmodules.lucene.index.LuceneIndexAccessException;
import org.springmodules.lucene.index.factory.IndexFactory;
import org.springmodules.lucene.index.factory.IndexWriterFactoryUtils;
import org.springmodules.lucene.index.object.AbstractIndexer;
import org.springmodules.lucene.index.support.database.SqlDocumentHandler;
import org.springmodules.lucene.index.support.database.SqlRequest;

/**
 * <b>This is the central class in the lucene database indexing package.</b>
 * It simplifies the use of lucene to index a database specifiying the requests
 * to execute and how to index the corresponding rows.
 * It helps to avoid common errors and to manage these resource in a flexible
 * manner.
 * It executes core Lucene workflow, leaving application code to focus on
 * the way to create Lucene documents from a row for a request.
 *
 * <p>This class is based on the IndexFactory abstraction which is a
 * factory to create IndexWriter for the configured
 * Directory. For the execution and the indexation of the corresponding
 * datas, the indexer uses the same IndexWriter. It calls the IndexWriterFactoryUtils
 * class to eventually release it. So the indexer doesn't need to always
 * hold resources during the indexation of every requests and
 * this avoids some locking problems on the index. You can too apply
 * different strategies for managing index resources.
 *
 * <p>Can be used within a service implementation via direct instantiation
 * with a IndexFactory reference, or get prepared in an application context
 * and given to services as bean reference. Note: The IndexFactory should
 * always be configured as a bean in the application context, in the first case
 * given to the service directly, in the second case to the prepared template.
 * 
 * @author Thierry Templier
 * @see org.springmodules.lucene.index.object.AbstractIndexer
 * @see org.springmodules.lucene.index.factory.IndexFactory
 * @see org.springmodules.lucene.index.support.database.SqlDocumentHandler
 * @see org.springmodules.lucene.index.object.database.DatabaseIndexingListener
 * @see org.springmodules.lucene.index.factory.IndexWriterFactoryUtils#getIndexWriter(IndexFactory)
 * @see org.springmodules.lucene.index.factory.IndexWriterFactoryUtils#releaseIndexWriter(IndexFactory, IndexWriter)
 */
public class DatabaseIndexer extends AbstractIndexer {
	private Map requestDocumentHandlers;
	private List listeners;

	/**
	 * Construct a new DatabaseIndexer, given an IndexFactory to obtain both
	 * IndexWriter.
	 * @param indexFactory IndexFactory to obtain IndexWriter
	 */
	public DatabaseIndexer(IndexFactory indexFactory) {
		setIndexFactory(indexFactory);
		requestDocumentHandlers=new HashMap();
		listeners=new ArrayList();
		registerDefautHandlers();
	}

	/**
	 * This method specifies the default handlers to automatically
	 * register when the indexer is instanciated.
	 * 
	 * <p>This method is empty but you can overwrite it to specify
	 * your default handlers.
	 */
	protected void registerDefautHandlers() {
	}

	/**
	 * This method is used to register a request to execute and an handler
	 * to specify how to index the rows corresponding to the request.
	 * 
	 * <p>The request is specify with an instance of the SqlRequest class
	 * which contains the sql requests, the parameter types and values.
	 * The implementation of the SqlDocumentHandler class definies a callback
	 * method which will be called for every row to index its datas.
	 * 
	 * @param sqlRequest the request to execute
	 * @param handler the handler to index the rows
	 */
	public void registerDocumentHandler(SqlRequest sqlRequest,SqlDocumentHandler handler) {
		if( sqlRequest!=null && handler!=null ) {
			requestDocumentHandlers.put(sqlRequest,handler);
		}
	}

	/**
	 * This method is used to unregister a request.
	 */ 
	public void unregisterDocumentHandler(SqlRequest sqlRequest) {
		if( sqlRequest!=null ) {
			requestDocumentHandlers.remove(sqlRequest);
		}
	}

	/**
	 * This method is used to get the SqlDocumentHandler implementation
	 * corresponding to SqlRequest passed as parameter.
	 * 
	 * @param sqlRequest the request to execute
	 * @return the handler to index the rows
	 */
	public SqlDocumentHandler getDocumentHandler(SqlRequest sqlRequest) {
		if( sqlRequest!=null ) {
			return (SqlDocumentHandler)requestDocumentHandlers.get(sqlRequest);
		} else {
			return null;
		}
	}

	/**
	 * This method returns all the registred requests and their
	 * corresponding handlers.
	 * 
	 * @return a map containing all the registred requests and their
	 * corresponding handlers
	 */
	public Map getDocumentHandlers() {
		return requestDocumentHandlers;
	}

	/**
	 * This method is used to add a listener to be notified during the
	 * indexing execution.
	 * 
	 * @param listener the listener to add
	 */
	public void addListener(DatabaseIndexingListener listener) {
		if( listener!=null ) {
			listeners.add(listener);
		}
	}

	/**
	 * This method is used to remove a specifed listener.
	 * 
	 * @param listener the listener to remove
	 */
	public void removeListener(DatabaseIndexingListener listener) {
		if( listener!=null ) {
			listeners.remove(listener);
		}
	}

	/**
	 * This method is used to get the list of listeners to notify
	 * during the indexing execution.
	 * 
	 * @return the list of listeners to notify
	 */
	public List getListeners() {
		return listeners;
	}

	/**
	 * This method is used to fire the "on before request" event to
	 * every listeners.
	 * 
	 * <p>This event will be fired before the request execution and
	 * the indexing of rows, even if there is an sql or indexing
	 * error.
	 * 
	 * @param request the request which will be executed
	 */
	protected void fireListenersOnBeforeRequest(SqlRequest request) {
		for(Iterator i=listeners.iterator();i.hasNext();) {
			DatabaseIndexingListener listener=(DatabaseIndexingListener)i.next();
			listener.beforeIndexingRequest(request);
		}
	}

	/**
	 * This method is used to fire the "on after request" event to
	 * every listeners.
	 * 
	 * <p>This event will be fired after the request execution and
	 * the indexing of rows. It will not happen if there is an sql
	 * or indexing error.
	 * 
	 * @param request the request which has been executed
	 */
	protected void fireListenersOnAfterRequest(SqlRequest request) {
		for(Iterator i=listeners.iterator();i.hasNext();) {
			DatabaseIndexingListener listener=(DatabaseIndexingListener)i.next();
			listener.afterIndexingRequest(request);
		}
	}

	/**
	 * This method is used to fire the "on error request" event to
	 * every listeners.
	 * 
	 * <p>This event will be fired if there is an sql or indexing error.
	 * 
	 * @param request the request which has been executed
	 */
	protected void fireListenersOnErrorRequest(SqlRequest request,Exception ex) {
		for(Iterator i=listeners.iterator();i.hasNext();) {
			DatabaseIndexingListener listener=(DatabaseIndexingListener)i.next();
			listener.onErrorIndexingRequest(request,ex);
		}
	}

	/**
	 * This method executes the sql request on a specified datasource with or
	 * with sql parameters. It is based on the Spring JDBC framework and uses
	 * a sub class of MappingSqlQuery.
	 * 
	 * <p>It is based on the IndexingMappingQuery class which delegates its mapRow
	 * method to the getDocument of a SqlDocumentHandler implementation. The 
	 * 
	 * @param dataSource the datasource to use
	 * @param request the request to execute
	 * @param handler the handler to use to index the rows
	 * @return the list of Lucene documents add to the index
	 * @see IndexingMappingQuery
	 */
	private List indexResultSql(DataSource dataSource,SqlRequest request,SqlDocumentHandler handler) {
		IndexingMappingQuery query=new IndexingMappingQuery(dataSource,request,handler);
		if( request.getParams()!=null ) {
			return query.execute(request.getParams());
		} else {
			return query.execute();
		}
	}

	/**
	 * This method adds a list of Lucene documents to an index using an
	 * corresponding IndexWriter instance.
	 * @param writer the IndexWriter instance to use
	 * @param documents the list of documents to add
	 */
	private void addDocumentsInIndex(IndexWriter writer, List documents) throws IOException{
		for(Iterator i=documents.iterator();i.hasNext();) {
			Document document=(Document)i.next();
			if( document!=null ) {
				writer.addDocument(document);
			}
		}
		
	}

	/**
	 * This method executes the request and adds the result of indexing
	 * of rows in the index. It gets an IndexWriter instance from the
	 * configured IndexFactory and releases it if necessary. It manages
	 * too both exceptions thrown duringthe request execution and the
	 * indexing of rows. 
	 * 
	 * <p>Before the return of the method, it optmizes too the index
	 * if the value of the optimizeIndex parameter is true.
	 * 
	 * @param dataSource the datasource to use
	 * @param request the request to execute
	 * @param handler the handler to use to index the rows
	 * @param optimizeIndex if the index must be optmized after
	 * the request indexing
	 * @see IndexWriterFactoryUtils#getIndexWriter(IndexFactory)
	 * @see IndexWriterFactoryUtils#releaseIndexWriter(IndexFactory, IndexWriter)
	 * @see #fireListenersOnBeforeRequest(SqlRequest)
	 * @see #fireListenersOnAfterRequest(SqlRequest)
	 * @see #fireListenersOnErrorRequest(SqlRequest, Exception)
	 */
	private void doHandleRequest(DataSource dataSource,SqlRequest request,
												SqlDocumentHandler handler) {
		IndexWriter writer = IndexWriterFactoryUtils.getIndexWriter(getIndexFactory());
		try {
			fireListenersOnBeforeRequest(request);
			List documents=indexResultSql(dataSource,request,handler);
			if( documents!=null ) {
				addDocumentsInIndex(writer,documents);
			}
			fireListenersOnAfterRequest(request);
		} catch(DataAccessException ex) {
			logger.error("Error during indexing the request",ex);
			fireListenersOnErrorRequest(request,ex);
		} catch(IOException ex) {
			logger.error("Error during indexing the request",ex);
			fireListenersOnErrorRequest(request,ex);
		} finally {
			IndexWriterFactoryUtils.releaseIndexWriter(getIndexFactory(),writer);
		}
	}

	/**
	 * This method is used to optimize the index when every request
	 * have been executed and their results indexed.
	 * 
	 * <p>This method gets an IndexWriter instance from the IndexWriterFactoryUtils
	 * class and release it at the end if necessary. 
	 * 
	 * @see IndexWriterFactoryUtils#getIndexWriter(IndexFactory)
	 * @see IndexWriterFactoryUtils#releaseIndexWriter(IndexFactory, IndexWriter)
	 */
	private void optimizeIndex() {
		IndexWriter writer = IndexWriterFactoryUtils.getIndexWriter(getIndexFactory());
		try {
			writer.optimize();
		} catch(IOException ex) {
			logger.error("Error during optimizing the index",ex);
			throw new LuceneIndexAccessException("Error during optimizing the index",ex);
		} finally {
			IndexWriterFactoryUtils.releaseIndexWriter(getIndexFactory(),writer);
		}
	}

	/**
	 * This method is the entry point to index a database using the specified
	 * datasource. It uses the registred requests and their corresponding handlers.
	 * 
	 * <p>In this case, the index will not be optimized.
	 *  
	 * @param dataSource the datasource to use
	 * @see #index(DataSource, boolean)
	 */
	public void index(DataSource dataSource) {
		index(dataSource,false);
	}

	/**
	 * This method is the entry point to index a database using the specified
	 * datasource. It uses the registred requests and their corresponding handlers.
	 * 
	 * <p>In this case, the index will be optimized after each request if the
	 * value of the optimizeIndex parameter is true.
	 * 
	 * <p>If there is an error during executing a request or indexing rows,
	 * the other requests will be executed. The error will notify to specified
	 * listeners.
	 *  
	 * @param dataSource the datasource to use
	 * @param optimizeIndex if the index must be optmized after
	 * the request indexing
	 * @see #doHandleRequest(DataSource, SqlRequest, SqlDocumentHandler, boolean)
	 */
	public void index(DataSource dataSource,boolean optimizeIndex) {
		Set requests=requestDocumentHandlers.keySet();
		for(Iterator i=requests.iterator();i.hasNext();) {
			SqlRequest request=(SqlRequest)i.next();
			SqlDocumentHandler handler=(SqlDocumentHandler)requestDocumentHandlers.get(request);
			doHandleRequest(dataSource,request,handler);
		}
		if( optimizeIndex ) {
			optimizeIndex();
		}
	}

	/**
	 * This is the sub class MappingSqlQuery used to delegate the mapRow callback
	 * to the getDocument of the specified SqlDocumentHandler for each result row
	 * of the request.
	 */
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
