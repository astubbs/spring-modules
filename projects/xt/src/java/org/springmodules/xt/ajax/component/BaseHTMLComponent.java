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

/**
 * Abstract class helping the implementation of HTML components.<br>
 * Subclasses will have to provide only the component HTML tag name (see {@link #getTagName()})
 * and the rendering implementation of the HTML body (see {@link #renderBody()}).
 *
 * @author Sergio Bossa
 * @author Peter Bona
 */
public abstract class BaseHTMLComponent implements Component {
    
    private Map<String, String> attributes = new HashMap<String, String>();
    
    /**
     * Construct an HTML component.
     */
    public BaseHTMLComponent() {
    }
    
    /**
     * Add a generic attribute.
     *
     * @param name The attribute name.
     * @param value The attribute value.
     */
    final public void addAttribute(String name, String value) {
        this.attributes.put(name, value);
    }
    
    /**
     * Render the start and end tags of the HTML component, delegating the
     * rendering of the body to the {@link #renderBody()} method..
     */
    final public String render() {
        StringBuilder response = new StringBuilder();
        
        response.append("<");
        response.append(this.getTagName());
        if (! this.attributes.isEmpty()) {
            this.renderAttributes(this.attributes, response);
        }
        response.append(">");
        
        response.append(this.renderBody());
        
        response.append("</");
        response.append(this.getTagName());
        response.append(">");
        
        return response.toString();
    }
    
    protected void renderAttributes(Map<String, String> attributes, StringBuilder component) {
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            component.append(" ")
            .append(entry.getKey())
            .append("=\"")
            .append(entry.getValue().replaceAll("\\\"", "&quot;"))
            .append("\"");
        }
    }
    
    /**
     * Get the component tag name.
     *
     * @return The component tag name.
     */
    abstract protected String getTagName();
    
    /**
     * Render the component HTML body.
     *
     * @return The component HTML body.
     */
    abstract protected String renderBody();
}
