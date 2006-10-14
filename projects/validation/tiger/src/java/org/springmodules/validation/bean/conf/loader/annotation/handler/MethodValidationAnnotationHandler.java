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
import java.lang.reflect.Method;

import org.springmodules.validation.bean.conf.MutableBeanValidationConfiguration;

/**
 * Represents a handler that knows how the handle method validation annotation based on which the given
 * configuration will be changed.
 *
 * @author Uri Boness
 */
public interface MethodValidationAnnotationHandler {

    /**
     * Returns whether the given handler can handle the given annotation for the given method.
     *
     * @param annotation The annotation to handle.
     * @param clazz The class of the validated bean.
     * @param method The annotated validation method.
     * @return <code>true</code> If this handler can handle the given annotation, <code>false</code> otherwise.
     */
    boolean supports(Annotation annotation, Class clazz, Method method);

    /**
     * Manipulates the given configuration based on the given annotation.
     *
     * @param annotation The validation annotation.
     * @param clazz The validated bean class.
     * @param method The annotated method.
     * @param configuration The configuration to manipulate.
     */
    void handleAnnotation(Annotation annotation, Class clazz, Method method, MutableBeanValidationConfiguration configuration);
}
