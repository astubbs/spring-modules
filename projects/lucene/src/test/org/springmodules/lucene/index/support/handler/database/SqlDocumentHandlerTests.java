package org.springmodules.lucene.index.support.handler.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.lucene.document.Document;
import org.springmodules.lucene.index.DocumentHandlerException;
import org.springmodules.lucene.index.support.handler.DocumentHandler;

public class SqlDocumentHandlerTests extends TestCase {
	public void testSupport() throws Exception {
		DocumentHandler documentHandler=new SqlDocumentHandler() {
			public Document getDocument(SqlRequest request, ResultSet rs) throws SQLException {
				return null;
			}
		};

		Map description = new HashMap();
		description.put(SqlRequest.SQL_REQUEST,"sql");
		documentHandler.getDocument(description, new MockResultSet());
	}

	public void testNotSupport() throws Exception {
		DocumentHandler documentHandler=new SqlDocumentHandler() {
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

		description.put(SqlRequest.SQL_REQUEST, "sql");
		documentHandler.getDocument(description, new MockResultSet());
	}

	public void testGetDocument() throws Exception {
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
	
		Map description = new HashMap();
		description.put(SqlRequest.SQL_REQUEST, sql);
		description.put(SqlRequest.REQUEST_PARAMETERS, params);
		description.put(SqlRequest.REQUEST_PARAMETER_TYPES, paramTypes);
		documentHandler.getDocument(description, new MockResultSet());

		assertTrue(called[0]);
		assertEquals(sqlRequests[0].getSql(), sql);
		assertEquals(sqlRequests[0].getParams()[0], params[0]);
		assertEquals(sqlRequests[0].getParams()[1], params[1]);
		assertEquals(sqlRequests[0].getTypes()[0], paramTypes[0]);
		assertEquals(sqlRequests[0].getTypes()[1], paramTypes[1]);
	}
}
