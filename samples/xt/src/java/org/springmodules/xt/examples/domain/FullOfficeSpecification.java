package org.springmodules.xt.examples.domain;

/**
 * BaseSpecification determining if a given office is full.
 * 
 * @author Sergio Bossa
 */
public class FullOfficeSpecification implements BaseSpecification<IOffice> {
    
    private int limit;
    
    public FullOfficeSpecification(int limit) {
        this.limit = limit;
    }
    
    public boolean isSatisfiedBy(IOffice o) {
        return o.getEmployees().size() > this.limit; 
    }
}
