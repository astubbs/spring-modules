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
import java.util.Calendar;
import java.util.Date;

import org.springmodules.validation.util.condition.Condition;
import org.springmodules.validation.util.condition.date.IsInTheFutureDateCondition;

/**
 * An {@link AbstractPropertyValidationAnnotationHandler} implementation that can handle {@link @InTheFuture} annotations
 * on properties of type {@link java.util.Date} or {@link java.util.Calendar}.
 *
 * @author Uri Boness
 */
public class DateInTheFutureValidationAnnotationHandler extends AbstractPropertyValidationAnnotationHandler {

    /**
     * Constructs a new DateInTheFutureValidationAnnotationHandler.
     */
    public DateInTheFutureValidationAnnotationHandler() {
        super(InTheFuture.class);
    }

    /**
     * In addition to the normal {@link AbstractPropertyValidationAnnotationHandler#supports(java.lang.annotation.Annotation, Class, java.beans.PropertyDescriptor)}
     * call, it also checks that the property is either a {@link java.util.Date} or {@link java.util.Calendar}.
     *
     * @see org.springmodules.validation.bean.conf.annotation.PropertyValidationAnnotationHandler#supports(java.lang.annotation.Annotation, Class, java.beans.PropertyDescriptor)
     */
    public boolean supports(Annotation annotation, Class clazz, PropertyDescriptor descriptor) {
        return
            super.supports(annotation, clazz, descriptor)
            &&
            (
                Date.class.isAssignableFrom(descriptor.getPropertyType())
                ||
                Calendar.class.isAssignableFrom(descriptor.getPropertyType())
            );
    }

    /**
     * Creates and returns a new {@link IsInTheFutureDateCondition}.
     *
     * @see AbstractPropertyValidationAnnotationHandler#extractCondition(java.lang.annotation.Annotation, Class, java.beans.PropertyDescriptor)
     */
    protected Condition extractCondition(Annotation annotation, Class clazz, PropertyDescriptor descriptor) {
        return new IsInTheFutureDateCondition();
    }

}
