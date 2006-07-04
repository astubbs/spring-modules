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

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.springmodules.xt.model.notification.Message;
import org.springmodules.xt.model.notification.Notification;
import org.springmodules.xt.model.specifications.Specification;
import org.springmodules.xt.model.specifications.composite.operator.BinaryOperator;
import org.springmodules.xt.model.specifications.composite.operator.OperatorFactory;
import org.springmodules.xt.model.specifications.AbstractSpecification;
import org.springmodules.xt.model.specifications.support.InverseSpecification;
import org.springmodules.xt.model.specifications.support.SpecificationGenericAdapter;
import org.springmodules.xt.model.specifications.support.SpecificationDescriptionException;
import org.springmodules.xt.model.specifications.support.SpecificationNotComposedException;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * {@link CompositeSpecification} implementation.<br>
 * You can use whatever specification class you implement, you have one only constraint: your specification method 
 * (i.e. "isSatisfiedBy" or "validate" or so) <b>must not</b> be overloaded.
 *
 * @author Sergio Bossa
 */
public class CompositeSpecificationImpl<S, O> extends AbstractSpecification<O> implements CompositeSpecification<S, O> {
    
    private Class<S> specificationClass;
    private Method specificationMethod;
    
    private OperatorFactory operatorFactory = OperatorFactory.getInstance();
    private List<Specification> specificationsList = new LinkedList<Specification>();
    private List<BinaryOperator> operatorsList = new LinkedList<BinaryOperator>();
    
    /**
     * Composite specification constructor: here you have to provide the <i>description</i> of the actual specification class
     * you want to compose.<br>
     *
     * @param specificationClass The class of the actual specification to compose.
     * @param specificationMethod The actual specification method to call for evaluation. 
     */
    public CompositeSpecificationImpl(Class<S> specificationClass, String specificationMethod) {
        this.specificationClass = specificationClass;
        
        try {
            Method[] methods = this.specificationClass.getDeclaredMethods();
            for (Method m : methods) {
                if (m.getName().equals(specificationMethod)) {
                    this.specificationMethod = m;
                    break;
                }
            }
            if (this.specificationMethod == null) {
                throw new SpecificationDescriptionException("Wrong specification method: " + specificationMethod);
            }
        }
        catch(SecurityException ex) {
            throw new SpecificationDescriptionException("Wrong specification method: " + specificationMethod);
        }
    }
    
    /**
     * Start composing the specification.<br>
     * This is the first method to be called for composition.
     * 
     * @param specification The actual specification to compose.
     */
    public CompositeSpecification compose(S specification) {
        this.specificationsList.clear();
        this.operatorsList.clear();
        this.specificationsList.add(this.adaptSpecification(specification));
        return this;
    }
    
    /**
     * Apply the logical <code>and</code> operator to this composite specification and the supplied one.
     * @param specification The supplied specification to compose.
     */
    public CompositeSpecification and(S specification) {
        if (this.specificationsList.isEmpty()) {
            throw new SpecificationNotComposedException("You have to compose your specification first.");
        }
        this.operatorsList.add(this.operatorFactory.getAndOperator());
        this.specificationsList.add(this.adaptSpecification(specification));
        return this;
    }
    
    /**
     * Apply the logical <code>or</code> operator to this composite specification and the supplied one.
     * @param specification The supplied specification to compose.
     */
    public CompositeSpecification or(S specification) {
        if (this.specificationsList.isEmpty()) {
            throw new SpecificationNotComposedException("You have to compose your specification first.");
        }
        this.operatorsList.add(this.operatorFactory.getOrOperator());
        this.specificationsList.add(this.adaptSpecification(specification));
        return this;
    }
    
    /**
     * Apply the logical <code>and</code> operator to this composite specification and another, supplied, composite specificaton.
     * @param specification The other composite specification.
     */
    public CompositeSpecification and(CompositeSpecification<S, O> specification) {
        if (!specification.equals(this)) {
            throw new IllegalArgumentException("You cannot compose specifications with different descriptions.");
        }
        if (this.specificationsList.isEmpty()) {
            throw new SpecificationNotComposedException("You have to compose your specification first.");
        }
        this.operatorsList.add(this.operatorFactory.getAndOperator());
        this.specificationsList.add(specification);
        return this;
    }
    
    /**
     * Apply the logical <code>or</code> operator to this composite specification and another, supplied, composite specificaton.
     * @param specification The other composite specification.
     */
    public CompositeSpecification or(CompositeSpecification<S, O> specification) {
        if (!specification.equals(this)) {
            throw new IllegalArgumentException("You cannot compose specifications with different descriptions.");
        }
        if (this.specificationsList.isEmpty()) {
            throw new SpecificationNotComposedException("You have to compose your specification first.");
        }
        this.operatorsList.add(this.operatorFactory.getOrOperator());
        this.specificationsList.add(specification);
        return this;
    }
    
    /**
     * Apply the logical, negated, <code>and</code> operator to this composite specification and the supplied one.
     * @param specification The supplied specification to compose.
     */
    public CompositeSpecification andNot(S specification) {
        if (this.specificationsList.isEmpty()) {
            throw new SpecificationNotComposedException("You have to compose your specification first.");
        }
        this.operatorsList.add(this.operatorFactory.getAndOperator());
        this.specificationsList.add(new InverseSpecification(this.adaptSpecification(specification)));
        return this;
    }
    
    /**
     * Apply the logical, negated, <code>or</code> operator to this composite specification and the supplied one.
     * @param specification The actual specification to compose.
     */
    public CompositeSpecification orNot(S specification) {
        if (this.specificationsList.isEmpty()) {
            throw new SpecificationNotComposedException("You have to compose your specification first.");
        }
        this.operatorsList.add(this.operatorFactory.getOrOperator());
        this.specificationsList.add(new InverseSpecification(this.adaptSpecification(specification)));
        return this;
    }
    
    /**
     * Apply the logical, negated, <code>and</code> operator to this composite specification and another, supplied, composite specificaton.
     * @param specification The other composite specification.
     */
    public CompositeSpecification andNot(CompositeSpecification<S, O> specification) {
        if (!specification.equals(this)) {
            throw new IllegalArgumentException("You cannot compose specifications with different descriptions.");
        }
        if (this.specificationsList.isEmpty()) {
            throw new SpecificationNotComposedException("You have to compose your specification first.");
        }
        this.operatorsList.add(this.operatorFactory.getAndOperator());
        this.specificationsList.add(new InverseSpecification(specification));
        return this;
    }
    
    /**
     * Apply the logical, negated, <code>or</code> operator to this composite specification and another, supplied, composite specificaton.
     * @param specification The other composite specification.
     */
    public CompositeSpecification orNot(CompositeSpecification<S, O> specification) {
        if (!specification.equals(this)) {
            throw new IllegalArgumentException("You cannot compose specifications with different descriptions.");
        }
        if (this.specificationsList.isEmpty()) {
            throw new SpecificationNotComposedException("You have to compose your specification first.");
        }
        this.operatorsList.add(this.operatorFactory.getOrOperator());
        this.specificationsList.add(new InverseSpecification(specification));
        return this;
    }
    
    public CompositeSpecification withMessage(Message message, boolean whenSatisfied) {
        Specification lastSpecification = this.specificationsList.get(this.specificationsList.size() - 1);
        lastSpecification.addMessage(message, whenSatisfied);
        return this;
    }
    
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (! (obj instanceof CompositeSpecificationImpl)) {
            return false;
        }
        else {
            CompositeSpecificationImpl otherSpec = (CompositeSpecificationImpl) obj;
            EqualsBuilder builder = new EqualsBuilder();
            return builder.append(this.specificationClass, otherSpec.specificationClass)
                                  .append(this.specificationMethod, otherSpec.specificationMethod)
                                  .isEquals();
        }
    }

    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        return builder.append(this.specificationClass)
                              .append(this.specificationMethod)
                              .toHashCode();
    }
    
    protected boolean internalEvaluate(O object, Notification notification) {
        if (this.specificationsList.isEmpty()) {
            throw new SpecificationNotComposedException("You have to compose your specification first.");
        }
        Iterator<Specification> specificationsIt = this.specificationsList.iterator();
        boolean result = specificationsIt.next().evaluate(object, notification);
        for (BinaryOperator op : operatorsList) {
            result = op.evaluate(specificationsIt.next(), result, object, notification);
        }
        return result;
    }

    private Specification adaptSpecification(S specification) {
        return new SpecificationGenericAdapter(specification, this.specificationMethod);
    }
}
