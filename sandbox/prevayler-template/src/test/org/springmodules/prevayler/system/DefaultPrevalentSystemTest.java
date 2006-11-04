package org.springmodules.prevayler.system;

import org.springmodules.prevayler.test.domain.Employee;
import org.springmodules.prevayler.test.domain.EmployeeImpl;
import org.springmodules.prevayler.test.domain.ManagerImpl;
import org.springmodules.prevayler.test.domain.OfficeImpl;
import org.springmodules.prevayler.PrevaylerConfigurationException;
import org.springmodules.prevayler.PrevaylerUnsavedObjectException;
import java.util.List;
import junit.framework.TestCase;

/**
 * @author Sergio Bossa
 */
public class DefaultPrevalentSystemTest extends TestCase {
    
    private DefaultPrevalentSystem prevalentSystem;
    
    public DefaultPrevalentSystemTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        PrevalenceInfo info = new PrevalenceInfo();
        info.setPrevalentClasses(new Class[]{Employee.class});
        
        this.prevalentSystem = new DefaultPrevalentSystem();
        this.prevalentSystem.setPrevalenceInfo(info);
    }

    public void testSave() {
        EmployeeImpl emp = new EmployeeImpl("a1");
        
        // Id null before adding:
        assertNull(emp.getId());
        
        emp = (EmployeeImpl) this.prevalentSystem.save(emp);
        
        // Id not null after adding:
        assertNotNull(emp.getId());
    }
    
    public void testCascadeSave() {
        // Create and save the manager:
        ManagerImpl man1 = new ManagerImpl("m1");
        man1 = (ManagerImpl) this.prevalentSystem.save(man1);
        
        // Create an employee and add it to the manager:
        EmployeeImpl emp1 = new EmployeeImpl("a1");
        man1.addManagedEmployee(emp1);
        // Update the manager and save the employee by cascade:
        man1 = (ManagerImpl) this.prevalentSystem.update(man1);
        
        // Verify that the id has been set:
        assertNotNull(emp1.getId());
        
        // Verify object identities:
        EmployeeImpl empA = (EmployeeImpl) this.prevalentSystem.get(Employee.class, emp1.getId());
        EmployeeImpl empB = (EmployeeImpl) man1.getManagedEmployees().iterator().next();
        assertSame(emp1, empA);
        assertSame(empA, empB);
    }
    
    public void testSaveEntityClassNotConfigured() {
        OfficeImpl o1 = new OfficeImpl("o1", "Office 1");
        
        try {
            // Save an object whose class is not configured as prevalent:
            this.prevalentSystem.save(o1);
            fail("Save must fail!");
        }
        catch(PrevaylerConfigurationException ex) {  
        }
    }

    public void testUpdate() {
        EmployeeImpl emp = new EmployeeImpl("a1");
        
        // Add an employee:
        emp = (EmployeeImpl) this.prevalentSystem.save(emp);
        // Firstname is null:
        assertNull(emp.getFirstname());
        
        // Set firstname:
        emp.setFirstname("Sergio");
        // Update the employee:
        this.prevalentSystem.update(emp);
        // Read and verify:
        emp = (EmployeeImpl) this.prevalentSystem.get(emp.getClass(), emp.getId());
        assertEquals("Sergio", emp.getFirstname());
    }
    
    public void testCascadeUpdate() {
        // Create and save the manager:
        ManagerImpl man1 = new ManagerImpl("m1");
        man1 = (ManagerImpl) this.prevalentSystem.save(man1);
        
        // Create an employee and add it to the manager:
        EmployeeImpl emp1 = new EmployeeImpl("a1");
        man1.addManagedEmployee(emp1);
        // Update the manager:
        man1 = (ManagerImpl) this.prevalentSystem.update(man1);
        
        // Get the employee from the manager and change employee firstname:
        emp1 = (EmployeeImpl) man1.getManagedEmployees().iterator().next();
        emp1.setFirstname("Sergio");
        // Update the manager and the employee by cascade:
        man1 = (ManagerImpl) this.prevalentSystem.update(man1);
        
        // Verify:
        emp1 = (EmployeeImpl) this.prevalentSystem.get(Employee.class, emp1.getId());
        assertEquals("Sergio", emp1.getFirstname());
    }
    
    public void testUpdateUnsavedObject() {
        EmployeeImpl emp = new EmployeeImpl("a1");
        
        try {
            // Update the transient employee:
            this.prevalentSystem.update(emp);
            fail("Update must fail!");
        }
        catch(PrevaylerUnsavedObjectException ex) {  
        }
    }

    public void testDeleteByEntity() {
        EmployeeImpl emp = new EmployeeImpl("a1");
        
        // Add an employee:
        this.prevalentSystem.save(emp);
        
        // Delete it:
        this.prevalentSystem.delete(emp);
        // Try to get it by id and verify null is returned:
        emp = (EmployeeImpl) this.prevalentSystem.get(emp.getClass(), emp.getId());
        assertNull(emp);
    }
    
    public void testDeleteByEntityClass() {
        EmployeeImpl emp = new EmployeeImpl("a1");
        
        // Add an employee:
        this.prevalentSystem.save(emp);
        // Verify:
        List result = this.prevalentSystem.get(emp.getClass());
        assertFalse(result.isEmpty());
        
        // Delete all:
        this.prevalentSystem.delete(emp.getClass());
        // Try to get all employees and verify that an empty list is returned:
        result = this.prevalentSystem.get(emp.getClass());
        assertTrue(result.isEmpty());
    }
    
    public void testDeleteUnsavedObject() {
        EmployeeImpl emp = new EmployeeImpl("a1");
        
        try {
            // Delete the transient employee:
            this.prevalentSystem.delete(emp);
            fail("Delete must fail!");
        }
        catch(PrevaylerUnsavedObjectException ex) {  
        }
    }
}
