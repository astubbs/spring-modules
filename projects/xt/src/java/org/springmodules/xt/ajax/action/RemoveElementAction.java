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

import java.util.ArrayList;
import org.springmodules.xt.ajax.action.matcher.DefaultMatcher;
import org.springmodules.xt.ajax.ElementMatcher;

/**
 * Ajax action for removing a given element, identified by its id.
 *
 * @author Sergio Bossa
 */
public class RemoveElementAction extends AbstractRenderingAction {
    
    private static final long serialVersionUID = 26L;
    
    private static final String OPEN = "<delete>";
    private static final String CLOSE = "</delete>";
    
    private String elementId;
    
    /** 
     * Construct the action.
     * @param elementId The id of the html element to remove.
     */
    public RemoveElementAction(String elementId) {
        super(new DefaultMatcher(elementId));
    }
    
    /** 
     * Construct the action.
     * @param matcher The matcher that identifies html elements to remove.
     */
    public RemoveElementAction(ElementMatcher matcher) {
        super(matcher);
    }
    
    protected String getOpeningTag() {
        return OPEN;
    }
    
    protected String getClosingTag() {
        return CLOSE;
    }

    protected String getContent() {
        return "";
    }
}
