package org.springmodules.prevayler.id;

import org.springframework.dao.DataAccessException;

/**
 * Exception thrown when the prevalent system fails to resolve the id of an object.
 * 
 * @author Sergio Bossa
 */
public class PrevaylerIdResolutionException extends DataAccessException {
    
    public PrevaylerIdResolutionException(String msg) {
        super(msg);
    }
    
    public PrevaylerIdResolutionException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
