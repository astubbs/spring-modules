package org.springmodules.prevayler;

import org.springmodules.prevayler.test.domain.Employee;
import org.springmodules.prevayler.test.domain.EmployeeImpl;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

/**
 * @author Sergio Bossa
 */
public class PrevaylerTemplateWithThreadTxTest extends AbstractDependencyInjectionSpringContextTests {
    
    private PrevaylerTemplate template;
    
    public PrevaylerTemplateWithThreadTxTest(String testName) {
        super(testName);
        this.setAutowireMode(AUTOWIRE_BY_NAME);
    }
    
    protected void onTearDown() throws Exception {
    }
    
    public void testSaveWithCommit() {
        EmployeeImpl emp = new EmployeeImpl("a1");
        Long id = null;
        
        this.template.accessTransactionManager().begin();
        
        // Id null before adding:
        assertNull(emp.getId());
        
        this.template.save(emp);
        
        // Id not null after adding:
        assertNotNull(emp.getId());
        
        id = emp.getId();
        
        this.template.accessTransactionManager().commit();
        
        // Verify that the employee has been actually saved:
        
        this.template.accessTransactionManager().begin();
        
        emp = (EmployeeImpl) this.template.get(Employee.class, id);
        assertNotNull(emp);
        
        this.template.accessTransactionManager().commit();
    }
    
    public void testSaveWithRollback() {
        EmployeeImpl emp = new EmployeeImpl("a1");
        Long id = null;
        
        this.template.accessTransactionManager().begin();
        
        // Id null before adding:
        assertNull(emp.getId());
        
        this.template.save(emp);
        
        // Id not null after adding:
        assertNotNull(emp.getId());
        
        id = emp.getId();
        
        // Verify that the employee has been saved into the current transaction:
        emp = (EmployeeImpl) this.template.get(Employee.class, id);
        assertNotNull(emp);
        
        this.template.accessTransactionManager().rollback();
        
        // Verify that the employee has NOT been actually saved (after rollback), that is
        // a new transaction doesn't see it:
        
        this.template.accessTransactionManager().begin();
        
        emp = (EmployeeImpl) this.template.get(Employee.class, id);
        assertNull(emp);
        
        this.template.accessTransactionManager().commit();
    }
    
    public void testSaveWithUnclosedTx() {
        EmployeeImpl emp = new EmployeeImpl("a1");
        Long id = null;
        
        this.template.accessTransactionManager().begin();
        this.template.save(emp);
        
        id = emp.getId();
        
        // The transaction here is not closed, so changes are not persisted:
        
        this.template.accessTransactionManager().begin();
        
        emp = (EmployeeImpl) this.template.get(Employee.class, id);
        assertNull(emp);
        
        this.template.accessTransactionManager().commit();
    }
    
    public void testCorrectIdSequenceBetweenRollbackAndCommit() {
        EmployeeImpl emp1 = new EmployeeImpl("a1");
        EmployeeImpl emp2 = new EmployeeImpl("a2");
        Long id1 = null;
        Long id2 = null;
        
        this.template.accessTransactionManager().begin();
        
        // Id null before adding:
        assertNull(emp1.getId());
        
        this.template.save(emp1);
        
        // Id not null after adding:
        assertNotNull(emp1.getId());
        
        // First id
        id1 = emp1.getId();
        
        // Rollback
        this.template.accessTransactionManager().rollback();
        
        // Now, make a save with commit and verify that the id is the same as before,
        // because the new transaction must re-assign the id assigned in the previously 
        // rolled back transaction:
        
        this.template.accessTransactionManager().begin();
        
        // Id null before adding:
        assertNull(emp2.getId());
        
        this.template.save(emp2);
        
        // Id not null after adding:
        assertNotNull(emp2.getId());
        
        // Second id
        id2 = emp2.getId();
        
        // The two ids must be equal:
        assertEquals(id1, id2);
        
        // Commit
        this.template.accessTransactionManager().commit();
    }
    
    public void testCorrectIdSequenceBetweenCommits() {
        EmployeeImpl emp1 = new EmployeeImpl("a1");
        EmployeeImpl emp2 = new EmployeeImpl("a2");
        Long id1 = null;
        Long id2 = null;
        
        this.template.accessTransactionManager().begin();
        
        // Id null before adding:
        assertNull(emp1.getId());
        
        this.template.save(emp1);
        
        // Id not null after adding:
        assertNotNull(emp1.getId());
        
        // First id
        id1 = emp1.getId();
        
        // Commit
        this.template.accessTransactionManager().commit();
        
        // Now, make a save with commit and verify that the second id is the first one plus one:
        
        this.template.accessTransactionManager().begin();
        
        // Id null before adding:
        assertNull(emp2.getId());
        
        this.template.save(emp2);
        
        // Id not null after adding:
        assertNotNull(emp2.getId());
        
        // Second id
        id2 = emp2.getId();
        
        // The two ids must be equal:
        assertEquals(new Long(id1.longValue() + 1), id2);
        
        // Commit
        this.template.accessTransactionManager().commit();
    }
    
    public void testConcurrency() throws InterruptedException {
        final Long id1 = null;
        final Long id2 = null;
        
        Runnable r1 = new Runnable() {
            public void run() {
                Long id1 = null;
                Long id2 = null;
                
                EmployeeImpl emp1 = new EmployeeImpl("a1");
                template.accessTransactionManager().begin();
                template.save(emp1);
                template.accessTransactionManager().commit();
                
                id1 = emp1.getId();
                assertNotNull(id1);
                
                template.accessTransactionManager().begin();
                EmployeeImpl emp2 = (EmployeeImpl) template.get(EmployeeImpl.class, id1);
                template.accessTransactionManager().commit();
                
                assertNotNull(emp2);
                assertEquals("First: " + emp1.getMatriculationCode() + " - Second: "+ emp2.getMatriculationCode(), emp1, emp2);
            }
        };
        
        Runnable r2 = new Runnable() {
            public void run() {
                Long id1 = null;
                Long id2 = null;
                
                EmployeeImpl emp1 = new EmployeeImpl("a2");
                template.accessTransactionManager().begin();
                template.save(emp1);
                template.accessTransactionManager().commit();
                
                id1 = emp1.getId();
                assertNotNull(id1);
                
                template.accessTransactionManager().begin();
                EmployeeImpl emp2 = (EmployeeImpl) template.get(EmployeeImpl.class, id1);
                template.accessTransactionManager().commit();
                
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
    
    public void setPrevaylerTemplateWithThreadTx(PrevaylerTemplate template) {
        this.template = template;
    }
    
    protected String[] getConfigLocations() {
        return new String[]{"testContext.xml"};
    }
}
