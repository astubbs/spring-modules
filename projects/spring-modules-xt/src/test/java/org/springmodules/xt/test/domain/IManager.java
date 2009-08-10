package org.springmodules.xt.test.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Interface for a manager.
 *
 * @author Sergio Bossa
 */
public interface IManager {
    
    void setManagedEmployees(List<IEmployee> employees);
    
    List<IEmployee> getManagedEmployees();
    
    String getMatriculationCode();
    
    void setMatriculationCode(String matriculationCode);
}
