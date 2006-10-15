package org.springmodules.prevayler;

import org.springframework.dao.DataAccessException;

/**
 * Exception thrown when the prevalent system has a wrong configuration.
 * 
 * @author Sergio Bossa
 */
public class PrevaylerConfigurationException extends DataAccessException {
    
    public PrevaylerConfigurationException(String msg) {
        super(msg);
    }
    
    public PrevaylerConfigurationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
