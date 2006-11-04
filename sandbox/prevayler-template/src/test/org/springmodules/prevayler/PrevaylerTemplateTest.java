package org.springmodules.prevayler;

import java.util.List;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import org.springmodules.prevayler.test.domain.Employee;
import org.springmodules.prevayler.test.domain.EmployeeImpl;
import org.springmodules.prevayler.test.domain.Manager;
import org.springmodules.prevayler.test.domain.ManagerImpl;
import org.springmodules.prevayler.test.domain.Office;
import org.springmodules.prevayler.test.domain.OfficeImpl;

/**
 * @author Sergio Bossa
 */
public class PrevaylerTemplateTest extends AbstractDependencyInjectionSpringContextTests {
    
    private PrevaylerTemplate template;
    
    public PrevaylerTemplateTest(String testName) {
        super(testName);
        this.setAutowireMode(AUTOWIRE_BY_NAME);
    }

    protected void onTearDown() throws Exception {
        try {
            this.template.delete(Employee.class);
            this.template.delete(Office.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void testObjectReferenceIntegrity() {
        // Create an employee, assign it an office and save:
        EmployeeImpl emp1 = new EmployeeImpl("a1");
        OfficeImpl o1 = new OfficeImpl("o1", "Office 1"); 
        emp1.setOffice(o1);
        this.template.save(emp1);
        // Verify:
        o1 = (OfficeImpl) emp1.getOffice();
        assertNotNull(emp1.getId());
        assertNotNull(o1.getId());
        
        // Change the office name and DIRECTLY update it:
        o1.setName("New name");
        this.template.update(o1);
        
        // If we directly get the office, we see its name has been changed:
        o1 = (OfficeImpl) this.template.get(Office.class, o1.getId());
        assertEquals("New name", o1.getName());
        
        // If we get the office from the employee, the name is changed too:
        emp1 = (EmployeeImpl) this.template.get(Employee.class, emp1.getId());
        OfficeImpl o2 = (OfficeImpl) emp1.getOffice();
        assertNotNull("Value: " + o2.getName(), o2.getName());
        // What's most important, the office directly retrieved from the template, 
        // and the one retrieved from the employee, are the same:
        assertSame(o1, o2);
    }
    
    public void testSimpleSave() {
        EmployeeImpl emp = new EmployeeImpl("a1");
        
        // Id null before adding:
        assertNull(emp.getId());
        
        this.template.save(emp);
        
        // Id not null after adding:
        assertNotNull(emp.getId());
    }
    
    public void testCascadeSave() {
        // Create the manager:
        ManagerImpl man1 = new ManagerImpl("m1");
        // Create an employee and add it to the manager:
        EmployeeImpl emp1 = new EmployeeImpl("a1");
        man1.addManagedEmployee(emp1);
        // Save the manager and the employee by cascade:
        this.template.save(man1);
        
        // The employee saved by cascade has a not null id:
        assertNotNull(emp1.getId());
        
        // Verify object identities:
        EmployeeImpl empA = (EmployeeImpl) this.template.get(Employee.class, ((EmployeeImpl) man1.getManagedEmployees().iterator().next()).getId());
        ManagerImpl manA = (ManagerImpl) this.template.get(Manager.class, man1.getId());
        EmployeeImpl empB = (EmployeeImpl) manA.getManagedEmployees().iterator().next();
        // The new employee, get from the manager, is not the same as the old one:
        assertNotSame(emp1, empA);
        // But the two employees got from the prevalent system, one by id, one from the manager, are the same:
        assertSame(empA, empB);
    }
    
    public void testUpdate() {
        EmployeeImpl emp = new EmployeeImpl("a1");
        
        // Add an employee:
        this.template.save(emp);
        // Firstname is null:
        assertNull(emp.getFirstname());
        
        // Set firstname:
        emp.setFirstname("Sergio");
        // Update the employee:
        this.template.update(emp);
        // Read and verify:
        emp = (EmployeeImpl) this.template.get(emp.getClass(), emp.getId());
        assertEquals("Sergio", emp.getFirstname());
    }
    
    public void testCascadeUpdate() {
        // Create and save the manager:
        ManagerImpl man1 = new ManagerImpl("m1");
        this.template.save(man1);
        
        // Create an employee and add it to the manager:
        EmployeeImpl emp1 = new EmployeeImpl("a1");
        man1.addManagedEmployee(emp1);
        // Update the manager:
        this.template.update(man1);
        
        // Get the employee from the manager and change employee firstname:
        emp1 = (EmployeeImpl) man1.getManagedEmployees().iterator().next();
        emp1.setFirstname("Sergio");
        // Update the manager and the employee by cascade:
        this.template.update(man1);
        
        // Verify:
        emp1 = (EmployeeImpl) this.template.get(Employee.class, emp1.getId());
        assertEquals("Sergio", emp1.getFirstname());
    }
    
    public void testDeleteByEntity() {
        EmployeeImpl emp = new EmployeeImpl("a1");
        
        // Add an employee:
        this.template.save(emp);
        
        // Delete it:
        this.template.delete(emp);
        // Try to get it by id and verify null is returned:
        emp = (EmployeeImpl) this.template.get(emp.getClass(), emp.getId());
        assertNull(emp);
    }
    
    public void testDeleteByEntityClass() {
        EmployeeImpl emp = new EmployeeImpl("a1");
        
        // Add an employee:
        this.template.save(emp);
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
        
        this.template.save(emp1);
        this.template.save(emp2);
        
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
        this.template.save(emp);
        final Object id = emp.getId();
        
        Runnable r1 = new Runnable() {
            public void run() {
                PrevaylerTemplateTest.this.template.execute(new SleepingPrevaylerCallback(id));
            }
        };
        
        Runnable r2 = new Runnable() {
            public void run() {
                PrevaylerTemplateTest.this.template.execute(new NonSleepingPrevaylerCallback(id));
            }
        };
        
        Thread t1 = new Thread(r1);
        Thread.sleep(300);
        Thread t2 = new Thread(r2);
        
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }
    
    public void setPrevaylerTemplate(PrevaylerTemplate template) {
        this.template = template;
    }
    
    protected String[] getConfigLocations() {
        return new String[]{"testContext.xml"};
    }
}
