package org.springmodules.prevayler.test.domain;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Sergio Bossa
 */
public class ManagerImpl extends EmployeeImpl implements Manager{
    
    private Set employees = new HashSet();
    
    public ManagerImpl(String matriculationCode) {
        super(matriculationCode);
    }
    
    public String getRole() {
        return "Boss";
    }
    
    public void addManagedEmployee(Employee employee) {
        employee.setManager(this);
        this.employees.add(employee);
    }
    
    public Set getManagedEmployees() {
        return this.employees;
    }
}
