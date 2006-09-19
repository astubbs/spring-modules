package org.springmodules.xt.examples.domain;

/**
 * BaseSpecification determining if a given office is full.
 * 
 * @author Sergio Bossa
 */
public class FullOfficeSpecification implements BaseSpecification<IOffice> {
    
    private int limit = IOffice.MAX_EMPLOYEES;
    
    public boolean isSatisfiedBy(IOffice o) {
        return o.getEmployees().size() > this.limit; 
    }
}
