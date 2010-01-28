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
 * Component implementing an HTML table row, containing {@link TableData} objects.
 *
 * @author Sergio Bossa
 */
public class TableRow extends SimpleHTMLComponent {
    
    private static final long serialVersionUID = 26L;
    
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
        for (TableData data : tableDataList) {
            this.internalAddContent(data);
        }
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
            this.internalAddContent(data);
        }
    }
    
    /**
     * Add a {@link TableData} to this row.
     *
     * @param data The table data to add.
     */
    public void addTableData(TableData data) {
        this.internalAddContent(data);
    }
    
    protected String getTagName() {
        return "tr";
    }
}
