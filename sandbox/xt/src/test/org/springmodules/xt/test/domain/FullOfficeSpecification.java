package org.springmodules.xt.test.domain;

/**
 * BaseSpecification determining if a given office is full.
 * 
 * @author Sergio Bossa
 */
public class FullOfficeSpecification implements BaseSpecification<IOffice> {
    
    private int limit = 3;
    
    public boolean isSatisfiedBy(IOffice o) {
        return o.getEmployees().size() > this.limit; 
    }
}
