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
import org.springframework.web.servlet.mvc.BaseCommandController;
import org.springframework.web.servlet.support.RequestContext;

/**
 * Access form related data in a Spring MVC environment.
 *
 * @author Sergio Bossa
 */
public class MVCFormDataAccessor implements FormDataAccessor {
    
    public Object getCommandObject(HttpServletRequest request, HttpServletResponse response, Object handler, Map model) {
        if (handler instanceof BaseCommandController) {
            String commandName = ((BaseCommandController) handler).getCommandName();
            return model.get(commandName);
        } else {
            return null;
        }
    }
    
    public Errors getValidationErrors(HttpServletRequest request, HttpServletResponse response, Object handler, Map model) {
        if (handler instanceof BaseCommandController) {
            String commandName = ((BaseCommandController) handler).getCommandName();
            RequestContext requestContext = new RequestContext(request, model);
            return requestContext.getErrors(commandName);
        } else {
            return null;
        }
    }
}
