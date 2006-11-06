package org.springmodules.prevayler.support;

import org.springframework.dao.DataAccessException;

/**
 * Exception thrown during execution of a transaction.
 * 
 * @author Sergio Bossa
 */
public class PrevaylerTransactionException extends DataAccessException {
    
    public PrevaylerTransactionException(String msg) {
        super(msg);
    }
    
    public PrevaylerTransactionException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
