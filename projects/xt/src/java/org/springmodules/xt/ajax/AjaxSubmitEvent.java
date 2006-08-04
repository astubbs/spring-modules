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
import org.springframework.validation.Errors;

/**
 * Special interface for an ajax submit event.<br>
 * It carries:
 * <ul>
 *      <li>The {@link org.springframework.validation.Errors} object containing validation errors, if any.</li>
 *      <li>The command object associated with the submitted form (if any), with all values bound.</li>
 *      <li>A {@link java.util.Map} containing the model set on submit.</li>
 * </ul>
 * The {@link org.springframework.validation.Errors} and command objects are set if the ajax submit event is generated from a form 
 * handled by a {@link org.springframework.web.servlet.mvc.BaseCommandController}, while the model map is always set but can be null if
 * the controller returned no model: they are the same form command object, errors and model map you use in the controller.<br>
 * Moreover, the command object is set if and only if the {@link org.springframework.web.servlet.ModelAndView} returned by the controller contains the original model, that is,
 * <code>errors.getModel()</code> is used for setting the {@link org.springframework.web.servlet.ModelAndView} model and adding other objects.
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
    
    /**
     * Get the command object associated with the form (if any), with all values bound.
     * @return The command object, or null if no command object has been found.
     */
    public Object getCommandObject();
    
    /**
     * Set the command object associated with the form (if any), with all values bound.
     * @param command The command object.
     */
    public void setCommandObject(Object command);
    
    /**
     * Get the model map (if any).
     * @return The model map, or null if no model has been found.
     */
    public Map getModel();
    
    /**
     * Set the model map (if any).
     * @param model The model map.
     */
    public void setModel(Map model);
}
