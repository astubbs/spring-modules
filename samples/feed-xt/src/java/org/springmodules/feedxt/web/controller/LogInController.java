package org.springmodules.feedxt.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springmodules.feedxt.domain.User;
import org.springmodules.feedxt.event.LoginEvent;
import org.springmodules.feedxt.service.UserService;
import org.springmodules.feedxt.web.controller.support.UserHolder;
import org.springmodules.web.servlet.mvc.EnhancedSimpleFormController;
import org.springmodules.xt.ajax.web.servlet.AjaxModelAndView;

/**
 * @author Sergio Bossa
 */
public class LogInController extends EnhancedSimpleFormController implements ApplicationEventPublisherAware {
    
    private UserService userService;
    private UserHolder userHolder;
    
    private ApplicationEventPublisher applicationEventPublisher;
    
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    
    public void setUserHolder(UserHolder userHolder) {
        this.userHolder = userHolder;
    }
    
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
    
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        return new LoginForm();
    }
    
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        LoginForm form = (LoginForm) command;
        String username = form.getUsername();
        String password = form.getPassword();
        User user = this.userService.getUserAccount(username, password);
        if (user != null) {
            this.userHolder.setUser(user);
            this.applicationEventPublisher.publishEvent(new LoginEvent(this, request.getSession(), user));
            return new AjaxModelAndView(this.getSuccessView(), errors);
        } else {
            errors.reject("wrong.login", "Wrong username or password.");
            return new AjaxModelAndView(null, errors);
        }
    }
    
    public class LoginForm {
        
        private String username;
        private String password;
        
        public String getUsername() {
            return username;
        }
        
        public void setUsername(String username) {
            this.username = username;
        }
        
        public String getPassword() {
            return password;
        }
        
        public void setPassword(String password) {
            this.password = password;
        }
    }
}
