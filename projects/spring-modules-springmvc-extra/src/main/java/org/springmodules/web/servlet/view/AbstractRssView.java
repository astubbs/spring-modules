/*
 * Copyright 2005 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springmodules.web.servlet.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.RequestUtils;
import org.springframework.web.servlet.view.AbstractView;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.SyndFeedOutput;

/**
 * Abstract superclass for rss views, using the
 * <a href="http://wiki.java.net/bin/view/Javawsxml/Rome">Rome</a> Library.
 * This library is created by Alejandro Abdelnur, Patrick Chanezon and
 * Elaine Chien. By default this class uses the atom_1.0 rss version, but
 * if you specify a parameter 'type' with another rss version that is supported
 * by Rome, it will create an rss feed based on that version. You can also change
 * the deafult type via the appropriate setter.
 *
 * You need to add two libraries to make use of this class:
 * - rome.jar (version 0.8)
 * - jdom.jar (version 1.0)
 *
 * Currently j2se1.4+ is also required.
 *
 * @author Jettro Coenradie (original author)
 * @author Sergio Bossa (maintainer)
 */
public abstract class AbstractRssView extends AbstractView {
    private static final String DEFAULT_FEED_TYPE = "atom_1.0";
    private static final String FEED_TYPE = "type";
    
    private String defaultFeedType;
    private String baseUrl;
    
    /**
     * This constructor sets the appropriate content type "application/xml; charset=UTF-8".
     */
    public AbstractRssView() {
        setContentType("application/xml; charset=UTF-8");
        setDefaultFeedType(DEFAULT_FEED_TYPE);
    }
    
        /* (non-Javadoc)
         * @see org.springframework.web.servlet.view.AbstractView#renderMergedOutputModel(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
         */
    protected final void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response)
    throws Exception {
        setBaseUrl(request);
        
        SyndFeed feed = newSyndFeed();
        buildFeed(model,request,response,feed);
        
        String feedType = RequestUtils.getStringParameter(request,FEED_TYPE);
        feedType = (feedType!=null) ? feedType : getDefaultFeedType();
        feed.setFeedType(feedType);
        
        response.setContentType(getContentType());
        SyndFeedOutput output = new SyndFeedOutput();
        output.output(feed,response.getWriter());
    }
    
    /**
     * This method must be implemented by your subclass and must create a
     * <code>com.sun.syndication.feed.synd.SyndFeed</code>
     * @param model the model Map
     * @param request in case we need locale etc. Shouldn't look at attributes.
     * @param respons in case we need to set cookies. Shouldn't write to it.
     * @param feed feed to be filled with data.
     * @throws Exception any exception that occured during the creation of the feed
     */
    abstract protected void buildFeed(Map model, HttpServletRequest request, HttpServletResponse respons, SyndFeed feed)
    throws Exception;
    
    /**
     * Creates a new instance of a SyndFeed, you can override this method to return
     * your own instance of a SyndFeed. Default a <code>com.sun.syndication.feed.synd.SyndFeedImpl</code>
     * is returned.
     * @return new instance of a SyndFeed
     * @see com.lowagie.text.Document
     */
    protected SyndFeed newSyndFeed() {
        return new SyndFeedImpl();
    }
    
    /**
     * Returns the base url of the server the rss in running on, is used to create
     * the url where the rss items link to.
     * @return String with the baseUrl.
     */
    protected String getBaseUrl() {
        return this.baseUrl;
    }
    
    /**
     * Sets the baseUrl based on parameters obtained from the request. This method is
     * called by renderMergedOutputModel and can be overridden.
     * @param request used to obtain data from the running server.
     */
    protected void setBaseUrl(HttpServletRequest request) {
        StringBuffer sb = new StringBuffer();
        sb.append("http://");
        sb.append(request.getServerName());
        sb.append(":");
        sb.append(request.getServerPort());
        sb.append(request.getContextPath());
        sb.append("/");
        this.baseUrl = sb.toString();
    }
    
    /* Getters and setter */
    public String getDefaultFeedType() {
        return defaultFeedType;
    }
    
    public void setDefaultFeedType(String defaultFeedType) {
        this.defaultFeedType = defaultFeedType;
    }
}

