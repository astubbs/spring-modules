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

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;

import org.springframework.util.StringUtils;
import org.springmodules.validation.bean.conf.MutableBeanValidationConfiguration;
import org.springmodules.validation.bean.conf.annotation.PropertyValidationAnnotationHandler;
import org.springmodules.validation.util.condition.parser.ConditionParser;
import org.springmodules.validation.util.condition.parser.ConditionParserAware;
import org.springmodules.validation.util.condition.parser.valang.ValangConditionParser;

/**
 * A {@link PropertyValidationAnnotationHandler} that handles {@link CascadeValidation} annotations.
 *
 * @author Uri Boness
 */
public class CascadeValidationAnnotationHandler implements PropertyValidationAnnotationHandler, ConditionParserAware {

    private ConditionParser conditionParser;

    /**
     * Constructs a new CascadeValidationAnnotationHandler.
     */
    public CascadeValidationAnnotationHandler() {
        this(new ValangConditionParser());
    }

    /**
     * Constructs a new CascadeValidationAnnotationHandler witha a given condition parser.
     *
     * @param conditionParser The condition parser to be used when parsing the applicability boolean expression.
     */
    public CascadeValidationAnnotationHandler(ConditionParser conditionParser) {
        this.conditionParser = conditionParser;
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
            cascadeValidation.setApplicabilityCondition(conditionParser.parse(cascadeValidationAnnotation.value()));
        }
        configuration.addCascadeValidation(cascadeValidation);
    }

    /**
     * Sets the {@link org.springmodules.validation.util.condition.parser.ConditionParser} to be used.
     *
     * @param conditionParser The condition parser to be used.
     */
    public void setConditionParser(ConditionParser conditionParser) {
        this.conditionParser = conditionParser;
    }

    /**
     * Returns the used {@link org.springmodules.validation.util.condition.parser.ConditionParser}.
     *
     * @return The used condition parser.
     */
    public ConditionParser getConditionParser() {
        return conditionParser;
    }

}
