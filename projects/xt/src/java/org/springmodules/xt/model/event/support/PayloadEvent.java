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

package org.springmodules.xt.model.event.support;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.context.ApplicationEvent;
import org.springmodules.xt.model.notification.Notification;

/**
 * Concrete {@link org.springframework.context.ApplicationEvent} carrying several information as a payload.<br>
 * Each event carries the following information:
 * <ul>
 * <li>Event source object (mandatory).</li>
 * <li>Payload object (optional).</li>
 * <li>{@link org.springmodules.xt.model.notification.Notification} object (optional).</li>
 * </ul>
 * <b>This class is immutable and hence thread safe</b>.
 *
 * @author Sergio Bossa
 */
public class PayloadEvent extends ApplicationEvent {
    
    private static final long serialVersionUID = 26L;
    
    private Object payload;
    private Notification notification;
    
    /**
     * Construct an event object.
     * 
     * @param source The event source.
     */
    public PayloadEvent(Object source) {
        super(source);
        if (source == null) {
            throw new IllegalArgumentException("Event source cannot be null!");
        }
    }
    
    /**
     * Construct an event object: the event source is the only mandatory parameter; other
     * parameters can be null if unused.
     * 
     * @param source The event source.
     * @param payload The event payload, or null if unused.
     * @param notification The event notification, or null if unused.
     */
    public PayloadEvent(Object source, Object payload, Notification notification) {
        super(source);
        if (source == null) {
            throw new IllegalArgumentException("Event source cannot be null!");
        }
        this.payload = payload;
        this.notification = notification;
    }

    /**
     * Get the event payload.
     * @return The event payload object, or null if this event has no payload.
     */
    public Object getPayload() {
        return this.payload;
    }

    /**
     * Get the event {@link org.springmodules.xt.model.notification.Notification}.
     * @return The event notification object, or null if this event has no notifcation.
     */
    public Notification getNotification() {
        return this.notification;
    }
    
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (!obj.getClass().equals(this.getClass())) {
            return false;
        } else {
            PayloadEvent other = (PayloadEvent) obj;
            EqualsBuilder builder = new EqualsBuilder().append(this.source, other.source)
            .append(this.payload, other.payload)
            .append(this.notification, other.notification);
            return builder.isEquals();
        }
    }
    
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder().append(this.source)
        .append(this.payload)
        .append(this.notification);
        return builder.toHashCode();
    }

    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this)
        .append("source", this.source)
        .append("payload", this.payload)
        .append("notification", this.notification);
        return builder.toString();
    }
}
