package org.springmodules.feedxt.web.controller.support;

import org.springmodules.feedxt.domain.User;

/**
 * {@link org.springmodules.feedxt.domain.User} holder.
 *
 * @author Sergio Bossa
 */
public class UserHolder {
    
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
