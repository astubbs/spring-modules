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
package org.springmodules.datamap;

import junit.framework.TestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;
import org.springmodules.datamap.dao.DataMapper;
import org.springmodules.datamap.jdbc.sqlmap.ActiveMapper;

import javax.sql.DataSource;

/**
 */
public class ActiveMapperIntegrationTests extends AbstractTransactionalDataSourceSpringContextTests {

    protected String[] getConfigLocations() {
        return new String[] {"classpath:org/springmodules/datamap/ActiveMapper-test-context.xml"};
    }

    protected void onSetUpInTransaction() throws Exception {
        jdbcTemplate.execute("create table beers (id integer, brand varchar(50))");
        jdbcTemplate.execute("create table beer_seq (value identity)");
        jdbcTemplate.execute("insert into beer_seq (value) values(0)");
    }

    protected void onTearDownInTransaction() {
        jdbcTemplate.execute("drop table beer_seq");
        jdbcTemplate.execute("drop table beers");
    }

    public void testSimpleFind() throws Exception {
        jdbcTemplate.execute("insert into beers (id, brand) values(12, 'Amstel')");
        DataMapper am = new ActiveMapper(Beer.class);
        am.setDataSource(jdbcTemplate.getDataSource());
        am.afterPropertiesSet();
        Beer b = (Beer) am.find(new Long(12));
        assertEquals("correct beer found", "Amstel", b.getBrand());
    }

    public void testSimpleAdd() throws Exception {
        DataMapper am = new ActiveMapper(Beer.class);
        am.setDataSource(jdbcTemplate.getDataSource());
        am.afterPropertiesSet();
        Beer b = new Beer();
        b.setBrand("Heineken");
        am.save(b);
        assertNotNull(b.getId());
        assertEquals("new beer is in db", 1, jdbcTemplate.queryForInt("select count(*) from beers where brand = 'Heineken'"));
    }

}
