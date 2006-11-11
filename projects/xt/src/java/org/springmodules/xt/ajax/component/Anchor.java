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

package org.springmodules.xt.ajax.component;

import java.util.HashMap;
import java.util.Map;
import org.springmodules.xt.ajax.component.support.ComponentUtils;

/**
 * Component representing an HTML anchor (a link).
 *
 * @author Sergio Bossa
 */
public class Anchor implements Component {
    
    private String link;
    private Component content;
    private Map<String, String> attributes = new HashMap<String, String>();
    
    /**
     * Construct the component.
     * @param link The anchor link.
     */
    public Anchor(String link) {
        this.link = link;
    }
    
    /**
     * Construct the component.
     * @param link The anchor link.
     * @param content The anchor content.
     */
    public Anchor(String link, Component content) {
        this.link = link;
        this.content = content;
    }
    
    /**
     * Add a generic attribute.
     * @param name The attribute name.
     * @param value The attribute value.
     */
    public void addAttribute(String name, String value) {
        this.attributes.put(name, value);
    }
    
    public String render() {
        StringBuilder response = new StringBuilder("<a href=\"");
        response.append(this.link).append("\"");
        if (!this.attributes.isEmpty()) {
            ComponentUtils.appendAsAttributes(this.attributes, response);
        }
        response.append(">");
        
        if (this.content != null) {
            response.append(this.content.render());
        }
        
        response.append("</a>");
        
        return response.toString();
    }
}
