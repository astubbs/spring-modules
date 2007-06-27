package org.springmodules.feedxt.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springmodules.feedxt.domain.User;
import org.springmodules.feedxt.domain.UserImpl;
import org.springmodules.feedxt.domain.repository.UserRepository;
import org.springmodules.feedxt.domain.support.UserAlreadyExistentException;
import org.springmodules.feedxt.event.LoginEvent;
import org.springmodules.feedxt.service.UserService;
import org.springmodules.feedxt.web.controller.support.UserHolder;
import org.springmodules.feedxt.web.view.SignUpUserView;
import org.springmodules.web.servlet.mvc.EnhancedSimpleFormController;
import org.springmodules.xt.ajax.web.servlet.AjaxModelAndView;
import org.springmodules.xt.model.generator.factory.DynamicFactoryGenerator;

/**
 * @author Sergio Bossa
 */
public class SignUpController extends EnhancedSimpleFormController implements ApplicationEventPublisherAware {
    
    private DynamicFactoryGenerator<SignUpUserView, UserImpl> generator;
    
    private UserService userService;
    private UserRepository userRepository;
    private UserHolder userHolder;
    
    private ApplicationEventPublisher applicationEventPublisher;

    public SignUpController() {
        this.generator = new DynamicFactoryGenerator<SignUpUserView, UserImpl>(SignUpUserView.class, UserImpl.class);
    }
    
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setUserHolder(UserHolder userHolder) {
        this.userHolder = userHolder;
    }

    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        SignUpUserView factoryView = this.generator.generate();
        factoryView.setUserRepository(this.userRepository);
        return factoryView;
    }

    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        SignUpUserView factoryView = (SignUpUserView) command;
        User user = (User) factoryView.makeUser();
        try {
            this.userService.signUpUserAccount(user, factoryView.getUsername(), factoryView.getPassword());
            this.userHolder.setUser(user);
            this.applicationEventPublisher.publishEvent(new LoginEvent(this, request.getSession(), user));
        } catch (UserAlreadyExistentException ex) {
            errors.reject("user.duplicated.username", "Username already existent.");
            return new AjaxModelAndView(null, errors);
        }
        return new AjaxModelAndView(this.getSuccessView(), errors);
    }
}
