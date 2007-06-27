package org.springmodules.feedxt.domain.support;

/**
 * Checked business exception thrown when trying to register an user with already existent username.
 *
 * @author Sergio Bossa
 */
public class UserAlreadyExistentException extends Exception {
    
    public UserAlreadyExistentException() {
    }
    
    public UserAlreadyExistentException(String msg) {
        super(msg);
    }
}
