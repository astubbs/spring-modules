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
import org.springmodules.validation.bean.rule.NotBlankValidationRule;

/**
 * An {@link AbstractPropertyValidationAnnotationHandler} implementation that handles {@link @NotBlank} annotations.
 *
 * @author Uri Boness
 */
public class NotBlankValidationAnnotationHandler extends AbstractPropertyValidationAnnotationHandler {

    /**
     * Constructs a new LengthValidationAnnotationHandler.
     */
    public NotBlankValidationAnnotationHandler() {
        super(NotBlank.class);
    }

    protected AbstractValidationRule createValidationRule(Annotation annotation, Class clazz, String propertyName) {
        return new NotBlankValidationRule();
    }

}
