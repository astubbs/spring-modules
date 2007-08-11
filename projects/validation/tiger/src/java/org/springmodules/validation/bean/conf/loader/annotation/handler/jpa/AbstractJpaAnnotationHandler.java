/*
 * Copyright 2002-2007 the original author or authors.
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

package org.springmodules.validation.bean.conf.loader.annotation.handler.jpa;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.springmodules.validation.bean.conf.loader.annotation.handler.PropertyValidationAnnotationHandler;

/**
 * @author Uri Boness
 */
public abstract class AbstractJpaAnnotationHandler implements PropertyValidationAnnotationHandler {

    protected Set<Annotation> getAllPropertyAnnotations(Class clazz, PropertyDescriptor descriptor) {
        Set<Annotation> annotations = new HashSet<Annotation>();

        Class parent = clazz;
        while (parent != null) {
            try {
                Field field = clazz.getDeclaredField(descriptor.getName());
                Annotation[] fieldAnnotations = field.getAnnotations();
                for (Annotation annotation : fieldAnnotations) {
                    annotations.add(annotation);
                }
                break;
            } catch (NoSuchFieldException nsfe) {
                // indicates this class doesn't hold the property field
            }
            parent = clazz.getSuperclass();
        }

        Method setter = descriptor.getWriteMethod();
        Annotation[] setterAnnotations = setter.getAnnotations();
        for (Annotation annotation : setterAnnotations) {
            annotations.add(annotation);
        }

        return annotations;
    }

    protected boolean contains(Set<Annotation> annotations, Class annotationType) {
        for (Annotation annotation : annotations) {
            if (annotationType.isInstance(annotation)) {
                return true;
            }
        }
        return false;
    }

}
