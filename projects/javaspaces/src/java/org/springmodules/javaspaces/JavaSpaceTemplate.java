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

	/**
	 * constructor
	 * @param space the space
	 */
	public JavaSpaceTemplate(JavaSpace space) {
		this.space = space;
		afterPropertiesSet();
	}

	/**
	 * @see org.springmodules.beans.factory.InitializingBean#afterPropertiesSet()
	 * @throws IllegalArgumentException in case the JavaSpaces instance is null
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

	/**
	 * The process of serializing an entry for transmission to a JavaSpaces
	 * service will be identical if the same entry is used twice. This is most
	 * likely to be an issue with templates that are used repeatedly to search
	 * for entries with read or take. The client-side implementations of read
	 * and take cannot reasonably avoid this duplicated effort, since they have
	 * no efficient way of checking whether the same template is being used
	 * without intervening modification. The snapshot method gives the
	 * JavaSpaces service implementor a way to reduce the impact of repeated use
	 * of the same entry. Invoking snapshot with an Entry will return another
	 * Entry object that contains a snapshot of the original entry. Using the
	 * returned snapshot entry is equivalent to using the unmodified original
	 * entry in all operations on the same JavaSpaces service. Modifications to
	 * the original entry will not affect the snapshot. You can snapshot a null
	 * template; snapshot may or may not return null given a null template. The
	 * entry returned from snapshot will be guaranteed equivalent to the
	 * original unmodified object only when used with the space. Using the
	 * snapshot with any other JavaSpaces service will generate an
	 * IllegalArgumentException unless the other space can use it because of
	 * knowledge about the JavaSpaces service that generated the snapshot. The
	 * snapshot will be a different object from the original, may or may not
	 * have the same hash code, and equals may or may not return true when
	 * invoked with the original object, even if the original object is
	 * unmodified. A snapshot is guaranteed to work only within the virtual
	 * machine in which it was generated. If a snapshot is passed to another
	 * virtual machine (for example, in a parameter of an RMI call), using
	 * it--even with the same JavaSpaces service--may generate an
	 * IllegalArgumentException.
	 *
	 * @param entry the entry to take a snapshot of.
	 * @return a snapshot of the entry.
	 */
	public Entry snapshot(final Entry entry) {
		return (Entry) execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException {
				return js.snapshot(entry);
			}
		});
	}

	/**
	 * Take a matching entry from the space, waiting until one exists. Matching
	 * is and timeout done as for read.
	 *
	 * @param template The template used for matching. Matching is done against
	 *            tmpl with null fields being wildcards ("match anything") other
	 *            fields being values ("match exactly on the serialized form").
	 * @param millis How long the client is willing to wait for a
	 *            transactionally proper matching entry. A timeout of NO_WAIT
	 *            means to wait no time at all; this is equivalent to a wait of
	 *            zero.
	 * @return the entry taken from the space
	 */
	public Entry take(final Entry template, final long millis) {
		return (Entry) execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException, TransactionException,
					UnusableEntryException, InterruptedException {
				return js.take(template, tx, millis);
			}
		});
	}

	/**
	 * Take a matching entry from the space, returning null if there is
	 * currently is none. Matching is and timeout done as for read, except that
	 * blocking in this call is done only if necessary to wait for transactional
	 * state to settle.
	 *
	 * @param template The template used for matching. Matching is done against
	 *            tmpl with null fields being wildcards ("match anything") other
	 *            fields being values ("match exactly on the serialized form").
	 * @param millis How long the client is willing to wait for a
	 *            transactionally proper matching entry. A timeout of NO_WAIT
	 *            means to wait no time at all; this is equivalent to a wait of
	 *            zero.
	 * @return the entry taken from the space
	 */
	public Entry takeIfExists(final Entry template, final long millis) {
		return (Entry) execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException, TransactionException,
					UnusableEntryException, InterruptedException {
				return js.takeIfExists(template, tx, millis);
			}
		});
	}

	/**
	 * Read any matching entry from the space, returning null if there is
	 * currently is none. Matching and timeouts are done as in read, except that
	 * blocking in this call is done only if necessary to wait for transactional
	 * state to settle.
	 *
	 * @param template The template used for matching. Matching is done against
	 *            tmpl with null fields being wildcards ("match anything") other
	 *            fields being values ("match exactly on the serialized form").
	 * @param millis How long the client is willing to wait for a
	 *            transactionally proper matching entry. A timeout of NO_WAIT
	 *            means to wait no time at all; this is equivalent to a wait of
	 *            zero.
	 * @return a copy of the entry read from the space
	 */
	public Entry readIfExists(final Entry template, final long millis) {
		return (Entry) execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException, TransactionException,
					UnusableEntryException, InterruptedException {
				return js.readIfExists(template, tx, millis);
			}
		});
	}

	/**
	 * Read any matching entry from the space, blocking until one exists. Return
	 * null if the timeout expires.
	 *
	 * @param template The template used for matching. Matching is done against
	 *            tmpl with null fields being wildcards ("match anything") other
	 *            fields being values ("match exactly on the serialized form").
	 * @param millis How long the client is willing to wait for a
	 *            transactionally proper matching entry. A timeout of NO_WAIT
	 *            means to wait no time at all; this is equivalent to a wait of
	 *            zero.
	 * @return a copy of the entry read from the space
	 */
	public Entry read(final Entry template, final long millis) {
		return (Entry) execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException, TransactionException,
					UnusableEntryException, InterruptedException {
				return js.read(template, tx, millis);
			}
		});
	}

	/**
	 * When entries are written that match this template notify the given
	 * listener with a RemoteEvent that includes the handback object. Matching
	 * is done as for read.
	 *
	 * @param template tmpl - The template used for matching. Matching is done
	 *            against tmpl with null fields being wildcards ("match
	 *            anything") other fields being values ("match exactly on the
	 *            serialized form").
	 * @param listener The remote event listener to notify.
	 * @param millis the requested lease time, in milliseconds
	 * @param handback
	 * @return An object to send to the listener as part of the event
	 *         notification.
	 */
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
	 * Perform multiple JavaSpaces tasks in a single transaction.
	 * @param jsc The javaspace callback
	 * @return the result of the execution.
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
	 *
	 * @return the Javaspace this template operates on
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
