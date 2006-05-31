/**
 * Created on Mar 14, 2006
 *
 * $Id$
 * $Revision$
 */
package org.springmodules.spaces.tx;

import java.rmi.RemoteException;

import net.jini.core.entry.Entry;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.lease.Lease;
import net.jini.core.transaction.Transaction;
import net.jini.core.transaction.TransactionException;
import net.jini.core.transaction.server.TransactionManager;
import net.jini.space.JavaSpace;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springmodules.jini.JiniUtils;
import org.springmodules.spaces.JavaSpaceCallback;
import org.springmodules.spaces.JavaSpaceTemplate;

/**
 * Functional tx tests - require the transaction service to be started.
 * 
 * @author Costin Leau
 * 
 */
public class TxTests extends AbstractDependencyInjectionSpringContextTests {

	/**
	 * @see org.springmodules.test.AbstractDependencyInjectionSpringContextTests#getConfigLocations()
	 */
	protected String[] getConfigLocations() {
		return new String[] { "/org/springframework/spaces/tx/tx-context.xml" };
	}

	private TransactionManager tm;

	private TransactionTemplate txTemplate;

	private JavaSpace space;

	private JavaSpaceTemplate spaceTemplate;

	public void testTM() {
		assertNotNull(tm);
	}

	public void testSimpleTransaction() throws Exception {
		assertNotNull(txTemplate);
		final Entry myEntry = new Entry() {
		};
		txTemplate.execute(new TransactionCallbackWithoutResult() {

			protected void doInTransactionWithoutResult(TransactionStatus status) {
				// we need to pass in a tx
				assertNotNull("no tx started", JiniUtils.getTransaction(space));
			}
		});
	}

	public void testRollbackScenario() throws Exception {
		final SomeBean template = new SomeBean();
		final long timeout = 1000 * 2;

		assertNull(spaceTemplate.read(template, timeout));
		final RuntimeException EXCEPTION = new RuntimeException("intentional exception");

		try {
			txTemplate.execute(new TransactionCallbackWithoutResult() {

				protected void doInTransactionWithoutResult(TransactionStatus status) {
					spaceTemplate.execute(new JavaSpaceCallback() {
						public Object doInSpace(JavaSpace js, Transaction transaction)
								throws RemoteException, TransactionException, UnusableEntryException,
								InterruptedException {
							// check that we have a tx
							assertNotNull("no tx started", transaction);
							Object result = js.read(template, transaction, timeout);
							assertNull(result);
							js.write(template, transaction, Lease.FOREVER);
							result = js.read(template, transaction, timeout);
							assertNotNull(result);
							throw EXCEPTION;
						}
					});
				}
			});
		}
		catch (RuntimeException re) {
			// it's okay (if it's our exception)
			assertSame(EXCEPTION, re);
		}

		assertNull(spaceTemplate.read(template, timeout));
	}

	public void testPropagationNotSupported() throws Exception {
		final SomeBean template = new SomeBean();
		final SomeBean anotherTemplate = new SomeBean();
		anotherTemplate.name = "another bean";

		final long timeout = 1000 * 2;

		final TransactionTemplate notSupportedTransaction = new TransactionTemplate(
				txTemplate.getTransactionManager());
		notSupportedTransaction.setPropagationBehavior(TransactionDefinition.PROPAGATION_NOT_SUPPORTED);
		notSupportedTransaction.afterPropertiesSet();

		final TransactionTemplate mandatoryTransaction = new TransactionTemplate(txTemplate.getTransactionManager());
		mandatoryTransaction.setPropagationBehavior(TransactionDefinition.PROPAGATION_MANDATORY);
		mandatoryTransaction.afterPropertiesSet();

		final RuntimeException EXCEPTION = new RuntimeException("intentional exception");

		try {
			// start tx (REQUIRED)
			txTemplate.execute(new TransactionCallbackWithoutResult() {

				protected void doInTransactionWithoutResult(final TransactionStatus status) {
					spaceTemplate.execute(new JavaSpaceCallback() {
						public Object doInSpace(JavaSpace js, final Transaction transaction)
								throws RemoteException, TransactionException, UnusableEntryException,
								InterruptedException {
							// check that we have a tx
							assertNotNull("no tx started", transaction);
							Object result = js.read(template, transaction, timeout);
							assertNull(result);
							// write
							js.write(template, transaction, Lease.ANY);

							notSupportedTransaction.execute(new TransactionCallbackWithoutResult() {
								protected void doInTransactionWithoutResult(final TransactionStatus status2) {
									spaceTemplate.execute(new JavaSpaceCallback() {
										public Object doInSpace(JavaSpace js2, Transaction transaction2)
												throws RemoteException, TransactionException,
												UnusableEntryException, InterruptedException {
											// NOT_SUPPORTED means tx was suspended
											assertNull(transaction2);
											
											assertSame(transaction2, JiniUtils.getTransaction(space));
											
											js2.write(anotherTemplate, transaction2, Lease.ANY);
											// check object from
											// find the object in the parent transaction 
											Object rez = js2.read(template, transaction, timeout);
											assertNotNull(rez);
											// but not in child transaction (NOT_SUPPORTED)
											rez = js2.read(template, transaction2, timeout);
											assertNull(rez);
											rez = js2.read(anotherTemplate, transaction2, timeout);
											assertNotNull(rez);
											return null;
										}
									});

								}
							});
							// find the template
							result = js.read(template, transaction, timeout);
							assertNotNull(result);
							result = js.read(anotherTemplate, transaction, timeout);
							assertNotNull(result);

							return mandatoryTransaction.execute(new TransactionCallbackWithoutResult() {
								protected void doInTransactionWithoutResult(TransactionStatus status) {
									Transaction tx = JiniUtils.getTransaction(space);
									assertSame(transaction ,tx);
									assertNotNull(spaceTemplate.readIfExists(template,timeout));
									throw EXCEPTION;
								}

							});
						}
					});
				}
			});
		}
		catch (RuntimeException e) {
			// it's okay (if it's ours)
			assertSame(EXCEPTION, e);

			assertNull(spaceTemplate.readIfExists(template, timeout));
			assertNotNull(spaceTemplate.readIfExists(anotherTemplate, timeout));
		}
	}

	/**
	 * @return Returns the tm.
	 */
	public TransactionManager getTm() {
		return tm;
	}

	/**
	 * @param tm
	 *            The tm to set.
	 */
	public void setTm(TransactionManager tm) {
		this.tm = tm;
	}

	public TransactionTemplate getTxTemplate() {
		return txTemplate;
	}

	public void setTxTemplate(TransactionTemplate txTemplate) {
		this.txTemplate = txTemplate;
	}

	public JavaSpace getSpace() {
		return space;
	}

	public void setSpace(JavaSpace space) {
		this.space = space;
	}

	public JavaSpaceTemplate getSpaceTemplate() {
		return spaceTemplate;
	}

	public void setSpaceTemplate(JavaSpaceTemplate spaceTemplate) {
		this.spaceTemplate = spaceTemplate;
	}

}
