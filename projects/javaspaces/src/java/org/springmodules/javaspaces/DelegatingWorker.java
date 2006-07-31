package org.springmodules.javaspaces;

import java.rmi.RemoteException;

import net.jini.core.entry.Entry;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.lease.Lease;
import net.jini.core.transaction.Transaction;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.javaspaces.entry.AbstractMethodCallEntry;
import org.springmodules.javaspaces.entry.MethodResultEntry;

/**
 * Generic worker designed to run in a thread. Takes method call entries from a
 * JavaSpace. Can either download code or work with a local delegate.
 * <p>
 * Must be configured with a JavaSpaceTemplate helper object to use to read and
 * write from the JavaSpace, and a businessInterface (usually a single interface
 * that this worker will "implement").
 * 
 * @author Rod Johnson
 */
public class DelegatingWorker implements Runnable {

	private static final Log log = LogFactory.getLog(DelegatingWorker.class);

	private long waitMillis = 500;

	private Object delegate;

	private JavaSpaceTemplate jsTemplate;

	private boolean running = true;

	/**
	 * Candidate that will match only this interface
	 */
	private Entry methodCallEntryTemplate;

	private Class businessInterface;

	public DelegatingWorker() {
	}

	public void setBusinessInterface(Class intf) {
		if (!intf.isInterface()) {
			throw new IllegalArgumentException(intf + " must be an interface");
		}
		this.businessInterface = intf;
	}

	/**
	 * Set a delegate if we are using the "service seeking" approach. For
	 * RunnableMethodCallEntries, there is no need for a service to be hosted.
	 * 
	 * @param delegate
	 */
	public void setDelegate(Object delegate) {
		this.delegate = delegate;
	}

	public void setJavaSpaceTemplate(JavaSpaceTemplate jsTemplate) {
		this.jsTemplate = jsTemplate;
	}

	public void stop() {
		this.running = false;
	}

	public void run() {
		AbstractMethodCallEntry t = new AbstractMethodCallEntry();
		// Needed for match
		t.uid = null;
		t.className = businessInterface.getName();
		this.methodCallEntryTemplate = jsTemplate.snapshot(t);

		final boolean debug = log.isDebugEnabled();

		if (debug)
			log.debug("Worker " + this + " starting...");

		while (running) {
			if (debug)
				log.debug("Worker " + this + " waiting...");

			// On reading from the space, the result will be computed and
			// written
			// back into the space in one transaction
			jsTemplate.execute(new JavaSpaceCallback() {
				public Object doInSpace(JavaSpace js, Transaction transaction) throws RemoteException,
						TransactionException, UnusableEntryException, InterruptedException {

					// look for method call
					AbstractMethodCallEntry call = (AbstractMethodCallEntry) js.take(methodCallEntryTemplate,
							transaction, waitMillis);

					if (call == null) {
						// TODO is this required?
						if (debug)
							log.debug("Skipping out of loop...");
						return null;
					}

					try {
						MethodResultEntry result = invokeMethod(call, delegate);
						// push the result back to the JavaSpace
						js.write(result, transaction, Lease.FOREVER);
					}
					catch (Exception ex) {
						// TODO fix me, should translate to JavaSpaceException
						// hierarchy
						throw new IllegalStateException(ex.getMessage());
					}

					return null;
				}
			});
		}
		if (debug)
			log.debug("Worker " + this + " terminating");
	}

	/**
	 * Invoke the method on the delegate object in order to get the
	 * MethodResultEntry. Subclasses can extend the method and add custom
	 * behavior (ex: security propagation).
	 * 
	 * @param localDelegate the delegate used for executing the method
	 * @return the methodResultEntry
	 */
	protected MethodResultEntry invokeMethod(AbstractMethodCallEntry call, Object localDelegate)
			throws IllegalAccessException {
		if (log.isDebugEnabled())
			log.debug("call is " + call.getClass().getName());
		MethodResultEntry result = call.invokeMethod(localDelegate);

		if (log.isDebugEnabled())
			log.debug("Got result " + result.result);
		return result;
	}
}
