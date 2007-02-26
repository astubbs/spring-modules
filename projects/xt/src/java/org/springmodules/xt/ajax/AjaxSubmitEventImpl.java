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
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.Errors;

/**
 * Implementation of {@link AjaxSubmitEvent}.
 *
 * @author Sergio Bossa
 */
public class AjaxSubmitEventImpl extends BaseAjaxEvent implements AjaxSubmitEvent {
    
    private static final long serialVersionUID = 26L;
    
    private Errors validationErrors;
    private Object commandObject;
    private Map model;
    
    public AjaxSubmitEventImpl(String eventId, HttpServletRequest httpRequest) {
        super(eventId, httpRequest);
    }
    
    public Errors getValidationErrors() {
        return this.validationErrors;
    }
    
    public void setValidationErrors(Errors errors) {
        this.validationErrors = errors;
    }

    public Object getCommandObject() {
        return this.commandObject;
    }

    public void setCommandObject(Object command) {
        this.commandObject = command;
    }

    public Map getModel() {
        return this.model;
    }

    public void setModel(Map model) {
        this.model = model;
    }
}
