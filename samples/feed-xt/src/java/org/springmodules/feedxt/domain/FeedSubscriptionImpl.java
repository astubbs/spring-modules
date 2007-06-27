package org.springmodules.feedxt.domain;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.DOMOutputter;
import org.jdom.output.XMLOutputter;
import org.springmodules.feedxt.domain.support.CannotAccessFeedException;

/**
 * {@link FeedSubscription} implementation.
 *
 * @author Sergio Bossa
 */
public class FeedSubscriptionImpl implements FeedSubscription {
    
    private static final Logger logger = Logger.getLogger(FeedSubscriptionImpl.class);
    
    private String name;
    private URL url;
    private Date lastAccess;
    
    public FeedSubscriptionImpl(String name, String url) throws MalformedURLException {
        this.name = name;
        this.url = new URL(url);
    }
    
    protected FeedSubscriptionImpl() {}
    
    public String getName() {
        return this.name;
    }
    
    public URL getUrl() {
        return this.url;
    }
    
    public Feed getFeed() throws CannotAccessFeedException {
        try {
            InputStream feedStream = this.url.openStream();
            SAXBuilder builder = new SAXBuilder();
            Document feedDocument = builder.build(feedStream);
            
            if (logger.isDebugEnabled()) {
                XMLOutputter logOutputter = new XMLOutputter();
                StringWriter writer = new StringWriter();
                logOutputter.output(feedDocument, writer);
                writer.flush();
                writer.close();
                logger.debug(writer.toString());
            }
            
            DOMOutputter outputter = new DOMOutputter();
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed romeFeed = input.build(outputter.output(feedDocument));
            Feed feed = this.makeFeed(romeFeed);
            
            this.lastAccess = new Date();
            
            return feed;
        } catch (IllegalArgumentException ex) {
            throw new CannotAccessFeedException("Unable to access feed at: " + this.url, ex);
        } catch (IOException ex) {
            throw new CannotAccessFeedException("Unable to access feed at: " + this.url, ex);
        } catch (FeedException ex) {
            throw new CannotAccessFeedException("Unable to access feed at: " + this.url, ex);
        } catch (JDOMException ex) {
            throw new CannotAccessFeedException("Unable to access feed at: " + this.url, ex);
        }
    }

    public Date getLastAccess() {
        return new Date(this.lastAccess.getTime());
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (!(obj instanceof FeedSubscriptionImpl)) {
            return false;
            
        } else {
            FeedSubscriptionImpl other = (FeedSubscriptionImpl) obj;
            return new EqualsBuilder()
            .append(this.name, other.name)
            .isEquals();
        }
    }
    
    public int hashCode() {
        return new HashCodeBuilder()
        .append(this.name)
        .toHashCode();
    }
    
    public String toString() {
        return new StringBuilder(this.name).append("@").append(this.url).toString();
    }
    
    private Feed makeFeed(SyndFeed romeFeed) {
        String title = romeFeed.getTitle();
        String author = romeFeed.getAuthor();
        Date publishedDate = romeFeed.getPublishedDate();
        List<SyndEntry> romeEntries = romeFeed.getEntries();
        List<Entry> entries = new ArrayList<Entry>(romeEntries.size());
        for (SyndEntry current : romeEntries) {
            Entry entry = this.makeEntry(current);
            entries.add(entry);
        }
        return new Feed(title, author, publishedDate, entries);
    }
    
    private Entry makeEntry(SyndEntry romeEntry) {
        String title = romeEntry.getTitle();
        Date publishedDate = romeEntry.getPublishedDate();
        Date updatedDate = romeEntry.getUpdatedDate();
        String link = romeEntry.getLink();
        List<SyndContent> contents = romeEntry.getContents();
        List<String> values = new ArrayList<String>(contents.size());
        for (SyndContent current : contents) {
            String value = new StringBuilder("<div>").append(current.getValue()).append("</div>").toString();
            SAXBuilder builder = new SAXBuilder();
            try {
                Document entryTest = builder.build(new StringReader(value));
            } catch (Exception ex) {
                logger.warn(ex.getMessage());
                value = null;
            }
            values.add(value);
        }
        return new Entry(title, publishedDate, updatedDate, link, values);
    }
}
