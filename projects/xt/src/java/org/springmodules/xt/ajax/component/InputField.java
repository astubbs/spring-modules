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

/**
 * Component implementing an input field.
 *
 * @author Sergio Bossa
 */
public class InputField implements Component {
    
    private String name;
    private String value;
    private InputField.InputType type;
    private Map<String, String> attributes = new HashMap();
    
    /**
     * Construct the component.
     * @param name The input field name.
     * @param value The input field value.
     * @param type The input field type.
     */
    public InputField(String name, String value, InputField.InputType type) {
        this.name = name;
        this.value = value;
        this.type = type;
    }
    
    /**
     * Add a generic attribute to the input field.
     * @param name The attribute name.
     * @param value The attribute value.
     */
    public void addAttribute(String name, String value) {
        this.attributes.put(name, value);
    }
    
    public String render() {
        StringBuilder response = new StringBuilder("<input name=\"")
                       .append(this.name)
                       .append("\" value=\"")
                       .append(this.value)
                       .append("\" type=\"")
                       .append(this.type)
                       .append("\"");
        
        if (!this.attributes.isEmpty()) {
            for (Map.Entry<String, String> entry : this.attributes.entrySet()) {
                response.append(" ")
                               .append(entry.getKey())
                               .append("=\"")
                               .append(entry.getValue())
                               .append("\"");
            }
        }
        
        response.append("/>");
        return response.toString();
    }
    
    public enum InputType { TEXT, CHECKBOX, RADIO, HIDDEN, PASSWORD };
}
