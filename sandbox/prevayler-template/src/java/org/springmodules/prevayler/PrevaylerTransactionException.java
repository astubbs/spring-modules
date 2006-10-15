package org.springmodules.prevayler;

import org.springframework.dao.UncategorizedDataAccessException;

/**
 * General (mostly unknown) exception thrown into prevayler transaction commands.
 * 
 * @author Sergio Bossa
 */
public class PrevaylerTransactionException extends UncategorizedDataAccessException {
    
    public PrevaylerTransactionException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
