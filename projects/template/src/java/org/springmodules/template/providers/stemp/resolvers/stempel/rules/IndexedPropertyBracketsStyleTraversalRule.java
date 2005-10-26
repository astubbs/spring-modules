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

import java.lang.reflect.*;
import java.util.*;

import org.springframework.beans.*;
import org.springmodules.template.providers.stemp.resolvers.stempel.*;

/**
 * A TraversalRule that can retreive a the value of indexed property. If for example a person
 * has a <code>friends</code> array (or list), then using this rule the 2nd. friend can be
 * retrieved using the following expressions: <code>person.friends[1]</code>
 *
 * @author Uri Boness
 */
public class IndexedPropertyBracketsStyleTraversalRule implements TraversalRule {

    private final static String LEFT_BRACKET = "[";
    private final static String RIGHT_BRACKET = "]";

    /**
     * @see TraversalRule#apply(String, org.springmodules.template.providers.stemp.resolvers.stempel.EvaluationContext)
     */
    public Object apply(String expression, EvaluationContext context) {
        String propertyName = expression.substring(0, expression.indexOf(LEFT_BRACKET));
        String indexAsString = expression.substring(expression.indexOf(LEFT_BRACKET) + 1, expression.indexOf(RIGHT_BRACKET));
        int index = Integer.valueOf(indexAsString).intValue();
        BeanWrapper bean = new BeanWrapperImpl(context.getCurrentObject());
        Object value = bean.getPropertyValue(propertyName);
        if (bean.getPropertyType(propertyName).isArray()) {
            return applyOnArray(value, index);
        }
        return applyOnList(value, index);
    }

    /**
     * @see TraversalRule#isApplicable(String, org.springmodules.template.providers.stemp.resolvers.stempel.EvaluationContext)
     */
    public boolean isApplicable(String expression, EvaluationContext context) {
        int leftBracketIndex = expression.indexOf(LEFT_BRACKET);
        if (leftBracketIndex < 0 || !expression.endsWith(RIGHT_BRACKET)) {
            return false;
        }
        String propertyName = expression.substring(0, leftBracketIndex);
        String indexAsString = expression.substring(expression.indexOf(LEFT_BRACKET) + 1, expression.indexOf(RIGHT_BRACKET));
        if (!isInteger(indexAsString)) {
            return false;
        }
        BeanWrapper bean = new BeanWrapperImpl(context.getCurrentObject());
        try {
            Class type = bean.getPropertyType(propertyName);
            return List.class.isAssignableFrom(type) || type.isArray();
        } catch (InvalidPropertyException ipe) {
            return false;
        }
    }

    //======================================= Helper Methods =============================================

    protected boolean isInteger(String intAsString) {
        try {
            Integer.parseInt(intAsString);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    protected Object applyOnArray(Object value, int index) {
        return Array.get(value, index);
    }

    protected Object applyOnList(Object value, int index) {
        List list = (List)value;
        return list.get(index);
    }

}
