/*
 * Copyright 2002-2005 the original author or authors.
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
package org.springmodules.validation.valang.javascript.taglib;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.mvc.BaseCommandController;
import org.springmodules.validation.valang.ValangValidator;

/**
 * Spring MVC interceptor implementation that will automatically export Valang 
 * validation rules that are used by any of the intercepted handlers into
 * the the ModelAndView so that they are accessible to the custom tag 
 * <code>ValangValidateTag</code>.
 * 
 * <p>Does nothing if the intercepted handler is not an instance of 
 * <code>BaseCommandController</code>, if the handler's validator 
 * implementation is not an instance of <code>ValangValidator</code> or
 * if the handler did not export a command object into the model.
 * 
 * @author Oliver Hutchison 
 * @see ValangValidateTag
 * @see ValangValidator
 */
public class ValangRulesExportInterceptor extends HandlerInterceptorAdapter {

    private static final Log logger = LogFactory.getLog(ValangRulesExportInterceptor.class);

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        if (handler instanceof BaseCommandController) {
            BaseCommandController controller = (BaseCommandController)handler;
            if (controller.getValidator() instanceof ValangValidator) {
                Map model = modelAndView.getModel();
                if (model == null || !model.containsKey(controller.getCommandName())) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Handler '" + handler + "' did not export command object '" + controller.getCommandName()
                                + "'; no rules added to model");
                    }
                }
                else {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Adding Valang rules from handler '" + handler + "' to model");
                    }
                    ValangJavaScriptTagUtils.addValangRulesToModel(controller, model);
                }
            }
            else {
                if (logger.isDebugEnabled()) {
                    logger.debug("Handler '" + handler
                            + "' does not have a validator of type 'ValangValidator'; no rules added to model");
                }
            }
        }
        else {
            if (logger.isDebugEnabled()) {
                logger.debug("Handler '" + handler
                        + "' is not an instance of BaseCommandController; no rules added to model");
            }
        }
    }
}
