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

import org.springmodules.xt.model.specifications.composite.CompositeSpecification;
import org.apache.commons.collections.Predicate;
import org.apache.log4j.Logger;

/**
 * Adapts a {@link org.springmodules.xt.model.specifications.composite.CompositeSpecification} to {@link org.apache.commons.collections.Predicate} interface.
 *
 * @author Sergio Bossa
 */
public class PredicateCompositeAdapter  implements Predicate {
    
    private static final Logger logger = Logger.getLogger(PredicateCompositeAdapter.class);
    
    private CompositeSpecification specification;
    
    /**
     * Adapter constructor.<br>
     * You must provide the composite specification to adapt.
     *
     * @param specification The composite specification to adapt.
     */
    public PredicateCompositeAdapter(CompositeSpecification specification) {
        this.specification = specification;
    }

    public boolean evaluate(Object object) {
        logger.debug(new StringBuilder().append("Adapting ").append(CompositeSpecification.class).append(" to ").append(Predicate.class));
        return this.specification.evaluate(object);
    }
}
