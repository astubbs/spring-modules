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

package org.springmodules.samples.lucene.dao.indexing;

import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.object.SqlFunction;

/**
 * @author Thierry Templier
 */
public class DocumentIdDaoImpl extends JdbcDaoSupport implements DocumentIdDao {

	public long getNextDocumentId() {
		SqlFunction sqlFunction=new SqlFunction(getDataSource(),
									"select next_document_id from document_id");
		sqlFunction.compile();
		return sqlFunction.run();
	}

	public void incrementDocumentId() {
		SqlFunction sqlFunction=new SqlFunction(getDataSource(),
									"select next_document_id from document_id");
		sqlFunction.compile();
		int nextDocumentId=sqlFunction.run();
		getJdbcTemplate().update("update document_id set next_document_id=?",
									new Object[] { new Integer(nextDocumentId+1)});
	}
}
