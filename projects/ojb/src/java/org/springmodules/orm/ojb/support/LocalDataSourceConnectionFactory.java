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

package org.springmodules.orm.ojb.support;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ojb.broker.accesslayer.ConnectionFactoryManagedImpl;
import org.apache.ojb.broker.accesslayer.LookupException;
import org.apache.ojb.broker.metadata.JdbcConnectionDescriptor;

import org.springframework.beans.factory.BeanFactory;

/**
 * OJB connection factory that delegates to Spring-managed DataSource beans.
 *
 * <p>Define the following entry in your OJB.properties to use this connection factory:
 *
 * <pre>
 * ConnectionFactoryClass=org.springframework.orm.ojb.support.LocalDataSourceConnectionFactory</pre>
 *
 * Interprets JCD aliases in OJB's JDBC connection descriptors as Spring bean names.
 * For example, the following will delegate to the Spring bean named "myDataSource":
 *
 * <pre>
 * &lt;jdbc-connection-descriptor jcd-alias="myDataSource" default-connection="true" useAutoCommit="1"/&gt;</pre>
 *
 * Depends on LocalOjbConfigurer being defined as Spring bean, which will expose
 * the Spring BeanFactory to the corresponding static field of this connection factory.
 *
 * <p>Consider using the subclass TransactionAwareDataSourceConnectionFactory instead,
 * to let Spring's OJB access to participate in JDBC-based transactions managed outside
 * of OJB (for example, by Spring's DataSourceTransactionManager), or to automatically
 * apply transaction timeouts managed by PersistenceBrokerTransactionManager.
 *
 * @author Juergen Hoeller
 * @since 1.1
 * @see LocalOjbConfigurer
 * @see TransactionAwareDataSourceConnectionFactory
 */
public class LocalDataSourceConnectionFactory extends ConnectionFactoryManagedImpl {

	/**
	 * This will hold the BeanFactory to retrieve DataSource beans from.
	 */
	protected static BeanFactory beanFactory;

	/**
	 * Map that holds already retrieved DataSources,
	 * with JCD alias Strings as keys and DataSources as values.
	 */
	private Map dataSources = new HashMap();

	public LocalDataSourceConnectionFactory() {
		if (beanFactory == null) {
			throw new IllegalStateException("No BeanFactory found for configuration - " +
					"LocalOjbConfigurer must be defined as Spring bean");
		}
	}

	public Connection lookupConnection(JdbcConnectionDescriptor jcd) throws LookupException {
		try {
			DataSource dataSource = null;
			synchronized (this.dataSources) {
				dataSource = (DataSource) this.dataSources.get(jcd.getJcdAlias());
				if (dataSource == null) {
					dataSource = getDataSource(jcd.getJcdAlias());
					this.dataSources.put(jcd.getJcdAlias(), dataSource);
				}
			}
			return dataSource.getConnection();
		}
		catch (Exception ex) {
			throw new LookupException("Could not obtain connection from data source", ex);
		}
	}

	/**
	 * Return the DataSource to use for the given JCD alias.
	 * <p>This implementation fetches looks for a bean with the
	 * JCD alias name in the provided Spring BeanFactory.
	 * @param jcdAlias the JCD alias to retrieve a DataSource for
	 * @return the DataSource to use
	 */
	protected DataSource getDataSource(String jcdAlias) {
		return (DataSource) beanFactory.getBean(jcdAlias, DataSource.class);
	}

}
