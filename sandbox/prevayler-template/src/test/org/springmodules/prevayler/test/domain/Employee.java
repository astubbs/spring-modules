package org.springmodules.prevayler.test.domain;

import java.io.Serializable;

/**
 * Interface for an employee.
 *
 * @author Sergio Bossa
 */
public interface Employee extends Serializable {
    
    public String getFirstname();
    
    public void setFirstname(String firstname);

    public String getMatriculationCode();

    public String getSurname();
    
    public void setSurname(String surname);
    
    public Office getOffice();
    
    public void setOffice(Office office);
    
    public void setManager(Manager manager);
    
    public Manager getManager();
}
