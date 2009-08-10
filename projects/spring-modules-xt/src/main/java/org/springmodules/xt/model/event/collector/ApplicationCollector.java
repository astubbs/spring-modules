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

package org.springmodules.xt.model.event.collector;

import java.util.List;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * {@link org.springframework.context.ApplicationListener} extended interface to be implemented by objects that want to listen to
 * {@link org.springframework.context.ApplicationEvent}s and collect (store) them, in order to make them available to 
 * other external objects.<br>
 * Events are collected and later accessed in FIFO order.
 * 
 * @author Sergio Bossa
 */
public interface ApplicationCollector extends ApplicationListener {
    
    /**
     * Poll the first collected event, removing it from the queue of collected events.
     * 
     * @return The first collected {@link org.springframework.context.ApplicationEvent}.
     */
    public ApplicationEvent pollEvent();
    
    /**
     * Get all collected events, without removing them from the queue of collected events.
     * 
     * @return The list of collected {@link org.springframework.context.ApplicationEvent}s, in FIFO order.
     */
    public List<ApplicationEvent> getEvents();
    
    /**
     * Clear all collected events.
     */
    public void clear();
}
