/**
 * Created on Mar 12, 2006
 *
 * $Id$
 * $Revision$
 */
package org.springmodules.transaction.jini;

import java.rmi.RemoteException;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import net.jini.core.lease.LeaseDeniedException;
import net.jini.core.transaction.CannotAbortException;
import net.jini.core.transaction.CannotCommitException;
import net.jini.core.transaction.Transaction;
import net.jini.core.transaction.TransactionFactory;
import net.jini.core.transaction.UnknownTransactionException;
import net.jini.core.transaction.server.NestableTransactionManager;
import net.jini.core.transaction.server.TransactionManager;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.InvalidIsolationLevelException;
import org.springframework.transaction.NestedTransactionNotSupportedException;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.ResourceHolderSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springmodules.jini.JiniUtils;

/**
 * Jini PlatformTransactionManager . Requires a Jini TransactionManager service
 * to perform to create the actual transactions using jini transactional context
 * (transactionalContext property). For JavaSpaces for example, the context will
 * be represented by the java space. 
 * Does <strong>not</strong> support nested transaction propagation yet.
 * 
 * @author Costin Leau
 * 
 * 
 * TODO: make it Serializable ? TODO: the default timeout (-1) is similar to
 * Lease.ANY which might create a tx with infinite timeout which results in
 * deadlock.
 * 
 */
public class JiniTransactionManager extends AbstractPlatformTransactionManager
		implements InitializingBean {

	// TransactionManager used for creating the actual transaction
	private transient TransactionManager transactionManager;

	// the jini participant - can be javaspace or any other service that wants
	// to take part in the transaction
	private Object transactionalContext;

	public JiniTransactionManager() {

	}

	public JiniTransactionManager(TransactionManager transactionManager,
			Object transactionalContext) {
		this.transactionManager = transactionManager;
		this.transactionalContext = transactionalContext;
		afterPropertiesSet();
	}

	/**
	 * @see org.springmodules.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() {
		if (transactionManager == null)
			throw new IllegalArgumentException(
					"transactionManager property is required");
		if (transactionalContext == null)
			throw new IllegalArgumentException(
					"transactionalContext property is required");

		if (transactionManager instanceof NestableTransactionManager)
			setNestedTransactionAllowed(true);
	}

	/**
	 * @see org.springmodules.transaction.support.AbstractPlatformTransactionManager#doGetTransaction()
	 */
	protected Object doGetTransaction() throws TransactionException {

		JiniTransactionObject txObject = new JiniTransactionObject();
		// txObject.setNestedTransactionAllowed
		// txObject.setJiniHolder(transactionalContext);

		// set the jini holder is one is found
		if (TransactionSynchronizationManager.hasResource(transactionalContext)) {
			JiniHolder jiniHolder = (JiniHolder) TransactionSynchronizationManager
					.getResource(transactionalContext);
			if (logger.isDebugEnabled()) {
				logger.debug("Found thread-bound tx data [" + jiniHolder
						+ "] for Jini resource " + transactionalContext);
			}
			txObject.setJiniHolder(jiniHolder, false);
		}

		return txObject;
	}

	protected RuntimeException convertJiniException(Exception e) {
		return JiniUtils.convertJiniException(e);
	}

	/**
	 * @see org.springmodules.transaction.support.AbstractPlatformTransactionManager#doBegin(java.lang.Object,
	 *      org.springmodules.transaction.TransactionDefinition)
	 */
	protected void doBegin(Object transaction, TransactionDefinition definition)
			throws TransactionException {
		JiniTransactionObject txObject = (JiniTransactionObject) transaction;
		if (logger.isDebugEnabled())
			logger.debug("Beginning Jini transaction " + txObject.toString());
		try {
			doJiniBegin(txObject, definition);
		}
		catch (NotSupportedException ex) {
			// assume nested transaction not supported
			throw new NestedTransactionNotSupportedException(
					"Jini implementation does not support nested transactions",
					ex);
		}
		catch (UnsupportedOperationException ex) {
			// assume nested transaction not supported
			throw new NestedTransactionNotSupportedException(
					"Jini implementation does not support nested transactions",
					ex);
		}
		catch (SystemException ex) {
			throw new CannotCreateTransactionException("Jini failure on begin",
					ex);
		}
	}

	/**
	 * Performs the transaction begin.
	 * 
	 * @param txObject
	 * @param definition
	 * @throws NotSupportedException
	 * @throws SystemException
	 */
	protected void doJiniBegin(JiniTransactionObject txObject,
			TransactionDefinition definition) throws NotSupportedException,
			SystemException {

		// create the tx

		try {
			if (txObject.getJiniHolder() == null) {
				if (logger.isDebugEnabled())
					logger.debug("creating new jini tx for "
							+ getTransactionalContext());
				Transaction.Created txCreated = TransactionFactory.create(
						transactionManager, definition.getTimeout());
				JiniHolder jiniHolder = new JiniHolder(txCreated);
				jiniHolder.setTimeoutInSeconds(definition.getTimeout());
				txObject.setJiniHolder(jiniHolder, true);
			}

			txObject.getJiniHolder().setSynchronizedWithTransaction(true);

			applyIsolationLevel(txObject, definition.getIsolationLevel());
			// check for timeout just in case
			// applyTimeout(txObject, definition.getTimeout());

			// Bind the session holder to the thread.
			if (txObject.isNewJiniHolder()) {
				TransactionSynchronizationManager.bindResource(
						getTransactionalContext(), txObject.getJiniHolder());
			}
		}
		catch (LeaseDeniedException e) {
			throw new CannotCreateTransactionException("lease denied", e);
		}
		catch (RemoteException e) {
			throw new CannotCreateTransactionException("remote exception", e);
		}

	}

	protected void applyIsolationLevel(JiniTransactionObject txObject,
			int isolationLevel) throws InvalidIsolationLevelException,
			SystemException {

		if (isolationLevel != TransactionDefinition.ISOLATION_DEFAULT) {
			throw new InvalidIsolationLevelException(
					"JiniTransactionManager does not support custom isolation levels");
		}
	}

	protected void applyTimeout(JiniTransactionObject txObject, int timeout)
			throws NotSupportedException {
		// TODO: maybe use a LeaseRenewalManager
		if (timeout != TransactionDefinition.TIMEOUT_DEFAULT) {
			throw new NotSupportedException(
					"JiniTransactionManager does not support custom timeouts");
		}
	}

	/**
	 * @see org.springmodules.transaction.support.AbstractPlatformTransactionManager#doCommit(org.springmodules.transaction.support.DefaultTransactionStatus)
	 */
	protected void doCommit(DefaultTransactionStatus status)
			throws TransactionException {
		JiniTransactionObject txObject = (JiniTransactionObject) status
				.getTransaction();
		if (logger.isDebugEnabled())
			logger.debug("Committing Jini transaction " + txObject.toString());
		try {
			txObject.getTransaction().commit();
		}
		catch (UnknownTransactionException e) {
			throw convertJiniException(e);
		}
		catch (CannotCommitException e) {
			throw convertJiniException(e);
		}
		catch (RemoteException e) {
			throw convertJiniException(e);
		}
	}

	/**
	 * @see org.springmodules.transaction.support.AbstractPlatformTransactionManager#isExistingTransaction(java.lang.Object)
	 */
	protected boolean isExistingTransaction(Object transaction)
			throws TransactionException {
		JiniTransactionObject txObject = (JiniTransactionObject) transaction;
		return txObject.hasTransaction();
	}

	/**
	 * @see org.springmodules.transaction.support.AbstractPlatformTransactionManager#doRollback(org.springmodules.transaction.support.DefaultTransactionStatus)
	 */
	protected void doRollback(DefaultTransactionStatus status)
			throws TransactionException {
		JiniTransactionObject txObject = (JiniTransactionObject) status
				.getTransaction();
		if (logger.isDebugEnabled())
			logger.debug("Rolling back Jini transaction" + txObject.toString());
		try {
			txObject.getTransaction().abort();
		}
		catch (UnknownTransactionException e) {
			throw convertJiniException(e);
		}
		catch (CannotAbortException e) {
			throw convertJiniException(e);
		}
		catch (RemoteException e) {
			throw convertJiniException(e);
		}
	}

	protected void doCleanupAfterCompletion(Object transaction) {
		JiniTransactionObject txObject = (JiniTransactionObject) transaction;
		// Remove the session holder from the thread.
		if (txObject.isNewJiniHolder()) {
			if (logger.isDebugEnabled()) {
				logger.debug("Removing per-thread Jini transaction for "
						+ getTransactionalContext());
			}
			TransactionSynchronizationManager
					.unbindResource(getTransactionalContext());
		}

		txObject.getJiniHolder().clear();
	}

	protected void doSetRollbackOnly(DefaultTransactionStatus status)
			throws TransactionException {
		JiniTransactionObject txObject = (JiniTransactionObject) status
				.getTransaction();
		if (status.isDebug()) {
			logger.debug("Setting Jini transaction on txContext ["
					+ getTransactionalContext() + "] rollback-only");
		}
		txObject.setRollbackOnly();
	}

	/**
	 * @see org.springmodules.transaction.support.AbstractPlatformTransactionManager#doResume(java.lang.Object,
	 *      java.lang.Object)
	 */
	protected void doResume(Object transaction, Object suspendedResources)
			throws TransactionException {
		JiniHolder jiniHolder = (JiniHolder) suspendedResources;
		TransactionSynchronizationManager.bindResource(
				getTransactionalContext(), jiniHolder);
	}

	/**
	 * @see org.springmodules.transaction.support.AbstractPlatformTransactionManager#doSuspend(java.lang.Object)
	 */
	protected Object doSuspend(Object transaction) throws TransactionException {
		JiniTransactionObject txObject = (JiniTransactionObject) transaction;
		txObject.setJiniHolder(null, false);
		JiniHolder jiniHolder = (JiniHolder) TransactionSynchronizationManager
				.unbindResource(getTransactionalContext());
		return jiniHolder;
	}

	/**
	 * @return Returns the transactionManager.
	 */
	public TransactionManager getTransactionManager() {
		return transactionManager;
	}

	/**
	 * @param transactionManager
	 *            The transactionManager to set.
	 */
	public void setTransactionManager(TransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	/**
	 * @see org.springmodules.transaction.support.AbstractPlatformTransactionManager#useSavepointForNestedTransaction()
	 */
	protected boolean useSavepointForNestedTransaction() {
		return false;
	}

	/**
	 * Jini Transaction object. Used as transaction object by
	 * JiniTransactionManager.
	 * 
	 * TODO: can SmartTransactionObject be implemented?
	 */
	private static class JiniTransactionObject {

		private JiniHolder jiniHolder;

		private boolean newJiniHolder;

		public boolean hasTransaction() {
			return (jiniHolder != null && jiniHolder.hasTransaction());
		}

		public void setJiniHolder(JiniHolder jiniHolder,
				boolean newSessionHolder) {
			this.jiniHolder = jiniHolder;
			this.newJiniHolder = newSessionHolder;
		}

		public JiniHolder getJiniHolder() {
			return jiniHolder;
		}

		public boolean isNewJiniHolder() {
			return newJiniHolder;
		}

		public boolean isRollbackOnly() {
			return (jiniHolder != null && jiniHolder.isRollbackOnly());
		}

		public void setRollbackOnly() {
			if (jiniHolder != null)
				jiniHolder.setRollbackOnly();
		}

		public Transaction getTransaction() {
			if (hasTransaction())
				return jiniHolder.txCreated.transaction;
			return null;
		}

	}

	// is ResourceHolder really required
	public static class JiniHolder extends ResourceHolderSupport {
		private Transaction.Created txCreated;

		public JiniHolder(Transaction.Created txCreated) {
			this.txCreated = txCreated;
		}

		/**
		 * @return Returns the txCreated.
		 */
		public Transaction.Created getTxCreated() {
			return txCreated;
		}

		public boolean hasTransaction() {
			return (txCreated != null && txCreated.transaction != null);
		}

	}

	/**
	 * @return Returns the transactionalContext.
	 */
	public Object getTransactionalContext() {
		return transactionalContext;
	}

	/**
	 * @param transactionalContext
	 *            The transactionalContext to set.
	 */
	public void setTransactionalContext(Object txResource) {
		this.transactionalContext = txResource;
	}

}
