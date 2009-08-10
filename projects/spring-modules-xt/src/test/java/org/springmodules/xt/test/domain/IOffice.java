package org.springmodules.xt.test.domain;

import java.io.Serializable;
import java.util.Set;

/**
 * Interface for an office.
 *
 * @author Sergio Bossa
 */
public interface IOffice extends Serializable {
    
    void addEmployee(IEmployee e);
    
    void removeEmployee(IEmployee e);
    
    Set<IEmployee> getEmployees();

    String getName();

    String getOfficeId();

    void setEmployees(Set<IEmployee> employees);

    void setName(String name);

    void setOfficeId(String officeId);
}
