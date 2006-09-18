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
 * Ajax action for making an HTML element growing, identifying it by its id.<br>
 * This action uses the Prototype Javascript library and the Scriptaculous Effects Javascript library, so you need to include them in your web pages.
 *
 * @author Sergio Bossa
 */
public class GrowAction extends AbstractExecuteJavascriptAction {
    
    private static final String DIRECTION = new String("direction");
    
    private String elementId;
    private Map options = new HashMap();
    
    /**
     * Action constructor.
     * @param elementId The id of the element.
     */
    public GrowAction(String elementId) {
        this.elementId = elementId;
        this.options.put(DIRECTION, "top-left");
    }
    
    /**
     * Action constructor.
     * @param elementId The id of the element.
     * @param direction The effect direction.
     */
    public GrowAction(String elementId, String direction) {
        this.elementId = elementId;
        this.options.put(DIRECTION, direction);
    }
    
    protected String getJavascript() {
        StringBuilder effect = new StringBuilder("new Effect.Grow(\"");
        
        effect.append(this.elementId).append("\"");
        if (!this.options.isEmpty()) {
            JSONObject json = new JSONObject(this.options);
            effect.append(",").append(json.toString());
        }
        effect.append(");");
        
        return effect.toString();
    }
}
