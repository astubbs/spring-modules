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

package org.springmodules.xt.model.event;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springmodules.xt.model.event.support.internal.FilteringApplicationListenerAdapter;

/**
 * {@link org.springframework.context.event.ApplicationEventMulticaster} implementation that used in conjunction
 * with {@link FilteringApplicationListener}s permits to efficiently filter published {@link org.springframework.context.ApplicationEvent}s,
 * in order to avoid notifying <b>every</b> listener of <b>every</b> published event, and in order to provide a better and clearer
 * separation of event filtering and processing, by decoupling the filtering logic from the processing one.
 * <br><br>
 * Registered {@link FilteringApplicationListener}s are notified only of those events whose class is supported by the listener
 * (see {@link FilteringApplicationListener#getSupportedEventClasses()}) and that are accepted
 * by the listener itself (see {@link FilteringApplicationListener#accepts(ApplicationEvent )}).<br>
 * Every other registered {@link org.springframework.context.ApplicationListener} implementation will be notified of any event,
 * without applying any filtering logic.
 * <br><br>
 * This class is completely <b>thread-safe</b>, optimized for concurrent scenarios where event multicasting operations vastly outnumber
 * listener adding and removal operations.<br>
 * Moreover, the event multicasting process (see {@link #multicastEvent(ApplicationEvent )})
 * is guaranteed to be <i>atomic</i> and <i>isolated</i> in respect to the adding and removal of listeners.
 *
 * @see FilteringApplicationListener
 * @see FilteringApplicationListener#getSupportedEventClasses()
 * @see FilteringApplicationListener#accepts(ApplicationEvent )
 *
 * @author Sergio Bossa
 */
public class FilteringApplicationEventMulticaster implements ApplicationEventMulticaster {
    
    private static final Logger logger = Logger.getLogger(FilteringApplicationEventMulticaster.class);
    
    private final Map<Class, Set<FilteringApplicationListener>> listenersMap = new HashMap<Class, Set<FilteringApplicationListener>>();
    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock readLock = rwl.readLock();
    private final Lock writeLock = rwl.writeLock();
    
    public void multicastEvent(ApplicationEvent event) {
        this.readLock.lock();
        try {
            if (logger.isDebugEnabled()) {
                logger.debug(new StringBuilder("Multicasting event: ").append(event));
            }
            for (Map.Entry<Class, Set<FilteringApplicationListener>> entry : this.listenersMap.entrySet()) {
                Class supportedClass = entry.getKey();
                if (supportedClass.isAssignableFrom(event.getClass())) {
                    Set<FilteringApplicationListener> listeners = entry.getValue();
                    for (FilteringApplicationListener listener : listeners) {
                        try {
                            if (listener.accepts(event)) {
                                listener.onApplicationEvent(event);
                            }
                        } catch (Exception ex) {
                            logger.warn(new StringBuilder("Exception while processing the following event: ").append(event));
                            logger.warn(ex.getMessage(), ex);
                        }
                    }
                }
            }
        } finally {
            this.readLock.unlock();
        }
    }
    
    public void addApplicationListener(ApplicationListener listener) {
        this.writeLock.lock();
        try {
            FilteringApplicationListener filteringListener = null;
            if (listener instanceof FilteringApplicationListener) {
                filteringListener = (FilteringApplicationListener) listener;
            } else {
                filteringListener = new FilteringApplicationListenerAdapter(listener);
            }
            Class[] supportedClasses = filteringListener.getSupportedEventClasses();
            for (Class clazz : supportedClasses) {
                if (this.listenersMap.containsKey(clazz)) {
                    this.listenersMap.get(clazz).add(filteringListener);
                } else {
                    Set<FilteringApplicationListener> set = new LinkedHashSet<FilteringApplicationListener>();
                    set.add(filteringListener);
                    this.listenersMap.put(clazz, set);
                }
            }
        } finally {
            this.writeLock.unlock();
        }
    }
    
    public void removeApplicationListener(ApplicationListener listener) {
        this.writeLock.lock();
        try {
            FilteringApplicationListener filteringListener = null;
            if (listener instanceof FilteringApplicationListener) {
                filteringListener = (FilteringApplicationListener) listener;
            } else {
                filteringListener = new FilteringApplicationListenerAdapter(listener);
            }
            Collection<Set<FilteringApplicationListener>> listenersCollection = this.listenersMap.values();
            for (Set<FilteringApplicationListener> listeners : listenersCollection) {
                listeners.remove(filteringListener);
            }
        } finally {
            this.writeLock.unlock();
        }
    }
    
    public void removeAllListeners() {
        this.writeLock.lock();
        try {
            this.listenersMap.clear();
        } finally {
            this.writeLock.unlock();
        }
    }
}
