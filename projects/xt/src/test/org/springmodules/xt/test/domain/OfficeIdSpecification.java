package org.springmodules.xt.test.domain;

/**
 * BaseSpecification determining if a given office has a valid id.
 * 
 * @author Sergio Bossa
 */
public class OfficeIdSpecification implements BaseSpecification<IOffice> {
    
    public boolean isSatisfiedBy(IOffice o) {
        return o.getOfficeId().matches("\\d+") || o.getOfficeId().matches("o\\d+"); 
    }
}
