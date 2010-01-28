package org.springmodules.xt.test.domain;

import java.util.List;

/**
 * Manager implementation.
 *
 * @author Sergio Bossa
 */
public class Manager implements IManager {
    
    private List<IEmployee> managedEmployees;
    private String matriculationCode;
    
    public void setManagedEmployees(List<IEmployee> employees) {
        this.managedEmployees = employees;
    }
    
    public List<IEmployee> getManagedEmployees() {
        return this.managedEmployees;
    }

    public String getMatriculationCode() {
        return this.matriculationCode;
    }

    public void setMatriculationCode(String matriculationCode) {
        this.matriculationCode = matriculationCode;
    }
}
