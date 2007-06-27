package org.springmodules.feedxt.web.controller.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springmodules.feedxt.domain.User;

/**
 * Interceptor for propagating the {@link org.springmodules.feedxt.domain.User} object into every request
 * as an attribute.
 *
 * @author Sergio Bossa
 */
public class UserPropagationInterceptor extends HandlerInterceptorAdapter {
    
    private UserHolder userHolder;
    
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = this.userHolder.getUser();
        request.setAttribute("user", user);
        return true;
    }
    
    public void setUserHolder(UserHolder userHolder) {
        this.userHolder = userHolder;
    }
}
