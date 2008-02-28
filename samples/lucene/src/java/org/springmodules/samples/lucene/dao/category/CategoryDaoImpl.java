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

package org.springmodules.samples.lucene.dao.category;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springframework.jdbc.object.SqlFunction;
import org.springmodules.samples.lucene.bean.indexing.DocumentCategory;

/**
 * @author Thierry Templier
 */
public class CategoryDaoImpl extends JdbcDaoSupport implements CategoryDao {

	private class CategoriesMappingQuery extends MappingSqlQuery {

		public CategoriesMappingQuery(DataSource ds) {
			super(ds, "SELECT CATEGORY_ID,CATEGORY_NAME FROM CATEGORY");
			compile();
		}

		public Object mapRow(ResultSet rs, int rowNumber) throws SQLException {
			DocumentCategory category = new DocumentCategory(rs.getInt("CATEGORY_ID"),
													rs.getString("CATEGORY_NAME"));
			return category;
		} 
	}

	private class CategoryMappingQuery extends MappingSqlQuery {

		public CategoryMappingQuery(DataSource ds) {
			super(ds, "SELECT CATEGORY_ID,CATEGORY_NAME FROM CATEGORY WHERE CATEGORY_ID=?");
			super.declareParameter(new SqlParameter("id", Types.INTEGER));
			compile();
		}

		public Object mapRow(ResultSet rs, int rowNumber) throws SQLException {
			DocumentCategory category = new DocumentCategory(rs.getInt("CATEGORY_ID"),
													rs.getString("CATEGORY_NAME"));
			return category;
		} 
	}

	public List getCategories() {
		CategoriesMappingQuery query = new CategoriesMappingQuery(getDataSource());
		return query.execute();
	}

	public DocumentCategory getCategory(int id) {
		CategoryMappingQuery query = new CategoryMappingQuery(getDataSource());
		List categories = query.execute(new Object[] {new Integer(id)});
		if( categories.size()==1 ) {
			return (DocumentCategory)categories.get(0);
		} else {
			return null;
		}
	}

	public void addCategory(DocumentCategory category) {
		SqlFunction sqlFunction = new SqlFunction(getDataSource(),
									"select category_id from category");
		sqlFunction.compile();
		int categoryId = sqlFunction.run()+1;
		category.setId(categoryId);

		getJdbcTemplate().update(
			"insert into category (category_id,category_name) values(?,?)",
			new Object[] {new Integer(category.getId()), category.getName()},
			new int[] { Types.INTEGER, Types.VARCHAR });
	}

	public void updateCategory(DocumentCategory category) {
		getJdbcTemplate().update(
			"update category set category_id=?,category_name=? where category_id=?",
			new Object[] {new Integer(category.getId()), category.getName(),
							new Integer(category.getId())},
			new int[] {Types.INTEGER, Types.VARCHAR,Types.INTEGER });
	}

	public void deleteCategory(DocumentCategory category) {
		getJdbcTemplate().update(
			"delete from category where category_id=?",
			new Object[] { new Integer(category.getId()) },
			new int[] { Types.INTEGER });
	}
}
