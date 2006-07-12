package org.springmodules.xt.test.domain;

/**
 * Generic BaseSpecification interface.
 * 
 * @author Sergio Bossa
 */
public interface BaseSpecification<T> {
    public boolean isSatisfiedBy(T object);
}
