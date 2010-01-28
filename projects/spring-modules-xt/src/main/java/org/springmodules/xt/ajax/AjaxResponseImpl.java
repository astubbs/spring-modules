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

package org.springmodules.xt.ajax;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Default implementation of {@link AjaxResponse}.
 *
 * @author Sergio Bossa
 */
public class AjaxResponseImpl implements AjaxResponse {
    
    private static final long serialVersionUID = 26L;
    
    private static final String ENCODING_PLACEHOLDER = AjaxResponseImpl.class + ".ENCODING_PLACEHOLDER";
    private static final String OPEN_RESPONSE = "<?xml version=\"1.0\" encoding=\"" + ENCODING_PLACEHOLDER + "\"?>\n<?xml-stylesheet type=\"text/css\" href=\"\"?>\n<ajax-response xml:space=\"preserve\">\n";
    private static final String CLOSE_RESPONSE = "\n</ajax-response>";
    
    public static final String DEFAULT_ENCODING = "ISO-8859-1";
    
    private List<AjaxAction> actions = new LinkedList<AjaxAction>();
    private String encoding;
    
    /**
     * Construct an AjaxResponse with a default response encoding of ISO-8859-1.
     */
    public AjaxResponseImpl() {
        this.encoding = DEFAULT_ENCODING;
    }
    
    /**
     * Construct an AjaxResponse with a given response encoding.
     */
    public AjaxResponseImpl(String encoding) {
        this.encoding = encoding;
    }
    
    public void addAction(AjaxAction action) {
        this.actions.add(action);
    }
    
    public boolean isEmpty() {
        return this.actions.isEmpty();
    }

    public String getEncoding() {
        return this.encoding;
    }

    public String render() {
        StringBuilder response = new StringBuilder(OPEN_RESPONSE.replaceFirst(ENCODING_PLACEHOLDER, this.encoding));
        for (Iterator it = actions.iterator(); it.hasNext(); ) {
            AjaxAction action = (AjaxAction) it.next();
            response.append(action.render());
        }
        response.append(CLOSE_RESPONSE);
        return response.toString();
    }
}
