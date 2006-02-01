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

import javax.sql.DataSource;

import org.springmodules.lucene.index.support.handler.database.SqlDocumentHandler;
import org.springmodules.lucene.index.support.handler.database.SqlRequest;

/**
 * @author Thierry Templier
 */
public interface DatabaseIndexer {

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
	public abstract void registerDocumentHandler(
		SqlRequest sqlRequest,
		SqlDocumentHandler handler);

	/**
	 * This method is used to unregister a request.
	 * 
	 * @param sqlRequest the request to execute
	 */
	public abstract void unregisterDocumentHandler(SqlRequest sqlRequest);

	/**
	 * This method is used to add a listener to be notified during the
	 * indexing execution.
	 * 
	 * @param listener the listener to add
	 */
	public abstract void addListener(DatabaseIndexingListener listener);

	/**
	 * This method is used to remove a specifed listener.
	 * 
	 * @param listener the listener to remove
	 */
	public abstract void removeListener(DatabaseIndexingListener listener);

	/**
	 * This method is the entry point to index a database using the specified
	 * datasource. It uses the registred requests and their corresponding handlers.
	 * 
	 * <p>In this case, the index will not be optimized.
	 *  
	 * @param dataSource the datasource to use
	 */
	public abstract void index(DataSource dataSource);

	/**
	 * This method is the entry point to index a database using the specified
	 * datasource. It uses the registred requests and their corresponding handlers.
	 * 
	 * <p>In this case, the index will be optimized after each request if the
	 * value of the optimizeIndex parameter is true.
	 * 
	 * <p>If there is an error during executing a request or indexing rows,
	 * the other requests will be executed. However the error will notify to
	 * specified listeners.
	 *  
	 * @param dataSource the datasource to use
	 * @param optimizeIndex if the index must be optmized after
	 * the request indexing
	 */
	public abstract void index(DataSource dataSource, boolean optimizeIndex);
}