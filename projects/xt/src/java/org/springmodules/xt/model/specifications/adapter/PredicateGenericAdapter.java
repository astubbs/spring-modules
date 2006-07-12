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

package org.springmodules.xt.model.specifications.adapter;

import java.lang.reflect.Method;
import org.apache.commons.collections.Predicate;
import org.apache.log4j.Logger;
import org.springmodules.xt.model.specifications.support.SpecificationAdapterException;
import org.springmodules.xt.model.specifications.support.SpecificationDescriptionException;

/**
 * Adapts generic specification objects to {@link org.apache.commons.collections.Predicate} interface.
 *
 * @author Sergio Bossa
 */
public class PredicateGenericAdapter  implements Predicate {
    
    private static final Logger logger = Logger.getLogger(PredicateGenericAdapter.class);
    
    private Object specification;
    private Method specificationMethod;
    
    /**
     * Adapter constructor.<br>
     * You must provide the specification object to adapt and its specification method to call.<br>
     * The specification method <b>must</b> return a boolean value.
     *
     * @param specification The actual specification to adapt.
     * @param specificationMethod The actual specification method to call in place of the Predicate 
     * <code>evaluate</code> method. 
     */
    public PredicateGenericAdapter(Object specification, Method specificationMethod) {
        this.specification = specification;
        this.specificationMethod = specificationMethod;
    }
    
    /**
     * Adapter constructor.<br>
     * You must provide the specification object to adapt and the name of its specification method to call.<br>
     * The specification method <b>must</b> return a boolean value and <b>must not</b> be overloaded.
     *
     * @param specification The actual specification to adapt.
     * @param specificationMethod The actual specification method to call in place of the Predicate 
     * <code>evaluate</code> method. 
     */
    public PredicateGenericAdapter(Object specification, String specificationMethod) {
        this.specification = specification;
        try {
            Method[] methods = this.specification.getClass().getDeclaredMethods();
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

    public boolean evaluate(Object object) {
        boolean result = false;
        
        logger.debug(new StringBuilder().append("Adapting ").append(this.specification.getClass()).append(" to ").append(Predicate.class));
            
        try {
            Object tmp = this.specificationMethod.invoke(specification, object);
            if (!(tmp instanceof Boolean)) {
                throw new SpecificationAdapterException(this.specificationMethod.toString() + " must return a boolean value.");
            }
            else {
                result = (Boolean) tmp;
            }
        }
        catch(Exception ex) {
            throw new SpecificationAdapterException("Error while calling: " + this.specificationMethod.toString(), ex);
        }
        
        return result;
    }
}
