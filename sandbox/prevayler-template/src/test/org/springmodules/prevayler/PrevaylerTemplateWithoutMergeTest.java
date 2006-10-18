package org.springmodules.prevayler;

import org.springmodules.prevayler.test.domain.Employee;
import org.springmodules.prevayler.test.domain.EmployeeImpl;
import org.springmodules.prevayler.test.domain.ManagerImpl;
import java.util.List;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

/**
 * @author Sergio Bossa
 */
public class PrevaylerTemplateWithoutMergeTest extends AbstractDependencyInjectionSpringContextTests {
    
    private PrevaylerTemplate template;
    
    public PrevaylerTemplateWithoutMergeTest(String testName) {
        super(testName);
        this.setAutowireMode(AUTOWIRE_BY_NAME);
    }

    protected void onTearDown() throws Exception {
        this.template.delete(Employee.class);
    }
    
    public void testSave() {
        EmployeeImpl emp = new EmployeeImpl("a1");
        
        // Id null before adding:
        assertNull(emp.getId());
        
        emp = (EmployeeImpl) this.template.save(emp);
        
        // Id not null after adding:
        assertNotNull(emp.getId());
    }
    
    public void testCascadeSave() {
        // Create and save the manager:
        ManagerImpl man1 = new ManagerImpl("m1");
        man1 = (ManagerImpl) this.template.save(man1);
        
        // Create an employee and add it to the manager:
        EmployeeImpl emp1 = new EmployeeImpl("a1");
        man1.addManagedEmployee(emp1);
        // Update the manager and save the employee by cascade:
        man1 = (ManagerImpl) this.template.update(man1);
        
        // Too bad, the id has not been saved into the original object:
        assertNull(emp1.getId());
        // But it has been set in the new instance returned by the update method:
        assertNotNull(((EmployeeImpl) man1.getManagedEmployees().iterator().next()).getId());
        
        // Verify object identities:
        EmployeeImpl empA = (EmployeeImpl) this.template.get(Employee.class, ((EmployeeImpl) man1.getManagedEmployees().iterator().next()).getId());
        EmployeeImpl empB = (EmployeeImpl) man1.getManagedEmployees().iterator().next();
        // The new employee, get from the manager, is not the same as the old one:
        assertNotSame(emp1, empA);
        // But the two employees got from the prevalent system, one by id, one from the manager, are the same:
        assertSame(empA, empB);
    }
    
    public void testUpdate() {
        EmployeeImpl emp = new EmployeeImpl("a1");
        
        // Add an employee:
        emp = (EmployeeImpl) this.template.save(emp);
        // Firstname is null:
        assertNull(emp.getFirstname());
        
        // Set firstname:
        emp.setFirstname("Sergio");
        // Update the employee:
        emp = (EmployeeImpl) this.template.update(emp);
        // Read and verify:
        emp = (EmployeeImpl) this.template.get(emp.getClass(), emp.getId());
        assertEquals("Sergio", emp.getFirstname());
    }
    
    public void testCascadeUpdate() {
        // Create and save the manager:
        ManagerImpl man1 = new ManagerImpl("m1");
        man1 = (ManagerImpl) this.template.save(man1);
        
        // Create an employee and add it to the manager:
        EmployeeImpl emp1 = new EmployeeImpl("a1");
        man1.addManagedEmployee(emp1);
        // Update the manager:
        man1 = (ManagerImpl) this.template.update(man1);
        
        // Get the employee from the manager and change employee firstname:
        emp1 = (EmployeeImpl) man1.getManagedEmployees().iterator().next();
        emp1.setFirstname("Sergio");
        // Update the manager and the employee by cascade:
        man1 = (ManagerImpl) this.template.update(man1);
        
        // Verify:
        emp1 = (EmployeeImpl) this.template.get(Employee.class, emp1.getId());
        assertEquals("Sergio", emp1.getFirstname());
    }
    
    public void testDeleteByEntity() {
        EmployeeImpl emp = new EmployeeImpl("a1");
        
        // Add an employee:
        emp = (EmployeeImpl) this.template.save(emp);
        
        // Delete it:
        this.template.delete(emp);
        // Try to get it by id and verify null is returned:
        emp = (EmployeeImpl) this.template.get(emp.getClass(), emp.getId());
        assertNull(emp);
    }
    
    public void testDeleteByEntityClass() {
        EmployeeImpl emp = new EmployeeImpl("a1");
        
        // Add an employee:
        emp = (EmployeeImpl) this.template.save(emp);
        // Verify:
        List result = this.template.get(emp.getClass());
        assertFalse(result.isEmpty());
        
        // Delete all:
        this.template.delete(emp.getClass());
        // Try to get all employees and verify that an empty list is returned:
        result = this.template.get(emp.getClass());
        assertTrue(result.isEmpty());
    }
    
    public void testExecuteCallback() {
        EmployeeImpl emp1 = new EmployeeImpl("a1");
        EmployeeImpl emp2 = new EmployeeImpl("a2");
        
        emp1 = (EmployeeImpl) this.template.save(emp1);
        emp2 = (EmployeeImpl) this.template.save(emp2);
        
        assertNull(emp1.getFirstname());
        assertNull(emp1.getSurname());
        
        // Execute a callback which looks for a1 and update it:
        PrevaylerCallback callback = new SimpleSearchAndUpdatePrevaylerCallback();
        // The callback execution returns a list of one employee:
        List result = (List) this.template.execute(callback);
        assertEquals(1, result.size());
        // Get it:
        emp1 = (EmployeeImpl) result.get(0);
        // Verify the update:
        assertTrue(emp1.getFirstname().equals("Sergio"));
        assertTrue(emp1.getSurname().equals("Bossa"));
        
        // Verify it is the same as the one directly retrieved from the prevalent system:
        EmployeeImpl emp1_1 = (EmployeeImpl) this.template.get(Employee.class, emp1.getId());
        assertSame(emp1, emp1_1);
    }
    
    public void testConcurrentUpdates() throws Exception {
        EmployeeImpl emp = new EmployeeImpl("a1");
        emp = (EmployeeImpl) this.template.save(emp);
        final Object id = emp.getId();
        
        Runnable r1 = new Runnable() {
            public void run() {
                PrevaylerTemplateWithoutMergeTest.this.template.execute(new SleepingPrevaylerCallback(id));
            }
        };
        
        Runnable r2 = new Runnable() {
            public void run() {
                PrevaylerTemplateWithoutMergeTest.this.template.execute(new NonSleepingPrevaylerCallback(id));
            }
        };
        
        Thread t1 = new Thread(r1);
        Thread.sleep(300);
        Thread t2 = new Thread(r2);
        
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        
        /* FIXME: Enable this?
        emp = (EmployeeImpl) this.template.get(emp.getClass(), id);
        assertEquals("Robert", emp.getFirstname());
        */
    }
    
    public void setPrevaylerTemplateWithoutMerge(PrevaylerTemplate template) {
        this.template = template;
    }
    
    protected String[] getConfigLocations() {
        return new String[]{"testContext.xml"};
    }
}
