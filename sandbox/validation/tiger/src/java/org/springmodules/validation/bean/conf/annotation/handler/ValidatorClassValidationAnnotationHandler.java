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

import java.lang.annotation.Annotation;

import org.springmodules.validation.bean.conf.MutableBeanValidationConfiguration;
import org.springmodules.validation.bean.conf.annotation.ClassValidationAnnotationHandler;

/**
 * A {@link ClassValidationAnnotationHandler} that handles {@link Validator} annotations.
 *
 * @author Uri Boness
 */
public class ValidatorClassValidationAnnotationHandler implements ClassValidationAnnotationHandler {

    /**
     * Returns <code>true</code> if the given annotation is of type <code>{@link Validator}</code>.
     *
     * @see ClassValidationAnnotationHandler#supports(java.lang.annotation.Annotation, Class)
     */
    public boolean supports(Annotation annotation, Class clazz) {
        return Validator.class.isInstance(annotation);
    }

    /**
     * Handles the given class level annotation and manipulates the given bean validation configuration appropriately.
     * This method assumes that {@link #supports(java.lang.annotation.Annotation, Class)} returns <code>true</code> for the
     * given annotation.
     *
     * @param annotation    The annotation to handle.
     * @param clazz         The annotated class.
     * @param configuration The bean validation configuration to manipulate.
     */
    public void handleAnnotation(Annotation annotation, Class clazz, MutableBeanValidationConfiguration configuration) {
        Validator validator = (Validator)annotation;
        configuration.setCustomValidator(createValidator(validator.value()));
    }


    //=============================================== Helper Methods ===================================================

    /**
     * Creates a new validator from the given validator class.     *
     */
    protected org.springframework.validation.Validator createValidator(Class<? extends org.springframework.validation.Validator> validatorClass) {
        try {
            return validatorClass.newInstance();
        } catch (IllegalAccessException iae) {
            throw new ValidationAnnotationConfigurationException("Could not instantiate validator class '" +
                validatorClass.getName() + "'", iae);
        } catch (InstantiationException ie) {
            throw new ValidationAnnotationConfigurationException("Could not instantiate validator class '" +
                validatorClass.getName() + "'", ie);
        }
    }
}
