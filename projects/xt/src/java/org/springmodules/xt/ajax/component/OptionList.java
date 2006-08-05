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
import org.springmodules.xt.ajax.support.RenderingException;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

/**
 * Component implementing a list of option elements, belonging to a <i>select</i> HTML element.
 *
 * @author Sergio Bossa
 */
@Deprecated
public class OptionList implements Component {
    
    private Object[] options;
    private Integer selectedIndex;
    private String valueProperty;
    private String contentProperty;
    private Map<String, String> attributes = new HashMap();
    private boolean hasFirstTextOption;
    private String firstOptionValue;
    private String firstOptionText;
    
    /**
     * Construct the component.
     * @param options An array of objects containing data to put in the list: each object will be an option.
     * @param selectedIndex The index of the selected option; can be null.
     * @param valueProperty The JavaBean property which will be retrieved from the object for setting the value of the option: there must be 
     * a standard JavaBean getter for this property.
     * @param contentProperty The JavaBean property which will be retrieved from the object for setting the content of the option: there must be 
     * a standard JavaBean getter for this property.
     */
    public OptionList(Object[] options, Integer selectedIndex, String valueProperty, String contentProperty) {
        this.options = options;
        this.selectedIndex = selectedIndex;
        this.valueProperty = valueProperty;
        this.contentProperty = contentProperty;
    }
    
    /**
     * Add a generic attribute to each option.
     * @param name The attribute name.
     * @param value The attribute value.
     */
    public void addAttribute(String name, String value) {
        this.attributes.put(name, value);
    }
    
    /**
     * Set the first option as text only.<br>
     * This is useful if you want to display an option  like "Select this ..." or so.
     * @param value The dummy value of the option.
     * @param text The text to display.
     */
    public void setFirstTextOption(String value, String text) {
        this.hasFirstTextOption = true;
        this.firstOptionValue = value;
        this.firstOptionText = text;
    }
    
    
    public String render() {
        StringBuilder response = new StringBuilder();
        try {
            if (this.hasFirstTextOption) {
                response.append("<option value=\"")
                               .append(this.firstOptionValue)
                               .append("\">")
                               .append(this.firstOptionText)
                               .append("</option>");
            }
            
            for (int i = 0; i< this.options.length; i++) {
                StringBuilder option = new StringBuilder("<option value=\"");
                BeanWrapper bean = new BeanWrapperImpl(options[i]);
                
                option.append(bean.getPropertyValue(valueProperty).toString())
                          .append("\"");
                        
                if (selectedIndex != null && selectedIndex.intValue() == i) { 
                    option.append(" selected=\"true\"");
                }
                
                if (!this.attributes.isEmpty()) {
                    for (Map.Entry<String, String> entry : this.attributes.entrySet()) {
                        option.append(" ")
                                  .append(entry.getKey())
                                  .append("=\"")
                                  .append(entry.getValue())
                                  .append("\"");
                    }
                }
                
                option.append(">")
                          .append(bean.getPropertyValue(contentProperty).toString())
                          .append("</option>");
            
                response.append(option);
            }
        }
        catch(Exception ex) {
            throw new RenderingException("Error while rendering a component of type: " + this.getClass().getName(), ex);
        }
        return response.toString();
    }
}
