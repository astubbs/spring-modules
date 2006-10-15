package org.springmodules.prevayler;

import org.springframework.dao.DataAccessException;

/**
 * Exception thrown when trying to update or delete an unsaved object.
 * 
 * @author Sergio Bossa
 */
public class PrevaylerUnsavedObjectException extends DataAccessException {
    
    public PrevaylerUnsavedObjectException(String msg) {
        super(msg);
    }
    
    public PrevaylerUnsavedObjectException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
