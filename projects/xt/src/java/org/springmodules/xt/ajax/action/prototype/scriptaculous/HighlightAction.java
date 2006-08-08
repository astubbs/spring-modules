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
 * Ajax action for highlighting an HTML element by its id.<br>
 * This action uses the Prototype Javascript library and the Scriptaculous Effects Javascript library, so you need to include them in your web pages.
 *
 * @author Sergio Bossa
 */
public class HighlightAction extends AbstractExecuteJavascriptAction {
    
    private String elementId;
    private Map options = new HashMap();
    
    /**
     * Action constructor.
     * @param elementId The id of the element to highlight in the HTML page.
     */
    public HighlightAction(String elementId) {
        this.elementId = elementId;
    }
    
    /**
     * Action constructor.
     * @param elementId The id of the element to highlight in the HTML page.
     * @param duration The effect duration.
     */
    public HighlightAction(String elementId, float duration) {
        this.elementId = elementId;
        this.options.put("duration", duration);
    }
    
    /**
     * Action constructor (you can leave null the unneeded arguments).
     * @param elementId The id of the element to highlight in the HTML page.
     * @param duration The effect duration.
     * @param startColor The highlighting color: defaults to ”#ffff99” (light yellow).
     * @param endColor The color shown at the end of the highlight: defaults to ”#ffffff” (white).
     * @param restoreColor The background color after the highlighting has finished: by default it is guessed.
     */
    public HighlightAction(String elementId, Float duration, String startColor, String endColor, String restoreColor) {
        this.elementId = elementId;
        if (duration != null) this.options.put("duration", duration);
        if (startColor != null) this.options.put("startcolor", startColor);
        if (endColor != null) this.options.put("endcolor", endColor);
        if (restoreColor != null) this.options.put("restorecolor", restoreColor);
    }
    
    protected String getJavascript() {
        StringBuilder effect = new StringBuilder("new Effect.Highlight(\"");
        
        effect.append(this.elementId).append("\"");
        if (!this.options.isEmpty()) {
            JSONObject json = new JSONObject(this.options);
            effect.append(",").append(json.toString());
        }
        effect.append(");");
        
        return effect.toString();
    }
}
