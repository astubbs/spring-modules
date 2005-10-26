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

package org.springmodules.template.providers.stemp;

import org.springmodules.template.*;
import org.springmodules.template.providers.stemp.resolvers.*;

/**
 * A BeanFactory that creates {@link StempTemplate}s.
 *
 * @author Uri Boness
 */
public class StempTemplateFactoryBean extends AbstractTemplateFactoryBean {

    private ExpressionResolver expressionResolver;
    private ExpressionWrapping expressionWrapping;

    private String expressionWrappingPrefix;
    private String expressionWrappingSuffix;

    /**
     * Empty contructor (JavaBean support)
     */
    public StempTemplateFactoryBean() {
        expressionResolver = new StempelExpressionResolver();
        expressionWrapping = new ExpressionWrapping("${", "}");
    }

    /**
     * Sets the expression resolver to be used by stemp.
     *
     * @param expressionResolver The expression resolver to be used by stemp.
     */
    public void setExpressionResolver(ExpressionResolver expressionResolver) {
        this.expressionResolver = expressionResolver;
    }

    /**
     * Sets the expression wrapping used in the template sources.
     *
     * @param expressionWrapping The expression wrapping used in the template sources.
     */
    public void setExpressionWrapping(ExpressionWrapping expressionWrapping) {
        this.expressionWrapping = expressionWrapping;
    }

    /**
     * Sets the prefix of the expression wrapping.
     *
     * @param expressionWrappingPrefix The prefix of the expression wrapping.
     */
    public void setExpressionWrappingPrefix(String expressionWrappingPrefix) {
        this.expressionWrappingPrefix = expressionWrappingPrefix;
    }

    /**
     * Sets the suffix of the expression wrapping.
     *
     * @param expressionWrappingSuffix The suffix of the expression wrapping.
     */
    public void setExpressionWrappingSuffix(String expressionWrappingSuffix) {
        this.expressionWrappingSuffix = expressionWrappingSuffix;
    }

    /**
     * Creates a new StempTemplateFactory.
     *
     * @see org.springmodules.template.AbstractTemplateFactoryBean#createTemplateFactory()
     */
    protected TemplateFactory createTemplateFactory() throws Exception {
        if (expressionWrappingPrefix != null && expressionWrappingSuffix != null) {
            expressionWrapping = new ExpressionWrapping(expressionWrappingPrefix, expressionWrappingSuffix);
        }
        return new StempTemplateFactory(expressionWrapping, expressionResolver);
    }
}
