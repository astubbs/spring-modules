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

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springmodules.xt.ajax.AjaxAction;
import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxResponseImpl;
import org.springmodules.xt.ajax.AjaxExceptionHandler;
import org.springmodules.xt.ajax.action.RedirectAction;

/**
 * {@link AjaxExceptionHandler} that produces an {@link AjaxResponse} 
 * for redirecting to a given URL.<br>
 * It exposes the exception message as a request attribute under the name
 * defined by {@link #setExceptionMessageAttribute(String)}.
 * 
 * @author Sergio Bossa
 */
public class RedirectExceptionHandler implements AjaxExceptionHandler {
    
    public static final String DEFAULT_EXCEPTION_MESSAGE_ATTRIBUTE = "exceptionMessage";
    
    private String redirectUrl;
    private String exceptionMessageAttribute = DEFAULT_EXCEPTION_MESSAGE_ATTRIBUTE;
    
    public AjaxResponse handle(HttpServletRequest request, Exception ex) {
        // Expose the exception message:
        Map model = new HashMap(1);
        if (ex.getMessage() != null && !ex.getMessage().equals("")) {
            model.put(this.exceptionMessageAttribute, ex.getMessage());
        } else {
            model.put(this.exceptionMessageAttribute, ex.getClass());
        }
        // Create the response with the redirect action:
        AjaxResponse ajaxResponse = new AjaxResponseImpl();
        AjaxAction ajaxRedirect = new RedirectAction(new StringBuilder(request.getContextPath()).append(this.redirectUrl).toString(), model);
        ajaxResponse.addAction(ajaxRedirect);
        return ajaxResponse;
    }

    /**
     * Get the redirect URL.
     */
    public String getRedirectUrl() {
        return this.redirectUrl;
    }

    /**
     * Set the redirect URL.
     */
    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    /**
     * Get the name of the exception message attribute that will be put as request parameter, 
     * containing the exception message that can be showed in the redirected page;
     * it defaults to "exceptionMessage".
     */
    public String getExceptionMessageAttribute() {
        return this.exceptionMessageAttribute;
    }

    /**
     * Set the name of the exception message attribute that will be put as request parameter, 
     * containing the exception message that can be showed in the redirected page;
     * it defaults to "exceptionMessage".
     */
    public void setExceptionMessageAttribute(String exceptionMessageAttribute) {
        this.exceptionMessageAttribute = exceptionMessageAttribute;
    }
}
