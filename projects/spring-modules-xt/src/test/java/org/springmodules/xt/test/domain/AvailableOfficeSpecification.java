package org.springmodules.xt.test.domain;

/**
 * BaseSpecification determining if a given office has available room.
 * 
 * @author Sergio Bossa
 */
public class AvailableOfficeSpecification implements BaseSpecification<IOffice> {
    
    private int limit = 3;
    
    public boolean isSatisfiedBy(IOffice o) {
        return o.getEmployees().size() < this.limit; 
    }
}
