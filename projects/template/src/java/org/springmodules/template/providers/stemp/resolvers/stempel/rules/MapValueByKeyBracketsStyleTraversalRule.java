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

import java.util.*;

import org.springframework.beans.*;
import org.springmodules.template.providers.stemp.resolvers.stempel.*;

/**
 * A TraversalRule that can retreive a the value of a Map entry using brackets style. That is, if a person
 * has a Map attribute <code>attributes</code>, then using this rule the <code>favorite_color</code> attribute can be
 * retrieved using the following expressions: <code>person.attributes["favorite_color"]</code> or
 * <code>person.attributes['favorite_color']</code>.
 *
 * @author Uri Boness
 */
public class MapValueByKeyBracketsStyleTraversalRule implements TraversalRule {

    private final static String LEFT_BRACKET = "[";
    private final static String RIGHT_BRACKET = "]";
    private final static String QUOTE = "\"";
    private final static String SINGLE_QUOTE = "'";

    /**
     * @see TraversalRule#apply(String, org.springmodules.template.providers.stemp.resolvers.stempel.EvaluationContext)
     */
    public Object apply(String expression, EvaluationContext context) {
        String propertyName = expression.substring(0, expression.indexOf(LEFT_BRACKET));
        BeanWrapper bean = new BeanWrapperImpl(context.getCurrentObject());
        Map map = (Map)bean.getPropertyValue(propertyName);
        String key = expression.substring(expression.indexOf(LEFT_BRACKET) + 1, expression.indexOf(RIGHT_BRACKET));
        if (isStringKey(key)) {
            return applyStringKey(key, map);
        }
        return applyExpressionKey(key, map, context);
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
        BeanWrapper bean = new BeanWrapperImpl(context.getCurrentObject());
        try {
            Class type = bean.getPropertyType(propertyName);
            return Map.class.isAssignableFrom(type);
        } catch (InvalidPropertyException ipe) {
            return false;
        }
    }

    private boolean isStringKey(String key) {
        return (key.startsWith(QUOTE) && key.endsWith(QUOTE)) ||
            (key.startsWith(SINGLE_QUOTE) && key.endsWith(SINGLE_QUOTE));
    }

    private Object applyStringKey(String key, Map context) {
        key = strip(key);
        return context.get(key);
    }

    private Object applyExpressionKey(String expression, Map map, EvaluationContext context) {
        Object result = context.getEvaluator().evaluate(expression, context.getModel());
        return map.get(result);
    }

    private String strip(String expression) {
        return expression.substring(1, expression.length()-1);
    }

}
