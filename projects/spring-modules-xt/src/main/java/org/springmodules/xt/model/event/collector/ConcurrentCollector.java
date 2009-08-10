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

/**
 * {@link org.springmodules.xt.model.event.ApplicationCollector} synchronized implementation that
 * wraps another non thread safe collector, in order to make it possible to safely share it among threads. 
 * 
 * @author Sergio Bossa
 */
public class ConcurrentCollector implements ApplicationCollector {
    
    private ApplicationCollector collector;

    public ConcurrentCollector(ApplicationCollector collector) {
        this.collector = collector;
    }

    public synchronized void onApplicationEvent(ApplicationEvent event) {
        this.collector.onApplicationEvent(event);
    }
    
    public synchronized ApplicationEvent pollEvent() {
        return this.collector.pollEvent();
    }
    
    public synchronized List<ApplicationEvent> getEvents() {
        return this.collector.getEvents();
    }
    
    public synchronized void clear() {
        this.collector.clear();
    }
}
