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
import org.apache.log4j.Logger;
import org.springmodules.xt.ajax.AbstractAjaxHandler;
import org.springmodules.xt.ajax.AjaxAction;
import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxSubmitEvent;
import org.springmodules.xt.ajax.action.AppendContentAction;
import org.springmodules.xt.ajax.action.matcher.ElementMatcher;
import org.springmodules.xt.ajax.action.matcher.WildcardMatcher;
import org.springmodules.xt.ajax.component.Component;
import org.springmodules.xt.ajax.action.RemoveContentAction;
import org.springmodules.xt.ajax.AjaxResponseImpl;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springmodules.xt.ajax.validation.support.DefaultErrorRenderingCallback;
import org.springmodules.xt.ajax.validation.support.DefaultSuccessRenderingCallback;

/**
 * <p>
 * Ready-to-use Ajax handler for handling validation errors on submit.<br>
 * This handler manages {@link org.springmodules.xt.ajax.AjaxSubmitEvent}s with id equals to "validate": for each validation error, it looks for
 * an HTML element with id equals to the error code, and append error messages to it.<br>
 * The DefaultValidationHandler has also simple <b>wildcard matching</b> capabilities: if you want to put an error message into more
 * than one HTML element, just put the '_' wildcard at the end of the HTML element id. I.E., an error message with code <i>error.code</i>
 * will match both <i>error.code</i> and <i>error._</i> identifiers.
 * </p>
 * <p>
 * The rendering of the error message can be customized through the configurable  {@link ErrorRenderingCallback}: by default it uses the
 * {@link org.springmodules.xt.ajax.validation.support.DefaultErrorRenderingCallback}.
 * </p>
 * <p>
 * If the validation succeeds, that is, the fired {@link org.springmodules.xt.ajax.AjaxSubmitEvent} has no validation errors,
 * this handler renders the actions returned by the configurable {@link SuccessRenderingCallback}: by default it uses the
 * {@link org.springmodules.xt.ajax.validation.support.DefaultSuccessRenderingCallback}, which returns no action.<br>
 * If no actions are found, the handler returns a <code>null</code> response.
 * </p>
 *
 * @author Sergio Bossa
 */
public class DefaultValidationHandler extends AbstractAjaxHandler implements MessageSourceAware {
    
    private static final Logger logger = Logger.getLogger(DefaultValidationHandler.class);
    
    private static final String ERRORS_PREFIX = DefaultValidationHandler.class.getName() + " - ";
    
    private MessageSource messageSource;
    private ErrorRenderingCallback errorRenderingCallback = new DefaultErrorRenderingCallback();
    private SuccessRenderingCallback successRenderingCallback = new DefaultSuccessRenderingCallback();
    
    public AjaxResponse validate(AjaxSubmitEvent event) {
        AjaxResponseImpl response = null;
        
        if (event.getValidationErrors() != null && event.getValidationErrors().hasErrors() == true) {
            response = new AjaxResponseImpl();
            this.removeOldErrors(event, response);
            this.putNewErrors(event, response);
        } else {
            AjaxAction[] successActions = this.successRenderingCallback.getSuccessActions(event);
            if (successActions != null && successActions.length > 0) {
                response = new AjaxResponseImpl();
                for (AjaxAction action : successActions) {
                    response.addAction(action);
                }
            }
        }
        
        return response;
    }
    
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
    
    public void setErrorRenderingCallback(ErrorRenderingCallback errorRenderingCallback) {
        this.errorRenderingCallback = errorRenderingCallback;
    }
    
    public void setSuccessRenderingCallback(SuccessRenderingCallback successRenderingCallback) {
        this.successRenderingCallback = successRenderingCallback;
    }
    
    private void removeOldErrors(AjaxSubmitEvent event, AjaxResponseImpl response) {
        HttpServletRequest request = event.getHttpRequest();
        Errors errors = (Errors) request.getSession(true).getAttribute(this.getErrorsAttributeName(request));
        if (errors != null) {
            logger.debug("Found errors for URL: " + request.getRequestURL().toString());
            logger.debug("Removing old errors.");
            // Remove old errors from session:
            request.getSession(true).removeAttribute(this.getErrorsAttributeName(request));
            // Remove old errors from HTML:
            for (Object o : errors.getAllErrors()) {
                ObjectError error = (ObjectError) o;
                ElementMatcher matcher = new WildcardMatcher(error.getCode());
                RemoveContentAction removeAction = new RemoveContentAction(matcher);
                response.addAction(removeAction);
            }
        }
    }
    
    private void putNewErrors(AjaxSubmitEvent event, AjaxResponseImpl response) {
        Errors errors = event.getValidationErrors();
        HttpServletRequest request = event.getHttpRequest();
        Locale locale = LocaleContextHolder.getLocale(); // <- Get the current Locale, if any ...
        // Put new errors into http session for later retrieval:
        logger.debug("Putting errors in session for URL: " + request.getRequestURL().toString());
        request.getSession(true).setAttribute(this.getErrorsAttributeName(request), errors);
        // Put new errors into HTML:
        for (Object o : errors.getAllErrors()) {
            ObjectError error = (ObjectError) o;
            ElementMatcher matcher = new WildcardMatcher(error.getCode());
            Component renderingComponent = this.errorRenderingCallback.getErrorComponent(event, error, this.messageSource, locale);
            AppendContentAction appendAction = new AppendContentAction(matcher, renderingComponent);
            response.addAction(appendAction);
            // Get the actions to execute *after* rendering the component:
            AjaxAction[] renderingActions = this.errorRenderingCallback.getErrorActions(event, error);
            if (renderingActions != null) {
                for (AjaxAction renderingAction : renderingActions) {
                    response.addAction(renderingAction);
                }
            }
        }
    }
    
    private String getErrorsAttributeName(HttpServletRequest request) {
        return new StringBuilder(ERRORS_PREFIX).append(request.getRequestURL().toString()).toString();
    }
}
