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

import org.springmodules.template.providers.stemp.resolvers.stempel.*;

/**
 * A TraversalRule that can retreive a the value of a Map entry using a bean property accessor style. That is, if a person
 * has a Map attribute <code>attributes</code>, then using this rule the <code>favorite_color</code> attribute can be
 * retrieved using the following expression: <code>person.attributes.favorite_color</code>.
 *
 * @author Uri Boness
 */
public class MapValueByKeyPropertyStyleTraversalRule implements TraversalRule {

    /**
     * @see TraversalRule#apply(String, org.springmodules.template.providers.stemp.resolvers.stempel.EvaluationContext)
     */
    public Object apply(String expression, EvaluationContext context) {
        Map map = (Map)context.getCurrentObject();
        return map.get(expression);
    }

    /**
     * @see TraversalRule#isApplicable(String, org.springmodules.template.providers.stemp.resolvers.stempel.EvaluationContext)
     */
    public boolean isApplicable(String expression, EvaluationContext context) {
        Object currentObject = context.getCurrentObject();
        return currentObject instanceof Map;
    }

}
