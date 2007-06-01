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
import org.springmodules.xt.ajax.AjaxRenderingAction;
import org.springmodules.xt.ajax.action.matcher.DefaultMatcher;
import org.springmodules.xt.ajax.ElementMatcher;
import org.springmodules.xt.ajax.component.Component;

/**
 * Abstract rendering action for modifying web pages content by directly rendering data.<br>
 * Web page elements to modify are identified:
 * <ul>
 * <li>By html element identifier.</li>
 * <li>By {@link org.springmodules.xt.ajax.action.elementMatcher.ElementMatcher}.</li>
 * </ul>
 * The concrete action (element replacing, content replacing etc.) is defined by subclasses via template methods.
 * 
 * @author Sergio Bossa
 */
public abstract class AbstractRenderingAction implements AjaxRenderingAction {
    
    private ElementMatcher elementMatcher;
    
    /**
     * @param elementMatcher The {@link org.springmodules.xt.ajax.action.elementMatcher.ElementMatcher} 
     * to use for selecting elements matching this action.
     */
    public AbstractRenderingAction(ElementMatcher elementMatcher) {
        this.elementMatcher = elementMatcher;
    }
    
    public ElementMatcher getElementMatcher() {
        return this.elementMatcher;
    }
    
    public String render() {
        StringBuilder response = new StringBuilder();
        
        response.append(this.getOpeningTag());
        
        response.append("<context>");
        response.append(this.elementMatcher.render());
        response.append("</context>");
        
        response.append("<content>");
        response.append(this.getContent());
        response.append("</content>");
        
        response.append(this.getClosingTag());
        
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
    
    /**
     * Get the action content
     */
    protected abstract String getContent();
}
