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

package org.springmodules.orm.ojb;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

import org.apache.ojb.broker.Identity;
import org.apache.ojb.broker.PBKey;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerException;
import org.apache.ojb.broker.accesslayer.LookupException;
import org.apache.ojb.broker.query.Query;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.util.Assert;

/**
 * Helper class that simplifies OJB PersistenceBroker data access code,
 * and converts PersistenceBrokerExceptions into Spring DataAccessExceptions,
 * following the <code>org.springframework.dao</code> exception hierarchy.
 *
 * <p>The central method is <code>execute</code>, supporting OJB code implementing
 * the PersistenceBrokerCallback interface. It provides PersistenceBroker handling
 * such that neither the PersistenceBrokerCallback implementation nor the calling
 * code needs to explicitly care about retrieving/closing PersistenceBrokers,
 * or handling OJB lifecycle exceptions.
 *
 * <p>Typically used to implement data access or business logic services that
 * use OJB within their implementation but are OJB-agnostic in their interface.
 * The latter or code calling the latter only have to deal with business objects,
 * query objects, and <code>org.springframework.dao</code> exceptions.
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
 * @see #setPbKey
 * @see PersistenceBrokerCallback
 * @see org.apache.ojb.broker.PersistenceBroker
 * @see #getIteratorByQuery
 * @see #getReportQueryIteratorByQuery
 * @see PersistenceBrokerTransactionManager
 * @see org.springframework.transaction.jta.JtaTransactionManager
 */
public class PersistenceBrokerTemplate extends OjbAccessor implements PersistenceBrokerOperations {

	private boolean allowCreate = true;


	/**
	 * Create a new PersistenceBrokerTemplate,
	 * using the default connection configured for OJB.
	 * Can be further configured via bean properties.
	 */
	public PersistenceBrokerTemplate() {
	}

	/**
	 * Create a new PersistenceBrokerTemplate,
	 * using the default connection configured for OJB.
	 * @param allowCreate if a non-transactional PersistenceBroker should be created
	 * when no transactional PersistenceBroker can be found for the current thread
	 */
	public PersistenceBrokerTemplate(boolean allowCreate) {
		setAllowCreate(allowCreate);
		afterPropertiesSet();
	}

	/**
	 * Create a new PersistenceBrokerTemplate.
	 * @param pbKey the PBKey of the PersistenceBroker configuration to use
	 */
	public PersistenceBrokerTemplate(PBKey pbKey) {
		setPbKey(pbKey);
		afterPropertiesSet();
	}

	/**
	 * Create a new PersistenceBrokerTemplate.
	 * @param pbKey the PBKey of the PersistenceBroker configuration to use
	 * @param allowCreate if a new PersistenceBroker should be created
	 * if no thread-bound found
	 */
	public PersistenceBrokerTemplate(PBKey pbKey, boolean allowCreate) {
		setPbKey(pbKey);
		setAllowCreate(allowCreate);
		afterPropertiesSet();
	}

	/**
	 * Set if a new PersistenceBroker should be created when no transactional
	 * PersistenceBroker can be found for the current thread.
	 * <p>JdoTemplate is aware of a corresponding PersistenceBroker bound to the
	 * current thread, for example when using PersistenceBrokerTransactionManager.
	 * If allowCreate is true, a new non-transactional PersistenceManager will be
	 * created if none found, which needs to be closed at the end of the operation.
	 * If false, an IllegalStateException will get thrown in this case.
	 * @see OjbFactoryUtils#getPersistenceBroker(org.apache.ojb.broker.PBKey, boolean)
	 */
	public void setAllowCreate(boolean allowCreate) {
		this.allowCreate = allowCreate;
	}

	/**
	 * Return if a new PersistenceBroker should be created if no thread-bound found.
	 */
	public boolean isAllowCreate() {
		return allowCreate;
	}


	public Object execute(PersistenceBrokerCallback action) throws DataAccessException {
		Assert.notNull(action, "Callback object must not be null");

		PersistenceBroker pb = getPersistenceBroker();
		try {
			return action.doInPersistenceBroker(pb);
		}
		catch (PersistenceBrokerException ex) {
			throw convertOjbAccessException(ex);
		}
		catch (LookupException ex) {
			throw new DataAccessResourceFailureException("Could not retrieve resource", ex);
		}
		catch (SQLException ex) {
			throw convertJdbcAccessException(ex);
		}
		catch (RuntimeException ex) {
			// callback code threw application exception
			throw ex;
		}
		finally {
			releasePersistenceBroker(pb);
		}
	}

	public Collection executeFind(PersistenceBrokerCallback action) throws DataAccessException {
		Object result = execute(action);
		if (result != null && !(result instanceof Collection)) {
			throw new InvalidDataAccessApiUsageException(
					"Result object returned from PersistenceBrokerCallback isn't a Collection: [" + result + "]");
		}
		return (Collection) result;
	}


	public Object getObjectById(final Class entityClass, final Object idValue) throws DataAccessException {
		return execute(new PersistenceBrokerCallback() {
			public Object doInPersistenceBroker(PersistenceBroker pb) throws PersistenceBrokerException {
				Identity id = pb.serviceIdentity().buildIdentity(entityClass, idValue);
				Object result = pb.getObjectByIdentity(id);
				if (result == null) {
					throw new ObjectRetrievalFailureException(entityClass, idValue);
				}
				return result;
			}
		});
	}

	public Object getObjectByQuery(final Query query) throws DataAccessException {
		return execute(new PersistenceBrokerCallback() {
			public Object doInPersistenceBroker(PersistenceBroker pb) throws PersistenceBrokerException {
				return pb.getObjectByQuery(query);
			}
		});
	}

	public Collection getCollectionByQuery(final Query query) throws DataAccessException {
		return executeFind(new PersistenceBrokerCallback() {
			public Object doInPersistenceBroker(PersistenceBroker pb) throws PersistenceBrokerException {
				return pb.getCollectionByQuery(query);
			}
		});
	}

	public Iterator getIteratorByQuery(final Query query) throws DataAccessException {
		return (Iterator) execute(new PersistenceBrokerCallback() {
			public Object doInPersistenceBroker(PersistenceBroker pb) throws PersistenceBrokerException {
				return pb.getIteratorByQuery(query);
			}
		});
	}

	public Iterator getReportQueryIteratorByQuery(final Query query) {
		return (Iterator) execute(new PersistenceBrokerCallback() {
			public Object doInPersistenceBroker(PersistenceBroker pb) throws PersistenceBrokerException {
				return pb.getReportQueryIteratorByQuery(query);
			}
		});
	}

	public int getCount(final Query query) throws DataAccessException {
		Integer count = (Integer) execute(new PersistenceBrokerCallback() {
			public Object doInPersistenceBroker(PersistenceBroker pb) throws PersistenceBrokerException {
				return new Integer(pb.getCount(query));
			}
		});
		return count.intValue();
	}

	public void removeFromCache(final Object entityOrId) throws DataAccessException {
		execute(new PersistenceBrokerCallback() {
			public Object doInPersistenceBroker(PersistenceBroker pb) throws PersistenceBrokerException {
				pb.removeFromCache(entityOrId);
				return null;
			}
		});
	}

	public void clearCache() throws DataAccessException {
		execute(new PersistenceBrokerCallback() {
			public Object doInPersistenceBroker(PersistenceBroker pb) throws PersistenceBrokerException {
				pb.clearCache();
				return null;
			}
		});
	}

	public void store(final Object entity) throws DataAccessException {
		execute(new PersistenceBrokerCallback() {
			public Object doInPersistenceBroker(PersistenceBroker pb) throws PersistenceBrokerException {
				pb.store(entity);
				return null;
			}
		});
	}

	public void delete(final Object entity) throws DataAccessException {
		execute(new PersistenceBrokerCallback() {
			public Object doInPersistenceBroker(PersistenceBroker pb) throws PersistenceBrokerException {
				pb.delete(entity);
				return null;
			}
		});
	}

	public void deleteByQuery(final Query query) throws DataAccessException {
		execute(new PersistenceBrokerCallback() {
			public Object doInPersistenceBroker(PersistenceBroker pb) throws PersistenceBrokerException {
				pb.deleteByQuery(query);
				return null;
			}
		});
	}


	/**
	 * Get an OJB PersistenceBroker for the PBKey of this template.
	 * <p>Default implementation delegates to OjbFactoryUtils.
	 * Can be overridden in subclasses, e.g. for testing purposes.
	 * @return the PersistenceBroker
	 * @throws DataAccessResourceFailureException if the PersistenceBroker couldn't be created
	 * @throws IllegalStateException if no thread-bound PersistenceBroker found and allowCreate false
	 * @see #setJcdAlias
	 * @see #setPbKey
	 * @see #setAllowCreate
	 * @see OjbFactoryUtils#getPersistenceBroker(PBKey, boolean)
	 */
	protected PersistenceBroker getPersistenceBroker()
			throws DataAccessResourceFailureException, IllegalStateException {

		return OjbFactoryUtils.getPersistenceBroker(getPbKey(), isAllowCreate());
	}

	/**
	 * Close the given PersistenceBroker, created for the PBKey of this
	 * template, if it isn't bound to the thread.
	 * <p>Default implementation delegates to OjbFactoryUtils.
	 * Can be overridden in subclasses, e.g. for testing purposes.
	 * @param pb PersistenceBroker to close
	 * @see #setJcdAlias
	 * @see #setPbKey
	 * @see OjbFactoryUtils#releasePersistenceBroker
	 */
	protected void releasePersistenceBroker(PersistenceBroker pb) {
		OjbFactoryUtils.releasePersistenceBroker(pb, getPbKey());
	}


}
