package org.springmodules.prevayler.transaction;

import org.springframework.dao.DataAccessException;

/**
 * Exception thrown during execution of a transaction in the context of a {@link org.springmodules.prevayler.transaction.TransactionManager}.
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
