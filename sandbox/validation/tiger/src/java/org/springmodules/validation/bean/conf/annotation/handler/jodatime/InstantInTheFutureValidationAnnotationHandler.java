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

package org.springmodules.validation.bean.conf.annotation.handler.jodatime;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;

import org.joda.time.Instant;
import org.springmodules.validation.bean.conf.annotation.handler.AbstractPropertyValidationAnnotationHandler;
import org.springmodules.validation.bean.conf.annotation.handler.InTheFuture;
import org.springmodules.validation.util.condition.Condition;
import org.springmodules.validation.util.condition.date.jodatime.IsInTheFutureInstantCondition;

/**
 * An {@link AbstractPropertyValidationAnnotationHandler} implementation that can handle {@link @InTheFuture} annotations
 * on properties of type {@link org.joda.time.Instant}.
 *
 * @author Uri Boness
 */
public class InstantInTheFutureValidationAnnotationHandler extends AbstractPropertyValidationAnnotationHandler {

    /**
     * Constructs a new DateInTheFutureValidationAnnotationHandler.
     */
    public InstantInTheFutureValidationAnnotationHandler() {
        super(InTheFuture.class);
    }

    /**
     * In addition to the normal {@link AbstractPropertyValidationAnnotationHandler#supports(java.lang.annotation.Annotation, Class, java.beans.PropertyDescriptor)}
     * call, it also checks that the property is of type {@link org.joda.time.Instant}.
     *
     * @see AbstractPropertyValidationAnnotationHandler#supports(java.lang.annotation.Annotation, Class, java.beans.PropertyDescriptor)
     */
    public boolean supports(Annotation annotation, Class clazz, PropertyDescriptor descriptor) {
        return super.supports(annotation, clazz, descriptor) &&
            Instant.class.isAssignableFrom(descriptor.getPropertyType());
    }

    /**
     * Creates and returns a new {@link org.springmodules.validation.util.condition.date.jodatime.IsInTheFutureInstantCondition}.
     *
     * @see AbstractPropertyValidationAnnotationHandler#extractCondition(java.lang.annotation.Annotation, Class, java.beans.PropertyDescriptor)
     */
    protected Condition extractCondition(Annotation annotation, Class clazz, PropertyDescriptor descriptor) {
        return new IsInTheFutureInstantCondition();
    }

}
