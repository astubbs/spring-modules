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

import java.util.HashMap;
import java.util.Map;

/**
 * Class which contains every informations about a sql request
 * and its parameters.
 * 
 * <p>This is used with the DatabaseIndexer to specify the request to
 * execute. So these requests are associated with a SqlDocumentHandler
 * to specify how to index the datas contained by the returned ResultSet.
 * 
 * @author Thierry Templier
 * @see org.springmodules.lucene.index.object.database.DatabaseIndexer
 * @see org.springmodules.lucene.index.support.database.SqlDocumentHandler
 */
public class SqlRequest {

	public static final Object SQL_REQUEST = "sql.request";
	public static final Object REQUEST_PARAMETERS = "request.parameters";
	public static final Object REQUEST_PARAMETER_TYPES = "request.parameter.types";

	private String sql;
	private Object[] params;
	private int[] types;

	public SqlRequest(String sql) {
		this(sql, null, null);
	}

	public SqlRequest(String sql, Object[] params, int[] types) {
		this.sql = sql;
		this.params = params;
		this.types = types;
	}

	public SqlRequest(Map description) {
		this.sql = (String)description.get(SqlRequest.SQL_REQUEST);
		this.params = (Object[])description.get(SqlRequest.REQUEST_PARAMETERS);
		if( this.params==null ) {
			this.params = new Object[0];
		}
		this.types = (int[])description.get(SqlRequest.REQUEST_PARAMETER_TYPES);
		if( this.types==null ) {
			this.types = new int[0];
		}
	}
	
	public Object[] getParams() {
		return params;
	}

	public String getSql() {
		return sql;
	}

	public int[] getTypes() {
		return types;
	}

	public Map getDescription() {
		Map description = new HashMap();
		description.put(SQL_REQUEST, sql);
		description.put(REQUEST_PARAMETERS, params);
		description.put(REQUEST_PARAMETER_TYPES, types);
		return description;
	}
	
}
