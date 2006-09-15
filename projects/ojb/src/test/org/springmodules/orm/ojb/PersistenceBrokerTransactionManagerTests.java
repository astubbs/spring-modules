/*
 * Copyright 2002-2005 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springmodules.orm.ojb;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import junit.framework.TestCase;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.accesslayer.ConnectionManagerIF;
import org.apache.ojb.broker.accesslayer.LookupException;
import org.easymock.MockControl;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * @author Juergen Hoeller
 * @since 08.07.2004
 */
public class PersistenceBrokerTransactionManagerTests extends TestCase {

	public void testTransactionCommit() throws LookupException, SQLException {
		MockControl pbControl = MockControl.createControl(PersistenceBroker.class);
		final PersistenceBroker pb = (PersistenceBroker) pbControl.getMock();
		MockControl cmControl = MockControl.createControl(ConnectionManagerIF.class);
		final ConnectionManagerIF cm = (ConnectionManagerIF) cmControl.getMock();
		final Object entity = new Object();
		MockControl conControl = MockControl.createControl(Connection.class);
		Connection con = (Connection) conControl.getMock();

		pb.serviceConnectionManager();
		pbControl.setReturnValue(cm, 2);
		cm.getConnection();
		cmControl.setReturnValue(con, 2);
		con.isReadOnly();
		conControl.setReturnValue(false, 1);
		pb.beginTransaction();
		pbControl.setVoidCallable(1);
		pb.delete(entity);
		pbControl.setVoidCallable(1);
		pb.commitTransaction();
		pbControl.setVoidCallable(1);
		pb.close();
		pbControl.setReturnValue(true, 1);

		pbControl.replay();
		cmControl.replay();
		conControl.replay();

		final PersistenceBrokerTransactionManager tm = new PersistenceBrokerTransactionManager() {
			protected PersistenceBroker getPersistenceBroker() {
				return pb;
			}
		};

		TransactionTemplate tt = new TransactionTemplate(tm);
		assertTrue("Hasn't thread broker", !TransactionSynchronizationManager.hasResource(tm.getPbKey()));
		tt.execute(new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				assertTrue("Has thread broker", TransactionSynchronizationManager.hasResource(tm.getPbKey()));
				PersistenceBrokerTemplate pbt = new PersistenceBrokerTemplate();
				pbt.delete(entity);
			}
		});
		assertTrue("Hasn't thread broker", !TransactionSynchronizationManager.hasResource(tm.getPbKey()));

		pbControl.verify();
		cmControl.verify();
		conControl.verify();
	}

	public void testTransactionRollback() throws LookupException, SQLException {
		MockControl pbControl = MockControl.createControl(PersistenceBroker.class);
		final PersistenceBroker pb = (PersistenceBroker) pbControl.getMock();
		MockControl cmControl = MockControl.createControl(ConnectionManagerIF.class);
		final ConnectionManagerIF cm = (ConnectionManagerIF) cmControl.getMock();
		final Object entity = new Object();
		MockControl conControl = MockControl.createControl(Connection.class);
		Connection con = (Connection) conControl.getMock();

		pb.serviceConnectionManager();
		pbControl.setReturnValue(cm, 2);
		cm.getConnection();
		cmControl.setReturnValue(con, 2);
		con.setReadOnly(true);
		conControl.setVoidCallable(1);
		con.getTransactionIsolation();
		conControl.setReturnValue(Connection.TRANSACTION_READ_COMMITTED, 1);
		con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
		conControl.setVoidCallable(1);
		con.isReadOnly();
		conControl.setReturnValue(true, 1);
		con.setReadOnly(false);
		conControl.setVoidCallable(1);
		con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		conControl.setVoidCallable(1);
		pb.beginTransaction();
		pbControl.setVoidCallable(1);
		pb.delete(entity);
		pbControl.setVoidCallable(1);
		pb.abortTransaction();
		pbControl.setVoidCallable(1);
		pb.close();
		pbControl.setReturnValue(true, 1);

		pbControl.replay();
		cmControl.replay();
		conControl.replay();

		final PersistenceBrokerTransactionManager tm = new PersistenceBrokerTransactionManager() {
			protected PersistenceBroker getPersistenceBroker() {
				return pb;
			}
		};

		TransactionTemplate tt = new TransactionTemplate(tm);
		tt.setReadOnly(true);
		tt.setIsolationLevel(TransactionDefinition.ISOLATION_SERIALIZABLE);
		assertTrue("Hasn't thread broker", !TransactionSynchronizationManager.hasResource(tm.getPbKey()));
		tt.execute(new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				assertTrue("Has thread broker", TransactionSynchronizationManager.hasResource(tm.getPbKey()));
				PersistenceBrokerTemplate pbt = new PersistenceBrokerTemplate();
				pbt.delete(entity);
				status.setRollbackOnly();
			}
		});
		assertTrue("Hasn't thread broker", !TransactionSynchronizationManager.hasResource(tm.getPbKey()));

		pbControl.verify();
		cmControl.verify();
		conControl.verify();
	}

	public void testParticipatingTransactionWithCommit() throws LookupException, SQLException {
		MockControl pbControl = MockControl.createControl(PersistenceBroker.class);
		final PersistenceBroker pb = (PersistenceBroker) pbControl.getMock();
		MockControl cmControl = MockControl.createControl(ConnectionManagerIF.class);
		final ConnectionManagerIF cm = (ConnectionManagerIF) cmControl.getMock();
		final Object entity = new Object();
		MockControl conControl = MockControl.createControl(Connection.class);
		Connection con = (Connection) conControl.getMock();

		pb.serviceConnectionManager();
		pbControl.setReturnValue(cm, 2);
		cm.getConnection();
		cmControl.setReturnValue(con, 2);
		con.isReadOnly();
		conControl.setReturnValue(false, 1);
		pb.beginTransaction();
		pbControl.setVoidCallable(1);
		pb.delete(entity);
		pbControl.setVoidCallable(1);
		pb.commitTransaction();
		pbControl.setVoidCallable(1);
		pb.close();
		pbControl.setReturnValue(true, 1);

		pbControl.replay();
		cmControl.replay();
		conControl.replay();

		final PersistenceBrokerTransactionManager tm = new PersistenceBrokerTransactionManager() {
			protected PersistenceBroker getPersistenceBroker() {
				return pb;
			}
		};

		assertTrue("Hasn't thread broker", !TransactionSynchronizationManager.hasResource(tm.getPbKey()));
		final TransactionTemplate tt = new TransactionTemplate(tm);
		tt.execute(new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				assertTrue("Has thread broker", TransactionSynchronizationManager.hasResource(tm.getPbKey()));
				tt.execute(new TransactionCallbackWithoutResult() {
					protected void doInTransactionWithoutResult(TransactionStatus status) {
						assertTrue("Has thread broker", TransactionSynchronizationManager.hasResource(tm.getPbKey()));
						PersistenceBrokerTemplate pbt = new PersistenceBrokerTemplate();
						pbt.delete(entity);
					}
				});
			}
		});
		assertTrue("Hasn't thread broker", !TransactionSynchronizationManager.hasResource(tm.getPbKey()));

		pbControl.verify();
		cmControl.verify();
		conControl.verify();
	}

	public void testParticipatingTransactionWithRollbackOnly() throws LookupException, SQLException {
		MockControl pbControl = MockControl.createControl(PersistenceBroker.class);
		final PersistenceBroker pb = (PersistenceBroker) pbControl.getMock();
		MockControl cmControl = MockControl.createControl(ConnectionManagerIF.class);
		final ConnectionManagerIF cm = (ConnectionManagerIF) cmControl.getMock();
		final Object entity = new Object();
		MockControl conControl = MockControl.createControl(Connection.class);
		Connection con = (Connection) conControl.getMock();

		pb.serviceConnectionManager();
		pbControl.setReturnValue(cm, 2);
		cm.getConnection();
		cmControl.setReturnValue(con, 2);
		con.isReadOnly();
		conControl.setReturnValue(false, 1);
		pb.beginTransaction();
		pbControl.setVoidCallable(1);
		pb.delete(entity);
		pbControl.setVoidCallable(1);
		pb.abortTransaction();
		pbControl.setVoidCallable(1);
		pb.close();
		pbControl.setReturnValue(true, 1);

		pbControl.replay();
		cmControl.replay();
		conControl.replay();

		final PersistenceBrokerTransactionManager tm = new PersistenceBrokerTransactionManager() {
			protected PersistenceBroker getPersistenceBroker() {
				return pb;
			}
		};

		assertTrue("Hasn't thread broker", !TransactionSynchronizationManager.hasResource(tm.getPbKey()));
		final TransactionTemplate tt = new TransactionTemplate(tm);
		try {
			tt.execute(new TransactionCallbackWithoutResult() {
				protected void doInTransactionWithoutResult(TransactionStatus status) {
					assertTrue("Has thread broker", TransactionSynchronizationManager.hasResource(tm.getPbKey()));
					tt.execute(new TransactionCallbackWithoutResult() {
						protected void doInTransactionWithoutResult(TransactionStatus status) {
							assertTrue("Has thread broker", TransactionSynchronizationManager.hasResource(tm.getPbKey()));
							PersistenceBrokerTemplate pbt = new PersistenceBrokerTemplate();
							pbt.delete(entity);
							status.setRollbackOnly();
						}
					});
				}
			});
			fail("Should have thrown UnexpectedRollbackException");
		}
		catch (UnexpectedRollbackException ex) {
			// expected
		}
		assertTrue("Hasn't thread broker", !TransactionSynchronizationManager.hasResource(tm.getPbKey()));

		pbControl.verify();
		cmControl.verify();
		conControl.verify();
	}

	public void testParticipatingTransactionWithRequiresNew() throws LookupException, SQLException {
		MockControl pb1Control = MockControl.createControl(PersistenceBroker.class);
		final PersistenceBroker pb1 = (PersistenceBroker) pb1Control.getMock();
		MockControl pb2Control = MockControl.createControl(PersistenceBroker.class);
		final PersistenceBroker pb2 = (PersistenceBroker) pb2Control.getMock();
		MockControl cmControl = MockControl.createControl(ConnectionManagerIF.class);
		final ConnectionManagerIF cm = (ConnectionManagerIF) cmControl.getMock();
		MockControl conControl = MockControl.createControl(Connection.class);
		Connection con = (Connection) conControl.getMock();

		pb1.serviceConnectionManager();
		pb1Control.setReturnValue(cm, 2);
		cm.getConnection();
		cmControl.setReturnValue(con, 4);
		con.isReadOnly();
		conControl.setReturnValue(false, 2);
		pb1.beginTransaction();
		pb1Control.setVoidCallable(1);
		pb2.serviceConnectionManager();
		pb2Control.setReturnValue(cm, 2);
		pb2.beginTransaction();
		pb2Control.setVoidCallable(1);
		pb2.commitTransaction();
		pb2Control.setVoidCallable(1);
		pb2.close();
		pb2Control.setReturnValue(true, 1);
		pb1.commitTransaction();
		pb1Control.setVoidCallable(1);
		pb1.close();
		pb1Control.setReturnValue(true, 1);

		pb1Control.replay();
		pb2Control.replay();
		cmControl.replay();
		conControl.replay();

		final PersistenceBrokerTransactionManager tm = new PersistenceBrokerTransactionManager() {
			int counter = 0;
			protected PersistenceBroker getPersistenceBroker() {
				counter++;
				return (counter > 1 ? pb2 : pb1);
			}
		};

		assertTrue("Hasn't thread broker", !TransactionSynchronizationManager.hasResource(tm.getPbKey()));
		final TransactionTemplate tt = new TransactionTemplate(tm);
		tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		tt.execute(new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				assertTrue("Has thread broker", TransactionSynchronizationManager.hasResource(tm.getPbKey()));
				assertEquals(pb1, OjbFactoryUtils.getPersistenceBroker(tm.getPbKey(), false));
				tt.execute(new TransactionCallbackWithoutResult() {
					protected void doInTransactionWithoutResult(TransactionStatus status) {
						assertTrue("Has thread broker", TransactionSynchronizationManager.hasResource(tm.getPbKey()));
						assertEquals(pb2, OjbFactoryUtils.getPersistenceBroker(tm.getPbKey(), false));
					}
				});
				assertEquals(pb1, OjbFactoryUtils.getPersistenceBroker(tm.getPbKey(), false));
			}
		});
		assertTrue("Hasn't thread broker", !TransactionSynchronizationManager.hasResource(tm.getPbKey()));

		pb1Control.verify();
		pb2Control.verify();
		cmControl.verify();
		conControl.verify();
	}

	public void testExposeJdbcTransaction() throws LookupException, SQLException {
		MockControl dsControl = MockControl.createControl(DataSource.class);
		final DataSource ds = (DataSource) dsControl.getMock();
		MockControl pbControl = MockControl.createControl(PersistenceBroker.class);
		final PersistenceBroker pb = (PersistenceBroker) pbControl.getMock();
		MockControl cmControl = MockControl.createControl(ConnectionManagerIF.class);
		final ConnectionManagerIF cm = (ConnectionManagerIF) cmControl.getMock();
		final Object entity = new Object();
		MockControl conControl = MockControl.createControl(Connection.class);
		Connection con = (Connection) conControl.getMock();

		pb.serviceConnectionManager();
		pbControl.setReturnValue(cm, 2);
		cm.getConnection();
		cmControl.setReturnValue(con, 2);
		con.isReadOnly();
		conControl.setReturnValue(false, 1);
		pb.beginTransaction();
		pbControl.setVoidCallable(1);
		pb.delete(entity);
		pbControl.setVoidCallable(1);
		pb.commitTransaction();
		pbControl.setVoidCallable(1);
		pb.close();
		pbControl.setReturnValue(true, 1);

		dsControl.replay();
		pbControl.replay();
		cmControl.replay();
		conControl.replay();

		final PersistenceBrokerTransactionManager tm = new PersistenceBrokerTransactionManager() {
			protected PersistenceBroker getPersistenceBroker() {
				return pb;
			}
		};
		tm.setDataSource(ds);

		TransactionTemplate tt = new TransactionTemplate(tm);
		assertTrue("Hasn't thread broker", !TransactionSynchronizationManager.hasResource(tm.getPbKey()));
		assertTrue("Hasn't thread connection", !TransactionSynchronizationManager.hasResource(ds));
		tt.execute(new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				assertTrue("Has thread broker", TransactionSynchronizationManager.hasResource(tm.getPbKey()));
				assertTrue("Has thread connection", TransactionSynchronizationManager.hasResource(ds));
				PersistenceBrokerTemplate pbt = new PersistenceBrokerTemplate();
				pbt.delete(entity);
			}
		});
		assertTrue("Hasn't thread broker", !TransactionSynchronizationManager.hasResource(tm.getPbKey()));
		assertTrue("Hasn't thread connection", !TransactionSynchronizationManager.hasResource(ds));

		dsControl.verify();
		pbControl.verify();
		cmControl.verify();
		conControl.verify();
	}

	protected void tearDown() {
		assertTrue(TransactionSynchronizationManager.getResourceMap().isEmpty());
		assertFalse(TransactionSynchronizationManager.isSynchronizationActive());
		assertFalse(TransactionSynchronizationManager.isCurrentTransactionReadOnly());
		assertFalse(TransactionSynchronizationManager.isActualTransactionActive());
	}

}
