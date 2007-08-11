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

package org.springmodules.validation.bean.conf.loader.annotation;

import org.springmodules.validation.bean.conf.loader.annotation.handler.*;
import org.springmodules.validation.bean.conf.loader.annotation.handler.jpa.JpaBasicAnnotationHandler;
import org.springmodules.validation.bean.conf.loader.annotation.handler.jpa.JpaManyToOneAnnotationHandler;
import org.springmodules.validation.bean.conf.loader.annotation.handler.jpa.JpaOneToOneAnnotationHandler;
import org.springmodules.validation.bean.conf.loader.annotation.handler.jpa.JpaColumnAnnotationHandler;
import org.springmodules.validation.bean.conf.loader.annotation.handler.hibernate.HibernatePropertyValidationAnnotationHandler;
import org.springmodules.validation.bean.conf.loader.annotation.handler.jodatime.InstantInTheFutureValidationAnnotationHandler;
import org.springmodules.validation.bean.conf.loader.annotation.handler.jodatime.InstantInThePastValidationAnnotationHandler;
import org.springmodules.validation.util.LibraryUtils;

/**
 * The default validation annotation handler registry.
 * <p/>
 * <p/>
 * Class annotation handlers:
 * <ul>
 * <li>{@link ExpressionClassValidationAnnotationHandler}</li>
 * <li>{@link ExpressionsClassValidationAnnotationHandler}</li>
 * <li>{@link ValidatorClassValidationAnnotationHandler}</li>
 * <li>{@link ValidatorsClassValidationAnnotationHandler}</li>
 * <li>{@link ValidatorReferenceClassValidationAnnotationHandler}</li>
 * </ul>
 * <p/>
 *
 * Method annotation handlers:
 * <ul>
 * <li>{@link ValidationMethodAnnotationHandler}</li>
 * </ul>
 * <p/>
 *
 * Property annotation handlers:
 * <ul>
 * <li>{@link CascadeValidationAnnotationHandler}</li>
 * <li>{@link EmailValidationAnnotationHandler}</li>
 * <li>{@link DateInTheFutureValidationAnnotationHandler}</li>
 * <li>{@link DateInThePastValidationAnnotationHandler}</li>
 * <li>{@link LengthValidationAnnotationHandler}</li>
 * <li>{@link NotBlankValidationAnnotationHandler}</li>
 * <li>{@link NotEmptyValidationAnnotationHandler}</li>
 * <li>{@link NotNullValidationAnnotationHandler}</li>
 * <li>{@link RangeValidationAnnotationHandler}</li>
 * <li>{@link RegExpValidationAnnotationHandler}</li>
 * <li>{@link SizeValidationAnnotationHandler}</li>
 * <li>{@link ExpressionPropertyValidationAnnotationHandler}</li>
 * <li>{@link ExpressionsPropertyValidationAnnotationHandler}</li>
 * <li>{@link ConditionReferenceValidationAnnotationHandler}</li>
 * </ul>
 * <ul>
 * <li>{@link InstantInTheFutureValidationAnnotationHandler} (only if joda-time is in the classpath)</li>
 * <li>{@link InstantInThePastValidationAnnotationHandler} (only if joda-time is in the classpath)</li>
 * </ul>
 * <ul>
 * <li>{@link HibernatePropertyValidationAnnotationHandler} (only if hibernate-annotations is in the classpath)</li>
 * </ul>
 * <ul>
 * <li>{@link JpaBasicAnnotationHandler} (only if JPA is in the classpath)</li>
 * <li>{@link JpaColumnAnnotationHandler} (only if JPA is in the classpath)</li>
 * <li>{@link JpaOneToOneAnnotationHandler} (only if JPA is in the classpath)</li>
 * <li>{@link JpaManyToOneAnnotationHandler} (only if JPA is in the classpath)</li>
 * </ul>
 *
 * @author Uri Boness
 */
public class DefaultValidationAnnotationHandlerRegistry extends SimpleValidationAnnotationHandlerRegistry {

    /**
     * Constructs a new DefaultValidationAnnotationHandlerRegistry.
     */
    public DefaultValidationAnnotationHandlerRegistry() {

        // class annotation handlers
        registerClassHandler(new ExpressionClassValidationAnnotationHandler());
        registerClassHandler(new ExpressionsClassValidationAnnotationHandler());
        registerClassHandler(new ValidatorClassValidationAnnotationHandler());
        registerClassHandler(new ValidatorsClassValidationAnnotationHandler());
        registerClassHandler(new ValidatorReferenceClassValidationAnnotationHandler());

        // method annotation handlers
        registerMethodHandler(new ValidationMethodAnnotationHandler());

        // property annotation handlers
        registerPropertyHandler(new CascadeValidationAnnotationHandler());
        registerPropertyHandler(new EmailValidationAnnotationHandler());
        registerPropertyHandler(new DateInTheFutureValidationAnnotationHandler());
        registerPropertyHandler(new DateInThePastValidationAnnotationHandler());
        registerPropertyHandler(new LengthValidationAnnotationHandler());
        registerPropertyHandler(new NotBlankValidationAnnotationHandler());
        registerPropertyHandler(new NotEmptyValidationAnnotationHandler());
        registerPropertyHandler(new NotNullValidationAnnotationHandler());
        registerPropertyHandler(new RangeValidationAnnotationHandler());
        registerPropertyHandler(new RegExpValidationAnnotationHandler());
        registerPropertyHandler(new SizeValidationAnnotationHandler());
        registerPropertyHandler(new ExpressionPropertyValidationAnnotationHandler());
        registerPropertyHandler(new ExpressionsPropertyValidationAnnotationHandler());
        registerPropertyHandler(new ConditionReferenceValidationAnnotationHandler());

        if (LibraryUtils.JODA_TIME_IN_CLASSPATH) {
            registerPropertyHandler(new InstantInTheFutureValidationAnnotationHandler());
            registerPropertyHandler(new InstantInThePastValidationAnnotationHandler());
        }

        if (LibraryUtils.HIBERNATE_VALIDATOR_IN_CLASSPATH) {
            registerPropertyHandler(new HibernatePropertyValidationAnnotationHandler());
        }

        if (LibraryUtils.JPA_IN_CLASSPATH) {
            registerPropertyHandler(new JpaBasicAnnotationHandler());
            registerPropertyHandler(new JpaManyToOneAnnotationHandler());
            registerPropertyHandler(new JpaOneToOneAnnotationHandler());
            registerPropertyHandler(new JpaColumnAnnotationHandler());
        }
        
    }
}
