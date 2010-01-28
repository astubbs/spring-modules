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

package org.springmodules.xt.model.event.filtering;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * {@link org.springframework.context.ApplicationListener} extended interface capable of filtering 
 * {@link org.springframework.context.ApplicationEvent}s multicasted through a 
 * {@link FilteringApplicationEventMulticaster}.
 * <br><br>
 * Implementors will have to implement the processing logic (as always) <b>plus</b> the filtering logic,
 * that is, <b>which</b> event classes are supported (see {@link #getSupportedEventClasses()}) 
 * and <b>how</b> events are filtered (see {@link #accepts(ApplicationEvent )}). 
 * <br><br>
 * <b>Note</b>: If a FilteringApplicationListener is used with another {@link org.springframework.context.event.ApplicationEventMulticaster}
 * implementation, it will behave like a standard ApplicationListener and filtering capabilities will not be used.
 *
 * @see #getSupportedEventClasses()
 * @see #accepts(ApplicationEvent )
 * 
 * @author Sergio Bossa
 */
public interface FilteringApplicationListener extends ApplicationListener {
    
    /**
     * Get the {@link org.springframework.context.ApplicationEvent} classes supported by this listener.<br>
     * The listener will actually support the returned classes plus all relative subclasses.
     * <br><br>
     * Events whose class is supported will become eligible for acceptance (see {@link #accepts(ApplicationEvent )})
     * and, if accepted, processing (see {@link #onApplicationEvent(ApplicationEvent )}).
     *
     * @return An array of supported Class objects: each Class must be a subclass of
     * {@link org.springframework.context.ApplicationEvent}.
     */
    public Class[] getSupportedEventClasses();
    
    /**
     * Check if the given {@link org.springframework.context.ApplicationEvent} is supported by this listener,
     * applying so the actual filtering logic.
     * <br><br>
     * Accepted events will be actually processed (see {@link #onApplicationEvent(ApplicationEvent )}).
     *
     * @return True if accepted for further processing, false otherwise.
     */
    public boolean accepts(ApplicationEvent event);
}
