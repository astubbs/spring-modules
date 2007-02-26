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

import java.util.List;

/**
 * Component implementing an HTML DIV or SPAN acting as a container of other
 * {@link Component}s.
 *
 * @author Sergio Bossa
 * @author Peter Bona
 */
public class Container extends SimpleHTMLComponent {
    
    private static final long serialVersionUID = 26L;
    
    private Container.Type type = Container.Type.DIV;
    
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
        super(components);
        this.type = type;
    }
    
    /**
     * Add a {@link Component} to this container.
     */
    public void addComponent(Component component) {
        this.internalAddContent(component);
    }
    
    protected String getTagName() {
		return this.type.getTagName();
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
    }
}
