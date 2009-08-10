package org.springmodules.web.test.domain;

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
    
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof IEmployee)) return false;
        
        IEmployee other = (IEmployee) obj;
        
        return new EqualsBuilder().append(this.getMatriculationCode(), other.getMatriculationCode()).isEquals();
    }
    
    public int hashCode() {
        return new HashCodeBuilder().append(this.getMatriculationCode()).toHashCode();
    }
}
