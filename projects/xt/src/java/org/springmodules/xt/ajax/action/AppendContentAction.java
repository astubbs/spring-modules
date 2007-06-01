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

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.springmodules.xt.ajax.action.matcher.DefaultMatcher;
import org.springmodules.xt.ajax.ElementMatcher;
import org.springmodules.xt.ajax.component.Component;

/**
 * Ajax action for appending content to a given html element.
 * 
 * @author Sergio Bossa
 */
public class AppendContentAction extends AbstractRenderingAction {
    
    private static final long serialVersionUID = 26L;
    
    private static final String OPEN = "<append-as-children>";
    private static final String CLOSE = "</append-as-children>";
    
    private List<Component> components = new LinkedList<Component>();
    
    /**
     * Construct the action.
     * @param elementId The id of the html element for appending content to.
     * @param components The list of components representing the content to render through this action.
     */
    public AppendContentAction(String elementId, List<Component> components) {
        super(new DefaultMatcher(elementId));
        this.components = components;
    }
    
    /**
     * Construct the action.
     * @param elementId The id of the html element for appending content to.
     * @param components The list of components representing the content to render through this action.
     */
    public AppendContentAction(String elementId, Component... components) {
        super(new DefaultMatcher(elementId));
        this.components = Arrays.asList(components);
    }
    
    /**
     * Construct the action.
     * @param matcher The matcher that identifies html elements to append content to.
     * @param components The list of components representing the content to render through this action.
     */
    public AppendContentAction(ElementMatcher matcher, List<Component> components) {
        super(matcher);
        this.components = components;
    }
    
    /**
     * Construct the action.
     * @param matcher The matcher that identifies html elements to append content to.
     * @param components The list of components representing the content to render through this action.
     */
    public AppendContentAction(ElementMatcher matcher, Component... components) {
        super(matcher);
        this.components = Arrays.asList(components);
    }
    
    protected String getOpeningTag() {
        return OPEN;
    }

    protected String getClosingTag() {
        return CLOSE;
    }
    
    protected String getContent() {
        StringBuilder response = new StringBuilder();
        for (Component c : this.components) {
            response.append(c.render());
        }
        return response.toString();
    }
}
