/*
 * Copyright 2002-2004 the original author or authors.
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

package org.springmodules.samples.jrules.daos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springmodules.samples.jrules.model.Car;

/**
 * Dao implementation to get the list of cars in the database
 * 
 * @author Thierry Templier
 */
public class CarsDAOImpl extends JdbcDaoSupport implements CarsDAO {

	private class CarsQuery extends MappingSqlQuery {

		public CarsQuery(DataSource datasource,String sql) {
			super(datasource,sql);
		}

		/**
		 * @see org.springframework.jdbc.object.MappingSqlQuery#mapRow(java.sql.ResultSet, int)
		 */
		protected Object mapRow(ResultSet rs, int pos) throws SQLException {
			Car car=new Car();
			car.setId(rs.getInt("car_id"));
			car.setName(rs.getString("car_name"));
			car.setMark(rs.getString("car_mark"));
			car.setModel(rs.getString("car_model"));
			car.setYear(rs.getInt("car_year"));
			car.setPrice(rs.getFloat("car_price"));
			return car;
		}
		
	}

	/**
	 * @see org.springmodules.samples.jrules.daos.CarsDAO#getCars()
	 */
	public List getCars() {
		CarsQuery query=new CarsQuery(getDataSource(),"select * from car");
		return query.execute();
	}

}
