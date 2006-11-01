package org.springmodules.xt.ajax.component.support;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import junit.framework.TestCase;
import org.springmodules.xt.test.domain.Employee;
import org.springmodules.xt.test.domain.Office;

/**
 *
 * @author Sergio Bossa
 */
public class BindStatusHelperTest extends TestCase {
    
    private Office office;
    
    public BindStatusHelperTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        this.office = new Office();
        
        Employee employee = new Employee();
        employee.setFirstname("Sergio");
        employee.setSurname("Bossa");
        employee.setMatriculationCode("abc");
        
        this.office.addEmployee(employee);
    }

    public void testGetStatusExpression() {
        BindStatusHelper helper = new BindStatusHelper("office.employees[0].matriculationCode", this.office);
        assertEquals("employees[0].matriculationCode", helper.getStatusExpression());
    }

    public void testGetStatusValue() {
        BindStatusHelper helper = new BindStatusHelper("office.employees[0].matriculationCode", this.office);
        assertEquals("abc", helper.getStatusValue());
    }
    
    public void testGetStatusValueWithPropertyEditor() {
        PropertyEditor editor = new PropertyEditorSupport() {
           public String getAsText() {
               Employee emp = (Employee) this.getValue();
               return emp.getFirstname() + " " + emp.getSurname();
           }  
        };
        BindStatusHelper helper = new BindStatusHelper("office.employees[0]", this.office);
        helper.setPropertyEditor(editor);
        assertEquals("Sergio Bossa", helper.getStatusValue());
    }
}
