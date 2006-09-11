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
 * Taconite based ajax action for redirecting to a given URL.
 *
 * @author Sergio Bossa
 */
public class RedirectAction implements AjaxAction {
    
    private static final String OPEN = new String("<taconite-redirect targetUrl=\"$1\" parseInBrowser=\"true\">");
    private static final String CLOSE = new String("</taconite-redirect>");
    
    private StringBuilder targetUrl;
    private Map model;
    
    /** 
     * Construct the action.
     * @param url The target url.
     * @param model The view model, containing parameters to pass to the new view. 
     */
    public RedirectAction(String url, ModelAndView model) {
        this.targetUrl = new StringBuilder(url);
        if (model.getModel() != null) {
            this.model= new HashMap(model.getModel());
        }
        else {
            this.model= new HashMap(1);
        }
    }
    
    public String execute() {
        try {
            this.appendQueryProperties(this.targetUrl, this.model, "UTF-8");
        }
        catch(UnsupportedEncodingException ex) {
            // FIXME : Unexpected ....
            throw new RuntimeException("Unexpected", ex);
        }
        StringBuilder response = new StringBuilder(OPEN.replaceFirst("\\$1", this.targetUrl.toString()));
        response.append(CLOSE);
        return response.toString();
    }
    
    /**
     * Append query properties to the redirect URL.
     * Stringifies, URL-encodes and formats model attributes as query properties.
     */
    protected void appendQueryProperties(StringBuilder targetUrl, Map model, String encodingScheme)
    throws UnsupportedEncodingException {
        // if there are not already some parameters, we need a ?
        boolean first = (this.targetUrl.toString().indexOf('?') < 0);
        Iterator entries = model.entrySet().iterator();
        while (entries.hasNext()) {
            if (first) {
                targetUrl.append('?');
                first = false;
            }
            else {
                targetUrl.append("&amp;");
            }
            Map.Entry entry = (Map.Entry) entries.next();
            String encodedKey = URLEncoder.encode(entry.getKey().toString(), encodingScheme);
            String encodedValue =
                (entry.getValue() != null ? URLEncoder.encode(entry.getValue().toString(), encodingScheme) : "");
            targetUrl.append(encodedKey).append('=').append(encodedValue);
        }
    }
}
