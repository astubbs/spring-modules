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

import java.util.HashMap;
import java.util.Map;
import net.sf.json.JSONObject;
import org.springmodules.xt.ajax.action.AbstractExecuteJavascriptAction;

/**
 * Ajax action for making a new Scriptaculous draggable element (see http://wiki.script.aculo.us/scriptaculous/show/Draggable).<br>
 * This action uses the Prototype Javascript library and the Scriptaculous Drag and Drop Javascript library, so you need to include them in your web pages.
 *
 * @author Sergio Bossa
 */
public class Draggable extends AbstractExecuteJavascriptAction {
    
    private static final long serialVersionUID = 26L;
    
    private String elementId;
    private Map<String, Object> options = new HashMap<String, Object>();
    
    /**
     * Construct a Scriptaculous draggable element.<br>
     *
     * @param elementId The id of the element.
     */
    public Draggable(String elementId) {
        this.elementId = elementId;
    }
    
    /**
     * Add an optional parameter to this effect, in the form of key/value pair.<br>
     *
     * @param key The key of the option (<b>case-sensitive</b>).
     * @param value The value of the option.
     */
    public void addOption(String key, Object value) {
        this.options.put(key, value);
    }
    
    protected String getJavascript() {
        StringBuilder effect = new StringBuilder("new Draggable");
        
        effect.append("(");
        
        effect.append("\"").append(this.elementId).append("\"");
        
        
        if (!this.options.isEmpty()) {
            JSONObject json = JSONObject.fromMap(this.options);
            effect.append(",").append(json.toString());
        }
        
        effect.append(");");
        
        return effect.toString();
    }
}
