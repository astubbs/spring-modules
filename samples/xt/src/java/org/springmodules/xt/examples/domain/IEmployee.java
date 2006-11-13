package org.springmodules.xt.examples.domain;

import java.io.Serializable;

/**
 * Interface for an employee.
 *
 * @author Sergio Bossa
 */
public interface IEmployee extends CopyAware, Serializable {
    
    public String getFirstname();

    public String getMatriculationCode();

    public String getSurname();
    
    public String getPassword();
    
    public void setMatriculationCode(String matriculationCode);

    public void setFirstname(String firstname);

    public void setSurname(String surname);
    
    public void setPassword(String password);
    
    public IEmployee copy();
}
