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

package org.springmodules.validation.bean.conf.loader.annotation;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.springmodules.validation.bean.conf.loader.annotation.handler.ClassValidationAnnotationHandler;
import org.springmodules.validation.bean.conf.loader.annotation.handler.MethodValidationAnnotationHandler;
import org.springmodules.validation.bean.conf.loader.annotation.handler.PropertyValidationAnnotationHandler;

/**
 * A registry for all validation annotation handlers.
 *
 * @author Uri Boness
 * @see {@link AnnotationBeanValidationConfigurationLoader#setHandlerRegistry(ValidationAnnotationHandlerRegistry)}
 */
public interface ValidationAnnotationHandlerRegistry {

    /**
     * Finds and returns the most appropriate {@link ClassValidationAnnotationHandler} to handle the given
     * annotation. If no such handler was found this method returns <code>null</code>.
     *
     * @param annotation The annotation to handle.
     * @param clazz The annotated class.
     * @return The most appropriate validation annotation handler to handle the given annotation.
     */
    ClassValidationAnnotationHandler findClassHanlder(Annotation annotation, Class clazz);

    /**
     * Finds and returns the most appropriate {@link PropertyValidationAnnotationHandler} to handle the given
     * annotation. If no such handler was found, this method returns <code>null</code>.
     *
     * @param annotation The annotation to handle.
     * @param clazz The annotated class.
     * @param descriptor The property descriptor of the annotated property.
     * @return The most appropriate validation annotation handler to handle the given annoation.
     */
    PropertyValidationAnnotationHandler findPropertyHanlder(Annotation annotation, Class clazz, PropertyDescriptor descriptor);

    /**
     * Finds and returns the most appropriate {@link MethodValidationAnnotationHandler} to handle the given
     * annotation. If no such handler was found, this method returns <code>null</code>.
     *
     * @param annotation The annotation to handle.
     * @param clazz The annotated class.
     * @param method The annotated method.
     * @return The most appropriate validation annotation handler to handle the given annoation.
     */
    MethodValidationAnnotationHandler findMethodHandler(Annotation annotation, Class clazz, Method method);
}
