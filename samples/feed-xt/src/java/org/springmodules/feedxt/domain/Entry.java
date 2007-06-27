package org.springmodules.feedxt.domain;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Simple feed entry representation.
 *
 * @author Sergio Bossa
 */
public class Entry {
  
    private String title;
    private Date publishedDate;
    private Date updatedDate;
    private String link;
    private List<String> contents = new LinkedList<String>();
    
    public Entry(String title, Date publishedDate, Date updatedDate, String link, List<String> contents) {
        this.title = title;
        this.publishedDate = publishedDate;
        this.updatedDate = updatedDate;
        this.link = link;
        if (contents != null) {
            this.contents = contents;
        }
    }

    public String getTitle() {
        return this.title;
    }

    public Date getPublishedDate() {
        return this.publishedDate;
    }

    public Date getUpdatedDate() {
        return this.updatedDate;
    }

    public String getLink() {
        return this.link;
    }

    public List<String> getContents() {
        return this.contents;
    }
}
