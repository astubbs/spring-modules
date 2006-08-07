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
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
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
 * <p>This interceptor delegates ajax requests handling to {@link AjaxHandler}s  configured via handler mappings (@see AjaxInterceptor#setHandlerMappings()). </p>
 * <p>Configured mappings are a {@link java.util.Properties} file / object where each entry associates an ANT based URL path with an {@link AjaxHandler}
 * configured in the Spring application context; you can also associate the same URL with multiple handlers: all handlers will be merged.</p>
 * <p>When the interceptor receives an ajax request, it looks its mappings for an appropriate set of handlers: then, each handler will be evaluated following
 * the longest path match order, that is, an handler configured in the path "/test" will be evaluated prior to an handler configured in the path "/*".</p>
 * <p>Finally, the first handler supporting the ajax event associated with the request will be executed.</p>
 * <p>Note that if two handlers support the same event, the one configured for matching the longest path will be executed (in the example above,
 * the one configured for the path "/test"): this is useful for overriding event handlers.</p>
 *
 * @author Sergio Bossa
 */
public class AjaxInterceptor extends HandlerInterceptorAdapter implements ApplicationContextAware {
    
    private static final Logger logger = Logger.getLogger(AjaxInterceptor.class);
    
    public static final String AJAX_ACTION_REQUEST = "ajax-action";
    public static final String AJAX_SUBMIT_REQUEST = "ajax-submit";
    public static final String AJAX_REDIRECT_PREFIX = "ajax-redirect:";
    
    private UrlPathHelper urlPathHelper = new UrlPathHelper();
    private PathMatcher pathMatcher = new AntPathMatcher();
    
    private String ajaxParameter = "ajax-request";
    private String eventParameter = "event-id";
    private String elementParameter = "source-element";
    private String elementIdParameter = "source-element-id";
    private String jsonParamsParameter = "json-params";
    
    private SortedMap<String, String> handlerMappings = new TreeMap();
    
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
            
            logger.warn(new StringBuilder("Pre-handling ajax request for event: ").append(eventId));
            
            List<AjaxHandler> handlers = this.lookupHandlers(request);
            if (handlers != null) {
                AjaxActionEvent event = new AjaxActionEventImpl(eventId, request);
                AjaxResponse ajaxResponse = null;
                
                boolean supported = false;
                for (AjaxHandler ajaxHandler : handlers) {
                    if (ajaxHandler.supports(event)) {
                        this.initEvent(event, request);
                        if (handler instanceof BaseCommandController) {
                            String commandName = ((BaseCommandController) handler).getCommandName();
                            event.setCommandObject(request.getAttribute(commandName));
                        }
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
     * Note: if the matching mapped handler returns a null ajax response, the interceptor proceed with the execution chain.
     *
     * @throws IllegalStateException If the ajax request doesn't have an event id as request parameter.
     * @throws UnsupportedEventException If the event associated with this ajax request is not supported by any
     * mapped handler.
     * @throws NoMatchingHandlerException If no mapped handler matching the URL can be found.
     */
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) 
    throws Exception {
        String requestType = request.getParameter(this.ajaxParameter);
        
        if (requestType != null && requestType.equals(AJAX_SUBMIT_REQUEST)) {
            String eventId = request.getParameter(this.eventParameter);    
            if (eventId == null) {
                throw new IllegalStateException("Event id cannot be null.");
            }
            
            logger.warn(new StringBuilder("Post-handling ajax request for event: ").append(eventId));
            
            List<AjaxHandler> handlers = this.lookupHandlers(request);
            if (handlers != null) {
                AjaxSubmitEvent event = new AjaxSubmitEventImpl(eventId, request);
                AjaxResponse ajaxResponse = null;

                boolean supported = false;
                for (AjaxHandler ajaxHandler : handlers) {
                    if (ajaxHandler.supports(event)) {
                        this.initEvent(event, request);
                        if (handler instanceof BaseCommandController) {
                            String commandName = ((BaseCommandController) handler).getCommandName();
                            RequestContext requestContext = new RequestContext(request, modelAndView.getModel());
                            event.setValidationErrors(requestContext.getErrors(commandName));
                            if (modelAndView.getModel() != null) {
                                event.setCommandObject(modelAndView.getModel().get(commandName));
                            }
                        }
                        event.setModel(modelAndView.getModel());
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
                        // Need to clear the ModelAndView because we are handling the response by ourselves:
                        modelAndView.clear();
                        this.sendResponse(response, ajaxResponse.getResponse());
                    }
                    else {
                        String view = modelAndView.getViewName();
                        if (view.startsWith(AJAX_REDIRECT_PREFIX)) {
                            String path = view.substring(AJAX_REDIRECT_PREFIX.length());
                            
                            logger.warn(new StringBuilder("After Ajax submit, handling an Ajax redirect to: ").append(path));

                            AjaxResponse ajaxRedirect = new AjaxResponseImpl();
                            AjaxAction ajaxAction = new RedirectAction(request.getContextPath() + path, modelAndView);
                            ajaxRedirect.addAction(ajaxAction);

                            // Need to clear the ModelAndView because we are handling the response by ourselves:
                            modelAndView.clear();
                            this.sendResponse(response, ajaxRedirect.getResponse());
                        }
                        else {
                            logger.warn(new StringBuilder("After Ajax submit, handling a normal forward/redirect to: ").append(view));
                        }
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
     * Each mapping associates an ANT based URL path with the name of an {@link AjaxHandler} configured in the Spring Application Context.<br>
     * Mappings are ordered in a sorted map, following the longest path order (from the longest path to the shorter).<br>
     *
     * @param mappings A {@link java.util.Properties} containing handler mappings.
     */
    public void setHandlerMappings(Properties mappings) {
        this.handlerMappings = new TreeMap(new Comparator() {
            public int compare(Object o1, Object o2) {
                if (!(o1 instanceof String) && !(o2 instanceof String)) {
                    throw new ClassCastException();
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
        });
        
        for (Map.Entry entry : mappings.entrySet()) {
            this.handlerMappings.put((String) entry.getKey(), (String) entry.getValue());
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
     *
     * @param request The current http request.
     * @return A {@link java.util.List} of {@link AjaxHandler}s associated with the URL of the given request, or null if no matching is found.
     */
    protected List<AjaxHandler> lookupHandlers(HttpServletRequest request) {   
        String urlPath = this.urlPathHelper.getLookupPathForRequest(request);
        List<AjaxHandler> handlers = new LinkedList();
        for (Map.Entry<String, String> entry : this.handlerMappings.entrySet()) {
            String configuredPath = entry.getKey();
            if (this.pathMatcher.match(configuredPath, urlPath)) {
                AjaxHandler current = (AjaxHandler) this.applicationContext.getBean(entry.getValue());
                if (current != null) handlers.add(current);
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
        
        logger.debug("Sending ajax response: " + response);
        
        httpResponse.setContentType("text/xml");
        httpResponse.setHeader("Cache-Control", "no-cache");
        out.print(response);
        out.close();
    }
    
    private void initEvent(AjaxEvent event, HttpServletRequest request) {
        String paramsString = request.getParameter(this.jsonParamsParameter);
        if (paramsString != null) {
            Map<String, String> parameters = new HashMap();
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
