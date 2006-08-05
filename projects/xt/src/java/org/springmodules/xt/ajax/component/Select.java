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

/**
 * Component implementing a select HTML element, containing {@link Option} components.
 *
 * @author Sergio Bossa
 */
public class Select implements Component {
    
    private String name;
    private List<Option> optionList = new LinkedList();
    private Map<String, String> attributes = new HashMap();
    
    /**
     * Construct an empty select list with a name.
     *
     * @param name The select name.
     */
    public Select(String name) {
        this.name = name;
    }
    
    /**
     * Construct with a name and some options.
     * 
     * @param name The select name.
     * @param optionList The list of {@link Option} elements.
     */
    public Select(String name, List<Option> optionList) {
        this.name = name;
        this.optionList = optionList;
    }
    
    /**
     * Add an {@link Option}.
     * @param option The option to add.
     */
    public void addOption(Option option) {
        this.optionList.add(option);
    }
    
    /**
     * Add a generic attribute.
     * @param name The attribute name.
     * @param value The attribute value.
     */
    public void addAttribute(String name, String value) {
        this.attributes.put(name, value);
    }
    
    public String render() {
        StringBuilder response = new StringBuilder();
        
        response.append("<select name=\"")
            .append(this.name)
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
        response.append(">");
        
        for (Option o : this.optionList) {
            response.append(o.render());
        }
        
        response.append("</select>");
        
        return response.toString();
    }
}
