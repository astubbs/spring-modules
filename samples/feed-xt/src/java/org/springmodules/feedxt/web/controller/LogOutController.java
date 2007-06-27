package org.springmodules.feedxt.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springmodules.feedxt.event.LogoutEvent;
import org.springmodules.feedxt.web.controller.support.UserHolder;

/**
 * @author Sergio Bossa
 */
public class LogOutController extends AbstractController implements ApplicationEventPublisherAware {
    
    private UserHolder userHolder;
    private String logOutView;
    
    private ApplicationEventPublisher applicationEventPublisher;
    
    public void setUserHolder(UserHolder userHolder) {
        this.userHolder = userHolder;
    }
    
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
    
    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        this.applicationEventPublisher.publishEvent(new LogoutEvent(this, httpServletRequest.getSession(), this.userHolder.getUser()));
        
        this.userHolder.setUser(null);
        httpServletRequest.getSession().invalidate();
        return new ModelAndView(this.logOutView);
    }
    
    public void setLogOutView(String logOutView) {
        this.logOutView = logOutView;
    }
}
