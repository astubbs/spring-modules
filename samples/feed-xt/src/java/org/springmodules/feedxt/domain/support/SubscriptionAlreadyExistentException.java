package org.springmodules.feedxt.domain.support;

/**
 * Checked business exception thrown when an user adds a subscription with an already existent name.
 *
 * @author Sergio Bossa
 */
public class SubscriptionAlreadyExistentException extends Exception {
    
    public SubscriptionAlreadyExistentException() {
    }
    
    public SubscriptionAlreadyExistentException(String msg) {
        super(msg);
    }
}
