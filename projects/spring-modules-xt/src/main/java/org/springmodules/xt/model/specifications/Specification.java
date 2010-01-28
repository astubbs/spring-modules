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

package org.springmodules.xt.model.specifications;

import org.springmodules.xt.model.notification.Message;
import org.springmodules.xt.model.notification.Notification;

/**
 * Generics-enabled specification interface capable of firing and collecting {@link org.springmodules.xt.model.notification.Message}s
 * into {@link org.springmodules.xt.model.notification.Notification} objects.
 *
 * @author Sergio Bossa
 */
public interface Specification<O> {
    
    /**
     * Specification evaluation method.
     *
     * @param object The object to evaluate.
     * @return True if satisfied, false otherwise.
     */
    public boolean evaluate(O object);
    
    /**
     * Specification evaluation method, with a {@link org.springmodules.xt.model.notification.Notification} object for collecting error messages.
     *
     * @param object The object to evaluate.
     * @param notification A notification object where errors regarding specification evaluation will be put.<br>
     * It can be left <code>null</code> if not used.
     * @return True if satisfied, false otherwise.
     */
    public boolean evaluate(O object, Notification notification);
    
    /**
     * Add a notification message to be notified by this specification when satisfied or unsatisfied.
     *
     * @param message The message to add.
     * @param whenSatisfied True if the message must be notified when the specification gets satisfied,
     * false if notified when unsatisfied.
     */
    public void addMessage(Message message, boolean whenSatisfied);
    
    /**
     * Remove a notification message.
     *
     * @param message The message to remove.
     * @param whenSatisfied True if the message was to be notified once satisfied,
     * false otherwise.
     * @return True if removed.
     */
    public boolean removeMessage(Message message, boolean whenSatisfied);
}
