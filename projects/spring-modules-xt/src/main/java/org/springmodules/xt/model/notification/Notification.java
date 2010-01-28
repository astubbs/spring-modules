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

import java.io.Serializable;

/**
 * Interface representing a message notification.<br>
 * Every notification can contain various {@link Message}s of different types ({@link Message.Type}).<br>
 * So, the notification acts as a collector/carrier of messages.
 *
 * @author Sergio Bossa
 */
public interface Notification extends Serializable {
    
    /**
     * Add a message.
     * @param message The message to add.
     */
    public void addMessage(Message message);
    
    /**
     * Remove a message.
     * @param message The message to remove.
     * @return True if removed.
     */
    public boolean removeMessage(Message message);
    
    /**
     * Get messages of the given type.
     * @param type The type of messages to retrieve.
     * @return An array of messages.
     */
    public Message[] getMessages(Message.Type type);
    
    /**
     * Check if this notification has messages of the given type.
     * @param type The type of messages to look for.
     * @return True if this notification has messages of the given type,
     * false otherwise.
     */
    public boolean hasMessages(Message.Type type);
    
    /**
     * Get all messages contained in this notification.
     * @return An array of messages.
     */
    public Message[] getAllMessages();
    
    /**
     * Check if this notification has messages of whatever type.
     * @return True if this notification has messages,
     * false otherwise.
     */
    public boolean hasMessages();
    
    /**
     * Add to this notification all the messages contained in the notification in argument.
     * @param notification The notification whose messages must be added. 
     */
    public void addAllMessages(Notification notification);
}
