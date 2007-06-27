package org.springmodules.feedxt.domain.repository;

import org.springmodules.feedxt.domain.User;

/**
 * Repository for {@link org.springmodules.feedxt.domain.User} entities.
 *
 * @author Sergio Bossa
 */
public interface UserRepository {
    
    public void addUser(User user);
    
    public void removeUser(User user);
    
    public User getUserByUsername(String username);
}
