package org.springmodules.prevayler;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springmodules.prevayler.test.domain.Employee;
import org.springmodules.prevayler.test.domain.EmployeeImpl;

/**
 * @author Sergio Bossa
 */
public class PrevaylerTemplateWithTxTest extends AbstractDependencyInjectionSpringContextTests {
    
    private PrevaylerTransactionManager transactionManager;
    private PrevaylerTemplate template;
    
    public PrevaylerTemplateWithTxTest(String testName) {
        super(testName);
        this.setAutowireMode(AUTOWIRE_BY_NAME);
    }
    
    protected void onTearDown() throws Exception {
    }
    
    public void testSaveWithCommit() {
        EmployeeImpl emp = new EmployeeImpl("a1");
        
        // Get the transaction for the first time:
        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
        // Id null before adding:
        assertNull(emp.getId());
        // Save:
        this.template.save(emp);
        // Id not null after adding:
        assertNotNull(emp.getId());
        // Commit the transaction status:
        this.transactionManager.commit(status);
        
        // Verify that the employee has been actually saved:
        EmployeeImpl emp2 = (EmployeeImpl) this.template.get(Employee.class, emp.getId());
        assertNotNull(emp);
        assertEquals(emp, emp2);
    }
    
    public void testDoubleCommit() {
        EmployeeImpl emp = new EmployeeImpl("a1");
        
        // Get the transaction for the first time:
        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
        // Save:
        this.template.save(emp);
        // Commit the transaction status:
        this.transactionManager.commit(status);
        // The double commit fails:
        try {
            this.transactionManager.commit(status);
            fail();
        } catch(Exception ex) { 
            System.out.println(ex.getMessage());
        }
    }
    
    public void testSaveWithTimeout() throws InterruptedException {
        EmployeeImpl emp = new EmployeeImpl("a1");
        
        // Get the transaction for the first time:
        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
        // Save
        this.template.save(emp);
        // Go to sleep, waiting for timeout:
        Thread.sleep(2000);
        // Trying to commit will failbecause the transaction has been aborted due to the timeout:
        try {
            this.transactionManager.commit(status);
            fail();
        } catch(Exception ex) { 
            System.out.println(ex.getMessage());
        }
    }
    
    public void testSaveWithRollback() {
        EmployeeImpl emp = new EmployeeImpl("a1");
        
        // Get the transaction for the first time:
        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
        // Save
        this.template.save(emp);
        // Verify that the employee has been saved in the current transaction scope:
        EmployeeImpl emp2 = (EmployeeImpl) this.template.get(Employee.class, emp.getId());
        assertNotNull(emp);
        assertEquals(emp, emp2);
        // Manually roll back:
        this.transactionManager.rollback(status);
        
        // Verify that the employee has NOT been actually saved after rolling back:
        emp = (EmployeeImpl) this.template.get(Employee.class, emp.getId());
        assertNull(emp);
    }
    
    public void testCorrectIdSequenceBetweenRollbackAndCommit() {
        EmployeeImpl emp1 = new EmployeeImpl("a1");
        EmployeeImpl emp2 = new EmployeeImpl("a2");
        Long id1 = null;
        Long id2 = null;
        
        // Get the transaction for the first time:
        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
        // Save
        this.template.save(emp1);
        // Rollback
        this.transactionManager.rollback(status);
        
        // First id
        id1 = emp1.getId();
        
        // Now, make a save with commit and verify that the id is the same as before,
        // because the new transaction must re-assign the id assigned in the previously
        // rolled back transaction:
        
        status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
        this.template.save(emp2);
        this.transactionManager.commit(status);
        
        // Second id
        id2 = emp2.getId();
        
        assertEquals(id1, id2);
    }
    
    public void testCorrectIdSequenceBetweenCommits() {
        EmployeeImpl emp1 = new EmployeeImpl("a1");
        EmployeeImpl emp2 = new EmployeeImpl("a2");
        Long id1 = null;
        Long id2 = null;
        
        // Get the transaction for the first time:
        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
        // Save
        this.template.save(emp1);
        // Rollback
        this.transactionManager.commit(status);
        
        // First id
        id1 = emp1.getId();
        
        // Now, make a save with commit and verify that the id is equal to the previous id plus one:
        
        status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
        this.template.save(emp2);
        this.transactionManager.commit(status);
        
        // Second id
        id2 = emp2.getId();
        
        assertEquals(id1.longValue() +1, id2.longValue());
    }
    
    public void testConcurrency() throws InterruptedException {
        Runnable r1 = new Runnable() {
            public void run() {
                EmployeeImpl emp1 = new EmployeeImpl("a1");
                TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
                template.save(emp1);
                transactionManager.commit(status);
                
                EmployeeImpl emp2 = (EmployeeImpl) template.get(EmployeeImpl.class, emp1.getId());
                
                assertNotNull(emp2);
                assertEquals("First: " + emp1.getMatriculationCode() + " - Second: "+ emp2.getMatriculationCode(), emp1, emp2);
            }
        };
        
        Runnable r2 = new Runnable() {
            public void run() {
                EmployeeImpl emp1 = new EmployeeImpl("a2");
                TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
                template.save(emp1);
                transactionManager.commit(status);
                
                EmployeeImpl emp2 = (EmployeeImpl) template.get(EmployeeImpl.class, emp1.getId());
                
                assertNotNull(emp2);
                assertEquals("First: " + emp1.getMatriculationCode() + " - Second: "+ emp2.getMatriculationCode(), emp1, emp2);
            }
        };
        
        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }
    
    public void setPrevaylerTemplateWithTx(PrevaylerTemplate template) {
        this.template = template;
    }

    public void setTransactionManager(PrevaylerTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }
    
    protected String[] getConfigLocations() {
        return new String[]{"testContext.xml"};
    }
}
