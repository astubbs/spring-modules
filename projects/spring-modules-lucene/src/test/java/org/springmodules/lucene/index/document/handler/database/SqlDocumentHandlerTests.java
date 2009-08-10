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

package org.springmodules.lucene.index.document.handler.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.lucene.document.Document;
import org.easymock.MockControl;
import org.springmodules.lucene.index.DocumentHandlerException;
import org.springmodules.lucene.index.document.handler.DocumentHandler;

public class SqlDocumentHandlerTests extends TestCase {
	public void testSupport() throws Exception {
		MockControl resultSetControl = MockControl.createControl(ResultSet.class);
		ResultSet resultSet = (ResultSet)resultSetControl.getMock();
		
		DocumentHandler documentHandler = new SqlDocumentHandler() {
			public Document getDocument(SqlRequest request, ResultSet rs) throws SQLException {
				return null;
			}
		};

		resultSetControl.replay();
		
		Map description = new HashMap();
		description.put(SqlRequest.SQL_REQUEST, "sql");
		documentHandler.getDocument(description, resultSet);

		resultSetControl.verify();
	}

	public void testNotSupport() throws Exception {
		DocumentHandler documentHandler = new SqlDocumentHandler() {
			public Document getDocument(SqlRequest request, ResultSet rs) throws SQLException {
				return null;
			}
		};
	
		Map description = new HashMap();
		description.put(SqlRequest.SQL_REQUEST, "sql");
		try {
			documentHandler.getDocument(description, "test");
			fail();
		} catch (DocumentHandlerException ex) {
		}
	}

	public void testDescription() throws Exception {
		MockControl resultSetControl = MockControl.createControl(ResultSet.class);
		ResultSet resultSet = (ResultSet)resultSetControl.getMock();
		
		DocumentHandler documentHandler = new SqlDocumentHandler() {
			public Document getDocument(SqlRequest request, ResultSet rs) throws SQLException {
				return null;
			}
		};
	
		Map description = new HashMap();
		description.put(SqlRequest.SQL_REQUEST, "sql");
		try {
			documentHandler.getDocument(description, "test");
			fail();
		} catch (DocumentHandlerException ex) {
		}

		resultSetControl.replay();
		
		description.put(SqlRequest.SQL_REQUEST, "sql");
		documentHandler.getDocument(description, resultSet);

		resultSetControl.verify();
	}

	public void testGetDocument() throws Exception {
		MockControl resultSetControl = MockControl.createControl(ResultSet.class);
		ResultSet resultSet = (ResultSet)resultSetControl.getMock();
		
		String sql = "sql";
		Object[] params = new Object[] { "param1", new Integer(12)};
		int[] paramTypes = new int[] { Types.VARCHAR, Types.INTEGER };

		final boolean[] called = { false };
		final SqlRequest[] sqlRequests = { null };
		DocumentHandler documentHandler = new SqlDocumentHandler() {
			public Document getDocument(SqlRequest request, ResultSet rs) throws SQLException {
				called[0] = true;
				sqlRequests[0] = request;
				return null;
			}
		};
	
		resultSetControl.replay();
		
		Map description = new HashMap();
		description.put(SqlRequest.SQL_REQUEST, sql);
		description.put(SqlRequest.REQUEST_PARAMETERS, params);
		description.put(SqlRequest.REQUEST_PARAMETER_TYPES, paramTypes);
		documentHandler.getDocument(description, resultSet);

		resultSetControl.verify();
		assertTrue(called[0]);
		assertEquals(sqlRequests[0].getSql(), sql);
		assertEquals(sqlRequests[0].getParams()[0], params[0]);
		assertEquals(sqlRequests[0].getParams()[1], params[1]);
		assertEquals(sqlRequests[0].getTypes()[0], paramTypes[0]);
		assertEquals(sqlRequests[0].getTypes()[1], paramTypes[1]);
	}
}
