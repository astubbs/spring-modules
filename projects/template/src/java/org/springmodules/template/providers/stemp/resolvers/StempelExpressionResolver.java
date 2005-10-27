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

package org.springmodules.template.providers.stemp.resolvers;

import java.util.*;

import org.springmodules.template.providers.stemp.resolvers.stempel.*;
import org.springmodules.template.providers.stemp.*;
import org.apache.commons.logging.*;

/**
 * An implementation of {@link org.springmodules.template.providers.stemp.ExpressionResolver} that uses stempel (stemp expression language) expressions.
 *
 * @see org.springmodules.template.providers.stemp.resolvers.stempel.StempelExpressionEvaluator
 * @author Uri Boness
 */
public class StempelExpressionResolver implements ExpressionResolver {

    private static final Log log = LogFactory.getLog(StempelExpressionResolver.class);

    /**
     * @see org.springmodules.template.providers.stemp.ExpressionResolver#resolve(String, java.util.Map)
     */
    public String resolve(String expression, Map model) {
        Object result = null;
        try {
             result = Stempel.evaluate(expression, model);
        } catch (ExpressionEvaluationException eee) {
            log.error("Could not resolve stempel expressoin '" + expression + "'.", eee);
        }
        return String.valueOf(result);
    }

}
