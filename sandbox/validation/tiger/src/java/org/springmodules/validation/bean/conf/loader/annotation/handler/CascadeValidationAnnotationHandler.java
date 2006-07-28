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

import org.springframework.util.StringUtils;
import org.springmodules.validation.bean.conf.MutableBeanValidationConfiguration;
import org.springmodules.validation.bean.conf.loader.annotation.PropertyValidationAnnotationHandler;
import org.springmodules.validation.util.cel.ConditionExpressionBased;
import org.springmodules.validation.util.cel.ConditionExpressionParser;
import org.springmodules.validation.util.cel.valang.ValangConditionExpressionParser;

/**
 * A {@link PropertyValidationAnnotationHandler} that handles {@link CascadeValidation} annotations.
 *
 * @author Uri Boness
 */
public class CascadeValidationAnnotationHandler implements PropertyValidationAnnotationHandler, ConditionExpressionBased {

    private ConditionExpressionParser conditionExpressionParser;

    /**
     * Constructs a new CascadeValidationAnnotationHandler.
     */
    public CascadeValidationAnnotationHandler() {
        this(new ValangConditionExpressionParser());
    }

    /**
     * Constructs a new CascadeValidationAnnotationHandler with a given condition expression parser to be used
     * when evaluating the applicability condition expression.
     */
    public CascadeValidationAnnotationHandler(ConditionExpressionParser conditionExpressionParser) {
        setConditionExpressionParser(conditionExpressionParser);
    }

    /**
     * @see ConditionExpressionBased#setConditionExpressionParser(org.springmodules.validation.util.cel.ConditionExpressionParser)
     */
    public void setConditionExpressionParser(ConditionExpressionParser conditionExpressionParser) {
        this.conditionExpressionParser = conditionExpressionParser;
    }

    /**
     * @return <code>true</code> if the annotation is of type <code>{@link CascadeValidation}</code>.
     *
     * @see PropertyValidationAnnotationHandler#supports(java.lang.annotation.Annotation, Class, java.beans.PropertyDescriptor)
     */
    public boolean supports(Annotation annotation, Class clazz, PropertyDescriptor descriptor) {
        return CascadeValidation.class.isInstance(annotation);
    }

    /**
     * Registers the annotated property for cascade validation.
     *
     * @see PropertyValidationAnnotationHandler#handleAnnotation(java.lang.annotation.Annotation, Class, java.beans.PropertyDescriptor, org.springmodules.validation.bean.conf.MutableBeanValidationConfiguration)
     */
    public void handleAnnotation(Annotation annotation, Class clazz, PropertyDescriptor descriptor, MutableBeanValidationConfiguration configuration) {
        CascadeValidation cascadeValidationAnnotation = (CascadeValidation)annotation;
        org.springmodules.validation.bean.conf.CascadeValidation cascadeValidation =
            new org.springmodules.validation.bean.conf.CascadeValidation(descriptor.getName());
        if (StringUtils.hasText(cascadeValidationAnnotation.value())) {
            cascadeValidation.setApplicabilityCondition(conditionExpressionParser.parse(cascadeValidationAnnotation.value()));
        }
        configuration.addCascadeValidation(cascadeValidation);
    }

}
