package org.springmodules.feedxt.infrastructure.dao.db4o;

import com.db4o.ObjectSet;
import com.db4o.query.Predicate;
import org.springmodules.db4o.Db4oTemplate;
import org.springmodules.feedxt.domain.User;
import org.springmodules.feedxt.domain.repository.UserRepository;

/**
 * Db4o {@link org.springmodules.feedxt.domain.repository.UserRepository}
 * implementation.
 *
 * @author Sergio Bossa
 */
public class Db4oUserDao implements UserRepository {
    
    private Db4oTemplate template;
    
    public void addUser(User user) {
        this.template.set(user);
    }
    
    public void removeUser(User user) {
        ObjectSet set = this.template.query(new UsernamePredicate(user.getUsername()));
        if (set.hasNext()) {
            this.template.delete(user);
        }
    }
    
    public User getUserByUsername(String username) {
        ObjectSet set = this.template.query(new UsernamePredicate(username));
        if (set.hasNext()) {
            User found = (User) set.next();
            found.setUserRepository(this);
            return found;
        } else {
            return null;
        }
    }
    
    public void setTemplate(Db4oTemplate template) {
        this.template = template;
    }
    
    private static class UsernamePredicate extends Predicate {
        
        private String username;
        
        public UsernamePredicate(String username) {
            this.username = username;
        }
        
        public boolean match(User user) {
            return user.getUsername().equals(this.username);
        }
    }
}
