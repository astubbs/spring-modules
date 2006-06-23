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

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springmodules.validation.util.bel.BeanExpressionException;
import org.springmodules.validation.util.bel.BeanExpressionResolver;

/**
 * A {@link org.springmodules.validation.util.bel.BeanExpressionResolver} that expects a {@link org.springframework.beans.BeanWrapper} compliant property
 * path as an expression. The resolved object is the nested property of the given bean. Since this expression type is
 * originally designed to access the bean properties, there is a small extension to this expression language to enable
 * self reference, that is, so it would be possible to return the bean itself. Using the <code>@this</code>
 * keyword will do just that.
 *
 * @author Uri Boness
 */
public class BeanWrapperExpressionResolver implements BeanExpressionResolver {

    private final static String THIS_REF = "@this";
    private final static String THIS_REF_PREFIX = THIS_REF + ".";

    /**
     * Returns the resolved nexted property as specified by the nested property expression.
     *
     * @see BeanExpressionResolver#resolve(Object, String)
     */
    public Object resolve(Object bean, String expression) throws BeanExpressionException {
        if (THIS_REF.equals(expression)) {
            return bean;
        }
        if (expression.startsWith(THIS_REF_PREFIX)) {
            expression = expression.substring(THIS_REF_PREFIX.length());
        }
        BeanWrapper wrapper = new BeanWrapperImpl(bean);
        return wrapper.getPropertyValue(expression);
    }

}
