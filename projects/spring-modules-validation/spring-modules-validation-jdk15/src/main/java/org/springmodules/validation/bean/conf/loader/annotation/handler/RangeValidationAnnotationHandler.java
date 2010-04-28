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
import org.springmodules.validation.bean.rule.MaxValidationRule;
import org.springmodules.validation.bean.rule.MinValidationRule;
import org.springmodules.validation.bean.rule.RangeValidationRule;

/**
 * An {@link AbstractPropertyValidationAnnotationHandler} that handles {@link Range}, {@link Min}, and {@link Max}
 * annotations.
 *
 * @author Uri Boness
 */
public class RangeValidationAnnotationHandler extends AbstractPropertyValidationAnnotationHandler {

    /**
     * Constructs a new RangeValidationAnnotationHandler.
     */
    public RangeValidationAnnotationHandler() {
        super(Range.class, Min.class, Max.class);
    }

    protected AbstractValidationRule createValidationRule(Annotation annotation, Class clazz, String propertyName) {
        if (Range.class.isInstance(annotation)) {
            Range range = (Range) annotation;
            return new RangeValidationRule(range.min(), range.max());
        }
        if (Min.class.isInstance(annotation)) {
            Min min = (Min) annotation;
            return new MinValidationRule(min.value());
        }
        if (Max.class.isInstance(annotation)) {
            Max max = (Max) annotation;
            return new MaxValidationRule(max.value());
        }
        throw new IllegalArgumentException("RangeValidationAnnotationHandler does not suppport annotations of type: " +
            annotation.getClass().getName());
    }

}
