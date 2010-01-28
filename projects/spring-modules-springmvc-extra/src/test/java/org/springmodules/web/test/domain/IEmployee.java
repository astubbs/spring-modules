package org.springmodules.web.test.domain;

import java.io.Serializable;

/**
 * Interface for an employee.
 *
 * @author Sergio Bossa
 */
public interface IEmployee extends Serializable {
    
    String getFirstname();

    String getMatriculationCode();

    String getSurname();

    void setFirstname(String firstname);

    void setMatriculationCode(String matriculationCode);

    void setSurname(String surname);
}
