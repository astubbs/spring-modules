package org.springmodules.orm.db4o;

import junit.framework.TestCase;

import org.springmodules.orm.db4o.Db4oCallback;
import org.springmodules.orm.db4o.Db4oTemplate;
import org.springmodules.orm.db4o.Db4oTransactionManager;
import org.easymock.MockControl;
import org.springframework.transaction.InvalidIsolationLevelException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

import com.db4o.ObjectContainer;
import com.db4o.ext.ExtObjectContainer;

public class Db4oTransactionManagerTests extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
    public void testTransactionCommit() throws Exception {
        MockControl containerControl = MockControl.createControl(ExtObjectContainer.class);
        final ExtObjectContainer container = (ExtObjectContainer) containerControl.getMock();

        containerControl.expectAndReturn(container.identity(), null);
        container.commit();
        containerControl.replay();

        PlatformTransactionManager tm = new Db4oTransactionManager(container);
        TransactionTemplate tt = new TransactionTemplate(tm);
        assertTrue("Has no container", !TransactionSynchronizationManager.hasResource(container));
        assertTrue("JTA synchronizations not active", !TransactionSynchronizationManager.isSynchronizationActive());

        tt.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                assertTrue("Has thread session", TransactionSynchronizationManager.hasResource(container));
                Db4oTemplate template = new Db4oTemplate(container);
                template.identity();
            }
        });

        assertTrue("Has no container", !TransactionSynchronizationManager.hasResource(container));
        assertTrue("JTA synchronizations not active", !TransactionSynchronizationManager.isSynchronizationActive());

        containerControl.verify();
    }

    public void testTransactionRollback() throws Exception {
        MockControl containerControl = MockControl.createControl(ExtObjectContainer.class);
        final ExtObjectContainer container = (ExtObjectContainer) containerControl.getMock();
        
        containerControl.expectAndReturn(container.ext(), container);
        containerControl.expectAndReturn(container.identity(), null);
        container.rollback();
        containerControl.replay();

        PlatformTransactionManager tm = new Db4oTransactionManager(container);
        TransactionTemplate tt = new TransactionTemplate(tm);
        assertTrue("Has no container", !TransactionSynchronizationManager.hasResource(container));
        assertTrue("JTA synchronizations not active", !TransactionSynchronizationManager.isSynchronizationActive());

        try {
            tt.execute(new TransactionCallbackWithoutResult() {
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    assertTrue("Has thread session", TransactionSynchronizationManager.hasResource(container));
                    Db4oTemplate template = new Db4oTemplate(container);
                    template.execute(new Db4oCallback() {
                        public Object doInDb4o(ObjectContainer cont) {
                        	cont.ext().identity();
                            throw new RuntimeException();
                        }

                    });
                }
            });
        } catch (RuntimeException e) {
            // it's okay
        }

        assertTrue("Has no container", !TransactionSynchronizationManager.hasResource(container));
        assertTrue("JTA synchronizations not active", !TransactionSynchronizationManager.isSynchronizationActive());

        containerControl.verify();
    }
    
    public void testTransactionRollbackOnly() throws Exception {
        MockControl containerControl = MockControl.createControl(ExtObjectContainer.class);
        final ExtObjectContainer container = (ExtObjectContainer) containerControl.getMock();
        
        containerControl.expectAndReturn(container.ext(), container);
        containerControl.expectAndReturn(container.identity(), null);
        container.rollback();
        containerControl.replay();

        PlatformTransactionManager tm = new Db4oTransactionManager(container);
        TransactionTemplate tt = new TransactionTemplate(tm);
        assertTrue("Has no container", !TransactionSynchronizationManager.hasResource(container));
        assertTrue("JTA synchronizations not active", !TransactionSynchronizationManager.isSynchronizationActive());

        try {
            tt.execute(new TransactionCallbackWithoutResult() {
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    assertTrue("Has thread session", TransactionSynchronizationManager.hasResource(container));
                    Db4oTemplate template = new Db4oTemplate(container);
                    template.execute(new Db4oCallback() {
                        public Object doInDb4o(ObjectContainer cont) {
                            cont.ext().identity();
                            return null;
                        }

                    });
                    status.setRollbackOnly();
                }
            });
        } catch (RuntimeException e) {
            // it's okay
        }

        assertTrue("Has no container", !TransactionSynchronizationManager.hasResource(container));
        assertTrue("JTA synchronizations not active", !TransactionSynchronizationManager.isSynchronizationActive());

        containerControl.verify();
    }    

    
    public void testInvalidIsolation() throws Exception {
        MockControl containerControl = MockControl.createControl(ExtObjectContainer.class);
        final ExtObjectContainer container = (ExtObjectContainer) containerControl.getMock();
        
        containerControl.replay();

        PlatformTransactionManager tm = new Db4oTransactionManager(container);
        TransactionTemplate tt = new TransactionTemplate(tm);
        assertTrue("Has no container", !TransactionSynchronizationManager.hasResource(container));
        assertTrue("JTA synchronizations not active", !TransactionSynchronizationManager.isSynchronizationActive());

        tt.setIsolationLevel(TransactionDefinition.ISOLATION_SERIALIZABLE);
        try {
            tt.execute(new TransactionCallbackWithoutResult() {
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    assertTrue("Has thread session", TransactionSynchronizationManager.hasResource(container));
                    Db4oTemplate template = new Db4oTemplate(container);
                    template.execute(new Db4oCallback() {
                        public Object doInDb4o(ObjectContainer cont) {
                            return null;
                        }
                    });
                }
            });
            fail("Should have thrown InvalidIsolationLevelException");

        } catch (InvalidIsolationLevelException e) {
            // it's okay
        }

        assertTrue("Has no container", !TransactionSynchronizationManager.hasResource(container));
        assertTrue("JTA synchronizations not active", !TransactionSynchronizationManager.isSynchronizationActive());

        containerControl.verify();
    }	

}
