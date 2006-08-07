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
import org.springmodules.xt.ajax.component.support.RenderingException;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

/**
 * Component implementing a list of HTML table rows.
 *
 * @author Sergio Bossa
 */
@Deprecated
public class RowList implements Component {
    
    private Object[] rows;
    private String[] columns;
    private Map<Integer, Component[]> componentsRows = new HashMap();
    private Map<String, String> rowAttributes = new HashMap();
    private Map<String, String> columnAttributes = new HashMap();
    private TextRenderingCallback columnContentRendering;
    
    /**
     * Construct the component.
     * 
     * @param rows An array of objects containing data to put in the table: each object represents a row.
     * @param columns An array of strings, each representing the name of a property in the objects contained in the array above,
     * whose value will be the value of the column.<br>
     * Property names adhere to the JavaBeans convention: so, for each property there must be a corresponding getter method in the objects 
     * representing the rows.
     */
    public RowList(Object[] rows, String[] columns) {
        this.rows = rows;
        this.columns = columns;
    }
    
    /**
     * Add a generic attribute to every rendered row.
     * @param name The attribute name.
     * @param value The attribute value.
     */
    public void addRowAttribute(String name, String value) {
        this.rowAttributes.put(name, value);
    }
    
    /**
     * Add a generic attribute to every rendered column.
     * @param name The attribute name.
     * @param value The attribute value.
     */
    public void addColumnAttribute(String name, String value) {
        this.columnAttributes.put(name, value);
    }
    
    /**
     * Append an array of components to the specified row.<br>
     * The row index <b>must be zero based</b>.
     * @param components The {@link org.springmodules.xt.ajax.component.Component}s to add.
     * @param rowIndex The index of the row where to add the given components (use 0 for the first row).
     */
    public void appendComponentsToRow(Component[] components, Integer rowIndex) {
        this.componentsRows.put(rowIndex, components);
    }
    
    public String render() {
        StringBuilder response = new StringBuilder();
        try {   
            for (int i = 0; i < this.rows.length; i++) {
                StringBuilder row = new StringBuilder("<tr");
                if (!this.rowAttributes.isEmpty()) {
                    for (Map.Entry<String, String> entry : this.rowAttributes.entrySet()) {
                        row.append(" ")
                              .append(entry.getKey())
                              .append("=\"")
                              .append(entry.getValue())
                              .append("\"");
                    }
                }
                row.append(">");
                
                BeanWrapper bean = new BeanWrapperImpl(rows[i]);
                for (int j = 0; j < this.columns.length; j++) {
                    StringBuilder column = new StringBuilder("<td");
                    if (!this.columnAttributes.isEmpty()) {
                        for (Map.Entry<String, String> entry : this.columnAttributes.entrySet()) {
                            column.append(" ")
                                       .append(entry.getKey())
                                       .append("=\"")
                                       .append(entry.getValue())
                                       .append("\"");
                        }
                    }
                    column.append(">");
                    if (this.columnContentRendering == null) {
                        column.append(bean.getPropertyValue(columns[j]).toString());
                    }
                    else {
                        column.append(this.columnContentRendering.getRenderingComponent(bean.getPropertyValue(columns[j]).toString()).render());
                    }
                    column.append("</td>");
                    
                    row.append(column);
                }
                
                Component[] components = componentsRows.get(i);
                if (components != null) {
                    for (int k = 0; k < components.length; k++) {
                    StringBuilder column = new StringBuilder("<td");
                    if (!this.columnAttributes.isEmpty()) {
                            for (Map.Entry<String, String> entry : this.columnAttributes.entrySet()) {
                                column.append(" ")
                                           .append(entry.getKey())
                                           .append("=\"")
                                           .append(entry.getValue())
                                           .append("\"");
                            }
                        }
                        column.append(">");

                        column.append(components[k].render());

                        column.append("</td>");

                        row.append(column);
                    }
                }
                
                row.append("</tr>");
                response.append(row);
            }
        }
        catch(Exception ex) {
            throw new RenderingException("Error while rendering a component of type: " + this.getClass().getName(), ex);
        }
        return response.toString();
    }

    public void setColumnContentRendering(TextRenderingCallback columnContentRendering) {
        this.columnContentRendering = columnContentRendering;
    }
}
