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
import org.springmodules.xt.ajax.action.matcher.ElementMatcher;

/**
 * Taconite based ajax action for setting an attribute value.
 *
 * @author Sergio Bossa
 */
public class SetAttributeAction extends AbstractRenderingAction {
    
    private static final long serialVersionUID = 26L;
    
    private static final String OPEN = "<taconite-set-attributes $key=\"$value\">";
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
    
    /** 
     * Construct the action.
     * @param matcher The matcher that identifies html elements whose attribute will be set.
     * @param attribute The attribute name.
     * @param value The value to set. 
     */
    public SetAttributeAction(ElementMatcher matcher, String attribute, String value) {
        super(matcher, new ArrayList(0));
        this.attribute = attribute;
        this.value = value;
    }
    
    protected String getOpeningTag() {
        return OPEN.replaceFirst("\\$key", this.attribute).replaceFirst("\\$value", this.value);
    }

    protected  String getClosingTag() {
        return CLOSE;
    }
}
