package org.springmodules.spaces;

import java.rmi.RemoteException;

import net.jini.core.entry.UnusableEntryException;
import net.jini.core.transaction.Transaction;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;

/**
 * Callback interface used by JavaSpaceTemplate.
 * The "template" concept is used throughout Spring.
 * 
 * @see org.springmodules.spaces.JavaSpaceTemplate
 * 
 * @author Rod Johnson
 * @author Costin Leau
 */
public interface JavaSpaceCallback {
	
	/**
	 * Perform a set of operations in a given JavaSpace,
	 * in a single JINI transaction
	 * @param js JavaSpace we are using
	 * @param status transaction status object, permitting rollback
	 * @return the result of the operation or null if there is no result
	 * @throws RemoteException
	 * @throws TransactionException
	 * @throws UnusableEntryException
	 * @throws InterruptedException
	 */
	Object doInSpace(JavaSpace js, Transaction transaction) throws 
        RemoteException, TransactionException, UnusableEntryException, InterruptedException;

}
