package org.springmodules.feedxt.event;

import javax.servlet.http.HttpSession;
import org.springmodules.feedxt.domain.User;

/**
 * {@link org.springframework.context.ApplicationEvent} to fire
 * on user login.
 *
 * @author Sergio Bossa
 */
public class LoginEvent extends AccessEvent {
    
    public LoginEvent(Object source, HttpSession session, User user) {
        super(source, session, user);
    }
}
