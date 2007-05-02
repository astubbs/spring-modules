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

import java.util.List;
import org.springmodules.xt.ajax.action.matcher.ElementMatcher;
import org.springmodules.xt.ajax.component.Component;

/**
 * Taconite based action for appending content to a given html element as first child.
 *
 * @author Sergio Bossa
 * @author Jean van Wyk
 */
public class AppendAsFirstContentAction extends AbstractRenderingAction {
    
    private static final long serialVersionUID = 26L;
    
    private static final String OPEN = "<taconite-append-as-first-child>";
    private static final String CLOSE = "</taconite-append-as-first-child>";
    
    /**
     * Construct the action.
     * @param elementId The id of the html element for appending content to.
     * @param components A list of components (html elements) that will be appended.
     */
    public AppendAsFirstContentAction(String elementId, List<Component> components) {
        super(elementId, components);
    }
    
    /**
     * Construct the action.
     * @param elementId The id of the html element for appending content to.
     * @param component The component (html element) that will be appended.
     */
    public AppendAsFirstContentAction(String elementId, Component component) {
        super(elementId, component);
    }
    
    /**
     * Construct the action.
     * @param matcher The matcher that identifies html elements to append content to.
     * @param components A list of components (html elements) that will be appended.
     */
    public AppendAsFirstContentAction(ElementMatcher matcher, List<Component> components) {
        super(matcher, components);
    }
    
    /**
     * Construct the action.
     * @param matcher The matcher that identifies html elements to append content to.
     * @param component The component (html element) that will be appended.
     */
    public AppendAsFirstContentAction(ElementMatcher matcher, Component component) {
        super(matcher, component);
    }
    
    protected String getOpeningTag() {
        return OPEN;
    }
    
    protected String getClosingTag() {
        return CLOSE;
    }
}
