package org.springmodules.feedxt.event;

import javax.servlet.http.HttpSession;
import org.springframework.context.ApplicationEvent;
import org.springmodules.feedxt.domain.User;

/**
 * @author Sergio Bossa
 */
public abstract class AccessEvent extends ApplicationEvent {
    
    private HttpSession session;
    private User user;
    
    public AccessEvent(Object source, HttpSession session, User user) {
        super(source);
        this.session = session;
        this.user = user;
    }

    public HttpSession getSession() {
        return this.session;
    }

    public User getUser() {
        return this.user;
    }
}
