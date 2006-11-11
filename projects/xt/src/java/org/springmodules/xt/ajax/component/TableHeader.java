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
 * Component implementing a table header.
 *
 * @author Sergio Bossa
 */
public class TableHeader implements Component {
    
    private String[] headers;
    private Map<String, String> rowAttributes = new HashMap<String, String>();
    private Map<String, String> columnAttributes = new HashMap<String, String>();
    
    /**
     * Construct the component.
     *
     * @param columns An array of strings representing headers text.
     */
    public TableHeader(String[] headers) {
        this.headers = headers;
    }
    
    /**
     * Add a generic attribute to the header row.
     * @param name The attribute name.
     * @param value The attribute value.
     */
    public void addRowAttribute(String name, String value) {
        this.rowAttributes.put(name, value);
    }
    
    /**
     * Add a generic attribute to every header column.
     * @param name The attribute name.
     * @param value The attribute value.
     */
    public void addColumnAttribute(String name, String value) {
        this.columnAttributes.put(name, value);
    }
    
    public String render() {
        StringBuilder row = new StringBuilder("<tr");
        
        if (!this.rowAttributes.isEmpty()) {
            ComponentUtils.appendAsAttributes(this.rowAttributes, row);
        }
        row.append(">");
        
        for (int i = 0; i < this.headers.length; i++) {
            StringBuilder column = new StringBuilder("<th");
            if (!this.columnAttributes.isEmpty()) {
                ComponentUtils.appendAsAttributes(this.columnAttributes, column);
            }
            column.append(">");
            column.append(this.headers[i]);
            column.append("</th>");
            row.append(column);
        }
        row.append("</tr>");
        
        return row.toString();
    }
}
