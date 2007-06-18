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

package org.springmodules.xt.ajax.support;

import java.awt.Event;
import java.util.Map;
import net.sf.json.JSONObject;

/**
 * Enumeration for obtaining a string representation of a call for an ajax action or an ajax submit.
 *
 * @author Sergio Bossa
 */
@Deprecated
public enum AjaxCall {
    
    AJAX_ACTION {
        
        public String getCall(String event) {
            return this.getCall(event, null);
        }
        
        public String getCall(String event, Map parameters) {
            StringBuilder call = new StringBuilder("XT.doAjaxAction");
            call.append("(\"");
            call.append(event);
            call.append("\",");
            call.append("this");
            if (parameters != null && !parameters.isEmpty()) {
                call.append(",");
                call.append(this.getJSONString(parameters));
            }
            call.append(");");
            return call.toString();
        }
    },
    AJAX_SUBMIT {
        
        public String getCall(String event) {
            return this.getCall(event, null);
        }
        
        public String getCall(String event, Map parameters) {
            StringBuilder call = new StringBuilder("XT.doAjaxSubmit");
            call.append("(\"");
            call.append(event);
            call.append("\",");
            call.append("this");
            if (parameters != null && !parameters.isEmpty()) {
                call.append(",");
                call.append(this.getJSONString(parameters));
            }
            call.append(");");
            return call.toString();
        }
    };
          
    public abstract String getCall(String event);
    
    public abstract String getCall(String event, Map parameters);
    
    protected  String getJSONString(Map parameters) {
        JSONObject json = JSONObject.fromMap(parameters);
        return  json.toString();
    }
}
