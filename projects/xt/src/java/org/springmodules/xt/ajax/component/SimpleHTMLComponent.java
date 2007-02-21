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
import java.util.LinkedList;

/**
 * Abstract class helping the implementation of HTML components containing a list of other components.<br>
 * Subclasses will have to provide only the component tag name (see {@link #getTagName()}). 
 * 
 * @author Peter Bona
 * @author Sergio Bossa
 */
public abstract class SimpleHTMLComponent extends BaseHTMLComponent {
    
    private List<Component> contents = new LinkedList<Component>();
    
    /**
     * Construct an empty component.
     */
    public SimpleHTMLComponent() {
    }
    
    /**
     * Construct the component with a given content.
     */
    public SimpleHTMLComponent(Component content) {
        this.contents.add(content);
    }
    
    /**
     * Construct the component with a given list of contents.
     */
    public SimpleHTMLComponent(List<Component> contents) {
        this.contents.addAll(contents);
    }
    
    /**
     * Render this component HTML body, composed by this component contents.
     */
    protected final String renderBody() {
        StringBuilder response = new StringBuilder(""); 
        if (this.contents != null) {
            for (Component content : contents) {
                response.append(content.render());
            }
        }
        return response.toString();
    }
    
    /**
     * Add contents to this component.
     */
    final protected void internalAddContent(Component content) {
        this.contents.add(content);
    }
    
    /**
     * Get this component contents.
     */
    final protected List<Component> internalGetContents() {
        return this.contents;
    }
    
    /**
     * Get the component tag name.
     *
     * @return The component tag name.
     */
    abstract protected String getTagName();
}
