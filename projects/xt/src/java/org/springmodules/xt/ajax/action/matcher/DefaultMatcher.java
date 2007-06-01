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
package org.springmodules.xt.ajax.action.matcher;

import org.springmodules.xt.ajax.ElementMatcher;

/**
 * Match elements to modify by identifier.
 *
 * @author Sergio Bossa
 */
public class DefaultMatcher implements ElementMatcher {
    
    private static final long serialVersionUID = 26L;
    
    private String elementId;
    
    /**
     * @param elementId The identifier of the element to match.
     */
    public DefaultMatcher(String elementId) {
        this.elementId = elementId;
    }

    public String render() {
        StringBuilder response = new StringBuilder();
        
        response.append("<matcher ");
        response.append("matchMode=\"plain\" ");
        response.append("contextNodeID=").append('"').append(this.elementId).append('"');
        response.append("/>");
        
        return response.toString();
    }
}
