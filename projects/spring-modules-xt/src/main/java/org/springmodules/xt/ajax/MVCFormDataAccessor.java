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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.support.RequestContext;

/**
 * Access form related data in a Spring MVC environment.<br>
 * It works with all Spring MVC controllers that satisfy one of the following requirements:
 * <ul>
 * <li>Subclass {@link org.springframework.web.servlet.mvc.BaseCommandController}.</li>
 * <li>Provide a getter method named as {@link #getCommandGetter()} that returns the name
 * of the command object: by default the {@link #getCommandGetter()} returns <i>getCommandName</i>.</li>
 * </ul>
 *
 * @author Sergio Bossa
 */
public class MVCFormDataAccessor implements FormDataAccessor {

    private static final Logger logger = Logger.getLogger(MVCFormDataAccessor.class);
    private String commandNameGetter = "getCommandName";

    public Object getCommandObject(HttpServletRequest request, HttpServletResponse response, Object handler, Map model) {
        String commandName = this.resolveCommandName(handler);
        if (commandName != null) {
            return model.get(commandName);
        } else {
            logger.warn("No command name found for handler : " + handler);
            return null;
        }
    }

    public Errors getValidationErrors(HttpServletRequest request, HttpServletResponse response, Object handler, Map model) {
        String commandName = this.resolveCommandName(handler);
        if (commandName != null) {
            RequestContext requestContext = new RequestContext(request, model);
            return requestContext.getErrors(commandName);
        } else {
            logger.warn("No command name found for handler : " + handler);
            return null;
        }
    }

    /**
     * Set the name of the method on the Spring MVC controller returning the name of the command object.<br>
     * Use this method if your controllers provide a custom method for returning the command name.
     * 
     * @param commandNameGetter The name of the command name getter method.
     */
    public void setCommandNameGetter(String commandNameGetter) {
        this.commandNameGetter = commandNameGetter;
    }

    /**
     * Get the name of the method on the Spring MVC controller returning the name of the command object.<br>
     * By default it's <i>getCommandName</i>.
     * 
     * @return The name of the command name getter method.
     */
    public String getCommandNameGetter() {
        return this.commandNameGetter;
    }
    
    private String resolveCommandName(Object handler) {
        String commandName = null;
        try {
            Method getter = handler.getClass().getMethod(this.commandNameGetter);
            commandName = (String) getter.invoke(handler);
        } catch (IllegalAccessException ex) {
            logger.warn(ex.getMessage(), ex);
        } catch (IllegalArgumentException ex) {
            logger.warn(ex.getMessage(), ex);
        } catch (InvocationTargetException ex) {
            logger.warn(ex.getMessage(), ex);
        } catch (NoSuchMethodException ex) {
            logger.warn(ex.getMessage(), ex);
        }
        return commandName;
    }
}
