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

package org.springmodules.orm.ojb;

import java.sql.SQLException;

import org.apache.ojb.broker.PBKey;
import org.apache.ojb.broker.PersistenceBrokerException;
import org.apache.ojb.broker.PersistenceBrokerFactory;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.support.JdbcAccessor;

/**
 * Base class for OJB-accessing classes, defining common properties like PBKey.
 * Extends JdbcAccessor to inherit SQLException translation capabilities.
 *
 * <p>Not intended to be used directly. See PersistenceBrokerTemplate.
 *
 * @author Juergen Hoeller
 * @since 1.1
 * @see PersistenceBrokerTemplate
 */
public class OjbAccessor extends JdbcAccessor {

	private PBKey pbKey = PersistenceBrokerFactory.getDefaultKey();


	/**
	 * Set the JDBC Connection Descriptor alias of the PersistenceBroker
	 * configuration to use. Default is the default connection configured for OJB.
	 */
	public void setJcdAlias(String jcdAlias) {
		this.pbKey = new PBKey(jcdAlias);
	}

	/**
	 * Return the JDBC Connection Descriptor alias of the PersistenceBroker
	 * configuration to use.
	 */
	public String getJcdAlias() {
		return (this.pbKey != null ? this.pbKey.getAlias() : null);
	}

	/**
	 * Set the PBKey of the PersistenceBroker configuration to use.
	 * Default is the default connection configured for OJB.
	 */
	public void setPbKey(PBKey pbKey) {
		this.pbKey = pbKey;
	}

	/**
	 * Return the PBKey of the PersistenceBroker configuration used.
	 */
	public PBKey getPbKey() {
		return pbKey;
	}


	/**
	 * Convert the given PersistenceBrokerException to an appropriate exception
	 * from the org.springframework.dao hierarchy. In case of a wrapped SQLException,
	 * the SQLExceptionTranslator inherited from the superclass gets applied.
	 * May be overridden in subclasses.
	 * @param ex PersistenceBrokerException that occured
	 * @return the corresponding DataAccessException instance
	 * @see #setExceptionTranslator
	 */
	public DataAccessException convertOjbAccessException(PersistenceBrokerException ex) {
		if (ex.getCause() instanceof PersistenceBrokerException) {
			return convertOjbAccessException((PersistenceBrokerException) ex.getCause());
		}
		else if (ex.getCause() instanceof SQLException) {
			return convertJdbcAccessException((SQLException) ex.getCause());
		}
		else {
			throw new OjbOperationException(ex);
		}
	}

	/**
	 * Convert the given SQLException to an appropriate exception from the
	 * org.springframework.dao hierarchy. Can be overridden in subclasses.
	 * <p>Note that SQLException can just occur here when callback code
	 * performs direct JDBC access via ConnectionManagerIF.getConnection().
	 * @param ex SQLException that occured
	 * @return the corresponding DataAccessException instance
	 * @see #setExceptionTranslator
	 * @see org.apache.ojb.broker.accesslayer.ConnectionManagerIF#getConnection
	 */
	protected DataAccessException convertJdbcAccessException(SQLException ex) {
		return getExceptionTranslator().translate("OJB operation", null, ex);
	}

}
