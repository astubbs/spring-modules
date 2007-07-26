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

import java.util.HashMap;
import java.util.Map;
import net.sf.json.JSONObject;

/**
 * Ajax action for executing a Javascript function (declared in the client side) with the following signature: <i>function name(options)</i>.<br>
 * The <i>options</i> argument must be a JSON object in the form: <i>{param1 : value1, param2 : value2 ... }</i>.
 *
 * @author Sergio Bossa
 */
public class ExecuteJavascriptFunctionAction extends AbstractExecuteJavascriptAction {
    
    private static final long serialVersionUID = 26L;
    
    private String name;
    private Map<String, Object> options = new HashMap<String, Object>();
    
    /**
     * Action constructor.
     * @param name The function name.
     * @param options A map of key/value pairs representing function options.
     */
    public ExecuteJavascriptFunctionAction(String name, Map<String, Object> options) {
        this.name = name;
        this.options = options;
    }
    
    protected String getJavascript() {
        StringBuilder function = new StringBuilder();
        
        function.append(name).append("(");
        if (this.options != null && !this.options.isEmpty()) {
            JSONObject json = JSONObject.fromMap(this.options);
            function.append(json.toString());
        }
        function.append(");");
        
        return function.toString();
    }
}
