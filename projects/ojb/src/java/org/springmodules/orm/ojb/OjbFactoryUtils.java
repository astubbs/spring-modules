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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ojb.broker.OJBRuntimeException;
import org.apache.ojb.broker.PBKey;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;

import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Helper class featuring methods for OJB PersistenceBroker handling,
 * allowing for reuse of PersistenceBroker instances within transactions.
 *
 * <p>Used by PersistenceBrokerTemplate and PersistenceBrokerTransactionManager.
 * Can also be used directly in application code.
 *
 * @author Juergen Hoeller
 * @since 1.1
 * @see PersistenceBrokerTemplate
 * @see PersistenceBrokerTransactionManager
 * @see org.springframework.transaction.jta.JtaTransactionManager
 */
public abstract class OjbFactoryUtils {

	/**
	 * Order value for TransactionSynchronization objects that clean up OJB
	 * PersistenceBrokers. Return DataSourceUtils.CONNECTION_SYNCHRONIZATION_ORDER - 100
	 * to execute PersistenceBroker cleanup before JDBC Connection cleanup, if any.
	 * @see org.springframework.jdbc.datasource.DataSourceUtils#CONNECTION_SYNCHRONIZATION_ORDER
	 */
	public static final int PERSISTENCE_BROKER_SYNCHRONIZATION_ORDER =
			DataSourceUtils.CONNECTION_SYNCHRONIZATION_ORDER - 100;

	private static final Log logger = LogFactory.getLog(OjbFactoryUtils.class);


	/**
	 * Get an OJB PersistenceBroker for the given PBKey. Is aware of a
	 * corresponding PersistenceBroker bound to the current thread, for
	 * example when using PersistenceBrokerTransactionManager. Will
	 * create a new PersistenceBroker else, if allowCreate is true.
	 * @param pbKey PBKey to create the PersistenceBroker for
	 * @param allowCreate if a non-transactional PersistenceBroker should be created
	 * when no transactional PersistenceBroker can be found for the current thread
	 * @return the PersistenceBroker
	 * @throws DataAccessResourceFailureException if the PersistenceBroker couldn't be created
	 * @throws IllegalStateException if no thread-bound PersistenceBroker found and allowCreate false
	 */
	public static PersistenceBroker getPersistenceBroker(PBKey pbKey, boolean allowCreate)
	    throws DataAccessResourceFailureException, IllegalStateException {

		PersistenceBrokerHolder pbHolder =
				(PersistenceBrokerHolder) TransactionSynchronizationManager.getResource(pbKey);
		if (pbHolder != null) {
			return pbHolder.getPersistenceBroker();
		}

		if (!allowCreate && !TransactionSynchronizationManager.isSynchronizationActive()) {
			throw new IllegalStateException("No OJB PersistenceBroker bound to thread, " +
					"and configuration does not allow creation of non-transactional one here");
		}

		try {
			logger.debug("Opening OJB PersistenceBroker");
			PersistenceBroker pb = PersistenceBrokerFactory.createPersistenceBroker(pbKey);

			if (TransactionSynchronizationManager.isSynchronizationActive()) {
				logger.debug("Registering transaction synchronization for OJB PersistenceBroker");
				// Use same PersistenceBroker for further OJB actions within the transaction.
				// Thread object will get removed by synchronization at transaction completion.
				pbHolder = new PersistenceBrokerHolder(pb);
				pbHolder.setSynchronizedWithTransaction(true);
				TransactionSynchronizationManager.registerSynchronization(
				    new PersistenceBrokerSynchronization(pbHolder, pbKey));
				TransactionSynchronizationManager.bindResource(pbKey, pbHolder);
			}

			return pb;
		}
		catch (OJBRuntimeException ex) {
			throw new DataAccessResourceFailureException("Could not open OJB PersistenceBroker", ex);
		}
	}

	/**
	 * Close the given PersistenceBroker, created for the given PBKey,
	 * if it is not managed externally (i.e. not bound to the thread).
	 * @param pb PersistenceBroker to close
	 * @param pbKey PBKey that the PersistenceBroker was created with
	 */
	public static void releasePersistenceBroker(PersistenceBroker pb, PBKey pbKey) {
		if (pb == null) {
			return;
		}

		PersistenceBrokerHolder pbHolder =
		    (PersistenceBrokerHolder) TransactionSynchronizationManager.getResource(pbKey);
		if (pbHolder != null && pb == pbHolder.getPersistenceBroker()) {
			// It's the transactional PersistenceBroker: Don't close it.
			return;
		}

		logger.debug("Closing OJB PersistenceBroker");
		pb.close();
	}


	/**
	 * Callback for resource cleanup at the end of a non-OJB transaction
	 * (e.g. when participating in a JtaTransactionManager transaction).
	 * @see org.springframework.transaction.jta.JtaTransactionManager
	 */
	private static class PersistenceBrokerSynchronization extends TransactionSynchronizationAdapter {

		private final PersistenceBrokerHolder persistenceBrokerHolder;

		private final PBKey pbKey;

		private boolean holderActive = true;

		private PersistenceBrokerSynchronization(PersistenceBrokerHolder pbHolder, PBKey pbKey) {
			this.persistenceBrokerHolder = pbHolder;
			this.pbKey = pbKey;
		}

		public int getOrder() {
			return PERSISTENCE_BROKER_SYNCHRONIZATION_ORDER;
		}

		public void suspend() {
			if (this.holderActive) {
				TransactionSynchronizationManager.unbindResource(this.pbKey);
			}
		}

		public void resume() {
			if (this.holderActive) {
				TransactionSynchronizationManager.bindResource(this.pbKey, this.persistenceBrokerHolder);
			}
		}

		public void beforeCompletion() {
			TransactionSynchronizationManager.unbindResource(this.pbKey);
			this.holderActive = false;
			releasePersistenceBroker(this.persistenceBrokerHolder.getPersistenceBroker(), this.pbKey);
		}
	}

}
