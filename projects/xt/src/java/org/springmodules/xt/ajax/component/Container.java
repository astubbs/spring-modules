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
import org.springmodules.xt.ajax.component.support.ComponentUtils;

/**
 * Component implementing an HTML DIV or SPAN container of other
 * {@link Component}s.
 *
 * @author Sergio Bossa
 */
public class Container implements Component {
    
    private Container.Type type = Container.Type.DIV;
    private List<Component> components = new LinkedList<Component>();
    private Map<String, String> attributes = new HashMap<String, String>();
    
    /**
     * Construct the container.
     *
     * @param type The container type.
     */
    public Container(Container.Type type) {
        this.type = type;
    }
    
    /**
     * Construct the container.
     *
     * @param type The container type.
     * @param components The {@link Component}s to add to.
     */
    public Container(Container.Type type, List<Component> components) {
        this.type = type;
        this.components = components;
    }
    
    /**
     * Add a {@link Component} to this container.
     */
    public void addComponent(Component component) {
        this.components.add(component);
    }
    
    /**
     * Add a generic attribute to the container.
     *
     * @param name The attribute name.
     * @param value The attribute value.
     */
    public void addAttribute(String name, String value) {
        this.attributes.put(name, value);
    }
    
    public String render() {
        StringBuilder response = new StringBuilder("<");
        
        response.append(this.type.getTagName());
        if (!this.attributes.isEmpty()) {
            ComponentUtils.appendAsAttributes(this.attributes, response);
        }
        response.append(">");
        
        for (Component c : this.components) {
            response.append(c.render());
        }
        
        response.append("</")
        .append(this.type.getTagName())
        .append(">");
        
        return response.toString();
    }
    
    /**
     * The container type.
     */
    public enum Type {
        
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
