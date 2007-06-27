package org.springmodules.feedxt.domain;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Simple feed representation.
 *
 * @author Sergio Bossa
 */
public class Feed {
    
    private String title;
    private String author;
    private Date publishedDate;
    private List<Entry> entries = new LinkedList<Entry>();
    
    public Feed(String title, String author, Date publishedDate, List<Entry> entries) {
        this.title = title;
        this.author = author;
        this.publishedDate = publishedDate;
        if (entries != null) {
            this.entries = entries;
        }
    }

    public String getTitle() {
        return this.title;
    }

    public String getAuthor() {
        return this.author;
    }
    
    public Date getPublishedDate() {
        return this.publishedDate;
    }

    public List<Entry> getEntries() {
        return this.entries;
    }
}
