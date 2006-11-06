package org.springmodules.prevayler.support;

import org.springframework.dao.UncategorizedDataAccessException;

/**
 * General (mostly unknown) exception thrown during execution of Prevayler operations.
 * 
 * @author Sergio Bossa
 */
public class PrevaylerOperationException extends UncategorizedDataAccessException {
    
    public PrevaylerOperationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
