package org.springmodules.feedxt.infrastructure.dao.db4o;

import com.db4o.ObjectContainer;
import com.db4o.config.ObjectConstructor;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.log4j.Logger;

/**
 * @author Sergio Bossa
 */
public class UrlConstructor implements ObjectConstructor {
    
    private static final Logger logger = Logger.getLogger(UrlConstructor.class);

    public Object onInstantiate(ObjectContainer objectContainer, Object storedObject) {
        String urlString = (String) storedObject;
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException ex) {
            logger.warn("Cannot create URL: " + urlString, ex);
        }
        logger.debug(new StringBuilder("Created URL object: ").append(url).toString());
        return url;
    }

    public Object onStore(ObjectContainer objectContainer, Object object) {
        URL url = (URL) object;
        String urlString = url.toString();
        logger.debug(new StringBuilder("Stored URL object: ").append(urlString).toString());
        return urlString;
    }

    public void onActivate(ObjectContainer objectContainer, Object appObject, Object storedObject) {
    }

    public Class storedClass() {
        return String.class;
    }
}
