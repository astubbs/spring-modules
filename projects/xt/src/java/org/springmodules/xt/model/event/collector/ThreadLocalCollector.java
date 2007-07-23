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
 * {@link org.springmodules.xt.model.event.ApplicationCollector} implementation
 * storing collected events with a thread local scope.
 * <br>
 * <br>
 * That said, this implementation is intrinsically <b>thread safe</b>. 
 * 
 * @author Sergio Bossa
 */
public class ThreadLocalCollector implements ApplicationCollector {
    
    private final ThreadLocal<Queue> threadLocalQueue = new ThreadLocal<Queue>() {
        protected Queue<ApplicationEvent> initialValue() {
            return new LinkedList<ApplicationEvent>();
        }
    };
    
    public void onApplicationEvent(ApplicationEvent event) {
        Queue<ApplicationEvent> queue = this.threadLocalQueue.get();
        queue.offer(event);
    }

    public ApplicationEvent pollEvent() {
        Queue<ApplicationEvent> queue = this.threadLocalQueue.get();
        return queue.poll();
    }

    public List<ApplicationEvent> getEvents() {
        return Collections.unmodifiableList(new ArrayList(this.threadLocalQueue.get()));
    }
    
    public void clear() {
        this.threadLocalQueue.get().clear();
    }
}
