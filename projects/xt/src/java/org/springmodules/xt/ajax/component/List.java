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
import java.util.LinkedList;
import java.util.Map;
import org.springmodules.xt.ajax.component.support.ComponentUtils;

/**
 * Component implementing an HTML (ordered or unordered) list.
 *
 * @author Sergio Bossa
 */
public class List implements Component {
    
    private List.ListType type;
    private java.util.List<ListItem> items = new LinkedList<ListItem>();
    private Map<String, String> attributes = new HashMap();
    
    /**
     * Construct the component.
     * @param type The list type.
     */
    public List(List.ListType type) {
        this.type = type;
    }
    
    /**
     * Construct the component.
     * @param type The list type.
     * @param items The list of contained items.
     */
    public List(List.ListType type, java.util.List<ListItem> items) {
        this.type = type;
        this.items = items;
    }
    
    /**
     * Add a {@link ListItem}.
     * @param item The item to add.
     */
    public void addItem(ListItem item) {
        this.items.add(item);
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
        StringBuilder response = null; 
        String open = null;
        String close = null;
        
        if (this.type.equals(List.ListType.ORDERED)) {
            open = "<ol";
            close= "</ol>";
        }
        else if (this.type.equals(List.ListType.UNORDERED)) {
            open = "<ul";
            close= "</ul>";
        }        
        
        response = new StringBuilder(open);
        if (!this.attributes.isEmpty()) {
            ComponentUtils.appendAsAttributes(this.attributes, response);
        }
        response.append(">");
        
        for (ListItem li : this.items) {
            response.append(li.render());
        }
        
        response.append(close);
        
        return response.toString();
    }
    
    public enum ListType { ORDERED, UNORDERED };
}
