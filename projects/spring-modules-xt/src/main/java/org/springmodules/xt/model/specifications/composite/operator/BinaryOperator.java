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

package org.springmodules.xt.model.specifications.composite.operator;

import org.springmodules.xt.model.notification.Notification;
import org.springmodules.xt.model.specifications.Specification;

/**
 * Interface representing a binary logical operator.
 *
 * @author Sergio Bossa
 */
public interface BinaryOperator {
    
    /**
     * Apply the operator, combining the two specifications and evaluating the given object
     * @param a The first specification to combine using this operator.
     * @param b The second specification to combine using this operator.
     * @param o The object to evaluate.
     */
    public boolean evaluate(Specification a, Specification b, Object o);
    
    /**
     * Apply the operator, combining the specification with the boolean value and evaluating the given object.
     * @param a The first specification to combine using this operator.
     * @param b The boolean value to combine using this operator.
     * @param o The object to evaluate.
     */
    public boolean evaluate(Specification a, boolean b, Object o);
    
    /**
     * Apply the operator, combining the two specifications and evaluating the given object
     * @param a The first specification to combine using this operator.
     * @param b The second specification to combine using this operator.
     * @param o The object to evaluate.
     * @param notification The notification object to pass to specifications.
     */
    public boolean evaluate(Specification a, Specification b, Object o, Notification notification);
    
    /**
     * Apply the operator, combining the specification with the boolean value and evaluating the given object.
     * @param a The first specification to combine using this operator.
     * @param b The boolean value to combine using this operator.
     * @param o The object to evaluate.
     * @param notification The notification object to pass to specifications.
     */
    public boolean evaluate(Specification a, boolean b, Object o, Notification notification);
}
