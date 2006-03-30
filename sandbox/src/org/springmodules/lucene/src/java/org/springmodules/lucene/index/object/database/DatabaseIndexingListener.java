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

import org.springmodules.lucene.index.support.database.SqlRequest;


/**
 * Database indexing listener to be notified when the indexing of the request
 * rows begins and when it ends successfully or with errors.
 * 
 * <p>The support defines an adapter to this listener to be able to use a
 * sub set of database indexing events.
 * 
 * <p>A database indexing listener must be added to the DatabaseIndexer class
 * to be call during the indexing.
 * 
 * @author Thierry Templier
 * @see org.springmodules.lucene.index.object.database.DatabaseIndexer#addListener(DatabaseIndexingListener)
 * @see org.springmodules.lucene.index.object.database.DatabaseIndexer#removeListener(DatabaseIndexingListener)
 * @see org.springmodules.lucene.index.object.database.DatabaseIndexingListenerAdapter
 */
public interface DatabaseIndexingListener {

	/**
	 * This callback method is called before executing the sql request
	 * and indexing each resulting rows.
	 * @param request the sql request which will be executed
	 */
	public void beforeIndexingRequest(SqlRequest request);

	/**
	 * This callback method is called after executing the sql request
	 * and indexing each resulting rows if it is successful.
	 * @param request the sql request which will be executed
	 */
	public void afterIndexingRequest(SqlRequest request);

	/**
	 * This callback method is called after executing the sql request
	 * and indexing each resulting rows if there are errors.
	 * @param request the sql request which will be executed
	 */
	public void onErrorIndexingRequest(SqlRequest request,Exception ex);
}
