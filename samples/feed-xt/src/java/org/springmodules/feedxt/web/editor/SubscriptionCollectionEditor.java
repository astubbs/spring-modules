package org.springmodules.feedxt.web.editor;

import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springmodules.feedxt.domain.FeedSubscription;
import org.springmodules.feedxt.domain.User;
import org.springmodules.feedxt.domain.support.UserNotExistentException;
import org.springmodules.feedxt.service.UserService;
import org.springmodules.feedxt.web.controller.support.UserHolder;

/**
 * @author Sergio Bossa
 */
public class SubscriptionCollectionEditor extends CustomCollectionEditor {
    
    private UserHolder userHolder;
    private UserService userService;
    
    public SubscriptionCollectionEditor(Class collection) {
        super(collection);
    }
    
    public SubscriptionCollectionEditor(Class collection, boolean nullAsEmptyCollection) {
        super(collection, nullAsEmptyCollection);
    }

    public void setUserHolder(UserHolder userHolder) {
        this.userHolder = userHolder;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    protected Object convertElement(Object element) {
        User user = this.userHolder.getUser();
        Object result = null;
        
        // Convert from string to subscription:
        if (element instanceof String) {
            if (user != null) {
                try {
                    result = this.userService.getUserSubscriptionByName(user, (String) element);
                } catch (UserNotExistentException ex) {
                    throw new IllegalStateException("User not found: " + user, ex);
                }
            }
        }
        // Convert from subscription to string:
        else if (element instanceof FeedSubscription) {
            FeedSubscription s = (FeedSubscription) element;
            result = s.getName();
        }
        
        return result;
    }
    
    protected boolean alwaysCreateNewCollection() {
        return true;
    }
}
