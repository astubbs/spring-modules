package org.springmodules.prevayler.test.domain;

import java.util.Set;


/**
 * Interface for a manager, a particular employee.
 *
 * @author Sergio Bossa
 */
public interface Manager extends Employee {
    
    public String getRole();
    
    public void addManagedEmployee(Employee employee);
    
    public Set getManagedEmployees();
}
