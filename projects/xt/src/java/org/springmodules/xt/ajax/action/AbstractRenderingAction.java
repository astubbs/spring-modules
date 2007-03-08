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
import org.springmodules.xt.ajax.component.Component;

/**
 * Taconite based action which matches html elements by their identifiers (id), 
 * and renders components acting on these elements.<br>
 * The action is constructed by passing a target identifier, that is the one of the target HTML element to match, and
 * a list of components to render.<br>
 * By default, the action matches the element whose identifier is equal to the target one.<br>
 * If the multiple matching capability is enabled (see {@link #setMultipleMatch(boolean)}),
 * the action will match elements whose identifier is exactly equal to the target one, or 
 * starts with the same substring and ends with the "_" wildcard character.<br><br>
 * The concrete action (element replacing, content replacing etc.) is defined by subclasses via template methods.
 *
 * @author Sergio Bossa
 */
public abstract class AbstractRenderingAction implements AjaxAction {
    
    private String elementId;
    private boolean multipleMatch;
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
    
    /**
     * Make this action matching multiple elements whose identifier is exactly equal to the
     * identifier passed to this action, or starts with the same substring and ends with 
     * the "_" wildcard.
    */
    public void setMultipleMatch(boolean multipleMatch) {
        this.multipleMatch = multipleMatch;
    }
    
    /**
     * True if multiple matching is enabled, false otherwise.
     */
    public boolean isMultipleMatch() {
        return this.multipleMatch;
    }
    
    /**
     * Execute the action, directly rendering into the web page.
     */
    public String execute() {
        StringBuilder response = new StringBuilder(this.getOpeningTag()
        .replaceFirst("\\$1", this.elementId)
        .replaceFirst("\\$2", Boolean.toString(this.multipleMatch)));
        for (Component c : this.components) {
            response.append(c.render());
        }
        response.append(this.getClosingTag());
        return response.toString();
    }
    
    /**
     * Get the action opening tag.<br>
     * It must contain the following placeholders:
     * <ul>
     * <li>The "$1" placeholder, for putting the element id to match.</li>
     * <li>The "$2" placeholder, for enabling the multiple matching capability.</li>
     * </ul>
     */
    protected abstract String getOpeningTag();
    
    /**
     * Get the action closing tag.
     */
    protected abstract String getClosingTag();
}
