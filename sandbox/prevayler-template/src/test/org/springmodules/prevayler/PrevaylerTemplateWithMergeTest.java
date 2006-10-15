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
        this.template.delete(Employee.class);
        this.template.delete(Office.class);
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
