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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.log4j.Logger;
import org.springmodules.xt.ajax.support.EventHandlingException;
import org.springmodules.xt.ajax.support.UnsupportedEventException;

/**
 * Abstract {@link AjaxHandler} with the {@link #handle(AjaxEvent)} method implemented as a <i>dynamic template method</i> 
 * with automatic dispatching of different requests depending on the {@link AjaxEvent} id.<br>
 * For handling different requests, hence different events, you have to simply implement a method named after the AjaxEvent id, with the following signature:
 * <br><br>
 * <code>public {@link AjaxResponse} eventId(<i>{@link AjaxEvent}</i> )</code>
 * <br><br>
 * The <i>AjaxEvent</i> in the signature above must be the effective subinterface of the {@link AjaxEvent} actually handled.<br>
 * Given an ajax event with id <i>countrySelection</i>, the handling method will be:
 * <br><br>
 * <code>public {@link AjaxResponse} countrySelection({@link AjaxEvent} )</code>
 * <br><br>
 * The already implemented {@link #handle(AjaxEvent )} will do the appropriate dispatching.
 * <br><br>
 * NOTE: Implementors are required to be thread-safe.
 *
 * @author Sergio Bossa
 */
public abstract class AbstractAjaxHandler implements AjaxHandler {
    
    private static final Logger logger = Logger.getLogger(AbstractAjaxHandler.class);
    
    /**
     * Dynamic template method for handling ajax requests depending on the event id.
     * <br><br>
     * @see AjaxHandler#handle(AjaxEvent )
     */
    public AjaxResponse handle(AjaxEvent event) {
        if (event == null || event.getEventId() == null) { 
            logger.error("Event and event id cannot be null.");
            throw new IllegalArgumentException("Event and event id cannot be null."); 
        }
        
        String id = event.getEventId();
        AjaxResponse response = null; 
        try {
            Method m = this.getMatchingMethod(event);
            if (m != null) {
                logger.info(new StringBuilder("Invoking method: ").append(m));
                response = (AjaxResponse) m.invoke(this, new Object[]{event});
            }
            else {
                logger.error("You need to call the supports() method first!");
                throw new UnsupportedEventException("You need to call the supports() method first!");
            }
        }
        catch(IllegalAccessException ex) {
            logger.error(ex.getMessage(), ex);
            logger.error("Cannot handle the given event with id: " + id);
            throw new UnsupportedEventException("Cannot handle the given event with id: " + id, ex);
        }
        catch(InvocationTargetException ex) {
            logger.error(ex.getMessage(), ex);
            logger.error("Exception while handling the given event with id: " + id);
            throw new EventHandlingException("Exception while handling the given event with id: " + id, ex);
        }
        
        return response;
    }
    
    /**
     * Supports the given event if the concrete class implements a method for handling it, that is, a method
     * with the following signature:
     * <br><br>
     * <i>public {@link AjaxResponse} eventId({@link AjaxEvent} )</i>
     * <br><br>
     * @see AjaxHandler#supports(AjaxEvent )
     */
    public boolean supports(AjaxEvent event) {
        String id = event.getEventId();
        
        if (id == null) { 
            logger.error("Event id cannot be null.");
            throw new IllegalArgumentException("Event id cannot be null."); 
        }
        
        Method m = this.getMatchingMethod(event);
        if (m != null) {
            logger.debug(new StringBuilder("Event supported by method: ").append(m));
            return true;
        }
        else {
            return false;
        }
    }
    
    /**
     * FIXME: Cache reflection results!
     */
    private Method getMatchingMethod(AjaxEvent event) {
        Class eventType = this.getEventType(event);
        Method[] methods = this.getClass().getMethods();
        Method ret = null;
        for (Method method : methods) {
            if (method.getName().equals(event.getEventId()) && method.getParameterTypes()[0].isAssignableFrom(eventType)) {
                ret = method;
                break;
            }
        }
        return ret;
    }
    
    /**
     * FIXME: Cache reflection results!
     */
    private Class getEventType(AjaxEvent event) {
        Class[] interfaces = event.getClass().getInterfaces();
        Class ret = event.getClass();
        for (Class intf : interfaces) {
            if (AjaxEvent.class.isAssignableFrom(intf)) {
                ret = intf;
                break;
            }
        }
        return ret;
    }
}
