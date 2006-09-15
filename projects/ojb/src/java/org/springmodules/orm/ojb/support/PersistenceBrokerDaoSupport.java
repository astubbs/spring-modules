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

package org.springmodules.orm.ojb.support;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerException;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.support.DaoSupport;
import org.springmodules.orm.ojb.OjbFactoryUtils;
import org.springmodules.orm.ojb.PersistenceBrokerTemplate;

/**
 * Convenient super class for OJB PersistenceBroker data access objects.
 *
 * <p>Allows a JDBC Connection Descriptor alias to be set, providing a
 * PersistenceBrokerTemplate based on it to subclasses. Can alternatively
 * be initialized directly with a PersistenceBrokerTemplate, to reuse the
 * latter's settings like PBKey, DataSource, etc.
 *
 * <p>This base class is mainly intended for PersistenceBrokerTemplate usage but
 * can also be used when working with OjbFactoryUtils directly. Convenience
 * <code>getPersistenceBroker</code> and <code>releasePersistenceBroker</code>
 * methods are provided for that usage style.
 *
 * <p>This class will create its own PersistenceBrokerTemplate if no explicit
 * instance is passed in. The "allowCreate" flag on that PersistenceBrokerTemplate
 * will be "true" by default. A custom PersistenceBrokerTemplate instance can be
 * used through overriding <code>createHibernateTemplate</code>.
 *
 * @author Juergen Hoeller
 * @since 1.1
 * @see #setJcdAlias
 * @see #setPersistenceBrokerTemplate
 * @see #createPersistenceBrokerTemplate
 * @see #getPersistenceBroker
 * @see #releasePersistenceBroker
 * @see org.springframework.orm.ojb.PersistenceBrokerTemplate
 */
public abstract class PersistenceBrokerDaoSupport extends DaoSupport {

	private PersistenceBrokerTemplate persistenceBrokerTemplate = createPersistenceBrokerTemplate();


	/**
	 * Create a new default PersistenceBrokerTemplate on initialization.
	 * The returned template instance might be populated with a JCD alias,
	 * or get overwritten by an explicitly passed-in PersistenceBrokerTemplate.
	 * <p>Can be overridden in subclasses to provide a PersistenceBrokerTemplate instance
	 * with different configuration, or a custom PersistenceBrokerTemplate subclass.
	 * @return the new PersistenceBrokerTemplate instance
	 * @see #setJcdAlias
	 * @see #setPersistenceBrokerTemplate
	 */
	protected PersistenceBrokerTemplate createPersistenceBrokerTemplate() {
		return new PersistenceBrokerTemplate();
	}

	/**
	 * Set the JDBC Connection Descriptor alias of the PersistenceBroker
	 * configuration to use. Default is the default connection configured for OJB.
	 */
	public final void setJcdAlias(String jcdAlias) {
		this.persistenceBrokerTemplate.setJcdAlias(jcdAlias);
	}

	/**
	 * Return the JDBC Connection Descriptor alias of the PersistenceBroker
	 * configuration to use.
	 */
	public final String getJcdAlias() {
		return this.persistenceBrokerTemplate.getJcdAlias();
	}

	/**
	 * Set the PersistenceBrokerTemplate for this DAO explicitly,
	 * as an alternative to specifying a JCD alias.
	 */
	public final void setPersistenceBrokerTemplate(PersistenceBrokerTemplate persistenceBrokerTemplate) {
		this.persistenceBrokerTemplate = persistenceBrokerTemplate;
	}

	/**
	 * Return the PersistenceBrokerTemplate for this DAO, pre-initialized
	 * with the JCD alias or set explicitly.
	 */
	public final PersistenceBrokerTemplate getPersistenceBrokerTemplate() {
		return persistenceBrokerTemplate;
	}

	protected final void checkDaoConfig() {
		if (this.persistenceBrokerTemplate == null) {
			throw new IllegalArgumentException("jcdAlias or persistenceBrokerTemplate is required");
		}
	}


	/**
	 * Get an OJB PersistenceBroker. Is aware of a corresponding
	 * PersistenceBroker bound to the current thread, for example when using
	 * PersistenceBrokerTransactionManager. Will create a new PersistenceBroker
	 * else, if allowCreate is true.
	 * @param allowCreate if a non-transactional PersistenceBroker should be created
	 * when no transactional PersistenceBroker can be found for the current thread
	 * @return the PersistenceBroker
	 * @throws DataAccessResourceFailureException if the PersistenceBroker couldn't be created
	 * @throws IllegalStateException if no thread-bound PersistenceBroker found and allowCreate false
	 */
	protected final PersistenceBroker getPersistenceBroker(boolean allowCreate)
	    throws DataAccessResourceFailureException, IllegalStateException {

		return OjbFactoryUtils.getPersistenceBroker(this.persistenceBrokerTemplate.getPbKey(), allowCreate);
	}

	/**
	 * Convert the given PersistenceBrokerException to an appropriate exception
	 * from the org.springframework.dao hierarchy. In case of a wrapped SQLException,
	 * the PersistenceBrokerTemplate's SQLExceptionTranslator gets applied.
	 * @param ex PersistenceBrokerException that occured
	 * @return the corresponding DataAccessException instance
	 * @see org.springframework.orm.ojb.PersistenceBrokerTemplate#convertOjbAccessException
	 */
	protected final DataAccessException convertOjbAccessException(PersistenceBrokerException ex) {
		return this.persistenceBrokerTemplate.convertOjbAccessException(ex);
	}

	/**
	 * Close the given PersistenceBroker if it isn't bound to the thread.
	 * @param pb PersistenceBroker to close
	 */
	protected final void releasePersistenceBroker(PersistenceBroker pb) {
		OjbFactoryUtils.releasePersistenceBroker(pb, this.persistenceBrokerTemplate.getPbKey());
	}

}
