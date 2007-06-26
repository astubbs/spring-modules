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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * {@link Message} immutable implementation.
 *
 * @author Sergio Bossa
 */
public class MessageImpl implements Message {
    
    private static final long serialVersionUID = 26L;
    
    private String code;
    private Message.Type type;
    private String propertyName;
    private String defaultMessage;
    
    /**
     * Message constructor.
     * @param code The message code.
     * @param type The message type.
     */
    public MessageImpl(String code, Message.Type type) {
        if (code == null) throw new NullPointerException("Null code");
        if (type == null) throw new NullPointerException("Null type");
        this.code = code;
        this.type = type;
    }
    
    /**
     * Message constructor.
     * @param code The message code.
     * @param type The message type.
     * @param message The default message string (can be <code>null</code>).
     */
    public MessageImpl(String code, Message.Type type, String message) {
        this(code, type);
        this.defaultMessage = message;
    }
    
    /**
     * Message constructor.
     * @param code The message code.
     * @param type The message type.
     * @param propertyName The related property name (can be <code>null</code>).
     * @param message The default message string (can be <code>null</code>).
     */
    public MessageImpl(String code, Message.Type type, String propertyName, String message) {
        this(code, type);
        this.propertyName = propertyName;
        this.defaultMessage = message;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public Message.Type getType() {
        return this.type;
    }
    
    public String getPropertyName() {
        return this.propertyName;
    }
    
    public String getDefaultMessage() {
        return this.defaultMessage;
    }
    
    public boolean equals(Object obj) {
        if (!(obj instanceof Message)) {
            return false;
        }
        
        Message other = (Message) obj;
        
        return new EqualsBuilder().append(this.getCode(), other.getCode())
        .append(this.getType(), other.getType())
        .isEquals();
    }
    
    public int hashCode() {
        return new HashCodeBuilder().append(this.getCode())
        .append(this.getType())
        .toHashCode();
    }
    
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this)
        .append("code", this.code)
        .append("type", this.type);
        return builder.toString();
    }
}
