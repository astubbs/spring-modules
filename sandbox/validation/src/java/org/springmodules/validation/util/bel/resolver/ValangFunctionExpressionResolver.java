/*
 * Copyright 2004-2005 the original author or authors.
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

package org.springmodules.validation.util.bel.resolver;

import org.springmodules.validation.util.bel.BeanExpressionException;
import org.springmodules.validation.util.bel.BeanExpressionResolver;
import org.springmodules.validation.valang.functions.Function;
import org.springmodules.validation.valang.functions.TargetBeanFunction;
import org.springmodules.validation.valang.parser.ParseException;
import org.springmodules.validation.valang.parser.SimpleValangBased;
import org.springmodules.validation.valang.parser.ValangParser;

/**
 * A {@link org.springmodules.validation.util.bel.BeanExpressionResolver} implementation that uses valang function expressions to resolve the objects.
 *
 * @author Uri Boness
 */
public class ValangFunctionExpressionResolver extends SimpleValangBased implements BeanExpressionResolver {

    /**
     * Evaluates the given valang expression on the given bean and returns the resolved object.
     *
     * @param bean The bean on which the given expression will be evaluated.
     * @param expression The given valang expression.
     * @return The resolved object.
     */
    public Object resolve(Object bean, String expression) {
        ValangParser parser = createValangParser(expression);
        try {
            Function function = parser.function(new TargetBeanFunction());
            return function.getResult(bean);
        } catch (ParseException pe) {
            throw new BeanExpressionException("Could not parse valang expression '" + expression + "' to a function", pe);
        }
    }

}
