package org.springmodules.feedxt.domain.support;

/**
 * Checked business exception thrown when trying to register an user with already existent username.
 *
 * @author Sergio Bossa
 */
public class UserNotExistentException extends Exception {
    
    public UserNotExistentException() {
    }
    
    public UserNotExistentException(String msg) {
        super(msg);
    }
}
