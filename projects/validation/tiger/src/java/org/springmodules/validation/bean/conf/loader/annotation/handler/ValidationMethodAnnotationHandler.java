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
import java.lang.reflect.Method;

import org.springframework.util.StringUtils;
import org.springmodules.validation.bean.rule.AbstractValidationRule;
import org.springmodules.validation.bean.rule.ValidationMethodValidationRule;

/**
 *
 *
 * @author Uri Boness
 */
public class ValidationMethodAnnotationHandler extends AbstractMethodValidationAnnotationHandler {

    public ValidationMethodAnnotationHandler() {
        super(ValidationMethod.class);
    }

    protected String extractErrorCode(Annotation annotation) {
        String errorCode = super.extractErrorCode(annotation);
        return (StringUtils.hasLength(errorCode)) ? errorCode : null;
    }

    protected String extractDefaultMessage(Annotation annotation) {
        String message = super.extractDefaultMessage(annotation);
        return (StringUtils.hasLength(message)) ? message : null;
    }

    public boolean supports(Annotation annotation, Class clazz, Method method) {
        if (!super.supports(annotation, clazz, method)) {
            return false;
        }
        return (method.getReturnType().equals(Boolean.class) || method.getReturnType().equals(boolean.class))
            && method.getParameterTypes().length == 0;
    }

    protected AbstractValidationRule createValidationRule(Annotation annotation, Class clazz, Method method) {
        return new ValidationMethodValidationRule(method);
    }

}
