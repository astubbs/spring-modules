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

import org.springmodules.xt.ajax.AjaxActionImpl;

/**
 * Taconite based ajax action for setting an attribute value.
 *
 * @author Sergio Bossa
 */
public class SetAttributeAction implements AjaxActionImpl {
    
    private static final String OPEN_SET_ATTRIBUTE = new String("<taconite-set-attributes contextNodeID=\"$1\" parseInBrowser=\"true\" $2=\"$3\">");
    private static final String CLOSE_SET_ATTRIBUTE = new String("</taconite-set-attributes>");
    
    private String elementId;
    private String attribute;
    private String value;
    
    /** 
     * Construct the action.
     * @param elementId The id of the html element whose attribute will be set.
     * @param attribute The attribute name.
     * @param value The value to set. 
     */
    public SetAttributeAction(String elementId, String attribute, String value) {
        this.elementId = elementId;
        this.attribute = attribute;
        this.value = value;
    }
    
    public String execute() {
        StringBuilder response = new StringBuilder(OPEN_SET_ATTRIBUTE.replaceFirst("\\$1", this.elementId).replaceFirst("\\$2", this.attribute).replaceFirst("\\$3", this.value));
        response.append(CLOSE_SET_ATTRIBUTE);
        return response.toString();
    }
}
