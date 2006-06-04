package org.springmodules.javaspaces.tx;

import java.rmi.RemoteException;

import junit.framework.TestCase;
import net.jini.core.entry.Entry;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.lease.Lease;
import net.jini.core.transaction.CannotAbortException;
import net.jini.core.transaction.CannotCommitException;
import net.jini.core.transaction.TimeoutExpiredException;
import net.jini.core.transaction.Transaction;
import net.jini.core.transaction.TransactionException;
import net.jini.core.transaction.UnknownTransactionException;
import net.jini.core.transaction.Transaction.Created;
import net.jini.core.transaction.server.TransactionManager;
import net.jini.space.JavaSpace;

import org.easymock.MockControl;
import org.springframework.transaction.InvalidIsolationLevelException;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springmodules.javaspaces.JavaSpaceCallback;
import org.springmodules.javaspaces.JavaSpaceTemplate;
import org.springmodules.transaction.jini.JiniTransactionManager;
import org.springmodules.transaction.jini.JiniTransactionManager.JiniHolder;

public class TransactionTests extends TestCase {

	private TransactionManager tm;

	private MockControl tmCtrl;

	private JiniTransactionManager jiniTM;

	private JavaSpace space;

	private MockControl spaceCtrl;

	private net.jini.core.transaction.server.TransactionManager.Created txCreated;

	private MockControl leaseCtrl;

	private Lease lease;

	private MockControl txCtrl;

	private Transaction tx;

	private long txId;

	private JavaSpaceTemplate spaceTemplate;

	protected void setUp() throws Exception {
		// mocks
		tmCtrl = MockControl.createControl(TransactionManager.class);
		tm = (TransactionManager) tmCtrl.getMock();
		txCtrl = MockControl.createControl(Transaction.class);
		tx = (Transaction) txCtrl.getMock();
		leaseCtrl = MockControl.createControl(Lease.class);
		lease = (Lease) leaseCtrl.getMock();

		txId = 1;
		txCreated = new net.jini.core.transaction.server.TransactionManager.Created(txId, lease);

		spaceCtrl = MockControl.createControl(JavaSpace.class);
		space = (JavaSpace) spaceCtrl.getMock();

		jiniTM = new JiniTransactionManager(tm, space);
		spaceTemplate = new JavaSpaceTemplate(space);
	}

	protected void tearDown() throws Exception {
		// super.tearDown();

		mockVerify();

		space = null;
		spaceCtrl = null;
		jiniTM = null;
		tm = null;
		tmCtrl = null;
		spaceTemplate = null;
	}

	private void mockReplay() {
		tmCtrl.replay();
		spaceCtrl.replay();
		leaseCtrl.replay();
		txCtrl.replay();
	}

	private void mockVerify() {
		tmCtrl.verify();
		spaceCtrl.verify();
		leaseCtrl.verify();
		txCtrl.verify();
	}

	private void assertSynchronizationManager()
	{
		assertTrue("Hasn't thread session", !TransactionSynchronizationManager
				.hasResource(space));
		assertTrue("JTA synchronizations not active",
				!TransactionSynchronizationManager.isSynchronizationActive());
	
	}
	
	public void testTransactionCommit() throws Exception {

		// tx begin
		
		tmCtrl.expectAndReturn(tm.create(-1), txCreated);
		// tx commit
		tm.commit(txId);

		final Entry entry = new Entry() {
		};

		// dummy mock
		Transaction tx = new Transaction() {

			public void abort() throws UnknownTransactionException,
					CannotAbortException, RemoteException {
			}

			public void abort(long arg0) throws UnknownTransactionException,
					CannotAbortException, TimeoutExpiredException,
					RemoteException {
			}

			public void commit() throws UnknownTransactionException,
					CannotCommitException, RemoteException {
			}

			public void commit(long arg0) throws UnknownTransactionException,
					CannotCommitException, TimeoutExpiredException,
					RemoteException {
			}
		};

		// space play
		space.write(entry, tx, Lease.FOREVER);
		spaceCtrl.setMatcher(MockControl.ALWAYS_MATCHER);
		spaceCtrl.setReturnValue(lease);

		mockReplay();

		TransactionTemplate tt = new TransactionTemplate(jiniTM);

		assertSynchronizationManager();
		
		tt.execute(new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				assertTrue("Has thread session",
						TransactionSynchronizationManager.hasResource(space));

				spaceTemplate.write(entry, Lease.FOREVER);
			}
		});

		assertSynchronizationManager();
	}

	public void testTransactionRollback() throws Exception {

		tmCtrl.expectAndReturn(tm.create(-1), txCreated);
		// tx abort
		tm.abort(txId);

		mockReplay();

		TransactionTemplate tt = new TransactionTemplate(jiniTM);

		assertSynchronizationManager();

		final RuntimeException testException = new RuntimeException();
		try {
			tt.execute(new TransactionCallbackWithoutResult() {
				protected void doInTransactionWithoutResult(
						TransactionStatus status) {
					assertTrue("Has thread session",
							TransactionSynchronizationManager
									.hasResource(space));

					throw testException;
				}
			});
		} catch (RuntimeException re) {
			assertSame(testException, re);
		}

		assertSynchronizationManager();
	}

	public void testTransactionRollbackOnly() throws Exception {

		tmCtrl.expectAndReturn(tm.create(-1), txCreated);
		// tx abort
		tm.abort(txId);

		final Entry entry = new Entry() {
		};
		spaceCtrl.expectAndReturn(space.write(entry, null, Lease.FOREVER),
				lease);

		mockReplay();

		TransactionTemplate tt = new TransactionTemplate(jiniTM);

		assertSynchronizationManager();

		tt.execute(new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				assertTrue("Has thread session",
						TransactionSynchronizationManager.hasResource(space));

				spaceTemplate.execute(new JavaSpaceCallback() {

					public Object doInSpace(JavaSpace js,
							Transaction transaction) throws RemoteException,
							TransactionException, UnusableEntryException,
							InterruptedException {
						assertSame(lease, js.write(entry, null, Lease.FOREVER));
						return null;
					}
				});
				status.setRollbackOnly();
			}
		});

		assertSynchronizationManager();
	}

    public void testInvalidIsolation() throws Exception {

		tmCtrl.expectAndReturn(tm.create(-1), txCreated);
		//  no need to call abort since the exception appears before the transaction gets used.
		//tm.abort(txId);

		mockReplay();
		
		TransactionTemplate tt = new TransactionTemplate(jiniTM);
        
		assertSynchronizationManager();
		
        tt.setIsolationLevel(TransactionDefinition.ISOLATION_SERIALIZABLE);
        try {
            tt.execute(new TransactionCallbackWithoutResult() {
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    assertTrue("Has thread session", TransactionSynchronizationManager.hasResource(space));
                    spaceTemplate.execute(new JavaSpaceCallback() {
                    	public Object doInSpace(JavaSpace js, Transaction transaction) throws RemoteException, TransactionException, UnusableEntryException, InterruptedException {
                    		return null;
                    	}
                    });
                }
            });
            fail("Should have thrown InvalidIsolationLevelException");

        } catch (InvalidIsolationLevelException e) {
            // it's okay
        }
        
        assertSynchronizationManager();
    }
    
    public void testTransactionCommitWithPrebound() throws Exception
    {
		mockReplay();
		
		TransactionTemplate tt = new TransactionTemplate(jiniTM);
		assertSynchronizationManager();
		

		Created crtd = new Created(tx, lease);
		// bind tx object
		JiniHolder holder = new JiniHolder(crtd);
		
    	TransactionSynchronizationManager.bindResource(space, holder);
    	
    	assertTrue("Has thread session", TransactionSynchronizationManager.hasResource(space));
        
        tt.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                assertTrue("Has thread session", TransactionSynchronizationManager.hasResource(space));
                spaceTemplate.execute(new JavaSpaceCallback() {
                	public Object doInSpace(JavaSpace js, Transaction transaction) throws RemoteException, TransactionException, UnusableEntryException, InterruptedException {
                	return null;
                }});
            }
        });

        assertTrue("Has thread session", TransactionSynchronizationManager.hasResource(space));
        TransactionSynchronizationManager.unbindResource(space);
        assertTrue("JTA synchronizations not active", !TransactionSynchronizationManager.isSynchronizationActive());
    }
    
    public void testTransactionRollbackOnlyWithPrebound() throws Exception
    {
		mockReplay();
		
		TransactionTemplate tt = new TransactionTemplate(jiniTM);
		assertSynchronizationManager();
		
		Created crtd = new Created(tx, lease);
		// bind tx object
		JiniHolder holder = new JiniHolder(crtd);
		
    	TransactionSynchronizationManager.bindResource(space, holder);
    	
    	assertTrue("Has thread session", TransactionSynchronizationManager.hasResource(space));
        
        tt.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                assertTrue("Has thread session", TransactionSynchronizationManager.hasResource(space));
                spaceTemplate.execute(new JavaSpaceCallback() {
                	public Object doInSpace(JavaSpace js, Transaction transaction) throws RemoteException, TransactionException, UnusableEntryException, InterruptedException {
                	return null;
                }});
                status.setRollbackOnly();
            }
        });

        assertTrue("Has thread session", TransactionSynchronizationManager.hasResource(space));
        TransactionSynchronizationManager.unbindResource(space);
        assertTrue("JTA synchronizations not active", !TransactionSynchronizationManager.isSynchronizationActive());
    }
}
