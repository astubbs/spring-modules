package org.springmodules.feedxt.web.editor;

import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springmodules.feedxt.domain.FeedSubscription;
import org.springmodules.feedxt.domain.User;
import org.springmodules.feedxt.web.controller.support.UserHolder;

/**
 * @author Sergio Bossa
 */
public class SubscriptionCollectionEditor extends CustomCollectionEditor {
    
    private UserHolder userHolder;
    
    public SubscriptionCollectionEditor(Class collection) {
        super(collection);
    }
    
    public SubscriptionCollectionEditor(Class collection, boolean nullAsEmptyCollection) {
        super(collection, nullAsEmptyCollection);
    }

    public void setUserHolder(UserHolder userHolder) {
        this.userHolder = userHolder;
    }

    protected Object convertElement(Object element) {
        User user = this.userHolder.getUser();
        Object result = null;
        
        // Convert from string to subscription:
        if (element instanceof String) {
            if (user != null) {
                result = user.viewSubscriptionByName((String) element);
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
