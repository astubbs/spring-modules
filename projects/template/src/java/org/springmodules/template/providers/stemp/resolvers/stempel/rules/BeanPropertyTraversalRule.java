/*
 * Copyright 2002-2005 the original author or authors.
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

package org.springmodules.template.providers.stemp.resolvers.stempel.rules;

import java.util.regex.*;

import org.springframework.beans.*;
import org.springmodules.template.providers.stemp.resolvers.stempel.*;

/**
 * A TraversalRule that can retreive a the value of a simple bean property. If for example a person
 * has a <code>name</code> property, then using this rule it can be
 * retrieved with the following expression: <code>person.name</code>.
 *
 * @author Uri Boness
 */
public class BeanPropertyTraversalRule implements TraversalRule {

    // a regular expression of a bean property in java.
    private final static Pattern pattern = Pattern.compile("[_a-zA-Z$][_a-zA-Z0-9$]*");

    /**
     * @see TraversalRule#apply(String, org.springmodules.template.providers.stemp.resolvers.stempel.EvaluationContext)
     */
    public Object apply(String expression, EvaluationContext context) {
        BeanWrapper bean = new BeanWrapperImpl(context.getCurrentObject());
        return bean.getPropertyValue(expression);
    }

    /**
     * @see TraversalRule#isApplicable(String, org.springmodules.template.providers.stemp.resolvers.stempel.EvaluationContext)
     */
    public boolean isApplicable(String expression, EvaluationContext context) {
        if (!pattern.matcher(expression).matches()) {
            return false;
        }
        BeanWrapper bean = new BeanWrapperImpl(context.getCurrentObject());
        try {
            bean.getPropertyDescriptor(expression);
            return true;
        } catch (InvalidPropertyException ipe) {
            return false;
        }
    }
}
