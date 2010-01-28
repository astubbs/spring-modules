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

/**
 * <p>Interface representing an ajax response.</p>
 * <p>An ajax response is a set of ajax actions taking effect on the client.<br>
 * Each action creates a given text response: an ajax response contains the response of all ajax actions.</p>
 *
 * @author Sergio Bossa
 */
public interface AjaxResponse extends Serializable {
    
    /**
     * Add an action to perform in the context of this response, e.g. setting an input field, an element attribute and so.
     *
     * @param action The {@link AjaxAction} to perform.
     */
    public void addAction(AjaxAction action);
    
    /**
     * Check if this response is empty, that is, has no actions.
     *
     * @return True if empty, false otherwise.
     */
    public boolean isEmpty();
    
    /**
     * Get the charset name of the encoding of the response.
     * It must be a String specifying only charsets defined by IANA Character Sets (http://www.iana.org/assignments/character-sets).<br>
     * See also: {@link javax.servlet.ServletResponse#setCharacterEncoding(String charset)}.
     *
     * @return The response encoding.
     */
    public String getEncoding();
    
    /**
     * Render the response, containing the result of all rendered added actions.
     *
     * @return A textual representation of this ajax response.
     */
    public String render();
    
    
}
