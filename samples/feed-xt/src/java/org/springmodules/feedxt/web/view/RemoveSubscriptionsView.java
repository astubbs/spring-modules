package org.springmodules.feedxt.web.view;

import java.util.List;
import org.springmodules.feedxt.domain.FeedSubscription;

/**
 * {@link org.springmodules.feedxt.domain.User} view to use for removing subscriptions.
 *
 * @author Sergio Bossa
 */
public interface RemoveSubscriptionsView {
    
    public List<FeedSubscription> getSubscriptionsToRemove();
    
    public void setSubscriptionsToRemove(List<FeedSubscription> subscriptions);
    
    public List<FeedSubscription> getSubscriptions();
}
