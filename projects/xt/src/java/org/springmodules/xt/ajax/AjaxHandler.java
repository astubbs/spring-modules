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

/**
 * Server side handler of ajax requests.
 *
 * @author Sergio Bossa
 */
public interface AjaxHandler {
    
    /**
     * Handle an ajax event (if supported).
     *
     * @param event The {@link AjaxEvent} to handle.
     * @return The resulting {@link AjaxResponse}.
     * @throws org.springmodules.xt.ajax.support.UnsupportedEventException
     * Unchecked exception thrown if this handler doesn't support the given event.
     * @throws org.springmodules.xt.ajax.support.EventHandlingException
     * Unchecked exception thrown during event handling.
     */
    public AjaxResponse handle(AjaxEvent event);

    /**
     * Check if this handler supports the handling of the given event.
     *
     * @param event The {@link AjaxEvent} to check for.
     * @return True if this handler supports the event, false otherwise.
     */
    public boolean supports(AjaxEvent event);
}
