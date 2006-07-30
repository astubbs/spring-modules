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

import org.springmodules.xt.ajax.AjaxActionImpl;

/**
 * Taconite based ajax action for removing a given element, identified by its id.
 *
 * @author Sergio Bossa
 */
public class RemoveElementAction implements AjaxActionImpl {
    
    private static final String OPEN = new String("<taconite-delete contextNodeID=\"$1\" parseInBrowser=\"true\">");
    private static final String CLOSE = new String("</taconite-delete>");
    
    private String elementId;
    
    /** 
     * Construct the action.
     * @param elementId The id of the html element to remove.
     */
    public RemoveElementAction(String elementId) {
        this.elementId = elementId;
    }
    
    public String execute() {
        StringBuilder response = new StringBuilder(OPEN.replaceFirst("\\$1", this.elementId));
        response.append(CLOSE);
        return response.toString();
    }
}
