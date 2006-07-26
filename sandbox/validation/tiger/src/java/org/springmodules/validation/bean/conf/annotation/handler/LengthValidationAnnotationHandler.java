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

import org.springframework.util.Assert;
import org.springmodules.validation.util.condition.Condition;
import org.springmodules.validation.util.condition.Conditions;

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
        super(Length.class);
    }

    /**
     * Extracts the condition of the validation rule represented by the given annotation.
     *
     * @param annotation The annotation from which the condition should be extracted.
     * @param clazz      The annotated class.
     * @param descriptor The property descriptor of the annotated property.
     * @return The extracted condition.
     */
    protected Condition extractCondition(Annotation annotation, Class clazz, PropertyDescriptor descriptor) {
        Length length = (Length)annotation;
        int min = length.min();
        int max = length.max();
        Assert.isTrue(max >= 0, "@Length annotation on property '" + clazz.getName() + "." + descriptor.getName() +
            "' is mal-configured - 'max' attribute cannot hold a negative value");
        Assert.isTrue(min >= 0, "@Length annotation on property '" + clazz.getName() + "." + descriptor.getName() +
            "' is mal-configured - 'min' attribute cannot hold a negative value");
        Assert.isTrue(max >= min, "@Length annotation on property '" + clazz.getName() + "." + descriptor.getName() +
            "' is mal-configured - 'max' attribute is smaller than 'min'");
        return Conditions.lengthBetween(min, max);
    }

}
