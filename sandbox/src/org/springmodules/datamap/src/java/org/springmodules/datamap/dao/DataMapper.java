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

package org.springmodules.datamap.dao;

import org.springframework.beans.factory.InitializingBean;

import javax.sql.DataSource;
import java.util.List;

/**
 * Interface defining the mapping contract.
 *
 * @author Thomas Risberg
 * @since 0.3
 * @see org.springframework.beans.factory.InitializingBean
 */
public interface DataMapper extends InitializingBean {

    public void setDataSource(DataSource dataSource);

    Object find(Object id);

    List findAll();

    List findBy(String field, int operator, Object argument);

    void save(Object o);

    void delete(Object o);
}
