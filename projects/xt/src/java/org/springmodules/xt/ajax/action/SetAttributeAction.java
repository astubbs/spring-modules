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

/**
 * Taconite based ajax action for setting an attribute value.
 *
 * @author Sergio Bossa
 */
public class SetAttributeAction extends AbstractRenderingAction {
    
    private static final String OPEN = "<taconite-set-attributes contextNodeID=\"$1\" multipleMatch=\"$2\" parseInBrowser=\"true\" $3=\"$4\">";
    private static final String CLOSE = "</taconite-set-attributes>";
    
    private String attribute;
    private String value;
    
    /** 
     * Construct the action.
     * @param elementId The id of the html element whose attribute will be set.
     * @param attribute The attribute name.
     * @param value The value to set. 
     */
    public SetAttributeAction(String elementId, String attribute, String value) {
        super(elementId, new ArrayList(0));
        this.attribute = attribute;
        this.value = value;
    }
    
    protected String getOpeningTag() {
        return OPEN.replaceFirst("\\$3", this.attribute).replaceFirst("\\$4", this.value);
    }

    protected  String getClosingTag() {
        return CLOSE;
    }
}
