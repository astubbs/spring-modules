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

/**
 * @author Thierry Templier
 */
public class SqlRequest {
	private final String sql;
	private final Object[] params;
	private final int[] types;

	public SqlRequest(String sql,Object[] params,int[] types) {
		this.sql=sql;
		this.params=params;
		this.types=types;
	}
	/**
	 * @return
	 */
	public Object[] getParams() {
		return params;
	}

	/**
	 * @return
	 */
	public String getSql() {
		return sql;
	}

	/**
	 * @return
	 */
	public int[] getTypes() {
		return types;
	}

}
