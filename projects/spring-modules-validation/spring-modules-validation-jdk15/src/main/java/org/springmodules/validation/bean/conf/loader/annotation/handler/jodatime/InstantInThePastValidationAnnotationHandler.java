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

package org.springmodules.validation.bean.conf.loader.annotation.handler.jodatime;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;

import org.joda.time.Instant;
import org.springmodules.validation.bean.conf.loader.annotation.handler.AbstractPropertyValidationAnnotationHandler;
import org.springmodules.validation.bean.conf.loader.annotation.handler.InThePast;
import org.springmodules.validation.bean.rule.AbstractValidationRule;
import org.springmodules.validation.bean.rule.InstantInThePastValidationRule;

/**
 * An {@link AbstractPropertyValidationAnnotationHandler} implementation that can handle {@link InThePast} annotations
 * on properties of type {@link org.joda.time.Instant}.
 *
 * @author Uri Boness
 */
public class InstantInThePastValidationAnnotationHandler extends AbstractPropertyValidationAnnotationHandler {

    /**
     * Constructs a new DateInTheFutureValidationAnnotationHandler.
     */
    public InstantInThePastValidationAnnotationHandler() {
        super(InThePast.class);
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

    protected AbstractValidationRule createValidationRule(Annotation annotation, Class clazz, String propertyName) {
        return new InstantInThePastValidationRule();
    }

}
