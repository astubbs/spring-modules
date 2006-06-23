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
 * Objects that use a {@link BeanExpressionResolver} and are aware of it should implement this interface.
 *
 * @author Uri Boness
 */
public interface BeanExpressionResolverAware {

    /**
     * Sets the {@link BeanExpressionResolver} to be used.
     *
     * @param resolver The bean expression resolver to be used.
     */
    void setBeanExpressionResolver(BeanExpressionResolver resolver);

    /**
     * Returns the used {@link BeanExpressionResolver}.
     *
     * @return The used bean expression resolver.
     */
    BeanExpressionResolver getBeanExpressionResolver();

}
