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
import java.util.List;
import java.util.Map;
import org.springmodules.xt.ajax.component.support.ComponentUtils;

/**
 * Component implementing an HTML table.
 *
 * @author Sergio Bossa
 */
public class Table implements Component {
    
    private TableHeader tableHeader;
    private List<TableRow> tableRowList = new LinkedList();
    private Map<String, String> tableAttributes = new HashMap();
    private Map<String, String> tableHeaderAttributes = new HashMap();
    private Map<String, String> tableBodyAttributes = new HashMap();
    
    /**
     * Construct an empty table.
     */
    public Table() {
    }
    
    /**
     * Construct a table with header.
     *
     * @param tableHeader The table header.
     */
    public Table(TableHeader tableHeader) {
        this.tableHeader = tableHeader;
    }
    
    /**
     * Construct a table with rows.
     *
     * @param tableRowList The list of {@link TableRow} (rows) to render into this table.
     */
    public Table(List<TableRow> tableRowList) {
        this.tableRowList = tableRowList;
    }
    
    /**
     * Construct a table with header and rows.
     *
     * @param tableHeader The table header.
     * @param tableRowList The list of {@link TableRow} (rows) to render into this table.
     */
    public Table(TableHeader tableHeader, List<TableRow> tableRowList) {
        this.tableHeader = tableHeader;
        this.tableRowList = tableRowList;
    }
    
    /**
     * Set this table header.
     * @param row The table header.
     */
    public void setTableHeader(TableHeader tableHeader) {
        this.tableHeader = tableHeader;
    }
    
    /**
     * Add a {@link TableRow} to this table.
     * @param row The table row to add.
     */
    public void addTableRow(TableRow row) {
        this.tableRowList.add(row);
    }
    
    /**
     * Add a generic attribute to this table.
     * @param name The attribute name.
     * @param value The attribute value.
     */
    public void addTableAttribute(String name, String value) {
        this.tableAttributes.put(name, value);
    }
    
    /**
     * Add a generic attribute to this table body.
     * @param name The attribute name.
     * @param value The attribute value.
     */
    public void addTableBodyAttribute(String name, String value) {
        this.tableBodyAttributes.put(name, value);
    }
    
    /**
     * Add a generic attribute to this table header.
     * @param name The attribute name.
     * @param value The attribute value.
     */
    public void addTableHeaderAttribute(String name, String value) {
        this.tableHeaderAttributes.put(name, value);
    }
    
    public String render() {
        StringBuilder response = new StringBuilder();
        
        // Open Table
        response.append("<table");
        if (!this.tableAttributes.isEmpty()) {
            ComponentUtils.appendAsAttributes(this.tableAttributes, response);
        }
        response.append(">");
        
        // Header
        response.append("<thead");
        if (!this.tableHeaderAttributes.isEmpty()) {
            ComponentUtils.appendAsAttributes(this.tableHeaderAttributes, response);
        }
        response.append(">");
        
        response.append(this.tableHeader.render());
        
        response.append("</thead>");
        
        // Body
        response.append("<tbody");
        if (!this.tableBodyAttributes.isEmpty()) {
            ComponentUtils.appendAsAttributes(this.tableBodyAttributes, response);
        }
        response.append("> ");
        
        for (TableRow tr : this.tableRowList) {
            response.append(tr.render());
        }
        
        response.append("</tbody>");
        
        // Close Table
        response.append("</table>");
        
        // Response
        return response.toString();
    }
}
