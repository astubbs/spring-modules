/*
 * Copyright 2006 the original author or authors.
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
package org.springmodules.xt.ajax.action;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.springframework.web.servlet.ModelAndView;
import org.springmodules.xt.ajax.AjaxAction;

/**
 * Ajax action for redirecting to a given URL.
 *
 * @author Sergio Bossa
 */
public class RedirectAction implements AjaxAction {
    
    private static final long serialVersionUID = 26L;
    
    private static final String OPEN = "<redirect>";
    private static final String CLOSE = "</redirect>";
    
    private StringBuilder redirectUrl;
    private Map model;
    
    /** 
     * Construct the action.
     * @param url The redirect url.
     * @param model The view model as a {@link org.springframework.web.servlet.ModelAndView} object, 
     * containing parameters to pass to the new view. 
     */
    public RedirectAction(String url, ModelAndView model) {
        this.redirectUrl = new StringBuilder(url);
        if (model != null && model.getModel() != null) {
            this.model = new HashMap(model.getModel());
        }
        else {
            this.model = new HashMap(1);
        }
    }
    
    /** 
     * Construct the action.
     * @param url The redirect url.
     * @param model The view model as a map object, containing parameters to pass to the new view. 
     */
    public RedirectAction(String url, Map model) {
        this.redirectUrl = new StringBuilder(url);
        if (model != null) {
            this.model = model;
        }
        else {
            this.model= new HashMap(1);
        }
    }
    
    public String render() {
        try {
            this.appendQueryProperties(this.model, "UTF-8");
        }
        catch(UnsupportedEncodingException ex) {
            // FIXME : Unexpected ....
            throw new RuntimeException("Unexpected", ex);
        }
        StringBuilder response = new StringBuilder();
        response.append(OPEN)
        .append("<content>")
        .append("<target ")
        .append("url=").append('"').append(this.redirectUrl.toString()).append('"')
        .append("/>")
        .append("</content>")
        .append(CLOSE);
        return response.toString();
    }
    
    /**
     * Append query properties to the redirect URL.
     * Stringifies, URL-encodes and formats model attributes as query properties.
     */
    protected void appendQueryProperties(Map model, String encodingScheme)
    throws UnsupportedEncodingException {
        // if there are not already some parameters, we need a ?
        boolean first = (this.redirectUrl.toString().indexOf('?') < 0);
        Iterator entries = model.entrySet().iterator();
        while (entries.hasNext()) {
            if (first) {
                this.redirectUrl.append('?');
                first = false;
            }
            else {
                this.redirectUrl.append("&amp;");
            }
            Map.Entry entry = (Map.Entry) entries.next();
            String encodedKey = URLEncoder.encode(entry.getKey().toString(), encodingScheme);
            String encodedValue = (entry.getValue() != null ? URLEncoder.encode(entry.getValue().toString(), encodingScheme) : "");
            this.redirectUrl.append(encodedKey).append('=').append(encodedValue);
        }
    }
}
