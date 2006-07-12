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
import org.springmodules.xt.model.notification.NotificationImpl;

/**
 * Abstract {@link org.springmodules.xt.model.specifications.Specification} implementation
 * which collects notification messages and add them to the notification passed at evaluation time.<br>
 * You have only to implement the <code>internalEvaluate</code> method.
 *
 * @author Sergio Bossa
 */
public abstract class AbstractSpecification<O> implements Specification<O> {
    
    private Notification satisfiedNotification = new NotificationImpl();
    private Notification unsatisfiedNotification  = new NotificationImpl();
    
    public void addMessage(Message message, boolean whenSatisfied) {
        if (whenSatisfied) {
            this.satisfiedNotification.addMessage(message);
        }
        else { 
            this.unsatisfiedNotification.addMessage(message);
        }
    }
    
    public boolean removeMessage(Message message, boolean whenSatisfied) {
        if (whenSatisfied) {
            return this.satisfiedNotification.removeMessage(message);
        }
        else { 
            return this.unsatisfiedNotification.removeMessage(message);
        }
    }

    public boolean evaluate(O object) {
        return this.internalEvaluate(object, null);
    }

    
    public boolean evaluate(O object, Notification notification) {
        boolean satisfied = this.internalEvaluate(object, notification);
        if (notification != null) {
            if (satisfied) {
                notification.addAllMessages(this.satisfiedNotification);
            }
            else {
                notification.addAllMessages(this.unsatisfiedNotification);
            }
        }
        return satisfied;
    }

    /**
     * Implement this for implementing the actual evaluation.
     */
    protected abstract boolean internalEvaluate(O object, Notification notification);
}
