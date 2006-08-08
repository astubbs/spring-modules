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

package org.springmodules.xt.ajax.validation;

import java.util.Locale;
import org.springmodules.xt.ajax.AjaxAction;
import org.springmodules.xt.ajax.component.Component;
import org.springframework.context.MessageSource;
import org.springframework.validation.ObjectError;

/**
 * Callback to implement for rendering validation errors.
 *
 * @author Sergio Bossa
 */
public interface ErrorRenderingCallback {
    
    /**
     * Get the component that will render the given error object.
     *
     * @param error The error object.
     * @param messageSource A Spring MessageSource for looking up messages related to this error.
     * @param locale The locale to use for messages.
     */
    public Component getRenderingComponent(ObjectError error, MessageSource messageSource, Locale locale);
    
    /**
     * Get the action that will be executed <b>after</b> rendering the component.
     *
     * @param error The error object.
     */
    public AjaxAction getRenderingAction(ObjectError error);
}
