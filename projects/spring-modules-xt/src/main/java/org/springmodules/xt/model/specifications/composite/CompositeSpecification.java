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

package org.springmodules.xt.model.specifications.composite;

import org.springmodules.xt.model.notification.Message;
import org.springmodules.xt.model.notification.Notification;
import org.springmodules.xt.model.specifications.Specification;

/**
 * Composite specification for composing whatever specification class using
 * standard logical operators.<br><br>
 * Simply specify the type of the specification you want to compose and of the object you want to evaluate 
 * and call the fluent interface methods:<br>
 * <code>compositeSpecification.compose(spec1).and(spec2).evaluate(objectToEvaluate);</code>
 * <br>
 * This class is also capable of assigning, to composed specifications, {@link org.springmodules.xt.model.notification.Message}s
 * to be thrown at evaluation time, when the specification is satisfied or not. These messages will be collected into a 
 * {@link org.springmodules.xt.model.notification.Notification} object. 
 *
 * @author Sergio Bossa
 */
public interface CompositeSpecification<S, O> extends Specification<O> {
    
    /**
     * Apply the logical <code>and</code> operator to this composite specification and another, supplied, composite specificaton.
     * @param specification The other composite specification.
     */
    public CompositeSpecification and(CompositeSpecification<S, O> specification);

    /**
     * Apply the logical <code>and</code> operator to this composite specification and the supplied one.
     * @param specification The supplied specification to compose.
     */
    public CompositeSpecification and(S specification);

    /**
     * Apply the logical, negated, <code>and</code> operator to this composite specification and another, supplied, composite specificaton.
     * @param specification The other composite specification.
     */
    public CompositeSpecification andNot(CompositeSpecification<S, O> specification);

    /**
     * Apply the logical, negated, <code>and</code> operator to this composite specification and the supplied one.
     * @param specification The supplied specification to compose.
     */
    public CompositeSpecification andNot(S specification);

    /**
     * Start composing the specification.<br>
     * This is the first method to be called for composition.
     * 
     * @param specification The actual specification to compose.
     */
    public CompositeSpecification compose(S specification);

    /**
     * Add a notification message to the last composed specification.
     * @param message The message to add.
     * @param whenSatisfied True if the message must be notified when the specification gets satisfied,
     * false if notified when unsatisfied.
     */
    public CompositeSpecification withMessage(Message message, boolean whenSatisfied);
    
    /**
     * Composite specification evaluation.
     * 
     * @param object The object to evaluate.
     */
    public boolean evaluate(O object);

    /**
     * Apply the logical <code>or</code> operator to this composite specification and another, supplied, composite specificaton.
     * @param specification The other composite specification.
     */
    public CompositeSpecification or(CompositeSpecification<S, O> specification);

    /**
     * Apply the logical <code>or</code> operator to this composite specification and the supplied one.
     * @param specification The supplied specification to compose.
     */
    public CompositeSpecification or(S specification);

    /**
     * Apply the logical, negated, <code>or</code> operator to this composite specification and another, supplied, composite specificaton.
     * @param specification The other composite specification.
     */
    public CompositeSpecification orNot(CompositeSpecification<S, O> specification);

    /**
     * Apply the logical, negated, <code>or</code> operator to this composite specification and the supplied one.
     * @param specification The actual specification to compose.
     */
    public CompositeSpecification orNot(S specification);
}
