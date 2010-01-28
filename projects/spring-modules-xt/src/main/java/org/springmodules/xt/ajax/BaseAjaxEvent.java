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

package org.springmodules.xt.ajax;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * Base implementation of {@link AjaxEvent}, to be used as base abstract class for concrete Ajax events.
 *
 * @author Sergio Bossa
 */
public abstract class BaseAjaxEvent implements AjaxEvent {
    
    private String eventId;
    private HttpServletRequest httpRequest;
    private String elementName;
    private String elementId;
    private Map<String, String> parameters;
    
    public BaseAjaxEvent(String eventId, HttpServletRequest httpRequest) {
        this.eventId = eventId;
        this.httpRequest = httpRequest;
    }
    
    public String getEventId() {
        return this.eventId;
    }

    public HttpServletRequest getHttpRequest() {
        return this.httpRequest;
    }

    public String getElementName() {
        return this.elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public String getElementId() {
        return this.elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public Map<String, String> getParameters() {
        return this.parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }
}
