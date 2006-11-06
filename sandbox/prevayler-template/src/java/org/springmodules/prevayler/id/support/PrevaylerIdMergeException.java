package org.springmodules.prevayler.id.support;

import org.springframework.dao.DataAccessException;

/**
 * Exception thrown when the prevalent system fails to merge identifiers.
 * 
 * @author Sergio Bossa
 */
public class PrevaylerIdMergeException extends DataAccessException {
    
    public PrevaylerIdMergeException(String msg) {
        super(msg);
    }
    
    public PrevaylerIdMergeException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
