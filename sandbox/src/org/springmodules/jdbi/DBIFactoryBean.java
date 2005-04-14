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

package org.springmodules.jdbi;

import org.skife.jdbi.spring.DBIBean;
import org.skife.jdbi.unstable.decorator.HandleDecorator;
import org.springframework.beans.factory.InitializingBean;

/**
 * Spring implementation of a factory bean to provide a DBI implementation.  This factory bean extends
 * the DBIBean provided by the DBI distibution.  It provides attached a handle decorator to provide
 * translation of SQLExceptions reported by DBI into Spring's DataAccessException hierarchy.
 *
 * @author Thomas Risberg
 */
public class DBIFactoryBean extends DBIBean implements InitializingBean {

    HandleDecorator builder;

    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        builder = new SQLExceptionTranslatingHandleDecorator();
        ((SQLExceptionTranslatingHandleDecorator)builder).setDataSource(getDataSource());
        super.setHandleDecoratorBuilder(builder);
    }

}
