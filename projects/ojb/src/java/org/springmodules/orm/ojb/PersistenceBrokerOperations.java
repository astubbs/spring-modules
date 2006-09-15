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

import java.util.Collection;
import java.util.Iterator;

import org.apache.ojb.broker.query.Query;

import org.springframework.dao.DataAccessException;

/**
 * Interface that specifies a basic set of OJB PersistenceBroker operations.
 * Implemented by PersistenceBrokerTemplate. Not often used, but a useful
 * option to enhance testability, as it can easily be mocked or stubbed.
 *
 * <p>Provides PersistenceBrokerTemplate's data access methods that mirror
 * various PersistenceBroker methods. See the OJB PersistenceBroker javadocs
 * for details on those methods. Additionally, there is a convenient
 * <code>getObjectById</code> method (Hibernate/JDO-style).
 *
 * <p>Note that operations that return an Iterator (that is,
 * <code>getIteratorByQuery</code> and <code>getReportQueryIteratorByQuery</code>)
 * are supposed to be used within Spring-managed transactions
 * (with PersistenceBrokerTransactionManager or JtaTransactionManager).
 * Else, the Iterator won't be able to read results from its ResultSet anymore,
 * as the underlying PersistenceBroker will already have been closed.
 *
 * @author Juergen Hoeller
 * @since 1.1
 * @see PersistenceBrokerTemplate
 * @see org.apache.ojb.broker.PersistenceBroker
 * @see #getIteratorByQuery
 * @see #getReportQueryIteratorByQuery
 * @see PersistenceBrokerTransactionManager
 * @see org.springframework.transaction.jta.JtaTransactionManager
 */
public interface PersistenceBrokerOperations {

	/**
	 * Execute the action specified by the given action object within a
	 * PersistenceBroker. Application exceptions thrown by the action object
	 * get propagated to the caller (can only be unchecked). OJB exceptions
	 * are transformed into appropriate DAO ones. Allows for returning a
	 * result object, i.e. a domain object or a collection of domain objects.
	 * <p>Note: Callback code is not supposed to handle transactions itself!
	 * Use an appropriate transaction manager like PersistenceBrokerTransactionManager.
	 * @param action action object that specifies the OJB action
	 * @return a result object returned by the action, or <code>null</code>
	 * @throws org.springframework.dao.DataAccessException in case of OJB errors
	 * @see PersistenceBrokerTransactionManager
	 * @see org.springframework.dao
	 * @see org.springframework.transaction
	 */
	Object execute(PersistenceBrokerCallback action) throws DataAccessException;

	/**
	 * Execute the specified action assuming that the result object is a
	 * Collection. This is a convenience method for executing OJB queries
	 * within an action.
	 * @param action action object that specifies the OJB action
	 * @return a result object returned by the action, or <code>null</code>
	 * @throws org.springframework.dao.DataAccessException in case of OJB errors
	 */
	Collection executeFind(PersistenceBrokerCallback action) throws DataAccessException;


	//-------------------------------------------------------------------------
	// Convenience methods for load, find, save, delete
	//-------------------------------------------------------------------------

	/**
	 * Return the persistent instance of the given entity class
	 * with the given id value, throwing an exception if not found.
	 * <p>The given id value is typically just unique within the namespace of the
	 * persistent class, corresponding to a single primary key in a database table.
	 * @param entityClass a persistent class
	 * @param idValue an id value of the persistent instance
	 * @return the persistent instance
	 * @throws org.springframework.orm.ObjectRetrievalFailureException if not found
	 * @throws org.springframework.dao.DataAccessException in case of OJB errors
	 * @see org.apache.ojb.broker.IdentityFactory#buildIdentity(Class, Object)
	 * @see org.apache.ojb.broker.PersistenceBroker#getObjectByIdentity
	 */
	Object getObjectById(Class entityClass, Object idValue) throws DataAccessException;

	/**
	 * @see org.apache.ojb.broker.PersistenceBroker#getObjectByQuery
	 * @throws org.springframework.dao.DataAccessException in case of OJB errors
	 */
	Object getObjectByQuery(Query query) throws DataAccessException;

	/**
	 * @see org.apache.ojb.broker.PersistenceBroker#getCollectionByQuery
	 * @throws org.springframework.dao.DataAccessException in case of OJB errors
	 */
	Collection getCollectionByQuery(Query query) throws DataAccessException;

	/**
	 * @see org.apache.ojb.broker.PersistenceBroker#getIteratorByQuery
	 * @throws org.springframework.dao.DataAccessException in case of OJB errors
	 */
	Iterator getIteratorByQuery(Query query) throws DataAccessException;

	/**
	 * @see org.apache.ojb.broker.PersistenceBroker#getReportQueryIteratorByQuery
	 * @throws org.springframework.dao.DataAccessException in case of OJB errors
	 */
	Iterator getReportQueryIteratorByQuery(Query query) throws DataAccessException;
	
	/**
	 * @see org.apache.ojb.broker.PersistenceBroker#getCount
	 * @throws org.springframework.dao.DataAccessException in case of OJB errors
	 */
	int getCount(Query query) throws DataAccessException;

	/**
	 * @see org.apache.ojb.broker.PersistenceBroker#removeFromCache
	 * @throws org.springframework.dao.DataAccessException in case of OJB errors
	 */
	void removeFromCache(Object entityOrId) throws DataAccessException;

	/**
	 * @see org.apache.ojb.broker.PersistenceBroker#clearCache
	 * @throws org.springframework.dao.DataAccessException in case of OJB errors
	 */
	void clearCache() throws DataAccessException;

	/**
	 * @see org.apache.ojb.broker.PersistenceBroker#store
	 * @throws org.springframework.dao.DataAccessException in case of OJB errors
	 */
	void store(Object entity) throws DataAccessException;

	/**
	 * @see org.apache.ojb.broker.PersistenceBroker#delete
	 * @throws org.springframework.dao.DataAccessException in case of OJB errors
	 */
	void delete(Object entity) throws DataAccessException;

	/**
	 * @see org.apache.ojb.broker.PersistenceBroker#deleteByQuery
	 * @throws org.springframework.dao.DataAccessException in case of OJB errors
	 */
	void deleteByQuery(Query query) throws DataAccessException;

}
