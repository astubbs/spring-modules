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

import org.springmodules.validation.bean.rule.AbstractValidationRule;
import org.springmodules.validation.bean.rule.MaxSizeValidationRule;
import org.springmodules.validation.bean.rule.MinSizeValidationRule;
import org.springmodules.validation.bean.rule.SizeValidationRule;

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

    protected AbstractValidationRule createValidationRule(Annotation annotation, Class clazz, String propertyName) {

        if (Size.class.isInstance(annotation)) {
            Size size = (Size) annotation;
            return new SizeValidationRule(size.min(), size.max());
        }

        if (MinSize.class.isInstance(annotation)) {
            MinSize minSize = (MinSize) annotation;
            return new MinSizeValidationRule(minSize.value());
        }
        if (MaxSize.class.isInstance(annotation)) {
            MaxSize maxSize = (MaxSize) annotation;
            return new MaxSizeValidationRule(maxSize.value());
        }
        throw new IllegalArgumentException("SizeValidationAnnotationHandler cannot handle annotation of type: " +
            annotation.getClass().getName());
    }

}
