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

package org.springmodules.xt.ajax;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Taconite based implementation of {@link AjaxResponse}.
 *
 * @author Sergio Bossa
 */
public class AjaxResponseImpl implements AjaxResponse<AjaxActionImpl> {
    
    private static final String OPEN_RESPONSE = new String("<?xml version=\"1.0\"?> <taconite-root xml:space=\"preserve\"> ");
    private static final String CLOSE_RESPONSE = new String(" </taconite-root>");
    
    private List<AjaxActionImpl> actions = new LinkedList();
    
    public void addAction(AjaxActionImpl action) {
        this.actions.add(action);
    }

    public String getResponse() {
        StringBuilder response = new StringBuilder(OPEN_RESPONSE);
        for (Iterator it = actions.iterator(); it.hasNext(); ) {
            AjaxAction action = (AjaxAction) it.next();
            response.append(action.execute());
        }
        response.append(CLOSE_RESPONSE);
        return response.toString();
    }
}
