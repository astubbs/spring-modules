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

import ognl.Ognl;
import ognl.OgnlException;
import org.springmodules.validation.util.bel.BeanExpressionException;
import org.springmodules.validation.util.bel.BeanExpressionResolver;

/**
 * A {@link org.springmodules.validation.util.bel.BeanExpressionResolver} implementation that uses OGNL expressions to resolve the objects.
 *
 * @author Uri Boness
 */
public class OgnlExpressionResolver implements BeanExpressionResolver {

    /**
     * Evaluates the given OGNL expression on the given bean and returns the resolved object.
     *
     * @param bean The bean on which the given expression will be evaluated.
     * @param expression The given OGNL expression.
     * @return The resolved object.
     */
    public Object resolve(Object bean, String expression) throws BeanExpressionException {
        try {
            return Ognl.getValue(expression, bean);
        } catch (OgnlException oe) {
            throw new BeanExpressionException("Could not parse OGNL expression '" + expression + "'", oe);
        }
    }

}
