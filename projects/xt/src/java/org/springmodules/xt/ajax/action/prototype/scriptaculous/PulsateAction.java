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
 * Ajax action for making an HTML element pulsate, identifying it by its id.<br>
 * This action uses the Prototype Javascript library and the Scriptaculous Effects Javascript library, so you need to include them in your web pages.
 *
 * @author Sergio Bossa
 */
public class PulsateAction extends AbstractExecuteJavascriptAction {
    
    private static final String DURATION = new String("duration");
    private static final String FROM = new String("from");
    
    private String elementId;
    private Map options = new HashMap();
    
    /**
     * Action constructor.
     * @param elementId The id of the element.
     */
    public PulsateAction(String elementId) {
        this.elementId = elementId;
    }
    
    /**
     * Action constructor.
     * @param elementId The id of the element.
     * @param duration The effect duration.
     */
    public PulsateAction(String elementId, float duration) {
        this.elementId = elementId;
        this.options.put(DURATION, duration);
    }
    
    /**
     * Action constructor (you can leave null the unneeded arguments).
     * @param elementId The id of the element.
     * @param duration The effect duration.
     * @param from The minimal opacity during the pulsate.
     */
    public PulsateAction(String elementId, Float duration, Float fromOpacity) {
        this.elementId = elementId;
        if (duration != null) this.options.put(DURATION, duration);
        if (fromOpacity != null) this.options.put(FROM, fromOpacity);
    }
    
    protected String getJavascript() {
        StringBuilder effect = new StringBuilder("new Effect.Pulsate(\"");
        
        effect.append(this.elementId).append("\"");
        if (!this.options.isEmpty()) {
            JSONObject json = new JSONObject(this.options);
            effect.append(",").append(json.toString());
        }
        effect.append(");");
        
        return effect.toString();
    }
}
