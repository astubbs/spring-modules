/*
 * Copyright 2006 the original author or authors.
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

package org.springmodules.xt.model.notification;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * {@link Notification} implementation.
 *
 * @author Sergio Bossa
 */
public class NotificationImpl implements Notification {
    
    private static final long serialVersionUID = 26L;
    
    private Map<Message.Type, List<Message>> messagesMap = new HashMap();
   
    public NotificationImpl() {
        for (Message.Type type : Message.Type.values()) {
            this.messagesMap.put(type, new LinkedList<Message>());
        }
    }
    
    public void addMessage(Message message) {
       this.messagesMap.get(message.getType()).add(message);
   }
    
    public boolean removeMessage(Message message) {
        return this.messagesMap.get(message.getType()).remove(message);
    }
    
    public Message[] getMessages(Message.Type type) {
        List<Message> messages = this.messagesMap.get(type);
        return messages.toArray(new Message[messages.size()]);
    }
    
    public boolean hasMessages(Message.Type type) {
        return !this.messagesMap.get(type).isEmpty();
    }
    
    public Message[] getAllMessages() {
        Collection<Message> result = new LinkedList();
        for (Message.Type type : Message.Type.values()) {
           CollectionUtils.addAll(result, this.messagesMap.get(type).toArray());
        }
        return result.toArray(new Message[result.size()]);
    }
    
    public boolean hasMessages() {
        boolean result = false;
        for (Message.Type type : Message.Type.values()) {
          if (! this.messagesMap.get(type).isEmpty()) {
              result = true;
              break;
          }
        }
        return result;
    }
    
    public void addAllMessages(Notification notification) {
        Message[] messages = null;
        for (Message.Type type : Message.Type.values()) {
            if (notification.hasMessages(type)) {
                messages = notification.getMessages(type);
                for (Message m : messages) {
                    this.addMessage(m);
                }
            }
        }
    }
    
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (!obj.getClass().equals(this.getClass())) {
            return false;
        } else {
            NotificationImpl other = (NotificationImpl) obj;
            EqualsBuilder builder = new EqualsBuilder().append(this.getAllMessages(), other.getAllMessages());
            return builder.isEquals();
        }
    }
    
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder().append(this.getAllMessages());
        return builder.toHashCode();
    }
    
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        for (Map.Entry<Message.Type, List<Message>> entry : this.messagesMap.entrySet()) {
            builder.append("type", entry.getKey())
            .append("messages", entry.getValue());
        }
        return builder.toString();
    }
}
