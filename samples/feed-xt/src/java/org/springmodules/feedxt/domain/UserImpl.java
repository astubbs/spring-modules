package org.springmodules.feedxt.domain;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.springmodules.feedxt.domain.repository.UserRepository;
import org.springmodules.feedxt.domain.support.SubscriptionAlreadyExistentException;
import org.springmodules.feedxt.domain.support.UserAlreadyExistentException;

/**
 * {@link User} implementation.
 *
 * @author Sergio Bossa
 */
public class UserImpl implements User {
    
    private static final String DIGEST_ALGO = "MD5";
    private static final String DIGEST_CHARSET = "UTF-8";
    
    private String firstname;
    private String surname;
    private Date birthdate;
    private String username;
    private byte[] password;
    private List<FeedSubscription> subscriptions = new LinkedList<FeedSubscription>();
    private UserRepository userRepository;
    
    public UserImpl(UserRepository repository) {
        this.userRepository = repository;
    }
    
    protected UserImpl() {}
    
    public void register(String username, String password) throws UserAlreadyExistentException {
        User other = this.userRepository.getUserByUsername(username);
        if (other != null) {
            throw new UserAlreadyExistentException("Already existent user with username: " + username);
        } else {
            try {
                MessageDigest digest = MessageDigest.getInstance(DIGEST_ALGO);
                digest.update(password.getBytes(DIGEST_CHARSET));
                this.password = digest.digest();
            } catch (UnsupportedEncodingException ex) {
                throw new RuntimeException("Unexpected exception while computing password digest ...", ex);
            } catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException("Unexpected exception while computing password digest ...", ex);
            }
            this.username = username;
        }
    }
    
    public boolean matchPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance(DIGEST_ALGO);
            return digest.isEqual(this.password, digest.digest(password.getBytes(DIGEST_CHARSET)));
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException("Unexpected exception while computing password digest ...", ex);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("Unexpected exception while computing password digest ...", ex);
        }
    }
    
    public void subscribe(FeedSubscription subscription) throws SubscriptionAlreadyExistentException {
        if (subscriptions.contains(subscription)) {
            throw new SubscriptionAlreadyExistentException("Subscription already existent: " + subscription);
        }
        this.subscriptions.add(subscription);
    }

    public boolean unsubscribe(FeedSubscription subscription) {
        return subscriptions.remove(subscription);
    }

    public List<FeedSubscription> getSubscriptions() {
        return Collections.unmodifiableList(this.subscriptions);
    }

    public FeedSubscription viewSubscriptionByName(String name) {
        FeedSubscription found = null;
        for (FeedSubscription s : this.subscriptions) {
            if (s.getName().equals(name)) {
                found = s;
                break;
            }
        }
        return found;
    }

    public String getUsername() {
        return this.username;
    }
    
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public String getFirstname() {
        return this.firstname;
    }
    
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    
    public String getSurname() {
        return this.surname;
    }
    
    public void setSurname(String surname) {
        this.surname = surname;
    }
    
    public Date getBirthdate() {
        return this.birthdate;
    }
    
    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }
    
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (!(obj instanceof UserImpl)) {
            return false;
        } else {
            UserImpl other = (UserImpl) obj;
            return new EqualsBuilder()
            .append(this.username, other.username)
            .isEquals();
        }
    }
    
    public int hashCode() {
        return new HashCodeBuilder()
        .append(this.username)
        .toHashCode();
    }
    
    public String toString() {
        return this.username;
    }
}
