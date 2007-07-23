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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.springframework.context.ApplicationEvent;

/**
 * {@link org.springmodules.xt.model.event.ApplicationCollector} implementation that,
 * by using a {@link java.util.concurrent.BlockingQueue},
 * blocks when polling for events if no event is actually available.<br>
 * Please note that the BlockingCollector doesn't support the {@link #getEvents()} method.
 * <br>
 * <br>
 * This class is <b>thread safe</b>.
 * 
 * @author Sergio Bossa
 */
public class BlockingCollector implements ApplicationCollector {
    
    private final BlockingQueue<ApplicationEvent> queue = new LinkedBlockingQueue<ApplicationEvent>();
    
    public void onApplicationEvent(ApplicationEvent event) {
        this.queue.offer(event);
    }
    
    /**
     * Poll each collected event, blocking if no event is available.
     * When available, remove it from the queue of collected events, and return it.
     * 
     * @return The collected {@link org.springframework.context.ApplicationEvent}.
     */
    public ApplicationEvent pollEvent() {
        try {
            return this.queue.take();
        } catch (InterruptedException ex) {
            throw new IllegalStateException("Interrupted event polling!", ex);
        }
    }
    
    /**
     * Not supported by the BlockingCollector, use {@link #pollEvent()} instead.
     */
    public List<ApplicationEvent> getEvents() {
        throw new UnsupportedOperationException("The BlockingCollector can only poll single events.");
    }
    
    public void clear() {
        this.queue.clear();
    }
}
