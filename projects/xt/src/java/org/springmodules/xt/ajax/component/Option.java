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
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springmodules.xt.ajax.component.support.ComponentUtils;

/**
 * Component implementing an option element.
 *
 * @author Sergio Bossa
 */
public class Option implements Component {
    
    private String value;
    private String content;
    private boolean selected;
    private Map<String, String> attributes = new HashMap();
    
    /**
     * Construct the option.
     *
     * @param value The option value.
     * @param content The option content.
     */
    public Option(String value, String content) {
        this.value = value;
        this.content = content;
    }
    
    /**
     * Construct the option.
     *
     * @param optionObject The object containing the value and content for this option.
     * @param valueProperty The JavaBean property which will be retrieved from the object for setting the value of the option: there must be
     * a standard JavaBean getter for this property.
     * @param contentProperty The JavaBean property which will be retrieved from the object for setting the content of the option: there must be
     * a standard JavaBean getter for this property.
     */
    public Option(Object optionObject, String valueProperty, String contentProperty) {
        BeanWrapper wrapper = new BeanWrapperImpl(optionObject);
        this.value = wrapper.getPropertyValue(valueProperty).toString();
        this.content = wrapper.getPropertyValue(contentProperty).toString();
    }
    
    /**
     * Set this option as selected.
     */
    public boolean getSelected() {
        return this.selected;
    }
    
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
    /**
     * Add a generic attribute to each option.
     * @param name The attribute name.
     * @param value The attribute value.
     */
    public void addAttribute(String name, String value) {
        this.attributes.put(name, value);
    }
    
    public String render() {
        StringBuilder response = new StringBuilder();
        
        response.append("<option value=\"")
        .append(this.value)
        .append("\"");
        
        if (selected) {
            response.append(" selected=\"true\"");
        }
        
        if (!this.attributes.isEmpty()) {
            ComponentUtils.appendAsAttributes(this.attributes, response);
        }
        
        response.append(">");
        
        response.append(this.content);
        
        response.append("</option>");
        
        return response.toString();
    }
}
