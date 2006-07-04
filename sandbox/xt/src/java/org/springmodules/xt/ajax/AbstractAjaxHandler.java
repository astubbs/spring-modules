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

import java.lang.reflect.Method;
import org.springmodules.xt.ajax.support.UnsupportedEventException;

/**
 * Abstract enhanced {@link AjaxHandler} with the {@link #handle(AjaxEvent)} method implemented as a <i>dynamic template method</i> 
 * with automatic dispatching of different requests depending on the {@link AjaxEvent} id.<br>
 * For handling different requests, hence different events, you have to simply implement a method named after the AjaxEvent id, with the following signature:<br>
 * <br><code>public {@link AjaxResponse} eventId(<i>{@link AjaxEvent}</i> )</code><br><br>
 * The <i>AjaxEvent</i> in the signature above must be the effective subinterface of the {@link AjaxEvent} actually handled.<br>
 * Given an ajax event with id <i>countrySelection</i>, the handling method will be:<br>
 * <br><code>public {@link AjaxResponse} countrySelection({@link AjaxEvent} )</code><br><br>
 * The already implemented {@link #handle(AjaxEvent )} will do the appropriate dispatching.
 * <br><br>
 * NOTE: Implementors are required to be thread-safe.
 *
 * @author Sergio Bossa
 */
public abstract class AbstractAjaxHandler implements AjaxHandler {
    
    /**
     * Dynamic template method for handling ajax requests depending on the event id.<br>
     * @see AjaxHandler#handle(AjaxEvent )
     */
    public AjaxResponse handle(AjaxEvent event) {
        String id = event.getEventId();
        AjaxResponse response = null; 
        
        if (id == null) { 
            throw new IllegalArgumentException("Event id cannot be null."); 
        }
        
        try {
            // TODO: Cache reflection results ...
            Method m = this.getClass().getDeclaredMethod(id, new Class[]{this.getEventInterface(event)});
            response = (AjaxResponse) m.invoke(this, new Object[]{event});
        }
        catch(Exception ex) {
            throw new UnsupportedEventException("Cannot handling the given event with id: " + id, ex);
        }
        
        return response;
    }
    
    /**
     * Supports the given event if the concrete class implements a method for handling it, that is, a method
     * with the following signature:
     * <br><br><i>public {@link AjaxResponse} eventId({@link AjaxEvent} )</i><br><br>
     * @see AjaxHandler#supports(AjaxEvent )
     */
    public boolean supports(AjaxEvent event) {
        String id = event.getEventId();
        boolean result = false;
        
        if (id == null) { 
            throw new IllegalArgumentException("Event id cannot be null."); 
        }
        
        try {
            // TODO: Cache reflection results ...
            this.getClass().getDeclaredMethod(id, new Class[]{this.getEventInterface(event)});
            result = true;
        }
        catch(Exception ex) {
            result = false;
        }
        
        return result;
    }
    
    private Class getEventInterface(AjaxEvent e) {
        Class[] interfaces = e.getClass().getInterfaces();
        Class ret = e.getClass();
        for (Class intf : interfaces) {
            if (intf.isInterface() && AjaxEvent.class.isAssignableFrom(intf)) {
                ret = intf;
                break;
            }
        }
        return ret;
    }
}
