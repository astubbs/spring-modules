package org.springmodules.javaspaces;

import java.rmi.MarshalledObject;
import java.rmi.RemoteException;

import net.jini.core.entry.Entry;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.event.EventRegistration;
import net.jini.core.event.RemoteEventListener;
import net.jini.core.lease.Lease;
import net.jini.core.transaction.Transaction;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;

import org.springframework.beans.factory.InitializingBean;
import org.springmodules.jini.JiniUtils;

// TODO properly fix exception handling

/**
 * Implementation of the Spring "template" concept for JavaSpaces. Translates
 * exceptions into Spring exception hierarchy. Simplifies the performance of
 * several operations in a single method.
 * 
 * @author Rod Johnson
 * @author Costin Leau
 * @see org.springmodules.javaspaces.JavaSpaceCallback
 */
public class JavaSpaceTemplate implements InitializingBean {

	private JavaSpace space;

	/**
	 * Required if operations with the space should be ran without a transaction
	 * while there is a transaction already taken place.
	 * 
	 */
	private boolean useTransaction = true;

	public JavaSpaceTemplate() {
	}

	public JavaSpaceTemplate(JavaSpace space) {
		this.space = space;
		afterPropertiesSet();
	}

	/**
	 * @see org.springmodules.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() {
		if (space == null)
			throw new IllegalArgumentException("space property is required");
	}

	/**
	 * Write using the current transaction.
	 * 
	 * @param entry
	 * @param millis
	 */
	public Lease write(final Entry entry, final long millis) {
		return (Lease) execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException, TransactionException,
					UnusableEntryException {
				return js.write(entry, tx, millis);
			}
		});
	}

	public Entry snapshot(final Entry entry) {
		return (Entry) execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException {
				return js.snapshot(entry);
			}
		});
	}

	public Entry take(final Entry template, final long millis) {
		return (Entry) execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException, TransactionException,
					UnusableEntryException, InterruptedException {
				return js.take(template, tx, millis);
			}
		});
	}

	public Entry takeIfExists(final Entry template, final long millis) {
		return (Entry) execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException, TransactionException,
					UnusableEntryException, InterruptedException {
				return js.takeIfExists(template, tx, millis);
			}
		});
	}

	public Entry readIfExists(final Entry template, final long millis) {
		return (Entry) execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException, TransactionException,
					UnusableEntryException, InterruptedException {
				return js.readIfExists(template, tx, millis);
			}
		});
	}

	public Entry read(final Entry template, final long millis) {
		return (Entry) execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException, TransactionException,
					UnusableEntryException, InterruptedException {
				return js.read(template, tx, millis);
			}
		});
	}

	public EventRegistration notify(final Entry template, final RemoteEventListener listener, final long millis,
			final MarshalledObject handback) {

		return (EventRegistration) execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException, TransactionException,
					UnusableEntryException, InterruptedException {
				return js.notify(template, tx, listener, millis, handback);
			}
		});
	}

	// TODO transaction nesting

	protected Transaction getCurrentTransaction() {
		return JiniUtils.getTransaction(getSpace());
	}

	/**
	 * Perform multiple JavaSpaces tasks in the one transaction.
	 * 
	 */
	public Object execute(JavaSpaceCallback jsc) {
		try {
			Transaction tx = (useTransaction ? getCurrentTransaction() : null);
			Object retval = jsc.doInSpace(this.space, tx);
			return retval;
		}
		catch (Exception ex) {
			throw convertSpaceException(ex);
		}
		finally {
		}
	}

	protected RuntimeException convertSpaceException(Exception e) {
		return JiniUtils.convertJiniException(e);
	}

	/**
	 * @param space
	 */
	public void setSpace(JavaSpace space) {
		this.space = space;
	}

	/**
	 * Return the Javaspace this template operates on
	 * 
	 * @return
	 */
	public JavaSpace getSpace() {
		return this.space;
	}

	/**
	 * @return Returns the useTransaction.
	 */
	public boolean isUseTransaction() {
		return useTransaction;
	}

	/**
	 * @param useTransaction
	 *            The useTransaction to set.
	 */
	public void setUseTransaction(boolean useTransaction) {
		this.useTransaction = useTransaction;
	}

}
