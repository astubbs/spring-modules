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

package org.springmodules.lucene.index.support.handler.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.springmodules.lucene.index.DocumentHandlerException;
import org.springmodules.lucene.index.support.handler.AbstractDocumentHandler;
import org.springmodules.lucene.index.support.handler.DocumentHandler;

/**
 * This is the abstract class which specifies the way to index a database
 * row basing a JDBC ResultSet.
 * 
 * <p>These handlers must be registred on the DatabaseIndexer to be used.
 * This latter class executes every registred sql requests thanks to the
 * Spring JBDC framework and uses the getDocument callback to determine
 * the way to index every row a JDBC ResultSet.
 * 
 * @author Thierry Templier
 * @see org.springmodules.lucene.index.support.database.SqlRequest
 * @see org.springmodules.lucene.index.object.database.DatabaseIndexer#registerDocumentHandler(SqlRequest, SqlDocumentHandler)
 * @see org.springmodules.lucene.index.object.database.DatabaseIndexer#unregisterDocumentHandler(SqlRequest)
 */
public abstract class SqlDocumentHandler extends AbstractDocumentHandler {

	private void checkDescriptionParameters(Map description) {
		if( description.get(SqlRequest.SQL_REQUEST)==null ) {
			throw new DocumentHandlerException("The parameter "+SqlRequest.SQL_REQUEST+
					" is required for this type of document handler");
		}
	}
	
	protected Document doGetDocument(Map description, Object object) throws Exception {
		checkDescriptionParameters(description);

		SqlRequest request=new SqlRequest(description);
		return getDocument(request,(ResultSet)object);
	}

	public boolean supports(Class clazz) {
		return (ResultSet.class).isAssignableFrom(clazz);
	}

	/**
	 * This is a callback method which must implement to specify how to
	 * create a Lucene document from each row of the ResultSet corresponding
	 * to the request.
	 * <p>The returned document will be automatically added to the
	 * index if it is not null.
	 * <p>The SqlRequest object is passed to this callback if the implementation
	 * needs to set some informations of the corresponding request on the
	 * document. 
	 * @param request SqlRequest executed
	 * @param rs ResultSet corresponding to the request execution. It represents
	 * a database row
	 * @return the document to index constructed with the row datas
	 * @throws SQLException if there's an error extracting data.
	 * Subclasses can simply not catch SQLExceptions, relying on the
	 * framework to clean up.
	 */
	public abstract Document getDocument(SqlRequest request,ResultSet rs) throws SQLException;
}
