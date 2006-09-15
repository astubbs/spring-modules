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

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

/**
 * Subclass of LocalDataSourceConnectionFactory that returns transaction-aware
 * proxies for all DataSources retrieved by OJB.
 *
 * <p>Define the following entry in your OJB.properties to use this connection factory:
 *
 * <pre>
 * ConnectionFactoryClass=org.springframework.orm.ojb.support.TransactionAwareDataSourceConnectionFactory</pre>
 *
 * This connection factory allows Spring's OJB access to participate in JDBC-based
 * transactions managed outside of OJB (for example, by Spring's DataSourceTransactionManager).
 * This can be convenient if you need a different local transaction strategy for another O/R
 * mapping tool, for example, but still want OJB access to join into those transactions.
 *
 * <p>A further benefit of this factory is that plain PersistenceBrokers
 * (opened directly via the PersistenceBrokerFactory, outside of Spring's OJB support)
 * will still participate in active Spring-managed transactions.
 *
 * <p>As a further effect, using a transaction-aware DataSource will apply remaining
 * transaction timeouts to all created JDBC Statements. This means that all operations
 * performed by OJB will automatically participate in Spring-managed transaction timeouts.
 * This is even desirable for transactions managed by PersistenceBrokerTransactionManager.
 *
 * @author Juergen Hoeller
 * @since 1.1.4
 * @see org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy
 * @see org.springframework.jdbc.datasource.DataSourceTransactionManager
 * @see org.springframework.orm.ojb.PersistenceBrokerTransactionManager
 */
public class TransactionAwareDataSourceConnectionFactory extends LocalDataSourceConnectionFactory {

	/**
	 * Return a TransactionAwareDataSourceProxy for the original DataSource
	 * (i.e. the Spring bean with the JCD alias name), provided that it
	 * isn't a TransactionAwareDataSourceProxy already.
	 */
	protected DataSource getDataSource(String jcdAlias) {
		DataSource originalDataSource = super.getDataSource(jcdAlias);
		if (originalDataSource instanceof TransactionAwareDataSourceProxy) {
			return originalDataSource;
		}
		else {
			return new TransactionAwareDataSourceProxy(originalDataSource);
		}
	}

}
