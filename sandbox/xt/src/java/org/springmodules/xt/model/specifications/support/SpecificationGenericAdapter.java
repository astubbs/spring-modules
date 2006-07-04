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

package org.springmodules.xt.model.specifications.support;

import java.lang.reflect.Method;
import org.springmodules.xt.model.notification.Notification;
import org.springmodules.xt.model.specifications.AbstractSpecification;
import org.apache.commons.collections.Predicate;
import org.apache.log4j.Logger;

/**
 * Adapts generic specification objects to {@link org.springmodules.xt.model.specifications.Specification} interface.
 * 
 * @author Sergio Bossa
 */
public class SpecificationGenericAdapter extends AbstractSpecification {
    
    private static final Logger logger = Logger.getLogger(SpecificationGenericAdapter.class);
    
    private Object specification;
    private Method specificationMethod;
    
    /**
     * Adapter constructor.<br>
     *
     * @param specification The actual specification to adapt.
     * @param specificationMethod The actual specification method to call in place of the Predicate 
     * <code>evaluate</code> method. 
     */
    public SpecificationGenericAdapter(Object specification, Method specificationMethod) {
        this.specification = specification;
        this.specificationMethod = specificationMethod;
    }
    
    protected boolean internalEvaluate(Object object, Notification notification) {
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
