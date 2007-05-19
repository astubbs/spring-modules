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

package org.springmodules.xt.ajax.action;

import java.util.LinkedList;
import java.util.List;
import org.springmodules.xt.ajax.AjaxAction;
import org.springmodules.xt.ajax.action.matcher.DefaultMatcher;
import org.springmodules.xt.ajax.action.matcher.ElementMatcher;
import org.springmodules.xt.ajax.component.Component;

/**
 * Abstract Taconite based action for modifying web pages content by rendering data into html elements.<br>
 * Html elements to modify are identified:
 * <ul>
 * <li>By html element identifier.</li>
 * <li>By {@link org.springmodules.xt.ajax.action.matcher.ElementMatcher}.</li>
 * </ul>
 * The concrete action (element replacing, content replacing etc.) is defined by subclasses via template methods.
 *
 * @author Sergio Bossa
 */
public abstract class AbstractRenderingAction implements AjaxAction {
    
    private ElementMatcher matcher;
    private List<Component> components = new LinkedList();
    
    /**
     * Construct the action.
     * @param elementId The id of the element matching this action.
     * @param components A list of components that will be rendered.
     */
    public AbstractRenderingAction(String elementId, List<Component> components) {
        this.matcher = new DefaultMatcher(elementId);
        this.components = components;
    }
    
    /**
     * Construct the action.
     * @param elementId The id of the element matching this action.
     * @param component The component that will be rendered.
     */
    public AbstractRenderingAction(String elementId, Component component) {
        this.matcher = new DefaultMatcher(elementId);
        this.components.add(component);
    }
    
    /**
     * Construct the action.
     * @param elementMatcher The {@link org.springmodules.xt.ajax.action.matcher.ElementMatcher} 
     * to use for selecting the elements matching this action.
     * @param components A list of components that will be rendered.
     */
    public AbstractRenderingAction(ElementMatcher elementMatcher, List<Component> components) {
        this.matcher = elementMatcher;
        this.components = components;
    }
    
    /**
     * Construct the action.
     * @param elementMatcher The {@link org.springmodules.xt.ajax.action.matcher.ElementMatcher} 
     * to use for selecting the elements matching this action.
     * @param component The component that will be rendered.
     */
    public AbstractRenderingAction(ElementMatcher elementMatcher, Component component) {
        this.matcher = elementMatcher;
        this.components.add(component);
    }
    
    public String execute() {
        String startTag = this.getOpeningTag().substring(0, this.getOpeningTag().length() - 1);
        String endTag = this.getClosingTag();
        StringBuilder response = new StringBuilder();
        
        response.append(startTag);
        response.append(" ").append(this.matcher.render());
        response.append(">");
        
        for (Component c : this.components) {
            response.append(c.render());
        }
        
        response.append(endTag);
        
        return response.toString();
    }
    
    /**
     * Get the action opening tag.
     */
    protected abstract String getOpeningTag();
    
    /**
     * Get the action closing tag.
     */
    protected abstract String getClosingTag();
}
