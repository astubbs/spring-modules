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

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import org.springframework.context.ApplicationEvent;

/**
 * {@link org.springmodules.xt.model.event.ApplicationCollector} implementation that
 * simply collects events in a queue.
 * <br>
 * <br>
 * This class is <b>not</b> thread safe.
 * 
 * @author Sergio Bossa
 */
public class SimpleCollector implements ApplicationCollector {
    
    private final Queue<ApplicationEvent> queue = new LinkedList<ApplicationEvent>();
    
    public void onApplicationEvent(ApplicationEvent event) {
        this.queue.offer(event);
    }
    
    public ApplicationEvent pollEvent() {
        return this.queue.poll();
    }
    
    public List<ApplicationEvent> getEvents() {
        return Collections.unmodifiableList(new ArrayList(this.queue));
    }
    
    public void clear() {
        this.queue.clear();
    }
}
