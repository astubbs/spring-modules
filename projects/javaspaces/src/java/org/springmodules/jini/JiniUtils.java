/**
 * Created on Mar 12, 2006
 *
 * $Id$
 * $Revision$
 */
package org.springmodules.jini;

import java.rmi.RemoteException;

import net.jini.core.entry.UnusableEntryException;
import net.jini.core.lease.LeaseException;
import net.jini.core.transaction.Transaction;
import net.jini.core.transaction.TransactionException;

import org.springframework.core.NestedRuntimeException;
import org.springframework.dao.DataAccessException;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springmodules.transaction.jini.JiniTransactionManager.JiniHolder;

/**
 * @author Costin Leau
 * 
 */
public abstract class JiniUtils {

	public static RuntimeException convertJiniException(RemoteException remoteException) {
		return null;
	}

	public static RuntimeException convertJiniException(Exception exception) {
		if (exception instanceof LeaseException)
			return new RemoteAccessException("Lease denied", exception);

		if (exception instanceof TransactionException)
			return new org.springframework.transaction.TransactionSystemException(exception.getMessage(),
					exception);

		if (exception instanceof RemoteException) {
			// Translate to Spring's unchecked remote access exception
			return new RemoteAccessException("RemoteException", exception);
		}
		if (exception instanceof UnusableEntryException) {
			return new RemoteAccessException("Unusable entry", exception);
		}

		if (exception instanceof RuntimeException)
			return (RuntimeException) exception;

		return new DataAccessException("unexpected exception ", exception) {
		};
	}

	/**
	 * Returns the running transaction for the given transactionalContext. If no
	 * transaction is binded, null is returned.
	 * 
	 * @param transactionalContext
	 * @return
	 */
	public static Transaction getTransaction(Object transactionalContext) {
		if (transactionalContext == null)
			return null;

		JiniHolder txObject = (JiniHolder) TransactionSynchronizationManager.getResource(transactionalContext);
		if (txObject != null && txObject.getTxCreated() != null)
			return txObject.getTxCreated().transaction;
		return null;
	}
}
