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

import org.springmodules.template.providers.stemp.*;

/**
 * This resolver tries to fetch a value from the model with the given expression as the key.
 * the fetched value is then converted to a String by using String.valueOf()
 * method.
 *
 * @author Uri Boness
 */
public class SimpleExpressionResolver implements ExpressionResolver {

    /**
     * @see org.springmodules.template.providers.stemp.ExpressionResolver#resolve(String, java.util.Map)
     */
    public String resolve(String expression, Map model) {
        return String.valueOf(model.get(expression));
    }
}
