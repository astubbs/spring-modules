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

package org.springmodules.validation.bean.conf.loader.annotation.handler;

import java.lang.annotation.Annotation;

import org.springmodules.validation.bean.rule.AbstractValidationRule;
import org.springmodules.validation.bean.rule.ExpressionValidationRule;

/**
 * An {@link AbstractPropertyValidationAnnotationHandler} implementation that handles {@link Expression} annnotations.
 *
 * @author Uri Boness
 */
public class ExpressionPropertyValidationAnnotationHandler extends AbstractPropertyValidationAnnotationHandler {

    /**
     * Constructs a new ExpressionPropertyValidationAnnotationHandler.
     */
    public ExpressionPropertyValidationAnnotationHandler() {
        super(Expression.class);
    }

    /**
     * The extracted condition is globally scoped if the {@link org.springmodules.validation.bean.conf.loader.annotation.handler.Expression#scope()}
     * attribute is set to {@link ExpressionScope#CONTAINING_OBJECT}.
     *
     * @see org.springmodules.validation.bean.conf.loader.annotation.handler.AbstractPropertyValidationAnnotationHandler#isConditionGloballyScoped(java.lang.annotation.Annotation)
     */
    protected boolean isConditionGloballyScoped(Annotation annotation) {
        Expression valang = (Expression)annotation;
        return valang.scope() == ExpressionScope.CONTAINING_OBJECT;
    }

    protected AbstractValidationRule createValidationRule(Annotation annotation, Class clazz, String propertyName) {
        Expression expression = (Expression)annotation;
        return new ExpressionValidationRule(getConditionExpressionParser(), expression.value());
    }

}
