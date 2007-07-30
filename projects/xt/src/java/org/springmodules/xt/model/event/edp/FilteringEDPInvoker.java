/*
 * Copyright 2007 the original author or authors.
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

package org.springmodules.xt.model.event.edp;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationEvent;
import org.springmodules.xt.model.event.filtering.FilteringApplicationListener;

/**
 * {@link DefaultEDPInvoker} implementation with filtering capabilities, to use with
 * a {@link org.springmodules.xt.model.event.filtering.FilteringApplicationEventMulticaster}.
 * <br><br>
 * Just set the supported event classes (see {@link #setSupportedEventClasses(Class[] )}) to notify the EDP only
 * of interesting events.
 * <br><br>
 * <b>Note</b>: This class is <b>not</b> thread-safe.
 *
 * @author Sergio Bossa
 */
public class FilteringEDPInvoker extends DefaultEDPInvoker implements FilteringApplicationListener {
    
    private static final Logger logger = Logger.getLogger(FilteringEDPInvoker.class);
    
    private Class[] supportedEventClasses = new Class[0];
    
    /**
     * Set the array of Class objects which this EDPInvoker is interested to.
     */
    public void setSupportedEventClasses(Class[] supportedEventClasses) {
        this.supportedEventClasses = supportedEventClasses;
    }

    /**
     * Get the array of Class objects which this EDPInvoker is interested to.
     */
    public Class[] getSupportedEventClasses() {
        return this.supportedEventClasses;
    }

    /**
     * Accept all events by default.<br>
     * Just override the {@link #internalAccepts(ApplicationEvent )} method to change this behaviour.
     */
    public final boolean accepts(ApplicationEvent event) {
        return this.internalAccepts(event);
    }

    protected boolean internalAccepts(ApplicationEvent event) {
        return true;
    }
}
