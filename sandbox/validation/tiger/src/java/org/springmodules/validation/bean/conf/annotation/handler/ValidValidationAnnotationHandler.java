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

import java.lang.annotation.Annotation;
import java.beans.PropertyDescriptor;

import org.springmodules.validation.bean.conf.annotation.PropertyValidationAnnotationHandler;
import org.springmodules.validation.bean.conf.MutableBeanValidationConfiguration;

/**
 * A {@link PropertyValidationAnnotationHandler} that handles {@link Valid} annotations.
 *
 * @author Uri Boness
 */
public class ValidValidationAnnotationHandler implements PropertyValidationAnnotationHandler {

    /**
     * @return <code>true</code> if the annotation is of type <code>{@link Valid}</code>.
     *
     * @see PropertyValidationAnnotationHandler#supports(java.lang.annotation.Annotation, Class, java.beans.PropertyDescriptor)
     */
    public boolean supports(Annotation annotation, Class clazz, PropertyDescriptor descriptor) {
        return Valid.class.isInstance(annotation);
    }

    /**
     * Registers the annotated property for cascade validation.
     *
     * @see PropertyValidationAnnotationHandler#handleAnnotation(java.lang.annotation.Annotation, Class, java.beans.PropertyDescriptor, org.springmodules.validation.bean.conf.MutableBeanValidationConfiguration)
     */
    public void handleAnnotation(Annotation annotation, Class clazz, PropertyDescriptor descriptor, MutableBeanValidationConfiguration configuration) {
        configuration.addRequiredValidatableProperty(descriptor.getName());
    }

}
