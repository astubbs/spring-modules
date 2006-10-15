package org.springmodules.prevayler.test.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * An office with some employees working on.
 *
 * @author Sergio Bossa
 */
public class OfficeImpl implements Office {
    
    private Long id;
    
    private String officeId;
    private String name;
    
    protected OfficeImpl() {}
    
    public OfficeImpl(String officeId, String name) {
        this.officeId = officeId;
        this.name = name;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getOfficeId() {
        return officeId;
    }
    
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Office)) return false;
        
        Office other = (Office) obj;
        
        return new EqualsBuilder().append(this.getOfficeId(), other.getOfficeId()).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(this.getOfficeId()).toHashCode();
    }
}
