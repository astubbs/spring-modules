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

package org.springmodules.xt.model.event.filtering.support.internal;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springmodules.xt.model.event.filtering.FilteringApplicationListener;

/**
 * Adapt an {@link org.springframework.context.ApplicationListener} to an
 * {@link org.springmodules.xt.model.event.FilteringApplicationListener},
 * in order to be properly registered into an {@link org.springmodules.xt.model.event.FilteringApplicationEventMulticaster}.
 *
 * @author Sergio Bossa
 */
public class FilteringApplicationListenerAdapter implements FilteringApplicationListener {
   
    private ApplicationListener listener;
   
    public FilteringApplicationListenerAdapter(ApplicationListener delegate) {
        this.listener = delegate;
    }

    public Class[] getSupportedEventClasses() {
        return new Class[]{ApplicationEvent.class};
    }

    public boolean accepts(ApplicationEvent event) {
        return true;
    }

    public void onApplicationEvent(ApplicationEvent event) {
        this.listener.onApplicationEvent(event);
    }

    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof FilteringApplicationListenerAdapter)) {
            return false;
        } else {
            return this.listener.equals(((FilteringApplicationListenerAdapter) obj).listener);
        }
    }

    public int hashCode() {
        return this.listener.hashCode();
    }

    public String toString() {
        return this.listener.toString();
    }
}
