package org.springmodules.feedxt.event;

import javax.servlet.http.HttpSession;
import org.springmodules.feedxt.domain.User;

/**
 * {@link org.springframework.context.ApplicationEvent} to fire
 * on user logout.
 *
 * @author Sergio Bossa
 */
public class LogoutEvent extends AccessEvent {
    
    public LogoutEvent(Object source, HttpSession session, User user) {
        super(source, session, user);
    }
}
