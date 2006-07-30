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
import javax.servlet.http.HttpServletRequest;
import org.springmodules.xt.ajax.AbstractAjaxHandler;
import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxSubmitEvent;
import org.springmodules.xt.ajax.component.Component;
import org.springmodules.xt.ajax.component.SimpleText;
import org.springmodules.xt.ajax.action.RemoveContentAction;
import org.springmodules.xt.ajax.action.ReplaceContentAction;
import org.springmodules.xt.ajax.AjaxResponseImpl;
import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

/**
 * Ready-to-use Ajax handler for handling validation errors on submit.<br>
 * This handler manages {@link org.springmodules.xt.ajax.AjaxSubmitEvent}s with id equals to "validate", replacing the content of HTML elements 
 * named after validation error codes, with related validation error messages, in case using {@link ErrorRenderingCallback}
 * if configured, or rendering error messages as simple text.<br>
 * If the fired {@link org.springmodules.xt.ajax.AjaxSubmitEvent} has no validation errors, this handler returns a null response.
 *
 * @author Sergio Bossa
 */
public class DefaultValidationHandler extends AbstractAjaxHandler implements MessageSourceAware {
    
    private static final Logger logger = Logger.getLogger(DefaultValidationHandler.class);
    
    private MessageSource messageSource;
    private ErrorRenderingCallback errorRenderingCallback;
    
    public AjaxResponse validate(AjaxSubmitEvent event) {
        AjaxResponseImpl response = null;
        
        if (event.getValidationErrors() != null && event.getValidationErrors().hasErrors() == true) {
            response = new AjaxResponseImpl();
            this.removeOldErrors(event, response);
            this.putNewErrors(event, response);
        }
        
        return response;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
    
    public void setErrorRenderingCallback(ErrorRenderingCallback errorRenderingCallback) {
        this.errorRenderingCallback = errorRenderingCallback;
    }

    private void removeOldErrors(AjaxSubmitEvent event, AjaxResponseImpl response) {
        HttpServletRequest request = event.getHttpRequest();
        Errors errors = (Errors) request.getSession().getAttribute(request.getRequestURL().toString());
         
        logger.debug("Removing old errors for URL: " + request.getRequestURL().toString());
        
        if (errors != null) {
            logger.debug("Found errors for URL: " + request.getRequestURL().toString());
            
            request.getSession().removeAttribute(request.getRequestURL().toString());
            
            for (Object o : errors.getAllErrors()) {
                ObjectError error = (ObjectError) o;
                RemoveContentAction action = new RemoveContentAction(error.getCode());
                response.addAction(action);
            }
        }
    }

    private void putNewErrors(AjaxSubmitEvent event, AjaxResponseImpl response) {
        Errors errors = event.getValidationErrors();
        HttpServletRequest request = event.getHttpRequest();
        Locale locale = LocaleContextHolder.getLocale(); // <- Get the current Locale, if any ...
        
        // Put new Errors into http session for later retrieval:
        request.getSession(true).setAttribute(request.getRequestURL().toString(), errors);
        logger.debug("Putting errors in session for URL: " + request.getRequestURL().toString());
        
        for (Object o : errors.getAllErrors()) {
            ObjectError error = (ObjectError) o;
            Component errorComponent = null;
            if (this.errorRenderingCallback != null) {
                errorComponent = this.errorRenderingCallback.getRenderingComponent(error, this.messageSource, locale);
            }
            else {
                errorComponent = new SimpleText(this.messageSource.getMessage(error.getCode(), null, error.getDefaultMessage(), locale));
            }
            ReplaceContentAction action = new ReplaceContentAction(error.getCode(), errorComponent);
            response.addAction(action);
        }
    }
}
