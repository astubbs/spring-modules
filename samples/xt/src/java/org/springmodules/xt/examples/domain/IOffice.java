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
    
    public void addEmployee(IEmployee e);
    
    public void removeEmployee(IEmployee e);
    
    public Set<IEmployee> getEmployees();

    public String getName();

    public String getOfficeId();
    
    public void setOfficeId(String officeId);

    public void setEmployees(Set<IEmployee> employees);

    public void setName(String name);
    
    public IOffice copy();
}
