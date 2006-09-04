/*
 * Copyright 2002-2006 the original author or authors.
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

package org.springmodules.orm.orbroker;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import net.sourceforge.orbroker.Broker;
import net.sourceforge.orbroker.BrokerException;
import net.sourceforge.orbroker.Executable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.jdbc.support.SQLStateSQLExceptionTranslator;
import org.springframework.util.Assert;

/**
 * Base class for BrokerTemplate.
 *
 * @author Omar Irbouh
 * @see BrokerTemplate
 * @since 2005.06.02
 */
public abstract class BrokerAccessor implements InitializingBean {

	protected final Log logger = LogFactory.getLog(getClass());

	/**
	 * Helper to translate SQL exceptions to DataAccessExceptions
	 */
	private SQLExceptionTranslator exceptionTranslator;

	private boolean lazyInit = true;

	private Broker broker;

	public DataSource getDataSource() {
		return (this.broker != null ? this.broker.getDataSource() : null);
	}

	/**
	 * Specify the database product name for the DataSource that this accessor uses.
	 * This allows to initialize a SQLErrorCodeSQLExceptionTranslator without
	 * obtaining a Connection from the DataSource to get the metadata.
	 *
	 * @param dbName the database product name that identifies the error codes entry
	 * @see org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator#setDatabaseProductName
	 * @see java.sql.DatabaseMetaData#getDatabaseProductName()
	 */
	public void setDatabaseProductName(String dbName) {
		this.exceptionTranslator = new SQLErrorCodeSQLExceptionTranslator(dbName);
	}

	/**
	 * Set the exception translator for this instance.
	 * <p>If no custom translator is provided, a default SQLErrorCodeSQLExceptionTranslator
	 * is used which examines the SQLException's vendor-specific error code.
	 *
	 * @param exceptionTranslator exception translator
	 * @see org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator
	 * @see org.springframework.jdbc.support.SQLStateSQLExceptionTranslator
	 */
	public void setExceptionTranslator(SQLExceptionTranslator exceptionTranslator) {
		this.exceptionTranslator = exceptionTranslator;
	}

	/**
	 * Return the exception translator for this instance.
	 * <p>Creates a default SQLErrorCodeSQLExceptionTranslator for the specified
	 * DataSource if none set.
	 */
	public SQLExceptionTranslator getExceptionTranslator() {
		if (this.exceptionTranslator == null) {
			DataSource ds = getDataSource();
			if (ds != null) {
				return new SQLErrorCodeSQLExceptionTranslator(ds);
			}
			return new SQLStateSQLExceptionTranslator();
		}
		return this.exceptionTranslator;
	}

	/**
	 * Set whether to lazily initialize the SQLExceptionTranslator for this accessor,
	 * on first encounter of a SQLException. Default is "true"; can be switched to
	 * "false" for initialization on startup.
	 * <p>Early initialization only applies if <code>afterPropertiesSet</code> is called.
	 *
	 * @see #getExceptionTranslator
	 * @see #afterPropertiesSet
	 */
	public void setLazyInit(boolean lazyInit) {
		this.lazyInit = lazyInit;
	}

	/**
	 * Return whether to lazily initialize the SQLExceptionTranslator for this accessor.
	 */
	public boolean isLazyInit() {
		return lazyInit;
	}

	public void setBroker(Broker broker) {
		this.broker = broker;
	}

	public Broker getBroker() {
		return broker;
	}

	/**
	 * Eagerly initialize the exception translator,
	 * creating a default one for the specified Broker if none set.
	 */
	public void afterPropertiesSet() {
		Assert.notNull(getBroker(), "broker is required");

		if (!isLazyInit()) {
			getExceptionTranslator();
		}
	}

	protected DataAccessException convertBrokerException(BrokerException e) {
		Throwable cause = e.getCause();
		if (cause != null && cause instanceof SQLException)
			throw translateJdbcException((SQLException) cause);
		else
			throw new BrokerOperationException(e);
	}

	protected DataAccessException translateJdbcException(SQLException cause) {
		return getExceptionTranslator().translate("ORBroker operation", null, cause);
	}

	protected Executable newExecutable(Broker broker, Connection connection) throws DataAccessException {
		Assert.notNull(broker, "No broker specified");
		return broker.obtainExecutable(connection);
	}

	protected void releaseExecutable(Broker broker, Executable executable) {
		Assert.notNull(broker, "No broker specified");
		Assert.notNull(executable, "No executable specified");

		// release orbroker executable
		try {
			broker.releaseExecutable(executable);
		} catch (BrokerException e) {
			logger.error("Could not release ORBroker Executable", e);
		}
	}

}