package org.springmodules.prevayler;

import org.springmodules.prevayler.test.domain.Employee;
import org.springmodules.prevayler.test.domain.EmployeeImpl;
import org.springmodules.prevayler.system.PrevalentSystem;

/**
 *
 * @author Sergio Bossa
 */
public class SleepingPrevaylerCallback implements PrevaylerCallback {
    
    private Object entityId;
    
    public SleepingPrevaylerCallback(Object entityId) {
        this.entityId = entityId;
    }
    
    public Object doInTransaction(PrevalentSystem system) {
        EmployeeImpl emp = (EmployeeImpl) system.get(Employee.class, this.entityId);
        try {
            Thread.sleep(1000);
        } 
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        System.out.println("Setting name to Paul!");
        System.out.println("Current name: " + emp.getFirstname());
        emp.setFirstname("Paul");
        emp = (EmployeeImpl) system.update(emp);
        System.out.println("Name set to Paul!");
        return emp;
    }
}
