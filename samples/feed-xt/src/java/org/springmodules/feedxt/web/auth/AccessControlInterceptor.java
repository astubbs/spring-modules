package org.springmodules.feedxt.web.auth;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.context.ApplicationEvent;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.UrlPathHelper;
import org.springmodules.feedxt.domain.User;
import org.springmodules.feedxt.event.LoginEvent;
import org.springmodules.feedxt.event.LogoutEvent;
import org.springmodules.feedxt.web.controller.support.UserHolder;
import org.springmodules.xt.ajax.AjaxInterceptor;
import org.springmodules.xt.ajax.util.AjaxRedirectSender;
import org.springmodules.xt.model.event.FilteringApplicationListener;

/**
 * Interceptor implementation for user access control. It ensures that:
 * <ul>
 * <li>Only logged in users can access protected web resources.</li>
 * <li>Double user logins are not allowed.</li>
 * </ul>
 *
 * @author Sergio Bossa
 */
public class AccessControlInterceptor extends HandlerInterceptorAdapter implements FilteringApplicationListener {
    
    private AjaxInterceptor ajaxInterceptor;
    
    private UserHolder userHolder;
    private String redirectUrl;
    private List<String> protectedPaths = new LinkedList();
    
    private UrlPathHelper urlPathHelper = new UrlPathHelper();
    private PathMatcher pathMatcher = new AntPathMatcher();
    
    private Map<User, HttpSession> users = new ConcurrentHashMap<User, HttpSession>();
    
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = this.userHolder.getUser();
        if (user == null) {
            String actualPath = this.urlPathHelper.getLookupPathForRequest(request);
            for (String protectedPath : this.protectedPaths) {
                if (this.pathMatcher.match(protectedPath, actualPath)) {
                    String fullUrl = null;
                    if (this.redirectUrl.startsWith("/")) {
                        fullUrl = request.getContextPath() + this.redirectUrl;
                    } else {
                        fullUrl = this.redirectUrl;
                    }
                    if (this.ajaxInterceptor.isAjaxRequest(request)) {
                        AjaxRedirectSender.sendRedirect(response, fullUrl, null);
                    } else {
                        response.sendRedirect(fullUrl);
                    }
                    return false;
                }
            }
        } else if ((user != null) && (this.users.get(user) != null) && (!this.users.get(user).equals(request.getSession()))) {
            this.userHolder.setUser(null);
            request.getSession().invalidate();
            
            String fullUrl = null;
            if (this.redirectUrl.startsWith("/")) {
                fullUrl = request.getContextPath() + this.redirectUrl;
            } else {
                fullUrl = this.redirectUrl;
            }
            if (this.ajaxInterceptor.isAjaxRequest(request)) {
                AjaxRedirectSender.sendRedirect(response, fullUrl, null);
            } else {
                response.sendRedirect(fullUrl);
            }
            return false;
        }
        return true;
    }
    
    public Class[] getSupportedEventClasses() {
        return new Class[]{LoginEvent.class, LogoutEvent.class};
    }
    
    public boolean accepts(ApplicationEvent applicationEvent) {
        return true;
    }
    
    public void onApplicationEvent(ApplicationEvent appEvent) {
        if (appEvent instanceof LoginEvent) {
            LoginEvent event = (LoginEvent) appEvent;
            HttpSession session = event.getSession();
            User user = event.getUser();
            this.users.put(user, session);
        } else if (appEvent instanceof LogoutEvent) {
            LogoutEvent event = (LogoutEvent) appEvent;
            HttpSession session = event.getSession();
            User user = event.getUser();
            this.users.remove(user);
        }
    }
    
    public void setAjaxInterceptor(AjaxInterceptor ajaxInterceptor) {
        this.ajaxInterceptor = ajaxInterceptor;
    }
    
    public void setUserHolder(UserHolder userHolder) {
        this.userHolder = userHolder;
    }
    
    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
    
    public void setProtectedPaths(List<String> protectedPaths) {
        this.protectedPaths = protectedPaths;
    }
}
