/**
 * 
 */
package org.springmodules.jsr94;

import java.rmi.RemoteException;

import javax.rules.InvalidRuleSessionException;
import javax.rules.StatefulRuleSession;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springmodules.jsr94.rulesource.RuleSource;

/**
 * Manages JSR94 transactions for stateful sessions. Stateless sessions do not need transaction
 * management at all. 
 * 
 * @author janm
 */
public class Jsr94TransactionManager extends AbstractPlatformTransactionManager implements InitializingBean {

	/**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 3905803076144084279L;
	
	/**
	 * The ruleSource
	 */
	private RuleSource ruleSource;

	/**
	 * Jsr94TransactionObject
	 * @author janm
	 */
	static class Jsr94TransactionObject {
		private StatefulRuleSession session;
		private boolean hasTransaction;

		/**
		 * Gets the value of session 
		 * @return Value of session.
		 */
		public final StatefulRuleSession getSession() {
			return session;
		}

		/**
		 * Sets new value for field session
		 * @param session The session to set.
		 */
		public final void setSession(StatefulRuleSession session) {
			this.session = session;
		}

		/**
		 * Gets the value of hasTransaction 
		 * @return Value of hasTransaction.
		 */
		public final boolean hasTransaction() {
			return hasTransaction;
		}

		/**
		 * Sets new value for field hasTransaction
		 * @param hasTransaction The hasTransaction to set.
		 */
		public final void setHasTransaction(boolean hasTransaction) {
			this.hasTransaction = hasTransaction;
		}

	}

	/**
	 * Releases the session 
	 * @throws TransactionException If the session cannot be released
	 */
	private void release() throws TransactionException {
		try {
			if (TransactionSynchronizationManager.hasResource(ruleSource)) {
				logger.debug("Releasing session");
				StatefulRuleSession session = (StatefulRuleSession) TransactionSynchronizationManager.getResource(ruleSource);
				session.release();
				logger.debug("Session released");
			}
		} catch (InvalidRuleSessionException ex) {
			throw new Jsr94TransactionException(ex);
		} catch (RemoteException ex) {
			throw new Jsr94TransactionException(ex);
		}
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		if (ruleSource == null) throw new IllegalArgumentException("Must set ruleSource on " + getClass().getName());
	}

	/* (non-Javadoc)
	 * @see org.springframework.transaction.support.AbstractPlatformTransactionManager#doGetTransaction()
	 */
	protected Object doGetTransaction() throws TransactionException {
		Jsr94TransactionObject txObject = new Jsr94TransactionObject();
		if (TransactionSynchronizationManager.hasResource(ruleSource)) {
			StatefulRuleSession session = (StatefulRuleSession) TransactionSynchronizationManager.getResource(ruleSource);
			if (logger.isDebugEnabled()) {
				logger.debug("Found thread-bound session [" + session + "] for Jsr94 Transaction");
			}
			// set the existing session holder
			txObject.setSession(session);
		}
		return txObject;
	}

	/* (non-Javadoc)
	 * @see org.springframework.transaction.support.AbstractPlatformTransactionManager#isExistingTransaction(java.lang.Object)
	 */
	protected boolean isExistingTransaction(Object transaction) throws TransactionException {
		return ((Jsr94TransactionObject) transaction).hasTransaction();
	}

	/* (non-Javadoc)
	 * @see org.springframework.transaction.support.AbstractPlatformTransactionManager#doCleanupAfterCompletion(java.lang.Object)
	 */
	protected void doCleanupAfterCompletion(Object transaction) {
		Jsr94TransactionObject txObject = (Jsr94TransactionObject) transaction;
		TransactionSynchronizationManager.unbindResource(ruleSource);
	}

	/* (non-Javadoc)
	 * @see org.springframework.transaction.support.AbstractPlatformTransactionManager#doBegin(java.lang.Object, org.springframework.transaction.TransactionDefinition)
	 */
	protected void doBegin(Object transaction, TransactionDefinition definition) throws TransactionException {
		Jsr94TransactionObject txObject = (Jsr94TransactionObject) transaction;
		logger.debug("Beginning transaction");
	}

	/* (non-Javadoc)
	 * @see org.springframework.transaction.support.AbstractPlatformTransactionManager#doCommit(org.springframework.transaction.support.DefaultTransactionStatus)
	 */
	protected void doCommit(DefaultTransactionStatus status) throws TransactionException {
		release();
		logger.debug("Committed transaction (noop)");
	}

	/* (non-Javadoc)
	 * @see org.springframework.transaction.support.AbstractPlatformTransactionManager#doRollback(org.springframework.transaction.support.DefaultTransactionStatus)
	 */
	protected void doRollback(DefaultTransactionStatus status) throws TransactionException {
		release();
		logger.debug("Rolled back transaction (noop)");
	}

	/**
	 * Sets new value for field ruleSource
	 * @param ruleSource The ruleSource to set.
	 */
	public final void setRuleSource(RuleSource ruleSource) {
		this.ruleSource = ruleSource;
	}

	/* (non-Javadoc)
	 * @see org.springframework.transaction.support.AbstractPlatformTransactionManager#doSuspend(java.lang.Object)
	 */
	protected Object doSuspend(Object transaction) throws TransactionException {
		return transaction;
	}

	/* (non-Javadoc)
	 * @see org.springframework.transaction.support.AbstractPlatformTransactionManager#doResume(java.lang.Object, java.lang.Object)
	 */
	protected void doResume(Object transaction, Object arg1) throws TransactionException {
		// noop
	}

	/* (non-Javadoc)
	 * @see org.springframework.transaction.support.AbstractPlatformTransactionManager#doSetRollbackOnly(org.springframework.transaction.support.DefaultTransactionStatus)
	 */
	protected void doSetRollbackOnly(DefaultTransactionStatus status) throws TransactionException {
		// noop
	}

}
