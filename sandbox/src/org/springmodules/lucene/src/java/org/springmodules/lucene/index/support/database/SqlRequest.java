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

package org.springmodules.lucene.index.support.database;

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
	private final String sql;
	private final Object[] params;
	private final int[] types;

	public SqlRequest(String sql) {
		this(sql,null,null);
	}

	public SqlRequest(String sql,Object[] params,int[] types) {
		this.sql=sql;
		this.params=params;
		this.types=types;
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

}
