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

import java.util.List;

/**
 * Component implementing a table header.
 *
 * @author Sergio Bossa
 */
public class TableHeader extends SimpleHTMLComponent {
    
    private static final long serialVersionUID = 26L;
    
    /**
     * Construct the component.
     *
     * @param headers An array of strings representing headers text.
     */
    public TableHeader(String[] headers) {
        for (String header : headers) {
            this.internalAddContent(new TableHeaderData(new SimpleText(header)));
        }
    }
    
    /**
     * Construct the component.
     *
     * @param headers An array of {@link TableHeaderData} objects that will render the headers.
     */
    public TableHeader(List<TableHeaderData> headers) {
        for (TableHeaderData data : headers) {
            this.internalAddContent(data);
        }
    }
    
    /**
     * Add a generic attribute to the header row.<br>
     * Same as {@link #addAttribute(String, String)}.
     *
     * @param name The attribute name.
     * @param value The attribute value.
     */
    public void addRowAttribute(String name, String value) {
        this.addAttribute(name, value);
    }
    
    /**
     * Add a generic attribute to all header columns.
     *
     * @param name The attribute name.
     * @param value The attribute value.
     */
    public void addColumnAttribute(String name, String value) {
        for (Component c : this.internalGetContents()) {
            TableHeaderData data = (TableHeaderData) c;
            data.addAttribute(name, value);
        }
    }
    
    /**
     * Add a {@link TableHeaderData} to this header row.
     *
     * @param data The table header data to add.
     */
    public void addTableHeaderData(TableHeaderData data) {
        this.internalAddContent(data);
    }
    
    protected String getTagName() {
        return "tr";
    }
}
