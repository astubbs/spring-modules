/*
 * Copyright 2006 - 2007 the original author or authors.
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
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.Errors;

/**
 * Access form related data: the command object and validation errors.
 *
 * @author Sergio Bossa
 */
public interface FormDataAccessor {
    
    /**
     * Get the form command object.
     *
     * @param request 
     * @param response 
     * @param handler 
     * @param model 
     * @return The command object, or null if no command object has been found.
     */
    public Object getCommandObject(HttpServletRequest request, HttpServletResponse response, Object handler, Map model);
    
    /**
     * Get the form validation errors.
     *
     * @param request 
     * @param response 
     * @param handler 
     * @param model 
     * @return The form errors, or null if no error is found.
     */
    public Errors getValidationErrors(HttpServletRequest request, HttpServletResponse response, Object handler, Map model);
}
