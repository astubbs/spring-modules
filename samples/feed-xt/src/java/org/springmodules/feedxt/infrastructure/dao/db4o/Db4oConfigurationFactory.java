package org.springmodules.feedxt.infrastructure.dao.db4o;

import com.db4o.Db4o;
import com.db4o.config.Configuration;
import com.db4o.config.ObjectClass;
import java.net.URL;
import org.springmodules.feedxt.domain.FeedSubscriptionImpl;
import org.springmodules.feedxt.domain.UserImpl;

/**
 * Factory for Db4o configuration object.
 *
 * @author Sergio Bossa
 */
public class Db4oConfigurationFactory {
    
    public Configuration getConfiguration() {
        Configuration configuration = Db4o.configure();
        this.configureUser(configuration);
        this.configureFeedSubscription(configuration);
        this.configureOtherObjects(configuration);
        return configuration;
    }
    
    private void configureUser(Configuration configuration) {
        ObjectClass userConfiguration = configuration.objectClass(UserImpl.class);
        userConfiguration.callConstructor(true);
        userConfiguration.objectField("subscriptions").cascadeOnActivate(true);
        userConfiguration.objectField("subscriptions").cascadeOnDelete(true);
        userConfiguration.objectField("subscriptions").cascadeOnUpdate(true);
    }
    
    private void configureFeedSubscription(Configuration configuration) {
        ObjectClass feedConfiguration = configuration.objectClass(FeedSubscriptionImpl.class);
        feedConfiguration.callConstructor(true);
    }

    private void configureOtherObjects(Configuration configuration) {
        ObjectClass urlConfiguration = configuration.objectClass(URL.class);
        urlConfiguration.translate(new UrlConstructor());
        urlConfiguration.cascadeOnActivate(true);
        urlConfiguration.cascadeOnDelete(true);
        urlConfiguration.cascadeOnUpdate(true);
    }
}
