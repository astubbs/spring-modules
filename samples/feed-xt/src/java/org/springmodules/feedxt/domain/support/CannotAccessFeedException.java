package org.springmodules.feedxt.domain.support;

/**
 * Checked business exception thrown when a feed cannot be accessed.
 *
 * @author Sergio Bossa
 */
public class CannotAccessFeedException extends Exception {
    
    public CannotAccessFeedException() {
    }
    
    public CannotAccessFeedException(String msg) {
        super(msg);
    }
    
    public CannotAccessFeedException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
