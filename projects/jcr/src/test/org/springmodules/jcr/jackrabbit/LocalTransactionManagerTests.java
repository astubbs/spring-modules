package org.springmodules.jcr.jackrabbit;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

import junit.framework.TestCase;

import org.apache.jackrabbit.api.XASession;
import org.easymock.MockControl;
import org.springframework.transaction.InvalidIsolationLevelException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springmodules.jcr.JcrCallback;
import org.springmodules.jcr.JcrTemplate;
import org.springmodules.jcr.SessionFactory;
import org.springmodules.jcr.SessionHolderProviderManager;
import org.springmodules.jcr.jackrabbit.support.UserTxSessionHolder;
import org.springmodules.jcr.support.ListSessionHolderProviderManager;

public class LocalTransactionManagerTests extends TestCase {

	public void testTransactionCommit() throws Exception {
		MockControl sfControl = MockControl.createControl(SessionFactory.class);
		final SessionFactory sf = (SessionFactory) sfControl.getMock();
		MockControl sessionControl = MockControl.createControl(XASession.class);
		final XASession session = (XASession) sessionControl.getMock();
		// create nice mock
		MockControl xaResControl = MockControl.createControl(XAResource.class);
		XAResource xaRes = (XAResource) xaResControl.getMock();

		sfControl.expectAndReturn(sf.getSession(), session);
		sessionControl.expectAndReturn(session.getXAResource(), xaRes);

		session.save();
		session.logout();

		/*
		 * MockControl repositoryControl =
		 * MockControl.createNiceControl(Repository.class); Repository
		 * repository = (Repository) repositoryControl.getMock();
		 * repositoryControl.replay();
		 * 
		 * sessionControl.expectAndReturn(session.getRepository(), repository,
		 * MockControl.ONE_OR_MORE); final SessionHolderProviderManager
		 * providerManager = new ListSessionHolderProviderManager();
		 * 
		 */

		Xid xidMock = new XidMock();

		xaRes.start(xidMock, XAResource.TMNOFLAGS);
		xaResControl.setMatcher(MockControl.ALWAYS_MATCHER);
		xaRes.prepare(xidMock);
		xaResControl.setMatcher(MockControl.ALWAYS_MATCHER);
		xaResControl.setReturnValue(0);
		xaRes.commit(xidMock, false);
		xaResControl.setMatcher(MockControl.ALWAYS_MATCHER);
		xaRes.end(xidMock, XAResource.TMSUCCESS);
		xaResControl.setMatcher(MockControl.ALWAYS_MATCHER);

		sfControl.replay();
		sessionControl.replay();
		xaResControl.replay();

		PlatformTransactionManager tm = new LocalTransactionManager(sf);
		TransactionTemplate tt = new TransactionTemplate(tm);
		final List l = new ArrayList();
		l.add("test");
		assertTrue("Hasn't thread session", !TransactionSynchronizationManager.hasResource(sf));
		assertTrue("JTA synchronizations not active", !TransactionSynchronizationManager.isSynchronizationActive());

		tt.execute(new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				assertTrue("Has thread session", TransactionSynchronizationManager.hasResource(sf));
				JcrTemplate template = new JcrTemplate(sf);
				template.save();
			}
		});

		assertTrue("Hasn't thread session", !TransactionSynchronizationManager.hasResource(sf));
		assertTrue("JTA synchronizations not active", !TransactionSynchronizationManager.isSynchronizationActive());

		sfControl.verify();
		sessionControl.verify();
		xaResControl.verify();
	}

	public void testTransactionRollback() throws Exception {
		MockControl sfControl = MockControl.createControl(SessionFactory.class);
		final SessionFactory sf = (SessionFactory) sfControl.getMock();
		MockControl sessionControl = MockControl.createControl(XASession.class);
		final XASession session = (XASession) sessionControl.getMock();
		// create nice mock
		MockControl xaResControl = MockControl.createControl(XAResource.class);
		XAResource xaRes = (XAResource) xaResControl.getMock();

		sfControl.expectAndReturn(sf.getSession(), session);

		sessionControl.expectAndReturn(session.getXAResource(), xaRes);
		session.save();
		session.logout();
		/*
		 * // used for ServiceProvider MockControl repositoryControl =
		 * MockControl.createNiceControl(Repository.class); Repository
		 * repository = (Repository) repositoryControl.getMock();
		 * repositoryControl.replay();
		 * 
		 * sessionControl.expectAndReturn(session.getRepository(), repository,
		 * MockControl.ONE_OR_MORE);
		 */

		Xid xidMock = new XidMock();

		xaRes.start(xidMock, XAResource.TMNOFLAGS);
		xaResControl.setMatcher(MockControl.ALWAYS_MATCHER);
		xaRes.end(xidMock, XAResource.TMFAIL);
		xaResControl.setMatcher(MockControl.ALWAYS_MATCHER);
		xaRes.rollback(xidMock);
		xaResControl.setMatcher(MockControl.ALWAYS_MATCHER);

		sfControl.replay();
		sessionControl.replay();
		xaResControl.replay();
		final SessionHolderProviderManager providerManager = new ListSessionHolderProviderManager();

		PlatformTransactionManager tm = new LocalTransactionManager(sf);
		TransactionTemplate tt = new TransactionTemplate(tm);
		final List l = new ArrayList();
		l.add("test");
		assertTrue("Hasn't thread session", !TransactionSynchronizationManager.hasResource(sf));
		assertTrue("JTA synchronizations not active", !TransactionSynchronizationManager.isSynchronizationActive());

		try {
			tt.execute(new TransactionCallbackWithoutResult() {
				protected void doInTransactionWithoutResult(TransactionStatus status) {
					assertTrue("Has thread session", TransactionSynchronizationManager.hasResource(sf));
					JcrTemplate template = new JcrTemplate(sf);
					template.execute(new JcrCallback() {
						public Object doInJcr(Session se) throws RepositoryException {
							se.save();
							throw new RuntimeException();
						}

					});
				}
			});
		}
		catch (RuntimeException e) {
			// it's okay
		}

		assertTrue("Hasn't thread session", !TransactionSynchronizationManager.hasResource(sf));
		assertTrue("JTA synchronizations not active", !TransactionSynchronizationManager.isSynchronizationActive());

		sfControl.verify();
		sessionControl.verify();
		xaResControl.verify();
	}

	public void testTransactionRollbackOnly() throws Exception {
		MockControl sfControl = MockControl.createControl(SessionFactory.class);
		final SessionFactory sf = (SessionFactory) sfControl.getMock();
		MockControl sessionControl = MockControl.createControl(XASession.class);
		final XASession session = (XASession) sessionControl.getMock();
		// create nice mock
		MockControl xaResControl = MockControl.createControl(XAResource.class);
		XAResource xaRes = (XAResource) xaResControl.getMock();

		sfControl.expectAndReturn(sf.getSession(), session);

		sessionControl.expectAndReturn(session.getXAResource(), xaRes);
		session.save();
		session.logout();

		Xid xidMock = new XidMock();

		xaRes.start(xidMock, XAResource.TMNOFLAGS);
		xaResControl.setMatcher(MockControl.ALWAYS_MATCHER);
		xaRes.end(xidMock, XAResource.TMFAIL);
		xaResControl.setMatcher(MockControl.ALWAYS_MATCHER);
		xaRes.rollback(xidMock);
		xaResControl.setMatcher(MockControl.ALWAYS_MATCHER);

		sfControl.replay();
		sessionControl.replay();
		xaResControl.replay();
		final SessionHolderProviderManager providerManager = new ListSessionHolderProviderManager();

		PlatformTransactionManager tm = new LocalTransactionManager(sf);
		TransactionTemplate tt = new TransactionTemplate(tm);
		final List l = new ArrayList();
		l.add("test");
		assertTrue("Hasn't thread session", !TransactionSynchronizationManager.hasResource(sf));
		assertTrue("JTA synchronizations not active", !TransactionSynchronizationManager.isSynchronizationActive());

		tt.execute(new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				assertTrue("Has thread session", TransactionSynchronizationManager.hasResource(sf));
				JcrTemplate template = new JcrTemplate(sf);
				template.execute(new JcrCallback() {
					public Object doInJcr(Session se) throws RepositoryException {
						se.save();
						return null;
					}

				});
				status.setRollbackOnly();
			}
		});

		assertTrue("Hasn't thread session", !TransactionSynchronizationManager.hasResource(sf));
		assertTrue("JTA synchronizations not active", !TransactionSynchronizationManager.isSynchronizationActive());

		sfControl.verify();
		sessionControl.verify();
		xaResControl.verify();
	}

	public void testInvalidIsolation() throws Exception {
		MockControl sfControl = MockControl.createControl(SessionFactory.class);
		final SessionFactory sf = (SessionFactory) sfControl.getMock();

		sfControl.replay();

		PlatformTransactionManager tm = new LocalTransactionManager(sf);
		TransactionTemplate tt = new TransactionTemplate(tm);

		assertTrue("Hasn't thread session", !TransactionSynchronizationManager.hasResource(sf));
		assertTrue("JTA synchronizations not active", !TransactionSynchronizationManager.isSynchronizationActive());

		tt.setIsolationLevel(TransactionDefinition.ISOLATION_SERIALIZABLE);
		try {
			tt.execute(new TransactionCallbackWithoutResult() {
				protected void doInTransactionWithoutResult(TransactionStatus status) {
					assertTrue("Has thread session", TransactionSynchronizationManager.hasResource(sf));
					JcrTemplate template = new JcrTemplate(sf);
					template.execute(new JcrCallback() {
						public Object doInJcr(Session session) throws RepositoryException {
							return null;
						}
					});
				}
			});
			fail("Should have thrown InvalidIsolationLevelException");

		}
		catch (InvalidIsolationLevelException e) {
			// it's okay
		}

		assertTrue("Hasn't thread session", !TransactionSynchronizationManager.hasResource(sf));
		assertTrue("JTA synchronizations not active", !TransactionSynchronizationManager.isSynchronizationActive());

		sfControl.verify();
	}

	public void testTransactionCommitWithPrebound() throws Exception {
		MockControl sfControl = MockControl.createControl(SessionFactory.class);
		final SessionFactory sf = (SessionFactory) sfControl.getMock();
		MockControl sessionControl = MockControl.createControl(XASession.class);
		final XASession session = (XASession) sessionControl.getMock();

		MockControl xaResControl = MockControl.createControl(XAResource.class);
		XAResource xaRes = (XAResource) xaResControl.getMock();

		sessionControl.expectAndReturn(session.getXAResource(), xaRes);
		session.save();

		sfControl.replay();
		sessionControl.replay();
		xaResControl.replay();
		final SessionHolderProviderManager providerManager = new ListSessionHolderProviderManager();

		PlatformTransactionManager tm = new LocalTransactionManager(sf);
		TransactionTemplate tt = new TransactionTemplate(tm);
		UserTxSessionHolder uTx = new UserTxSessionHolder(session);
		TransactionSynchronizationManager.bindResource(sf, uTx);

		assertTrue("Has thread session", TransactionSynchronizationManager.hasResource(sf));

		tt.execute(new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				assertTrue("Has thread session", TransactionSynchronizationManager.hasResource(sf));
				JcrTemplate template = new JcrTemplate(sf);
				template.save();
			}
		});

		assertTrue("Has thread session", TransactionSynchronizationManager.hasResource(sf));
		TransactionSynchronizationManager.unbindResource(sf);
		assertTrue("JTA synchronizations not active", !TransactionSynchronizationManager.isSynchronizationActive());

		sfControl.verify();
		sessionControl.verify();
		xaResControl.verify();
	}

	public void testTransactionRollbackOnlyWithPrebound() throws Exception {
		MockControl sfControl = MockControl.createControl(SessionFactory.class);
		final SessionFactory sf = (SessionFactory) sfControl.getMock();
		MockControl sessionControl = MockControl.createControl(XASession.class);
		final XASession session = (XASession) sessionControl.getMock();

		MockControl xaResControl = MockControl.createControl(XAResource.class);
		XAResource xaRes = (XAResource) xaResControl.getMock();

		sessionControl.expectAndReturn(session.getXAResource(), xaRes);
		session.save();

		sfControl.replay();
		sessionControl.replay();
		xaResControl.replay();

		PlatformTransactionManager tm = new LocalTransactionManager(sf);
		TransactionTemplate tt = new TransactionTemplate(tm);
		UserTxSessionHolder uTx = new UserTxSessionHolder(session);
		TransactionSynchronizationManager.bindResource(sf, uTx);

		assertTrue("Has thread session", TransactionSynchronizationManager.hasResource(sf));
		uTx.setRollbackOnly();

		try {
			tt.execute(new TransactionCallbackWithoutResult() {
				protected void doInTransactionWithoutResult(TransactionStatus status) {
					assertTrue("Has thread session", TransactionSynchronizationManager.hasResource(sf));
					JcrTemplate template = new JcrTemplate(sf);
					template.save();
				}
			});

		}
		catch (UnexpectedRollbackException e) {
			System.out.println(e);
		}

		assertTrue("Has thread session", TransactionSynchronizationManager.hasResource(sf));
		TransactionSynchronizationManager.unbindResource(sf);
		assertTrue("JTA synchronizations not active", !TransactionSynchronizationManager.isSynchronizationActive());

		sfControl.verify();
		sessionControl.verify();
		xaResControl.verify();
	}

	/**
	 * Simple mock which overrides equals.
	 * 
	 * @author Costin Leau
	 * 
	 */
	protected class XidMock implements Xid {
		/**
		 * @see javax.transaction.xa.Xid#getBranchQualifier()
		 */
		public byte[] getBranchQualifier() {
			return null;
		}

		/**
		 * @see javax.transaction.xa.Xid#getFormatId()
		 */
		public int getFormatId() {
			return 0;
		}

		/**
		 * @see javax.transaction.xa.Xid#getGlobalTransactionId()
		 */
		public byte[] getGlobalTransactionId() {
			return null;
		}

		/**
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		public boolean equals(Object obj) {
			return true;
		}

	}

}
