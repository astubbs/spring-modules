package org.springmodules.prevayler;

import org.springmodules.prevayler.test.domain.Employee;
import org.springmodules.prevayler.test.domain.EmployeeImpl;
import org.springmodules.prevayler.system.PrevalentSystem;

/**
 *
 * @author Sergio Bossa
 */
public class NonSleepingPrevaylerCallback implements PrevaylerCallback {
    
    private Object entityId;
    
    public NonSleepingPrevaylerCallback(Object entityId) {
        this.entityId = entityId;
    }
    
    public Object doInTransaction(PrevalentSystem system) {
        EmployeeImpl emp = (EmployeeImpl) system.get(Employee.class, this.entityId);
        System.out.println("Setting name to Robert!");
        System.out.println("Current name: " + emp.getFirstname());
        emp.setFirstname("Robert");
        emp = (EmployeeImpl) system.update(emp);
        System.out.println("Name set to Robert!");
        return emp;
    }
}
