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
 * Interface defining how to resolve exceptions generated during Ajax request processing:
 * the exception is resolved in an {@link AjaxResponse} delivered to clients.
 *
 * @see #resolve(HttpServletRequest , Exception )
 * 
 * @author Sergio Bossa
 */
public interface AjaxExceptionResolver {

    /**
     * Resolve the given exception, rendering an {@link AjaxResponse} to clients:
     * implementors have to define how to resolve the exception and what to render.
     * 
     * @param request The http request of the current Ajax call.
     * @param ex The exception to resolve
     * @return The {@link AjaxResponse} to render as a response back to the client.
     */
    public AjaxResponse resolve(HttpServletRequest request, Exception ex);
}
