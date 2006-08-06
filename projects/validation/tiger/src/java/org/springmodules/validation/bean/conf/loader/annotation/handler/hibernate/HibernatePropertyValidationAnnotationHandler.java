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

package org.springmodules.validation.bean.conf.loader.annotation.handler.hibernate;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;

import org.hibernate.validator.Valid;
import org.hibernate.validator.Validator;
import org.hibernate.validator.ValidatorClass;
import org.springframework.beans.BeanUtils;
import org.springmodules.validation.bean.conf.CascadeValidation;
import org.springmodules.validation.bean.conf.MutableBeanValidationConfiguration;
import org.springmodules.validation.bean.conf.loader.annotation.handler.PropertyValidationAnnotationHandler;
import org.springmodules.validation.bean.rule.DefaultValidationRule;
import org.springmodules.validation.util.condition.AbstractCondition;
import org.springmodules.validation.util.condition.Condition;
import org.springmodules.validation.util.condition.Conditions;

/**
 * A {@link org.springmodules.validation.bean.conf.loader.annotation.handler.PropertyValidationAnnotationHandler} that handles Hibernate-Annotation validation annotations.
 *
 * @author Uri Boness
 */
public class HibernatePropertyValidationAnnotationHandler implements PropertyValidationAnnotationHandler {

    /**
     * Checks whether the annnotation is one of the annotations defined by hibernate-annotations lib.
     *
     * @see PropertyValidationAnnotationHandler#supports(java.lang.annotation.Annotation, Class, java.beans.PropertyDescriptor)
     */
    public boolean supports(Annotation annotation, Class clazz, PropertyDescriptor descriptor) {
        Class annotationClass = annotation.annotationType();
        ValidatorClass validatorClassAnnotation = (ValidatorClass)annotationClass.getAnnotation(ValidatorClass.class);
        if (validatorClassAnnotation != null) {
            return true;
        }

        if (annotation instanceof Valid) {
            return true;
        }

        return false;
    }

    /**
     * Handles the given property level annotation and manipulates the given bean validation configuration accordingly.
     *
     * @param annotation    The annotation to handle.
     * @param clazz         The annotated class.
     * @param descriptor    The property descriptor of the annotated property.
     * @param configuration The bean validation configuration to manipulate.
     */
    public void handleAnnotation(Annotation annotation, Class clazz, PropertyDescriptor descriptor, MutableBeanValidationConfiguration configuration) {

        if (annotation instanceof Valid) {
            configuration.addCascadeValidation(new CascadeValidation(descriptor.getName()));
            return;
        }

        Class annotationClass = annotation.annotationType();
        ValidatorClass validatorClassAnnotation = (ValidatorClass)annotationClass.getAnnotation(ValidatorClass.class);
        Class<? extends Validator> validatorClass = validatorClassAnnotation.value();
        Validator validator = (Validator) BeanUtils.instantiateClass(validatorClass);
        validator.initialize(annotation);
        Condition condition = Conditions.property(descriptor.getName(), new HibernateValidatorCondition(validator));
        String message;
        try {
            message = (String) annotationClass.getMethod("message").invoke(annotation);
        } catch (NoSuchMethodException nsme) {
            message = annotationClass.getSimpleName() + ".error";
        } catch (IllegalAccessException e) {
            message = annotationClass.getSimpleName() + ".error";
        } catch (InvocationTargetException e) {
            message = annotationClass.getSimpleName() + ".error";
        }

        configuration.addPropertyRule(descriptor.getName(), new DefaultValidationRule(condition, message, message, new Object[0]));

    }

    /**
     * A {@link Condition} implementation that delegates checking to a Hibernate validator.
     */
    protected static class HibernateValidatorCondition extends AbstractCondition {

        private Validator validator;

        public HibernateValidatorCondition(Validator validator) {
            this.validator = validator;
        }

        public boolean doCheck(Object object) {
            return validator.isValid(object);
        }

    }

}
