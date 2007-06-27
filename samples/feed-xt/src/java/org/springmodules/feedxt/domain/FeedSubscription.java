package org.springmodules.feedxt.domain;

import java.io.Serializable;
import java.net.URL;
import java.util.Date;
import org.springmodules.feedxt.domain.support.CannotAccessFeedException;

/**
 * Interface representing a subscription to a feed.
 *
 * @author Sergio Bossa
 */
public interface FeedSubscription extends Serializable {
    
    /**
     * Get the user-specified name of this subscription.
     */
    public String getName();
    
    /**
     * Get the subscription URL.
     */
    public URL getUrl();
    
    /**
     * Get the feed content, updating the last access time.
     */
    public Feed getFeed() throws CannotAccessFeedException;
    
    /**
     * Get the last time this subscription has been accessed via the {@link #getFeed()} method.
     */
    public Date getLastAccess();
}
