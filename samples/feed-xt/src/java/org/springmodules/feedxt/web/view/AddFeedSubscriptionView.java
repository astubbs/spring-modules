package org.springmodules.feedxt.web.view;

import org.springmodules.feedxt.domain.FeedSubscription;
import org.springmodules.xt.model.generator.annotation.ConstructorArg;
import org.springmodules.xt.model.generator.annotation.FactoryMethod;

/**
 * {@link org.springmodules.feedxt.domain.FeedSubscription} factory view to
 * use for adding subscriptions.
 *
 * @author Sergio Bossa
 */
public interface AddFeedSubscriptionView {
    
    @ConstructorArg(position=0)
    public void setName(String name);
    
    @ConstructorArg(position=1)
    public void setFeedUrl(String url);
    
    public String getName();
    
    public String getFeedUrl();
    
    @FactoryMethod()
    public FeedSubscription makeFeedSubscription();
}
