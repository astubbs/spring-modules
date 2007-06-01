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
 * Match elements to modify by identifier <b>and</b> the underscore ("_") wildcard.<br>
 * The WildcardMatcher will make actions matching elements whose identifier starts with a substring of the identifier provided here, 
 * and ends with the "_" wildcard character.<br>
 * For instance, if you construct a WildcardMatcher with "foo.bar" as element id, you'll
 * match the element with "foo.bar" id, and all elements starting with a substring of "foo.bar" and ending with the
 * "_" wildcard, like "foo_", "foo._" and so on.
 *
 * @author Sergio Bossa
 */
public class WildcardMatcher implements ElementMatcher {
    
    private static final long serialVersionUID = 26L;
    
    private String elementId;
    
    /**
     * @param elementId The identifier of the element to match.
     */
    public WildcardMatcher(String elementId) {
        this.elementId = elementId;
    }

    public String render() {
        StringBuilder response = new StringBuilder();
        
        response.append("<matcher ");
        response.append("matchMode=\"wildcard\" ");
        response.append("contextNodeID=").append('"').append(this.elementId).append('"');
        response.append("/>");
        
        return response.toString();
    }
}
