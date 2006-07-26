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

package org.springmodules.validation.bean.conf.annotation.handler;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;

import org.springmodules.validation.util.condition.Condition;
import org.springmodules.validation.util.condition.Conditions;

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

    /**
     * This method delegates to {@link #extractRangeCondition(Range, Class, java.beans.PropertyDescriptor)},
     * {@link #extractMinCondition(Min, Class, java.beans.PropertyDescriptor)}, or
     * {@link #extractMaxCondition(Max, Class, java.beans.PropertyDescriptor)} based on the annotation type.
     *
     * @see AbstractPropertyValidationAnnotationHandler#extractCondition(java.lang.annotation.Annotation, Class, java.beans.PropertyDescriptor)
     */
    protected Condition extractCondition(Annotation annotation, Class clazz, PropertyDescriptor descriptor) {
        if (Range.class.isInstance(annotation)) {
            return extractRangeCondition((Range)annotation, clazz, descriptor);
        }
        if (Min.class.isInstance(annotation)) {
            return extractMinCondition((Min)annotation, clazz, descriptor);
        }
        if (Max.class.isInstance(annotation)) {
            return extractMaxCondition((Max)annotation, clazz, descriptor);
        }
        throw new IllegalArgumentException("RangeValidationAnnotationHandler does not suppport annotations of type: " +
            annotation.getClass().getName());
    }

    /**
     * Creates a {@link org.springmodules.validation.util.condition.range.BetweenIncludingCondition} or
     * {@link org.springmodules.validation.util.condition.range.BetweenCondition} for the given {@link Range}
     * annotation.
     */
    protected Condition extractRangeCondition(Range range, Class clazz, PropertyDescriptor descriptor) {
        double min = range.min();
        double max = range.max();
        return (range.inclusive()) ? Conditions.isBetweenIncluding(min, max) : Conditions.isBetween(min, max);
    }

    /**
     * Creates a {@link org.springmodules.validation.util.condition.range.GreaterThanOrEqualsCondition} or
     * {@link org.springmodules.validation.util.condition.range.GreaterThanCondition} for the given {@link Min}
     * annotation.
     */
    protected Condition extractMinCondition(Min min, Class clazz, PropertyDescriptor descriptor) {
        return (min.inclusive()) ? Conditions.isGte(min.value()) : Conditions.isGt(min.value());
    }

    /**
     * Creates a {@link org.springmodules.validation.util.condition.range.LessThanOrEqualsCondition} or
     * {@link org.springmodules.validation.util.condition.range.LessThanCondition} for the given {@link Max}
     * annotation.
     */
    protected Condition extractMaxCondition(Max max, Class clazz, PropertyDescriptor descriptor) {
        return (max.inclusive()) ? Conditions.isLte(max.value()) : Conditions.isLt(max.value());
    }
}
