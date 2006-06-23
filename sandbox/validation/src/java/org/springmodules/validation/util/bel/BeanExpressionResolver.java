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

package org.springmodules.validation.util.bel;

/**
 * Evaluates an expression on a given bean to resolve/extract/create another object.
 *
 * @author Uri Boness
 */
public interface BeanExpressionResolver {

    /**
     * Evaluates the given expression upon the given bean and returns the resolved object.
     *
     * @param bean The bean upon which the expression will be evalutated.
     * @param expression The expression.
     * @return The resolved object.
     */
    Object resolve(Object bean, String expression) throws BeanExpressionException;

}
