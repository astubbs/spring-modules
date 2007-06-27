package org.springmodules.feedxt.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springmodules.feedxt.domain.FeedSubscription;
import org.springmodules.feedxt.domain.User;
import org.springmodules.feedxt.service.UserService;
import org.springmodules.feedxt.web.controller.support.UserHolder;
import org.springmodules.feedxt.web.view.RemoveSubscriptionsView;
import org.springmodules.web.servlet.mvc.EnhancedSimpleFormController;
import org.springmodules.xt.ajax.web.servlet.AjaxModelAndView;
import org.springmodules.xt.model.introductor.bean.DynamicBeanIntroductor;

/**
 * @author Sergio Bossa
 */
public class RemoveSubscriptionsController extends EnhancedSimpleFormController {
    
    private DynamicBeanIntroductor introductor;
    private UserService userService;
    private UserHolder userHolder;

    public RemoveSubscriptionsController() {
        this.introductor = new DynamicBeanIntroductor();
    }
    
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setUserHolder(UserHolder userHolder) {
        this.userHolder = userHolder;
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        User user = this.userHolder.getUser();
        return this.introductor.introduceInterfaces(user, new Class[]{RemoveSubscriptionsView.class});
    }

    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        RemoveSubscriptionsView view = (RemoveSubscriptionsView) command;
        User user = (User) this.introductor.getTarget(view);
        if (view.getSubscriptionsToRemove() != null) {
            for (FeedSubscription s : view.getSubscriptionsToRemove()) {
                this.userService.removeSubscriptionToFeed(user, s);
            }
        }
        return new AjaxModelAndView(this.getSuccessView(), errors);
    }
}
