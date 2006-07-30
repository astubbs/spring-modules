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

package org.springmodules.xt.ajax.taconite;

import java.util.LinkedList;
import java.util.List;
import org.springmodules.xt.ajax.component.Component;

/**
 * Taconite based action which refers a particular html element (identified by its id) and renders components acting on the referred element.<br>
 * The concrete rendering type (element replacing, content replacing etc.) is defined by subclasses via template methods.
 * 
 * @author Sergio Bossa
 */
public abstract class AbstractRenderingAction implements AjaxActionImpl {
    
    private String elementId;
    private List<Component> components = new LinkedList();
    
    /**
     * Construct the action.
     * @param elementId The id of the element this action refers to.
     * @param components A list of components that will be rendered.
     */
    public AbstractRenderingAction(String elementId, List<Component> components) {
        this.elementId = elementId;
        this.components = components;
    }
    
    /**
     * Construct the action.
     * @param elementId The id of the element this action refers to.
     * @param component The component that will be rendered.
     */
    public AbstractRenderingAction(String elementId, Component component) {
        this.elementId = elementId;
        this.components.add(component);
    }
    
    public String execute() {
        StringBuilder response = new StringBuilder(this.getOpeningTag().replaceFirst("\\$1", elementId));
        for (Component c : this.components) {
            response.append(c.render());
        }
        response.append(this.getClosingTag());
        return response.toString();
    }

    protected abstract String getOpeningTag();
    
    protected abstract String getClosingTag();
}
