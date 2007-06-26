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
 * Interface representing a message to be notified.<br>
 * Each message has a code, a default message string, a property name, and a type.<br>
 * This interface has only getter methods because a message should be immutable.
 *
 * @author Sergio Bossa
 */
public interface Message extends Serializable {
    
    public enum Type { ERROR, WARNING, INFO };
    
    /**
     * Get the message code.
     * @return The message code.
     */
    public String getCode();
    
    /**
     * Get the message type.
     * @return The message type.
     */
    public Message.Type getType();
    
    /**
     * Get the property name which this message refers to.
     * @return The related property name;
     */
    public String getPropertyName();
    
    /**
     * Get the default message string of this message object.
     * @return The default message string.
     */
    public String getDefaultMessage();
}
