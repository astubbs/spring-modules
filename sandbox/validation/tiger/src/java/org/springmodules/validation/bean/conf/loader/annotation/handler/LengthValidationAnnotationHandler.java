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

import org.springframework.util.Assert;
import org.springmodules.validation.bean.rule.AbstractValidationRule;
import org.springmodules.validation.bean.rule.LengthValidationRule;
import org.springmodules.validation.bean.rule.MinLengthValidationRule;

/**
 * An {@link AbstractPropertyValidationAnnotationHandler} implementation that handles {@link Length} annotations.
 *
 * @author Uri Boness
 */
public class LengthValidationAnnotationHandler extends AbstractPropertyValidationAnnotationHandler {

    /**
     * Constructs a new LengthValidationAnnotationHandler.
     */
    public LengthValidationAnnotationHandler() {
        super(Length.class, MinLength.class, MaxLength.class);
    }

    protected AbstractValidationRule createValidationRule(Annotation annotation, Class clazz, String propertyName) {

        if (Length.class.isInstance(annotation)) {
            Length length = (Length)annotation;
            validateMinMax(length.min(), length.max(), clazz, propertyName);
            return new LengthValidationRule(length.min(), length.max());
        }

        if (MinLength.class.isInstance(annotation)) {
            MinLength minLength = (MinLength)annotation;
            validateMin(minLength.value(), clazz, propertyName);
            return new MinLengthValidationRule(minLength.value());
        }

        if (Length.class.isInstance(annotation)) {
            Length length = (Length)annotation;
            validateMinMax(length.min(), length.max(), clazz, propertyName);
            return new LengthValidationRule(length.min(), length.max());
        }

        throw new IllegalArgumentException("LengthValidationAnnotationHandler does not suppport annotations of type: " +
            annotation.getClass().getName());
    }


    protected void validateMinMax(int min, int max, Class clazz, String propertyName) {
        Assert.isTrue(max >= 0, "@Length annotation on property '" + clazz.getName() + "." + propertyName +
            "' is mal-configured - 'max' attribute cannot hold a negative value");
        Assert.isTrue(min >= 0, "@Length annotation on property '" + clazz.getName() + "." + propertyName +
            "' is mal-configured - 'min' attribute cannot hold a negative value");
        Assert.isTrue(max >= min, "@Length annotation on property '" + clazz.getName() + "." + propertyName +
            "' is mal-configured - 'max' attribute is smaller than 'min'");
    }

    protected void validateMax(int max, Class clazz, String propertyName) {
        Assert.isTrue(max >= 0, "@Length annotation on property '" + clazz.getName() + "." + propertyName +
            "' is mal-configured - 'max' attribute cannot hold a negative value");
    }

    protected void validateMin(int min, Class clazz, String propertyName) {
        Assert.isTrue(min >= 0, "@Length annotation on property '" + clazz.getName() + "." + propertyName +
            "' is mal-configured - 'min' attribute cannot hold a negative value");
    }
}
