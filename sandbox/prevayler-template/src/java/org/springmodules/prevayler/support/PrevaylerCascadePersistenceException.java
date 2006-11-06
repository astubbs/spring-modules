package org.springmodules.prevayler.support;

import org.springframework.dao.DataAccessException;

/**
 * Exception thrown during cascade saving of objects.
 * 
 * @author Sergio Bossa
 */
public class PrevaylerCascadePersistenceException extends DataAccessException {
    
    public PrevaylerCascadePersistenceException(String msg) {
        super(msg);
    }
    
    public PrevaylerCascadePersistenceException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
