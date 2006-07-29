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

import org.springmodules.validation.bean.conf.MutableBeanValidationConfiguration;

/**
 * Represents a handler that knows how the handle property validation annotation based on which the given
 * configuration will be changed.
 *
 * @author Uri Boness
 */
public interface PropertyValidationAnnotationHandler {

    /**
     * Indicates whether this handler can handle the given annotation.
     *
     * @param annotation The annotation to handle.
     * @return <code>true</code> if this handler can handle the given annotation, <code>false</code> otherwise.
     */
    boolean supports(Annotation annotation, Class clazz, PropertyDescriptor descriptor);

    /**
     * Handles the given property level annotation and manipulates the given bean validation configuration accordingly.
     *
     * @param annotation The annotation to handle.
     * @param clazz The annotated class.
     * @param descriptor The property descriptor of the annotated property.
     * @param configuration The bean validation configuration to manipulate.
     */
    void handleAnnotation(Annotation annotation, Class clazz, PropertyDescriptor descriptor, MutableBeanValidationConfiguration configuration);

}
