package org.springmodules.feedxt.domain;

import java.io.Serializable;
import java.util.List;
import org.springmodules.feedxt.domain.repository.UserRepository;
import org.springmodules.feedxt.domain.support.SubscriptionAlreadyExistentException;
import org.springmodules.feedxt.domain.support.UserAlreadyExistentException;

/**
 * Interface representing an user of the system.
 *
 * @author Sergio Bossa
 */
public interface User extends Serializable {
    
    /**
     * Get the username.
     */
    public String getUsername();
    
    /**
     * Register the user, by providing user name and password.
     */
    public void register(String username, String password) throws UserAlreadyExistentException;
    
    /**
     * Return true if the user matches the given password, false otherwise.
     */
    public boolean matchPassword(String password);
    
    /**
     * Subscribe to a feed.
     */
    public void subscribe(FeedSubscription subscription) throws SubscriptionAlreadyExistentException;
    
    /**
     * Remove the subscription to a feed.
     * @return True if actually removed, false if not removed because not found.
     */
    public boolean unsubscribe(FeedSubscription subscription);
    
    /**
     * View all user subscriptions.
     */
    public List<FeedSubscription> getSubscriptions();
    
    /**
     * View a subscription by its name.
     */
    public FeedSubscription viewSubscriptionByName(String name);
    
    /**
     * Set the {@link org.springmodules.feedxt.domain.repository.UserRepository}.
     */
    public void setUserRepository(UserRepository userRepository);
}
