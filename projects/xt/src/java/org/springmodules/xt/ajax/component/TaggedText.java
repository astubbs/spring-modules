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
 * Component implementing text surrounded by tags represented
 * by the {@link Tag} enumeration. By default it uses a div tag.
 *
 * @author Sergio Bossa
 */
public class TaggedText implements Component {
    
    private String text;
    private TaggedText.Tag tag = TaggedText.Tag.DIV;
    private Map<String, String> attributes = new HashMap<String, String>();
    
    /**
     * Construct the component.
     *
     * @param text The text.
     */
    public TaggedText(String text) {
        this.text = text;
    }
    
    /**
     * Construct the component.
     *
     * @param text The text.
     * @param tag The tag to use for enclosing the given text.
     */
    public TaggedText(String text, TaggedText.Tag tag) {
        this.text = text;
        this.tag = tag;
    }
    
    /**
     * Add a generic attribute to the surrounding tag.
     *
     * @param name The attribute name.
     * @param value The attribute value.
     */
    public void addAttribute(String name, String value) {
        this.attributes.put(name, value);
    }
    
    public String render() {
        StringBuilder response = new StringBuilder("<");
        
        response.append(this.tag.getTagName());
        if (!this.attributes.isEmpty()) {
            ComponentUtils.appendAsAttributes(this.attributes, response);
        }
        response.append(">");
        
        response.append(this.text);
        
        response.append("</")
        .append(this.tag.getTagName())
        .append(">");
        
        return response.toString();
    }
    
    /**
     * The wrapping tag.
     */
    public enum Tag {
        
        DIV {
            public String getTagName() {
                return "div";
            }
        },
        
        SPAN {
            public String getTagName() {
                return "span";
            }
        };
        
        public abstract String getTagName();
    };
}
