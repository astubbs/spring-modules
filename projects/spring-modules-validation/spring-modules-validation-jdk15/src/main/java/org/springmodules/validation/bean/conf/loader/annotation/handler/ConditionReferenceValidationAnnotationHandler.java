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

import org.springmodules.validation.bean.rule.AbstractValidationRule;
import org.springmodules.validation.bean.rule.NotNullValidationRule;
import org.springmodules.validation.bean.rule.DefaultValidationRule;
import org.springmodules.validation.bean.rule.ConditionReferenceValidationRule;
import org.springmodules.validation.util.condition.Condition;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.BeansException;

/**
 * An {@link org.springmodules.validation.bean.conf.loader.annotation.handler.AbstractPropertyValidationAnnotationHandler}
 * implementation that handles {@link @ConditionRef} annotations for which it creates an appropriate validation rule.
 *
 * @author Uri Boness
 */
public class ConditionReferenceValidationAnnotationHandler extends AbstractPropertyValidationAnnotationHandler
    implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    protected boolean isNullSupported() {
        return true;
    }

    /**
     * Constructs a new ConditionReferenceValidationAnnotationHandler.
     */
    public ConditionReferenceValidationAnnotationHandler() {
        super(ConditionRef.class);
    }

    protected AbstractValidationRule createValidationRule(Annotation annotation, Class clazz, String propertyName) {
        if (applicationContext == null) {
            throw new UnsupportedOperationException("This handler is only supported when deployed within a " +
                "spring application context");
        }
        ConditionRef conditionRef = (ConditionRef)annotation;
        return new ConditionReferenceValidationRule(conditionRef.value(), applicationContext);
    }


    //================================== Interface: ApplicationContextAware ============================================

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
