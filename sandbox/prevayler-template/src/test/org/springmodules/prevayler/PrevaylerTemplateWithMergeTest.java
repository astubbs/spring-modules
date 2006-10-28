package org.springmodules.prevayler;

import org.springmodules.prevayler.test.domain.Employee;
import org.springmodules.prevayler.test.domain.EmployeeImpl;
import org.springmodules.prevayler.test.domain.ManagerImpl;
import org.springmodules.prevayler.test.domain.Office;
import org.springmodules.prevayler.test.domain.OfficeImpl;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

/**
 * @author Sergio Bossa
 */
public class PrevaylerTemplateWithMergeTest extends AbstractDependencyInjectionSpringContextTests {
    
    private PrevaylerTemplate template;
    
    public PrevaylerTemplateWithMergeTest(String testName) {
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
    
    public void testSaveWithMerge() {
        EmployeeImpl emp = new EmployeeImpl("a1");
        
        // Id null before adding:
        assertNull(emp.getId());
        
        this.template.save(emp);
        
        // Id not null after adding:
        assertNotNull(emp.getId());
    }
    
    public void testCascadeSaveWithMerge() throws Exception {
        OfficeImpl o1 = new OfficeImpl("o1", "Office 1");
        ManagerImpl m1 = new ManagerImpl("m1");
        EmployeeImpl e1 = new EmployeeImpl("e1");
        e1.setOffice(o1);
        m1.addManagedEmployee(e1);
        
        assertNull(o1.getId());
        assertNull(m1.getId());
        assertNull(e1.getId());
        
        this.template.save(m1);
        
        assertNotNull(o1.getId());
        assertNotNull(m1.getId());
        assertNotNull(e1.getId());
    }
    
    public void testCascadeUpdateWithMerge() throws Exception {
        ManagerImpl m1 = new ManagerImpl("m1");
        EmployeeImpl e1 = new EmployeeImpl("e1");
        m1.addManagedEmployee(e1);
        
        assertNull(m1.getId());
        assertNull(e1.getId());
        
        this.template.save(m1);
        
        assertNotNull(m1.getId());
        assertNotNull(e1.getId());
        
        OfficeImpl o1 = new OfficeImpl("o1", "Office 1");
        e1.setOffice(o1);
        
        assertNull(o1.getId());
        
        this.template.save(m1);
        
        assertNotNull(o1.getId());
    }
  
    public void setPrevaylerTemplateWithMerge(PrevaylerTemplate template) {
        this.template = template;
    }
    
    protected String[] getConfigLocations() {
        return new String[]{"testContext.xml"};
    }
}
