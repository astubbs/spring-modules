package org.springmodules.xt.examples.domain;

import java.io.Serializable;
import java.util.Set;

/**
 * Interface for an office.
 *
 * @author Sergio Bossa
 */
public interface IOffice extends CopyAware, Serializable {
    
    public static final int MAX_EMPLOYEES = 3;
    
    void addEmployee(IEmployee e);
    
    void removeEmployee(IEmployee e);
    
    Set<IEmployee> getEmployees();

    String getName();

    String getOfficeId();
    
    void setOfficeId(String officeId);

    void setEmployees(Set<IEmployee> employees);

    void setName(String name);
    
    public IOffice copy();
}
