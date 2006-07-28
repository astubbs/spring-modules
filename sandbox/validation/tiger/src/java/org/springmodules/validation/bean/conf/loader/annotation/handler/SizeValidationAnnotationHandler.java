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

import org.springmodules.validation.util.condition.Condition;
import org.springmodules.validation.util.condition.Conditions;

/**
 * An {@link AbstractPropertyValidationAnnotationHandler} implementation that handles {@link Size}, {@link MinSize},
 * {@link MaxSize}.
 *
 * @author Uri Boness
 */
public class SizeValidationAnnotationHandler extends AbstractPropertyValidationAnnotationHandler {

    /**
     * Constructs a new SizeValidationAnnotationHandler.
     */
    public SizeValidationAnnotationHandler() {
        super(Size.class, MinSize.class, MaxSize.class);
    }

    /**
     * Delegates to {@link #extractSizeCondition(Size, Class, java.beans.PropertyDescriptor)},
     * {@link #extractMinSizeCondition(MinSize, Class, java.beans.PropertyDescriptor)} or
     * {@link #extractMaxSizeCondition(MaxSize, Class, java.beans.PropertyDescriptor)} based on the annotation type.
     *
     * @see AbstractPropertyValidationAnnotationHandler#extractCondition(java.lang.annotation.Annotation, Class, java.beans.PropertyDescriptor)
     */
    protected Condition extractCondition(Annotation annotation, Class clazz, PropertyDescriptor descriptor) {
        if (Size.class.isInstance(annotation)) {
            return extractSizeCondition((Size)annotation, clazz, descriptor);
        }
        if (MinSize.class.isInstance(annotation)) {
            return extractMinSizeCondition((MinSize)annotation, clazz, descriptor);
        }
        if (MaxSize.class.isInstance(annotation)) {
            return extractMaxSizeCondition((MaxSize)annotation, clazz, descriptor);
        }
        throw new IllegalArgumentException("SizeValidationAnnotationHandler cannot handle annotation of type: " +
            annotation.getClass().getName());
    }

    /**
     * Creates and returnes a new {@link org.springmodules.validation.util.condition.collection.SizeRangeCollectionCondition}
     * from the given {@link Size} annotation.
     */
    protected Condition extractSizeCondition(Size size, Class clazz, PropertyDescriptor descriptor) {
        return Conditions.sizeRange(size.min(), size.max());
    }

    /**
     * Creates and returns a new {@link org.springmodules.validation.util.condition.collection.MinSizeCollectionCondition}
     * from the given {@link MinSize} annotation.
     */
    protected Condition extractMinSizeCondition(MinSize minSize, Class clazz, PropertyDescriptor descriptor) {
        return Conditions.minSize(minSize.value());
    }

    /**
     * Creates and returns a new {@link org.springmodules.validation.util.condition.collection.MaxSizeCollectionCondition}
     * from the given {@link MaxSize} annotation.
     */
    protected Condition extractMaxSizeCondition(MaxSize maxSize, Class clazz, PropertyDescriptor descriptor) {
        return Conditions.maxSize(maxSize.value());
    }
}
