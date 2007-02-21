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

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

/**
 * Abstract component implementing HTML table data inside a {@link TableRow} or {@link TableHeader}.
 *
 * @author Sergio Bossa
 */
public abstract class AbstractTableData extends SimpleHTMLComponent {
    
    /**
     * Construct the component.
     *
     * @param data The {@link Component} representing the data to render.
     */
    public AbstractTableData(Component data) {
    	super(data);
    }
    
    /**
     * Construct the component.
     *
     * @param dataObject An object containing data to render.
     * @param property The object property whose value will be this table data.<br>
     * Property name adheres to the JavaBeans convention: so, for the property there must be a corresponding getter method in the object.
     * @param textRenderingCallback A callback to implement for rendering the object property value as a specific component: if <code>null</code>,
     * the object property value will be rendered as simple text.
     */
    public AbstractTableData(Object dataObject, String property, TextRenderingCallback textRenderingCallback) {
        if (dataObject != null) {
            BeanWrapper wrapper = new BeanWrapperImpl(dataObject);
            Object tmp = wrapper.getPropertyValue(property);
            if (tmp != null) {
                String value = tmp.toString();
                if (textRenderingCallback != null) {
                	this.internalAddContent(textRenderingCallback.getRenderingComponent(value));
                }
                else {
                	this.internalAddContent(new SimpleText(value));
                }
            }
            else {
                this.internalAddContent(new SimpleText(""));
            }
        }
        else {
            throw new IllegalArgumentException("The object to render in the table data component cannot be null!");
        }
    }
}
