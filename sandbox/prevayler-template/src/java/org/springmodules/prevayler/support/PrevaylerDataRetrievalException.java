package org.springmodules.prevayler.support;

import org.springframework.dao.DataRetrievalFailureException;

/**
 * Exception thrown when trying to retrieve data that doesn't exist.
 * 
 * @author Sergio Bossa
 */
public class PrevaylerDataRetrievalException extends DataRetrievalFailureException {
    
    public PrevaylerDataRetrievalException(String msg) {
        super(msg);
    }
    
    public PrevaylerDataRetrievalException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
