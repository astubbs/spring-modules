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
 * Component implementing an HTML table row, containing {@link TableData} objects.
 *
 * @author Sergio Bossa
 */
public class TableRow implements Component {
    
    private List<TableData> tableDataList = new LinkedList<TableData>();
    private Map<String, String> attributes = new HashMap<String, String>();
    
    /**
     * Construct the component (an empty row).
     */
    public TableRow() {
    }
    
    /**
     * Construct the component.
     *
     * @param tableDataList The list of {@link TableData} (columns) to render into this row.
     */
    public TableRow(List<TableData> tableDataList) {
        this.tableDataList = tableDataList;
    }
    
    /**
     * Construct the component.
     *
     * @param rowObject The object containing data to render in this row.
     * @param properties The object properties whose values will be rendered into this row, in the same order as they are listed.<br>
     * Property names adhere to the JavaBeans convention: so, for each property there must be a corresponding getter method in the object.
     * @param textRenderingCallback A callback to implement for rendering each object property value as a specific component: if <code>null</code>,
     * the object property value will be rendered as simple text.
     */
    public TableRow(Object rowObject, String[] properties, TextRenderingCallback textRenderingCallback) {
        for (int i = 0; i < properties.length; i++) {
            TableData data = new TableData(rowObject, properties[i], textRenderingCallback);
            this.tableDataList.add(data);
        }
    }
    
    /**
     * Add a {@link TableData} to this row.
     *
     * @param data The table data to add.
     */
    public void addTableData(TableData data) {
        this.tableDataList.add(data);
    }
    
    /**
     * Add a generic attribute to this row.
     *
     * @param name The attribute name.
     * @param value The attribute value.
     */
    public void addAttribute(String name, String value) {
        this.attributes.put(name, value);
    }
    
    public String render() {
        StringBuilder response = new StringBuilder();
        
        response.append("<tr");
        if (!this.attributes.isEmpty()) {
            ComponentUtils.appendAsAttributes(this.attributes, response);
        }
        response.append(">");
        
        for (TableData td : this.tableDataList) {
            response.append(td.render());
        }
        
        response.append("</tr>");
        
        return response.toString();
    }
}
