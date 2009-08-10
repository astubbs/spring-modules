package org.springmodules.xt.test.domain;

import java.io.Serializable;

/**
 * Interface for an employee.
 *
 * @author Sergio Bossa
 */
public interface IEmployee extends Serializable {
    
    String getMatriculationCode();
    
    String getNickname();
    
    String getFirstname();

    String getSurname();
    
    String getPassword();
    
    void setMatriculationCode(String matriculationCode);

    void setFirstname(String firstname);

    void setSurname(String surname);
    
    void setPassword(String password);
}
