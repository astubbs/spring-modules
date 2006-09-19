package org.springmodules.xt.examples.domain;

import java.io.Serializable;

/**
 * Interface for an employee.
 *
 * @author Sergio Bossa
 */
public interface IEmployee extends CopyAware, Serializable {
    
    String getFirstname();

    String getMatriculationCode();

    String getSurname();
    
    void setMatriculationCode(String matriculationCode);

    void setFirstname(String firstname);

    void setSurname(String surname);
    
    public IEmployee copy();
}
