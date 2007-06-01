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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.springmodules.xt.ajax.ElementMatcher;

/**
 * Match elements to modify against a given a list of identifiers.<br>
 * The ListMatcher will make actions matching elements whose identifier
 * is contained into the list provided to the matcher.
 *
 * @author Sergio Bossa
 */
public class ListMatcher implements ElementMatcher {
    
    private static final long serialVersionUID = 26L;
    
    private List<String> elementsId = new LinkedList();
    
    /**
     * @param elementsId The list of elements identifier to match.
     */
    public ListMatcher(List<String> elementsId) {
        this.elementsId = elementsId;
    }

    public String render() {
        StringBuilder response = new StringBuilder();
        
        response.append("<matcher ");
        response.append("matchMode=\"plain\" ");
        response.append("contextNodeID=").append('"');
        Iterator<String> it = this.elementsId.iterator();
        while (it.hasNext()) {
            String element = it.next();
            response.append(element);
            if (it.hasNext()) {
                response.append(",");
            }
        }
        response.append('"');
        response.append("/>");
        
        return response.toString();
    }
}
