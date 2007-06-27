package org.springmodules.feedxt.service;

import org.springmodules.feedxt.domain.FeedSubscription;
import org.springmodules.feedxt.domain.User;
import org.springmodules.feedxt.domain.support.SubscriptionAlreadyExistentException;
import org.springmodules.feedxt.domain.support.UserAlreadyExistentException;
import org.springmodules.feedxt.domain.support.UserNotExistentException;

/**
 * Service representing main use cases. 
 *
 * @author Sergio Bossa
 */
public interface UserService {
    
    /**
     * Register a new User account.
     */
    public void signUpUserAccount(User user, String username, String password) throws UserAlreadyExistentException;
    
    /**
     * Get an User account with matching password.
     */
    public User getUserAccount(String username, String password);
    
    /**
     * Check if an user account with the given username already exists.
     */
    public boolean checkUserAccount(String username);
    
    /**
     * Subscribe to feed.
     */
    public void subscribeToFeed(User user, FeedSubscription subscription) throws SubscriptionAlreadyExistentException, UserNotExistentException;
    
    /**
     * Remove a subscription to a feed.
     * @return True if actually removed, false if not removed because not found.
     */
    public boolean removeSubscriptionToFeed(User user, FeedSubscription subscription) throws UserNotExistentException;
    
    /**
     * Get a subscription to a feed.
     * @return The user feed subscription.
     */
    public FeedSubscription getUserSubscriptionByName(User user, String subscriptionName) throws UserNotExistentException;
}
