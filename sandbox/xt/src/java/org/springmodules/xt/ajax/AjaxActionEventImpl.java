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

import javax.servlet.http.HttpServletRequest;

/**
 * Implementation of {@link AjaxActionEvent}.
 *
 * @author Sergio Bossa
 */
public class AjaxActionEventImpl implements AjaxActionEvent {
    
    private String eventId;
    private HttpServletRequest httpRequest;
    private String elementName;
    
    public AjaxActionEventImpl(String eventId, HttpServletRequest httpRequest) {
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
}
