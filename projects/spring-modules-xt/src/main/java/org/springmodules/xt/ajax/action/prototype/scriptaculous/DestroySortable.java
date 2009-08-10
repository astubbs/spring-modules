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

package org.springmodules.xt.ajax.action.prototype.scriptaculous;

import org.springmodules.xt.ajax.action.AbstractExecuteJavascriptAction;

/**
 * Ajax action for destroying a Scriptaculous sortable container element (see http://wiki.script.aculo.us/scriptaculous/show/Sortable.destroy).<br>
 * This action uses the Prototype Javascript library and the Scriptaculous Drag and Drop Javascript library, so you need to include them in your web pages.
 *
 * @author Sergio Bossa
 */
public class DestroySortable extends AbstractExecuteJavascriptAction {
    
    private static final long serialVersionUID = 26L;
    
    private String elementId;
    
    /**
     * Construct a Scriptaculous action for destroying the sortable container.<br>
     *
     * @param elementId The id of the element.
     */
    public DestroySortable(String elementId) {
        this.elementId = elementId;
    }
    
    protected String getJavascript() {
        StringBuilder effect = new StringBuilder("Sortable.destroy");
        
        effect.append("(");
        
        effect.append("${\"").append(this.elementId).append("\"}");
        
        effect.append(");");
        
        return effect.toString();
    }
}
