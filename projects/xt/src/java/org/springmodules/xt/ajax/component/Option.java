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
 * Component implementing an option element.
 *
 * @author Sergio Bossa
 * @author Peter Bona
 */
public class Option extends SimpleHTMLComponent {
    
    private static final long serialVersionUID = 26L;
    
    private boolean selected;
    
    /**
     * Construct the option.
     *
     * @param value The option value.
     * @param content The option content.
     */
    public Option(String value, String content) {
        super();
        this.addAttribute("value", value);
        this.internalAddContent(new SimpleText(content));
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
        if (optionObject != null) {
            BeanWrapper wrapper = new BeanWrapperImpl(optionObject);
            
            Object tmp = wrapper.getPropertyValue(valueProperty);
            if (tmp != null) {
                this.addAttribute("value", tmp.toString());
            } 
            else {
                this.addAttribute("value", "");
            }
            
            tmp = wrapper.getPropertyValue(contentProperty);
            if (tmp != null) {
                this.internalAddContent(new SimpleText(tmp.toString()));
            } 
            else {
                this.internalAddContent(new SimpleText(""));
            }
        } 
        else {
            throw new IllegalArgumentException("The object to render in the option component cannot be null!");
        }
    }
    
    /**
     * Check if this option is selected.
     */
    public boolean getSelected() {
        return this.selected;
    }
    
    /**
     * Set this option as selected.
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
        this.addAttribute("selected", Boolean.toString(selected));
    }
    
    protected String getTagName() {
        return "option";
    }
}
