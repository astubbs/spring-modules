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

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.util.Calendar;
import java.util.Date;

import org.springmodules.validation.bean.rule.AbstractValidationRule;
import org.springmodules.validation.bean.rule.DateInThePastValidationRule;

/**
 * An {@link org.springmodules.validation.bean.conf.loader.annotation.handler.AbstractPropertyValidationAnnotationHandler}
 * implementation that can handle {@link @InThePast} annotations on properties of type
 * {@link java.util.Date} or {@link java.util.Calendar}.
 *
 * @author Uri Boness
 */
public class DateInThePastValidationAnnotationHandler extends AbstractPropertyValidationAnnotationHandler {

    /**
     * Constructs a new DateInThePastValidationAnnotationHandler.
     */
    public DateInThePastValidationAnnotationHandler() {
        super(InThePast.class);
    }

    /**
     * In addition to the normal {@link org.springmodules.validation.bean.conf.loader.annotation.handler.AbstractPropertyValidationAnnotationHandler#supports(java.lang.annotation.Annotation, Class, java.beans.PropertyDescriptor)}
     * call, it also checks that the property is either a {@link java.util.Date} or {@link java.util.Calendar}.
     *
     * @see org.springmodules.validation.bean.conf.loader.annotation.handler.PropertyValidationAnnotationHandler#supports(java.lang.annotation.Annotation, Class, java.beans.PropertyDescriptor)
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

    protected AbstractValidationRule createValidationRule(Annotation annotation, Class clazz, String propertyName) {
        return new DateInThePastValidationRule();
    }

}
