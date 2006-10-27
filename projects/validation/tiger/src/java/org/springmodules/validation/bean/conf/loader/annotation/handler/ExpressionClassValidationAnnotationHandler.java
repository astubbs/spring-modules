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
 * An {@link AbstractClassValidationAnnotationHandler} implementation that handles {@link Expression} annnotations.
 * The condition expression will be parsed by the same condition expression parser as used to parse the applicability
 * condition expression.
 *
 * @author Uri Boness
 */
public class ExpressionClassValidationAnnotationHandler extends AbstractClassValidationAnnotationHandler {

    /**
     * Constructs a new ExpressionPropertyValidationAnnotationHandler.
     */
    public ExpressionClassValidationAnnotationHandler() {
        super(Expression.class);
    }

    protected AbstractValidationRule createValidationRule(Annotation annotation, Class clazz) {
        Expression expression = (Expression) annotation;
        return new ExpressionValidationRule(getConditionExpressionParser(), expression.value());
    }

}
