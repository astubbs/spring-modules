package org.springmodules.feedxt.service;

import org.springmodules.feedxt.domain.FeedSubscription;
import org.springmodules.feedxt.domain.User;
import org.springmodules.feedxt.domain.repository.UserRepository;
import org.springmodules.feedxt.domain.support.SubscriptionAlreadyExistentException;
import org.springmodules.feedxt.domain.support.UserAlreadyExistentException;
import org.springmodules.feedxt.domain.support.UserNotExistentException;

/**
 * {@link UserService} implementation.
 *
 * @author Sergio Bossa
 */
public class UserServiceImpl implements UserService {
    
    private UserRepository userRepository;
    
    public void signUpUserAccount(User user, String username, String password) throws UserAlreadyExistentException {
        user.register(username, password);
        this.userRepository.addUser(user);
    }
    
    public User getUserAccount(String username, String password) {
        User user = this.userRepository.getUserByUsername(username);
        if (user != null && user.matchPassword(password)) {
            return user;
        } else {
            return null;
        }
    }

    public boolean checkUserAccount(String username) {
        User user = this.userRepository.getUserByUsername(username);
        if (user != null) {
            return true;
        } else {
            return false;
        }
    }

    public void subscribeToFeed(User user, FeedSubscription subscription) throws SubscriptionAlreadyExistentException, UserNotExistentException {
        if (this.userRepository.getUserByUsername(user.getUsername()) != null) {
            user.subscribe(subscription);
            this.userRepository.addUser(user);
        } else {
            throw new UserNotExistentException("User not found: " + user);
        }
    }

    public boolean removeSubscriptionToFeed(User user, FeedSubscription subscription) throws UserNotExistentException {
        if (this.userRepository.getUserByUsername(user.getUsername()) != null) {
            boolean removed = user.unsubscribe(subscription);
            if (removed) {
                this.userRepository.addUser(user);
            }
            return removed;
        } else {
            throw new UserNotExistentException("User not found: " + user);
        }
    }

    public FeedSubscription getUserSubscriptionByName(User user, String subscriptionName) throws UserNotExistentException {
        if (this.userRepository.getUserByUsername(user.getUsername()) != null) {
            FeedSubscription subscription = user.viewSubscriptionByName(subscriptionName);
            return subscription;
        } else {
            throw new UserNotExistentException("User not found: " + user);
        }
    }

    public UserRepository getUserRepository() {
        return this.userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
