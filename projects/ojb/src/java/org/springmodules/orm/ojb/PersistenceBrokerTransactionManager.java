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

import java.sql.Connection;

import javax.sql.DataSource;

import org.apache.ojb.broker.OJBRuntimeException;
import org.apache.ojb.broker.PBKey;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.TransactionAbortedException;
import org.apache.ojb.broker.accesslayer.LookupException;

import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.JdbcTransactionObjectSupport;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.IllegalTransactionStateException;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * PlatformTransactionManager implementation for a single OJB persistence broker key.
 * Binds an OJB PersistenceBroker from the specified key to the thread, potentially
 * allowing for one thread PersistenceBroker per key. OjbFactoryUtils and
 * PersistenceBrokerTemplate are aware of thread-bound persistence brokers and
 * participate in such transactions automatically. Using either is required for
 * OJB access code supporting this transaction management mechanism.
 *
 * <p>This implementation is appropriate for applications that solely use OJB for
 * transactional data access. JTA (usually through JtaTransactionManager) is necessary
 * for accessing multiple transactional resources, in combination with transactional
 * DataSources as connection pools (to be specified in OJB's configuration).
 *
 * @author Juergen Hoeller
 * @since 1.1
 * @see #setJcdAlias
 * @see #setPbKey
 * @see OjbFactoryUtils#getPersistenceBroker
 * @see OjbFactoryUtils#releasePersistenceBroker
 * @see PersistenceBrokerTemplate
 * @see org.springframework.transaction.jta.JtaTransactionManager
 */
public class PersistenceBrokerTransactionManager extends AbstractPlatformTransactionManager {

	private PBKey pbKey = PersistenceBrokerFactory.getDefaultKey();

	private DataSource dataSource;


	/**
	 * Create a new PersistenceBrokerTransactionManager,
	 * sing the default connection configured for OJB.
	 */
	public PersistenceBrokerTransactionManager() {
	}

	/**
	 * Create a new PersistenceBrokerTransactionManager.
	 * @param jcdAlias the JDBC Connection Descriptor alias
	 * of the PersistenceBroker configuration to use
	 */
	public PersistenceBrokerTransactionManager(String jcdAlias) {
		setJcdAlias(jcdAlias);
	}

	/**
	 * Create a new PersistenceBrokerTransactionManager.
	 * @param pbKey the PBKey of the PersistenceBroker configuration to use
	 */
	public PersistenceBrokerTransactionManager(PBKey pbKey) {
		setPbKey(pbKey);
	}

	/**
	 * Set the JDBC Connection Descriptor alias of the PersistenceBroker
	 * configuration to use. Default is the default connection configured for OJB.
	 */
	public void setJcdAlias(String jcdAlias) {
		this.pbKey = new PBKey(jcdAlias);
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
	 * Set the JDBC DataSource that this instance should manage transactions for.
	 * The DataSource should match the one configured for the OJB JCD alias:
	 * for example, you could specify the same JNDI DataSource for both.
	 * <p>A transactional JDBC Connection for this DataSource will be provided to
	 * application code accessing this DataSource directly via DataSourceUtils
	 * or JdbcTemplate. The Connection will be taken from the Hibernate Session.
	 * <p>The DataSource specified here should be the target DataSource to manage
	 * transactions for, not a TransactionAwareDataSourceProxy. Only data access
	 * code may work with TransactionAwareDataSourceProxy, while the transaction
	 * manager needs to work on the underlying target DataSource. If there's
	 * nevertheless a TransactionAwareDataSourceProxy passed in, it will be
	 * unwrapped to extract its target DataSource.
	 * @see org.springframework.orm.hibernate.LocalDataSourceConnectionProvider
	 * @see org.springframework.orm.hibernate.LocalSessionFactoryBean#setDataSource
	 * @see org.springframework.jdbc.datasource.DataSourceUtils#getConnection
	 * @see org.springframework.jdbc.core.JdbcTemplate
	 * @see org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy
	 */
	public void setDataSource(DataSource dataSource) {
		if (dataSource instanceof TransactionAwareDataSourceProxy) {
			// If we got a TransactionAwareDataSourceProxy, we need to perform transactions
			// for its underlying target DataSource, else data access code won't see
			// properly exposed transactions (i.e. transactions for the target DataSource).
			this.dataSource = ((TransactionAwareDataSourceProxy) dataSource).getTargetDataSource();
		}
		else {
			this.dataSource = dataSource;
		}
	}

	/**
	 * Return the JDBC DataSource that this instance manages transactions for.
	 */
	public DataSource getDataSource() {
		return dataSource;
	}


	protected Object doGetTransaction() {
		PersistenceBrokerTransactionObject txObject = new PersistenceBrokerTransactionObject();
		PersistenceBrokerHolder pbHolder =
		    (PersistenceBrokerHolder) TransactionSynchronizationManager.getResource(getPbKey());
		txObject.setPersistenceBrokerHolder(pbHolder);
		return txObject;
	}

	protected boolean isExistingTransaction(Object transaction) {
		PersistenceBrokerTransactionObject txObject = (PersistenceBrokerTransactionObject) transaction;
		return (txObject.getPersistenceBrokerHolder() != null);
	}

	protected void doBegin(Object transaction, TransactionDefinition definition) {
		if (getDataSource() != null && TransactionSynchronizationManager.hasResource(getDataSource())) {
			throw new IllegalTransactionStateException(
					"Pre-bound JDBC Connection found - PersistenceBrokerTransactionManager does not support " +
					"running within DataSourceTransactionManager if told to manage the DataSource itself. " +
					"It is recommended to use a single PersistenceBrokerTransactionManager for all transactions " +
					"on a single DataSource, no matter whether PersistenceBroker or JDBC access.");
		}

		PersistenceBroker pb = null;

		try {
			pb = getPersistenceBroker();
			if (logger.isDebugEnabled()) {
				logger.debug("Opened new PersistenceBroker [" + pb + "] for OJB transaction");
			}

			PersistenceBrokerTransactionObject txObject = (PersistenceBrokerTransactionObject) transaction;
			txObject.setPersistenceBrokerHolder(new PersistenceBrokerHolder(pb));

			Connection con = pb.serviceConnectionManager().getConnection();
			Integer previousIsolationLevel = DataSourceUtils.prepareConnectionForTransaction(con, definition);
			txObject.setPreviousIsolationLevel(previousIsolationLevel);

			pb.beginTransaction();

			// Register the OJB PersistenceBroker's JDBC Connection for the DataSource, if set.
			if (getDataSource() != null) {
				ConnectionHolder conHolder = new ConnectionHolder(con);
				if (definition.getTimeout() != TransactionDefinition.TIMEOUT_DEFAULT) {
					conHolder.setTimeoutInSeconds(definition.getTimeout());
				}
				if (logger.isDebugEnabled()) {
					logger.debug("Exposing OJB transaction as JDBC transaction [" + conHolder.getConnection() + "]");
				}
				TransactionSynchronizationManager.bindResource(getDataSource(), conHolder);
				txObject.setConnectionHolder(conHolder);
			}

			// Bind the persistence broker holder to the thread.
			TransactionSynchronizationManager.bindResource(getPbKey(), txObject.getPersistenceBrokerHolder());
		}

		catch (Exception ex) {
			releasePersistenceBroker(pb);
			throw new CannotCreateTransactionException("Could not open OJB PersistenceBroker for transaction", ex);
		}
	}

	protected Object doSuspend(Object transaction) {
		PersistenceBrokerTransactionObject txObject = (PersistenceBrokerTransactionObject) transaction;
		txObject.setPersistenceBrokerHolder(null);
		PersistenceBrokerHolder pbHolder =
				(PersistenceBrokerHolder) TransactionSynchronizationManager.unbindResource(getPbKey());
		ConnectionHolder connectionHolder = null;
		if (getDataSource() != null) {
			connectionHolder = (ConnectionHolder) TransactionSynchronizationManager.unbindResource(getDataSource());
		}
		return new SuspendedResourcesHolder(pbHolder, connectionHolder);
	}

	protected void doResume(Object transaction, Object suspendedResources) {
		SuspendedResourcesHolder resourcesHolder = (SuspendedResourcesHolder) suspendedResources;
		if (TransactionSynchronizationManager.hasResource(getPbKey())) {
			// From non-transactional code running in active transaction synchronization
			// -> can be safely removed, will be closed on transaction completion.
			TransactionSynchronizationManager.unbindResource(getPbKey());
		}
		TransactionSynchronizationManager.bindResource(getPbKey(), resourcesHolder.getPersistenceBrokerHolder());
		if (getDataSource() != null) {
			TransactionSynchronizationManager.bindResource(getDataSource(), resourcesHolder.getConnectionHolder());
		}
	}

	protected void doCommit(DefaultTransactionStatus status) {
		PersistenceBrokerTransactionObject txObject = (PersistenceBrokerTransactionObject) status.getTransaction();
		if (status.isDebug()) {
			logger.debug("Committing OJB transaction on PersistenceBroker [" +
					txObject.getPersistenceBrokerHolder().getPersistenceBroker() + "]");
		}
		try {
			txObject.getPersistenceBrokerHolder().getPersistenceBroker().commitTransaction();
		}
		catch (TransactionAbortedException ex) {
			// assumably from commit call to underlying JDBC connection
			throw new TransactionSystemException("Could not commit OJB transaction", ex);
		}
	}

	protected void doRollback(DefaultTransactionStatus status) {
		PersistenceBrokerTransactionObject txObject = (PersistenceBrokerTransactionObject) status.getTransaction();
		if (status.isDebug()) {
			logger.debug("Rolling back OJB transaction on PersistenceBroker [" +
					txObject.getPersistenceBrokerHolder().getPersistenceBroker() + "]");
		}
		txObject.getPersistenceBrokerHolder().getPersistenceBroker().abortTransaction();
	}

	protected void doSetRollbackOnly(DefaultTransactionStatus status) {
		PersistenceBrokerTransactionObject txObject = (PersistenceBrokerTransactionObject) status.getTransaction();
		if (status.isDebug()) {
			logger.debug("Setting OJB transaction on PersistenceBroker [" +
					txObject.getPersistenceBrokerHolder().getPersistenceBroker() + "] rollback-only");
		}
		txObject.setRollbackOnly();
	}

	protected void doCleanupAfterCompletion(Object transaction) {
		PersistenceBrokerTransactionObject txObject = (PersistenceBrokerTransactionObject) transaction;

		// Remove the persistence broker holder from the thread.
		TransactionSynchronizationManager.unbindResource(getPbKey());
		txObject.getPersistenceBrokerHolder().clear();

		// Remove the JDBC connection holder from the thread, if set.
		if (getDataSource() != null) {
			TransactionSynchronizationManager.unbindResource(getDataSource());
		}

		PersistenceBroker pb = txObject.getPersistenceBrokerHolder().getPersistenceBroker();
		try {
			Connection con = pb.serviceConnectionManager().getConnection();
			DataSourceUtils.resetConnectionAfterTransaction(con, txObject.getPreviousIsolationLevel());
		}
		catch (LookupException ex) {
			logger.info("Could not look up JDBC Connection of OJB PersistenceBroker", ex);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Closing OJB PersistenceBroker [" + pb + "] after transaction");
		}
		releasePersistenceBroker(pb);
	}


	/**
	 * Get an OJB PersistenceBroker for the PBKey of this transaction manager.
	 * <p>Default implementation simply creates a new PersistenceBroker.
	 * Can be overridden in subclasses, e.g. for testing purposes.
	 * @return the PersistenceBroker
	 * @throws OJBRuntimeException if PersistenceBroker cretion failed
	 * @see #setJcdAlias
	 * @see #setPbKey
	 * @see org.apache.ojb.broker.PersistenceBrokerFactory#createPersistenceBroker(org.apache.ojb.broker.PBKey)
	 */
	protected PersistenceBroker getPersistenceBroker() throws OJBRuntimeException {
		return PersistenceBrokerFactory.createPersistenceBroker(getPbKey());
	}

	/**
	 * Close the given PersistenceBroker, created for the PBKey of this
	 * transaction manager, if it isn't bound to the thread.
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


	/**
	 * OJB transaction object, representing a PersistenceBrokerHolder.
	 * Used as transaction object by PersistenceBrokerTransactionManager.
	 *
	 * <p>Derives from JdbcTransactionObjectSupport to inherit the capability
	 * to manage JDBC 3.0 Savepoints for underlying JDBC Connections.
	 *
	 * @see PersistenceBrokerHolder
	 */
	private static class PersistenceBrokerTransactionObject extends JdbcTransactionObjectSupport {

		private PersistenceBrokerHolder persistenceBrokerHolder;

		public void setPersistenceBrokerHolder(PersistenceBrokerHolder persistenceBrokerHolder) {
			this.persistenceBrokerHolder = persistenceBrokerHolder;
		}

		public PersistenceBrokerHolder getPersistenceBrokerHolder() {
			return persistenceBrokerHolder;
		}

		public void setRollbackOnly() {
			getPersistenceBrokerHolder().setRollbackOnly();
			if (getConnectionHolder() != null) {
				getConnectionHolder().setRollbackOnly();
			}
		}

		public boolean isRollbackOnly() {
			return getPersistenceBrokerHolder().isRollbackOnly() ||
					(getConnectionHolder() != null && getConnectionHolder().isRollbackOnly());
		}
	}


	/**
	 * Holder for suspended resources.
	 * Used internally by doSuspend and doResume.
	 */
	private static class SuspendedResourcesHolder {

		private final PersistenceBrokerHolder persistenceBrokerHolder;

		private final ConnectionHolder connectionHolder;

		private SuspendedResourcesHolder(PersistenceBrokerHolder pbHolder, ConnectionHolder conHolder) {
			this.persistenceBrokerHolder = pbHolder;
			this.connectionHolder = conHolder;
		}

		private PersistenceBrokerHolder getPersistenceBrokerHolder() {
			return persistenceBrokerHolder;
		}

		private ConnectionHolder getConnectionHolder() {
			return connectionHolder;
		}
	}

}
