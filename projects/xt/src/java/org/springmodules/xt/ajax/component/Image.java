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
import org.springmodules.xt.ajax.component.support.ComponentUtils;

/**
 * Component representinh an HTML inline image.
 *
 * @author Sergio Bossa
 */
public class Image implements Component {
    
    private String location;
    private String alternateText;
    private Map<String, String> attributes = new HashMap();
    
    /**
     * Construct the component.
     * @param location The image location.
     * @param alternateText The image alternate text for this image.
     */
    public Image(String location, String alternateText) {
        this.location = location;
        this.alternateText = alternateText;
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
        StringBuilder response = new StringBuilder("<img");
        response.append(" src=\"").append(this.location).append("\"");
        response.append(" alt=\"").append(this.alternateText).append("\"");
        if (!this.attributes.isEmpty()) {
            ComponentUtils.appendAsAttributes(this.attributes, response);
        }
        response.append(">");
        
        response.append("</img>");
        
        return response.toString();
    }
}
