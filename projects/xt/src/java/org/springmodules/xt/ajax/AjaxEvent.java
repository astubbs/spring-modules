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

import java.io.Serializable;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * Interface for an ajax event.<br>
 * Each one has:
 * <ul>
 * <li>An event id.</li>
 * <li>The http request object.</li>
 * <li>The name of the html element that fired the event (optional).</li>
 * <li>The id of the html element that fired the event (optional).</li>
 * <li>A map of string parameters (optional).</li>
 * </ul>
 * 
 * @author Sergio Bossa
 */
public interface AjaxEvent extends Serializable {
    
    /**
     * Get the event id.
     * @return The event id.
     */
    public String getEventId();
    
    /**
     * Get the http request object.
     * @return The request.
     */
    public HttpServletRequest getHttpRequest();
    
    /**
     * Get the name of the html element firing the event (the event source).
     * @return The source element name.
     */
    public String getElementName();
    
    /**
     * Set the name of the html element firing the event (the event source).
     * @param elementName The source element name.
     */
    public void setElementName(String elementName);
    
    /**
     * Get the id of the html element firing the event (the event source).
     * @return The source element id.
     */
    public String getElementId();
    
    /**
     * Set the id of the html element firing the event (the event source).
     * @param elementId The source element id.
     */
    public void setElementId(String elementName);
    
    /**
     * Get a map of extra parameters passed with the ajax request.
     * @return The map of parameters;
     */
    public Map<String, String> getParameters();

    /**
     * Set a map of extra parameters passed with the ajax request.
     * @param parameters A map of parameters;
     */
    public void setParameters(Map<String, String> parameters);
}
