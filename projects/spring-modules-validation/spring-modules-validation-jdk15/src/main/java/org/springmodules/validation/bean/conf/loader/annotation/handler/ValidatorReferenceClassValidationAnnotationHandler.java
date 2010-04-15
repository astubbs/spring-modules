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

package org.springmodules.validation.bean.conf.loader.annotation.handler;

import java.lang.annotation.Annotation;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.validation.Validator;
import org.springmodules.validation.bean.conf.MutableBeanValidationConfiguration;

/**
 * A {@link org.springmodules.validation.bean.conf.loader.annotation.handler.ClassValidationAnnotationHandler} that
 * handles {@link org.springmodules.validation.bean.conf.loader.annotation.handler.ValidatorRef} annotations.
 *
 * @author Uri Boness
 */
public class ValidatorReferenceClassValidationAnnotationHandler implements ClassValidationAnnotationHandler, ApplicationContextAware {

    private ApplicationContext applicationContext;

    /**
     * Returns <code>true</code> if the given annotation is of type <code>{@link org.springmodules.validation.bean.conf.loader.annotation.handler.ValidatorRef}</code>.
     *
     * @see org.springmodules.validation.bean.conf.loader.annotation.handler.ClassValidationAnnotationHandler#supports(java.lang.annotation.Annotation, Class)
     */
    public boolean supports(Annotation annotation, Class clazz) {
        return ValidatorRef.class.isInstance(annotation);
    }

    /**
     * Handles the given class level annotation and manipulates the given bean validation configuration appropriately.
     * This method assumes that {@link #supports(java.lang.annotation.Annotation, Class)} returns <code>true</code> for the
     * given annotation. In practice, the bean name will be extracted from the {@link @org.springmodules.validation.bean.conf.loader.annotation.handler.ValidatorRef(})
     * annotation and will be used to retrieve the validator bean from the application context. NOTE: obviously this
     * hanlder is only supported when the registry in which it is defined is deployed within spring application context.
     *
     * @param annotation The annotation to handle.
     * @param clazz The annotated class.
     * @param configuration The bean validation configuration to manipulate.
     */
    public void handleAnnotation(Annotation annotation, Class clazz, MutableBeanValidationConfiguration configuration) {
        ValidatorRef validator = (ValidatorRef) annotation;
        configuration.setCustomValidator(getSpringValidatorBean(validator.value()));
    }


    //====================================== Interface: ApplicationContextAware ========================================

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    //=============================================== Helper Methods ===================================================

    /**
     * Creates a new validator from the given validator class.
     *
     * @param beanName The name of the spring validator bean as deployed in the associated application context.
     * @return The spring validator bean as deployed by the given name in the associated application context.
     */
    protected org.springframework.validation.Validator getSpringValidatorBean(String beanName) {
        if (applicationContext == null) {
            throw new UnsupportedOperationException("This handler can only work when deployed within spring application context");
        }
        return (org.springframework.validation.Validator)applicationContext.getBean(beanName, Validator.class);
    }
}
