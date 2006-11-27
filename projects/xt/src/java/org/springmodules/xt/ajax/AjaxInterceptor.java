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
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;
import org.springmodules.xt.ajax.support.ModelHolder;
import org.springmodules.xt.ajax.support.NoMatchingHandlerException;
import org.springmodules.xt.ajax.support.UnsupportedEventException;
import org.springmodules.xt.ajax.action.RedirectAction;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.mvc.BaseCommandController;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.util.UrlPathHelper;

/**
 * <p>Spring web interceptor which intercepts http requests and handles ajax requests.<br> 
 * Ajax requests are identified by a particular request parameter, by default named "ajax-request": it can assume two different values, depending on the type of ajax request:
 * an "action request", causing no form submission, and a "submit request", causing form submission.</p>
 *
 * <p>This interceptor delegates ajax requests handling to {@link AjaxHandler}s  configured via handler mappings (@see AjaxInterceptor#setHandlerMappings()).</p>
 *
 * <p>Configured mappings are a {@link java.util.Properties} file / object where each entry associates an ANT based URL path with a comma separated list of 
 * {@link AjaxHandler}s configured in the Spring application context; when associating the same URL pattern with multiple handlers, 
 * all handlers will be merged.</p>
 *
 * <p>When the interceptor receives an ajax request, it looks its mappings for an appropriate set of handlers: then, each handler will be evaluated following
 * the longest path match order, that is, an handler configured in the path "/test" will be evaluated prior to an handler configured in the path "/*".</p>
 *
 * <p>Finally, the first handler supporting the ajax event associated with the request will be executed.<br>
 * If the same URL is associated with more than one handler, the one supporting the current event will be executed.</p>
 *
 * <p>Note that if more handlers support the same event, the one configured for matching the longest path will be executed (in the example above,
 * the one configured for the path "/test"): this is useful for overriding event handlers.</p>
 *
 * @author Sergio Bossa
 */
public class AjaxInterceptor extends HandlerInterceptorAdapter implements ApplicationContextAware {
    
    public static final String AJAX_ACTION_REQUEST = "ajax-action";
    public static final String AJAX_SUBMIT_REQUEST = "ajax-submit";
    public static final String AJAX_REDIRECT_PREFIX = "ajax-redirect:";
    public static final String AJAX_VIEW_KEYWORD = "ajax-view";
    
    private static final Logger logger = Logger.getLogger(AjaxInterceptor.class);
    
    private static final ModelHolder modelHolder = new ModelHolder();
    
    private UrlPathHelper urlPathHelper = new UrlPathHelper();
    private PathMatcher pathMatcher = new AntPathMatcher();
    
    private String ajaxParameter = "ajax-request";
    private String eventParameter = "event-id";
    private String elementParameter = "source-element";
    private String elementIdParameter = "source-element-id";
    private String jsonParamsParameter = "json-params";
    
    private MultiMap handlerMappings;
    
    private ApplicationContext applicationContext;
    
    /**
     * Pre-handle the http request and if this is an ajax request firing an action, looks for a mapped ajax handler, executes it and
     * returns an ajax response.<br>
     * Note: if the matching mapped handler returns a null ajax response, the interceptor proceed with the execution chain.
     *
     * @throws IllegalStateException If the ajax request doesn't have an event id as request parameter.
     * @throws UnsupportedEventException If the event associated with this ajax request is not supported by any
     * mapped handler.
     * @throws NoMatchingHandlerException If no mapped handler matching the URL can be found.
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) 
    throws Exception {
        String requestType = request.getParameter(this.ajaxParameter);
        if (requestType != null && requestType.equals(AJAX_ACTION_REQUEST)) {
            String eventId = request.getParameter(this.eventParameter);
            if (eventId == null) {
                throw new IllegalStateException("Event id cannot be null.");
            }
            logger.info(new StringBuilder("Pre-handling ajax request for event: ").append(eventId));
            
            List<AjaxHandler> handlers = this.lookupHandlers(request);
            if (handlers != null) {
                AjaxActionEvent event = new AjaxActionEventImpl(eventId, request);
                AjaxResponse ajaxResponse = null;
                boolean supported = false;
                for (AjaxHandler ajaxHandler : handlers) {
                    if (ajaxHandler.supports(event)) {
                        // Set base event properties:
                        this.initEvent(event, request);
                        if (handler instanceof BaseCommandController) {
                            // Get the command name:
                            String commandName = ((BaseCommandController) handler).getCommandName();
                            // Set the command object:
                            Map model = AjaxInterceptor.modelHolder.getModel();
                            if (model != null) {
                                event.setCommandObject(model.get(commandName));
                            }
                        }
                        // Handling event:
                        ajaxResponse = ajaxHandler.handle(event);
                        supported = true;
                        break;
                    }
                }
                if (!supported) {
                    throw new UnsupportedEventException("Cannot handling the given event with id: " + eventId);
                }
                else {
                    if (ajaxResponse != null) {
                        this.sendResponse(response, ajaxResponse.getResponse());
                        return false;
                    }
                    else {
                        logger.info("Null Ajax response after Ajax action, proceeding with the request.");
                        return true;
                    }
                }
            }
            else {
                throw new NoMatchingHandlerException("Cannot find an handler matching the request: " + 
                        this.urlPathHelper.getLookupPathForRequest(request));
            }
        }
        else {
            return true;
        }
    }

    /**
     * Post-handle the http request and if it was an ajax request firing a submit, looks for a mapped ajax handler, executes it and
     * returns an ajax response.<br>
     * Note: if the matching mapped handler returns a null ajax response, the interceptor redirects to the configured view.
     *
     * @throws IllegalStateException If the ajax request doesn't have an event id as request parameter.
     * @throws UnsupportedEventException If the event associated with this ajax request is not supported by any
     * mapped handler.
     * @throws NoMatchingHandlerException If no mapped handler matching the URL can be found.
     */
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) 
    throws Exception {
        // Set the model map:
        AjaxInterceptor.modelHolder.set(modelAndView.getModel());
        // Continue processing:
        String requestType = request.getParameter(this.ajaxParameter);
        if (requestType != null && requestType.equals(AJAX_SUBMIT_REQUEST)) {
            String eventId = request.getParameter(this.eventParameter); 
            if (eventId == null) {
                throw new IllegalStateException("Event id cannot be null.");
            }
            logger.info(new StringBuilder("Post-handling ajax request for event: ").append(eventId));
            
            List<AjaxHandler> handlers = this.lookupHandlers(request);
            if (handlers != null) {
                AjaxSubmitEvent event = new AjaxSubmitEventImpl(eventId, request);
                AjaxResponse ajaxResponse = null;
                boolean supported = false;
                for (AjaxHandler ajaxHandler : handlers) {
                    if (ajaxHandler.supports(event)) {
                        // Set base event properties:
                        this.initEvent(event, request);
                        if (handler instanceof BaseCommandController) {
                            // Get the command name:
                            String commandName = ((BaseCommandController) handler).getCommandName();
                            // Set validation errors:
                            RequestContext requestContext = new RequestContext(request, modelAndView.getModel());
                            event.setValidationErrors(requestContext.getErrors(commandName));
                            // Set the command object:
                            Map model = AjaxInterceptor.modelHolder.getModel();
                            if (model != null) {
                                event.setCommandObject(model.get(commandName));
                            }
                        }
                        // Set the model:
                        event.setModel(AjaxInterceptor.modelHolder.getModel());
                        // Handling event: 
                        ajaxResponse = ajaxHandler.handle(event);
                        supported = true;
                        break;
                    }
                }
                if (!supported) {
                    throw new UnsupportedEventException("Cannot handling the given event with id: " + eventId);
                }
                else {
                    String view = modelAndView.getViewName();
                    if (ajaxResponse != null) {
                        if ((view == null) || (! view.equals(AJAX_VIEW_KEYWORD))) {
                            StringBuilder msg = new StringBuilder("Warning: you should configure the ")
                            .append(AJAX_VIEW_KEYWORD)
                            .append(" keyword as model view name. Found: ")
                            .append(view);
                            logger.warn(msg);
                            // This warning is raised because the user should configure the AJAX_VIEW_KEYWORD in order to
                            // make it explicit that we are using Ajax for rendering.
                        }
                        // Need to clear the ModelAndView because we are handling the response by ourselves:
                        modelAndView.clear();
                        this.sendResponse(response, ajaxResponse.getResponse());
                    }
                    else {
                        if ((view != null) && (view.startsWith(AJAX_REDIRECT_PREFIX))) {
                            view = view.substring(AJAX_REDIRECT_PREFIX.length());
                        }
                        else {
                            StringBuilder msg = new StringBuilder("After Ajax submit, no Ajax redirect prefix: ")
                            .append(AJAX_REDIRECT_PREFIX)
                            .append(" configured, so we are handling an ajax redirect to: ")
                            .append(view);
                            logger.warn(msg);
                        }
                        // Creating Ajax redirect action:
                        AjaxResponse ajaxRedirect = new AjaxResponseImpl();
                        AjaxAction ajaxAction = new RedirectAction(new StringBuilder(request.getContextPath()).append(view).toString(), modelAndView);
                        ajaxRedirect.addAction(ajaxAction);
                        // Need to clear the ModelAndView because we are handling the response by ourselves:
                        modelAndView.clear();
                        this.sendResponse(response, ajaxRedirect.getResponse());
                    }
                }
            }
            else {
                throw new NoMatchingHandlerException("Cannot find an handler matching the request: " + 
                        this.urlPathHelper.getLookupPathForRequest(request));
            }
        }
    }
    
    /**
     * Set mappings configured in the given {@link java.util.Properties} object.<br>
     * Each mapping associates an ANT based URL path with a comma separated list of {@link AjaxHandler}s configured in the Spring Application Context.<br>
     * Mappings are ordered in a sorted map, following the longest path order (from the longest path to the shorter).<br>
     * Please note that multiple mappings to the same URL are supported thanks to a {@link org.apache.commons.collections.map.MultiValueMap}.
     *
     * @param mappings A {@link java.util.Properties} containing handler mappings.
     */
    public void setHandlerMappings(Properties mappings) {
        this.handlerMappings = MultiValueMap.decorate(new TreeMap<String, String>(new Comparator() {
            public int compare(Object o1, Object o2) {
                if (!(o1 instanceof String) && !(o2 instanceof String)) {
                    throw new ClassCastException("You have to map an URL to a comma separated list of handler names.");
                }
                if (o1.equals(o2)) {
                    return 0;
                }
                else if (o1.toString().length() > o2.toString().length()) {
                    return -1;
                }
                else {
                    return 1;
                }
            }
        }));
        
        for (Map.Entry entry : mappings.entrySet()) {
            String[] handlers = ((String) entry.getValue()).split(",");
            for (String handler : handlers) {
                this.handlerMappings.put((String) entry.getKey(), handler.trim());
            }
        }
    }
    
    public void setAjaxParameter(String ajaxParameter) {
        this.ajaxParameter = ajaxParameter;
    }

    public void setElementParameter(String elementParameter) {
        this.elementParameter = elementParameter;
    }
    
    public void setElementIdParameter(String elementIdParameter) {
        this.elementIdParameter = elementIdParameter;
    }

    public void setEventParameter(String eventParameter) {
        this.eventParameter = eventParameter;
    }
    
    public void setJsonParamsParameter(String jsonParamsParameter) {
        this.jsonParamsParameter = jsonParamsParameter;
    }
    
    public String getAjaxParameter() {
        return this.ajaxParameter;
    }

    public String getElementParameter() {
        return this.elementParameter;
    }
    
    public String getElementIdParameter() {
        return this.elementIdParameter;
    }

    public String getEventParameter() {
        return this.eventParameter;
    }
    
    public String getJsonParamsParameter() {
        return this.jsonParamsParameter;
    }
    
    public void setApplicationContext(ApplicationContext applicationContext) 
    throws BeansException {
        this.applicationContext = applicationContext;
    }
    
    /**
     * Look up ajax handlers associated with the URL path of the given request.
     * <p>Supports direct matches, e.g.  "/test" matches "/test",
     * and various Ant-style pattern matches, e.g. "/t*" matches
     * both "/test" and "/team".</p>
     * <p>Remember that multiple matches are merged: e.g., if both "/test" and "/t*" match, 
     * mappings will be merged and evaluated following longest path order.</p>
     * <p>Moreover, remember that multiple handlers can be mapped to the same URL.</p>
     *
     * @param request The current http request.
     * @return A {@link java.util.List} of {@link AjaxHandler}s associated with the URL of the given request, or null if no matching is found.
     */
    protected List<AjaxHandler> lookupHandlers(HttpServletRequest request) {   
        String urlPath = this.urlPathHelper.getLookupPathForRequest(request);
        List<AjaxHandler> handlers = new LinkedList<AjaxHandler>();
        for (Map.Entry entry : (Set<Map.Entry>) this.handlerMappings.entrySet()) {
            String configuredPath = (String) entry.getKey();
            if (this.pathMatcher.match(configuredPath, urlPath)) {
                Collection handlerNames = (Collection) entry.getValue();
                for (Object handlerName : handlerNames) {
                    AjaxHandler current = (AjaxHandler) this.applicationContext.getBean((String) handlerName);
                    if (current != null) { 
                        handlers.add(current);
                    } 
                    else {
                        logger.warn(new StringBuilder("Non-existent handler ").append(handlerName).append(" mapped at ").append(configuredPath));
                    }
                }
            }
        }

        return handlers;
    }
    
    /**
     * Send the ajax response.
     */
    protected void sendResponse(HttpServletResponse httpResponse, String response) 
    throws IOException {
        ServletOutputStream out = httpResponse.getOutputStream();
        logger.debug(new StringBuilder("Sending ajax response: ").append(response));
        httpResponse.setContentType("text/xml");
        httpResponse.setHeader("Cache-Control", "no-cache");
        out.print(response);
        out.close();
    }
    
    private void initEvent(AjaxEvent event, HttpServletRequest request) {
        String paramsString = request.getParameter(this.jsonParamsParameter);
        if (paramsString != null) {
            Map<String, String> parameters = new HashMap<String, String>();
            JSONObject json = new JSONObject(paramsString);
            Iterator keys = json.keys();
            while (keys.hasNext()) {
                String key = keys.next().toString();
                parameters.put(key, json.opt(key).toString());
            }
            event.setParameters(parameters);
        }
        
        event.setElementName(request.getParameter(this.elementParameter));
        event.setElementId(request.getParameter(this.elementIdParameter));
    }
}
