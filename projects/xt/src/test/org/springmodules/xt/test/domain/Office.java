package org.springmodules.xt.test.domain;

import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * An office with some employees working on.
 *
 * @author Sergio Bossa
 */
public class Office implements IOffice {
    
    private String officeId;
    private String name;
    private Set<IEmployee> employees = new HashSet();
    private AvailableOfficeSpecification availableOfficeSpecification = new AvailableOfficeSpecification();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }
    
    public Set getEmployees() {
        return employees;
    }

    public void setEmployees(Set<IEmployee> employees) {
       this.employees = employees;
    }
    
    public  void addEmployee(IEmployee e) {
        if (this.availableOfficeSpecification.isSatisfiedBy(this)) {
            this.employees.add(e);
        }
        else {
            BusinessException ex = new BusinessException();
            ex.addError(new Error("full.office", "Full Office", "employees"));
            throw ex;
        }
    }
    
    public void removeEmployee(IEmployee e) {
        this.employees.remove(e);
    }
    
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof IOffice)) return false;
        
        IOffice other = (IOffice) obj;
        
        return new EqualsBuilder().append(this.getOfficeId(), other.getOfficeId()).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(this.getOfficeId()).toHashCode();
    }
}
