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

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springmodules.xt.ajax.util.AjaxResponseSender;

/**
 * {@link org.springframework.web.servlet.HandlerExceptionResolver} implementation 
 * to deal with exceptions occurred while processing the Ajax request. It works by 
 * mapping an exception class with an {@link AjaxExceptionHandler}
 * that will be used to actually handle the exception in a response delivered to clients
 * (see {@link #setExceptionMappings(Map)}).
 *
 * @author Sergio Bossa
 */
public class AjaxExceptionHandlerResolver implements ApplicationContextAware, HandlerExceptionResolver, Ordered {
    
    private static final Logger logger = Logger.getLogger(AjaxExceptionHandlerResolver.class);
    
    public static final int ORDER = -5;
    
    private int internalOrder = ORDER;
    private Map<Class<? extends Exception>, AjaxExceptionHandler> exceptionMappings = new LinkedHashMap<Class<? extends Exception>, AjaxExceptionHandler>();
    private ApplicationContext applicationContext;
    
    /**
     * Associate the best {@link AjaxExceptionHandler} to the given exception.<br>
     * The association is done using the actual exception class: if an {@link AjaxExceptionHandler}
     * is mapped for both the actual exception class and one of its superclasses,
     * the exception handler configured for the actual exception class will be used.<br><br>
     * Once an {@link AjaxExceptionHandler} is found, its ajax response is sent to the client and
     * a cleared ModelAndView object is returned.<br><br>
     * If no {@link AjaxExceptionHandler} is found, a null ModelAndView object is returned
     * and other {@link org.springframework.web.servlet.HandlerExceptionResolver}s will be processed.<br><br>
     * Please note that the exception is resolved only if the current request is an Ajax one; otherwise,
     * other {@link org.springframework.web.servlet.HandlerExceptionResolver}s will be processed.
     * 
     * @return A cleared ModelAndView, or null.
     */
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            if (this.isAjaxRequest(request)) {
                // Important : log the exception
                logger.error(ex.getMessage(), ex);
                // !!!
                AjaxExceptionHandler exceptionHandler = this.lookupExceptionHandler(ex);
                if (exceptionHandler != null) {
                    logger.info(new StringBuilder("Resolving exception of type : ").append(ex.getClass()));
                    AjaxResponse ajaxResponse = exceptionHandler.handle(request, ex);
                    if (ajaxResponse != null) {
                        AjaxResponseSender.sendResponse(response, ajaxResponse);
                        ModelAndView mv = new ModelAndView();
                        mv.clear();
                        return mv;
                    } 
                    else {
                        logger.info("Null Ajax response after resolving exception.");
                        return null;
                    }
                } 
                else {
                    logger.info("No exception resolver found.");
                    return null;
                }
            } else {
                return null;
            }
        } 
        catch (IOException currEx) {
            logger.error("Unexpected exception ... ", currEx);
            return null;
        }
    }
    
    public void setApplicationContext(ApplicationContext applicationContext)
    throws BeansException {
        this.applicationContext = applicationContext;
    }
    
    public int getOrder() {
        return this.internalOrder;
    }
    
    /**
     * Set the mappings that associate exception classes to {@link AjaxExceptionHandler}s.
     * 
     * @param mappings A map containing exception to resolver mappings.
     */
    public void setExceptionMappings(Map<Class<? extends Exception>, AjaxExceptionHandler> mappings) {
        this.exceptionMappings = mappings;
    }
    
    /**
     * Use in subclasses for changing order.
     */
    protected void setInternalOrder(int order) {
        this.internalOrder = order;
    }
    
    /**
     * {@link AjaxExceptionHandler} actual lookup.
     * 
     * @param ex The exception to use for looking up the handler.
     * @return The {@link AjaxExceptionHandler} or null if no resolver
     * is found for the given exception.
     */
    protected AjaxExceptionHandler lookupExceptionHandler(Exception ex) {
        Class bestMatch = null;
        for (Class current : this.exceptionMappings.keySet()) {
            if (current.isAssignableFrom(ex.getClass())) {
                if (bestMatch == null || (bestMatch != null && bestMatch.isAssignableFrom(current))) {
                    bestMatch = current;
                }
            }
        }
        if (bestMatch != null) {
            return this.exceptionMappings.get(bestMatch);
        } else {
            return null;
        }
    }
    
    private boolean isAjaxRequest(HttpServletRequest request) {
        Collection beans = this.applicationContext.getBeansOfType(AjaxInterceptor.class).values();
        if (beans.size() > 1) {
            throw new IllegalStateException("Error: more AjaxInterceptors configured!");
        } 
        else {
            AjaxInterceptor interceptor = (AjaxInterceptor) beans.iterator().next();
            return interceptor.isAjaxRequest(request);
        }
    }
}
