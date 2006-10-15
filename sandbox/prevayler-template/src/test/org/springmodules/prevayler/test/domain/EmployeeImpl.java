package org.springmodules.prevayler.test.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * An employee.
 * 
 * @author Sergio Bossa
 */
public class EmployeeImpl implements Employee {

    private Long id;
    
    private String matriculationCode;
    private String firstname;
    private String surname;
    private Office office;
    
    private Manager manager;
    
    protected EmployeeImpl() {}
    
    public EmployeeImpl(String matriculationCode) {
        this.matriculationCode = matriculationCode;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMatriculationCode() {
        return matriculationCode;
    }

    public Office getOffice() {
        return this.office;
    }
    
    public void setOffice(Office office) {
        this.office = office;
    }
    
    public void setManager(Manager manager) {
        this.manager = manager;
    }
    
    public Manager getManager() {
        return this.manager;
    }
    
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Employee)) return false;
        
        Employee other = (Employee) obj;
        
        return new EqualsBuilder().append(this.getMatriculationCode(), other.getMatriculationCode()).isEquals();
    }
    
    public int hashCode() {
        return new HashCodeBuilder().append(this.getMatriculationCode()).toHashCode();
    }
}
