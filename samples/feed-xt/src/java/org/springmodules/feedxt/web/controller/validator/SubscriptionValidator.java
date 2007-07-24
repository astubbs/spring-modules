package org.springmodules.feedxt.web.controller.validator;

import java.net.MalformedURLException;
import java.net.URL;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springmodules.feedxt.web.view.AddFeedSubscriptionView;

/**
 * @author Sergio Bossa
 */
public class SubscriptionValidator implements Validator { 
        
    public boolean supports(Class aClass) {
        return AddFeedSubscriptionView.class.isAssignableFrom(aClass);
    }

    public void validate(Object object, Errors errors) {
        AddFeedSubscriptionView view = (AddFeedSubscriptionView) object;
        if (view.getName() == null || view.getName().equals("")) {
            errors.rejectValue("name", "subscription.empty.name", "Empty name.");
        }
        if (view.getFeedUrl() == null) {
            errors.rejectValue("feedUrl", "subscription.empty.url", "Empty feed URL.");
        } else {
            try {
                URL url = new URL(view.getFeedUrl());
            } catch (MalformedURLException ex) {
                errors.rejectValue("feedUrl", "subscription.malformed.url", "Malformed URL.");
            }
        }
    }
}
