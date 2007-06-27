package org.springmodules.feedxt.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springmodules.feedxt.domain.FeedSubscription;
import org.springmodules.feedxt.domain.FeedSubscriptionImpl;
import org.springmodules.feedxt.domain.User;
import org.springmodules.feedxt.domain.support.SubscriptionAlreadyExistentException;
import org.springmodules.feedxt.service.UserService;
import org.springmodules.feedxt.web.controller.support.UserHolder;
import org.springmodules.feedxt.web.view.AddFeedSubscriptionView;
import org.springmodules.web.servlet.mvc.EnhancedSimpleFormController;
import org.springmodules.xt.ajax.web.servlet.AjaxModelAndView;
import org.springmodules.xt.model.generator.factory.DynamicFactoryGenerator;

/**
 * @author Sergio Bossa
 */
public class AddSubscriptionController extends EnhancedSimpleFormController {
    
    private DynamicFactoryGenerator<AddFeedSubscriptionView, FeedSubscriptionImpl> generator;
    
    private UserService userService;
    private UserHolder userHolder;
    
    public AddSubscriptionController() {
        this.generator = new DynamicFactoryGenerator<AddFeedSubscriptionView, FeedSubscriptionImpl>(AddFeedSubscriptionView.class, FeedSubscriptionImpl.class);
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setUserHolder(UserHolder userHolder) {
        this.userHolder = userHolder;
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        return generator.generate();
    }

    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        AddFeedSubscriptionView factoryView = (AddFeedSubscriptionView) command;
        FeedSubscription subscription = factoryView.makeFeedSubscription();
        User user = this.userHolder.getUser();
        try {
            this.userService.subscribeToFeed(user, subscription);
        } catch (SubscriptionAlreadyExistentException ex) {
            errors.reject("subscription.duplicated.name", "Subscription with the same name already existent.");
            return new AjaxModelAndView(null, errors);
        }
        return new AjaxModelAndView(this.getSuccessView(), errors);
    }
}
