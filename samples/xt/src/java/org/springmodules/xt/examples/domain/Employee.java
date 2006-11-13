package org.springmodules.xt.examples.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * An employee.
 * 
 * @author Sergio Bossa
 */
public class Employee implements IEmployee {

    private String matriculationCode;
    private String firstname;
    private String surname;
    private String password;
    
    public Employee() {}
    
    public Employee(String matriculationCode) {
        this.matriculationCode = matriculationCode;
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
    
    public void setMatriculationCode(String matriculationCode) {
        this.matriculationCode = matriculationCode;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public IEmployee copy() {
        Employee copy = new Employee(new String(this.matriculationCode));
        copy.firstname = new String(this.firstname);
        copy.surname = new String(this.surname);
        
        return copy;
    }
    
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof IEmployee)) return false;
        
        IEmployee other = (IEmployee) obj;
        
        return new EqualsBuilder().append(this.getMatriculationCode(), other.getMatriculationCode()).isEquals();
    }
    
    public int hashCode() {
        return new HashCodeBuilder().append(this.getMatriculationCode()).toHashCode();
    }
}
