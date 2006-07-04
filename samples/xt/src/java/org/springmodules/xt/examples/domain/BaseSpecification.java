package org.springmodules.xt.examples.domain;

/**
 * Generic BaseSpecification interface.
 * 
 * @author Sergio Bossa
 */
public interface BaseSpecification<T> {
    public boolean isSatisfiedBy(T object);
}
