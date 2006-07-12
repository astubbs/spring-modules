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

import org.springframework.validation.Errors;

/**
 * Special interface for an ajax submit event.<br>
 * It has also an {@link org.springframework.validation.Errors} object containing validation errors, if any.
 * 
 * @author Sergio Bossa
 */
public interface AjaxSubmitEvent extends AjaxEvent {
    
    /**
     * Get validation errors (if any).
     * @return An {@link org.springframework.validation.Errors} object, or null if there's no error.
     */
    public Errors getValidationErrors();
    
    /**
     * Set validation errors (if any).
     * @param errors An {@link org.springframework.validation.Errors} object.
     */
    public void setValidationErrors(Errors errors);
}
