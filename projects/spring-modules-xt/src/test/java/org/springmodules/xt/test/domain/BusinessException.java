package org.springmodules.xt.test.domain;

import java.util.LinkedList;
import java.util.List;

/**
 * Business exception containing {@link Error}s.
 * 
 * @author Sergio Bossa
 */
public class BusinessException extends RuntimeException {
    
    private List<Error> errors = new LinkedList<Error>();
    
    public BusinessException() {
        super();
    }
    
    public BusinessException(String message) {
        super(message);
    }
    
    public void addError(Error e) {
        this.errors.add(e);
    }
    
    public List<Error> getErrors() {
        return this.errors;
    }
}
