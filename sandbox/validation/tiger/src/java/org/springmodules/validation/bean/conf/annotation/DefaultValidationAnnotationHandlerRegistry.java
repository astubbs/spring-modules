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

package org.springmodules.validation.bean.conf.annotation;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.beans.PropertyDescriptor;

import org.springmodules.validation.bean.conf.annotation.handler.EmailValidationAnnotationHandler;
import org.springmodules.validation.bean.conf.annotation.handler.Email;
import org.springmodules.validation.bean.conf.annotation.handler.DateInTheFutureValidationAnnotationHandler;
import org.springmodules.validation.bean.conf.annotation.handler.DateInThePastValidationAnnotationHandler;
import org.springmodules.validation.bean.conf.annotation.handler.LengthValidationAnnotationHandler;
import org.springmodules.validation.bean.conf.annotation.handler.NotBlankValidationAnnotationHandler;
import org.springmodules.validation.bean.conf.annotation.handler.NotEmptyValidationAnnotationHandler;
import org.springmodules.validation.bean.conf.annotation.handler.NotNullValidationAnnotationHandler;
import org.springmodules.validation.bean.conf.annotation.handler.RangeValidationAnnotationHandler;
import org.springmodules.validation.bean.conf.annotation.handler.RegExpValidationAnnotationHandler;
import org.springmodules.validation.bean.conf.annotation.handler.SizeValidationAnnotationHandler;
import org.springmodules.validation.bean.conf.annotation.handler.ValangPropertyValidationAnnotationHandler;
import org.springmodules.validation.bean.conf.annotation.handler.ValangClassValidationAnnotationHandler;
import org.springmodules.validation.bean.conf.annotation.handler.ValidatorClassValidationAnnotationHandler;
import org.springmodules.validation.bean.conf.annotation.handler.ValidatorsClassValidationAnnotationHandler;
import org.springmodules.validation.bean.conf.annotation.handler.ValidValidationAnnotationHandler;
import org.springmodules.validation.bean.conf.annotation.handler.hibernate.HibernatePropertyValidationAnnotationHandler;
import org.springmodules.validation.bean.conf.annotation.handler.jodatime.InstantInTheFutureValidationAnnotationHandler;
import org.springmodules.validation.bean.conf.annotation.handler.jodatime.InstantInThePastValidationAnnotationHandler;
import org.springmodules.validation.util.LibraryUtils;
import org.springmodules.validation.util.BasicContextAware;
import org.springframework.beans.factory.InitializingBean;

/**
 * The default validation annotation handler registry. This registry come with the following pre-registered handlers:
 * <ul>
 *  <li>{@link EmailValidationAnnotationHandler} - handlers {@link Email} annotations</li>
 * </ul>
 *
 * @author Uri Boness
 */
public class DefaultValidationAnnotationHandlerRegistry extends BasicContextAware
    implements ValidationAnnotationHandlerRegistry, InitializingBean {

    private List<ClassValidationAnnotationHandler> classHandlers;
    private List<PropertyValidationAnnotationHandler> propertyHandlers;

    /**
     * Constructs a new DefaultValidationAnnotationHandlerRegistry.
     */
    public DefaultValidationAnnotationHandlerRegistry() {
        classHandlers = new ArrayList<ClassValidationAnnotationHandler>();
        propertyHandlers = new ArrayList<PropertyValidationAnnotationHandler>();
        registerDefaultHandlers();
    }

    /**
     * @see ValidationAnnotationHandlerRegistry#findClassHanlder(java.lang.annotation.Annotation, Class)
     */
    public ClassValidationAnnotationHandler findClassHanlder(Annotation annotation, Class clazz) {
        for (ClassValidationAnnotationHandler handler : classHandlers) {
            if (handler.supports(annotation, clazz)) {
                return handler;
            }
        }
        return null;
    }

    /**
     * @see ValidationAnnotationHandlerRegistry#findPropertyHanlder(java.lang.annotation.Annotation, Class, java.beans.PropertyDescriptor)
     */
    public PropertyValidationAnnotationHandler findPropertyHanlder(Annotation annotation, Class clazz, PropertyDescriptor descriptor) {
        for (PropertyValidationAnnotationHandler handler : propertyHandlers) {
            if (handler.supports(annotation, clazz, descriptor)) {
                return handler;
            }
        }
        return null;
    }

    //=============================================== Setter/Getter ====================================================

    /**
     * Registers the given class validation annotation handler with this registry. The newly added handler will have
     * precedence over the already registered handlers, that is, for every annotation, this handler will be checked
     * for support before the already registered handlers.
     *
     * @param handler The class validation annoation handler to be registered.
     */
    public void registerClassHandler(ClassValidationAnnotationHandler handler) {
        classHandlers.add(0, handler);
    }

    /**
     * Registers the given class validation annoation handlers to the registry. These handlers will have precedence
     * over the already registered handlers, that is, for every annotation, these handlers will be check for suppport
     * before the already registered ones. The order of the given handler list is important for it determines the
     * precedence of the handlers within this list.
     *
     * @param handlers The extra class validation annotation handlers to register with this registry.
     */
    public void setExtraClassHandlers(List<ClassValidationAnnotationHandler> handlers) {
        Collections.reverse(handlers);
        for (ClassValidationAnnotationHandler handler : handlers) {
            registerClassHandler(handler);
        }
    }

    /**
     * Registers the given property validation annoation handler with this registry. The newly added handler will have
     * precedence over the already registered handlers, that is, for every annotation, this handler will be checked
     * for support before the already registered handlers.
     *
     * @param handler The property validation annoation handler to be registered.
     */
    public void registerPropertyHandler(PropertyValidationAnnotationHandler handler) {
        propertyHandlers.add(0, handler);
    }

    /**
     * Registers the given property validation annoation handlers to the registry. These handlers will have precedence
     * over the already registered handlers, that is, for every annotation, these handlers will be check for suppport
     * before the already registered ones. The order of the given handler list is important for it determines the
     * precedence of the handlers within this list.
     *
     * @param handlers The extra property validation annotation handlers to register with this registry.
     */
    public void setExtraPropertyHandlers(List<PropertyValidationAnnotationHandler> handlers) {
        Collections.reverse(handlers);
        for (PropertyValidationAnnotationHandler handler : handlers) {
            registerPropertyHandler(handler);
        }
    }

    public void afterPropertiesSet() throws Exception {
        for (ClassValidationAnnotationHandler handler : classHandlers) {
            initLifecycle(handler);
        }
        for (PropertyValidationAnnotationHandler handler : propertyHandlers) {
            initLifecycle(handler);
        }
    }

    //=============================================== Helper Methods ===================================================

    /**
     * Registers the default (pre-registered) validation annotation handlers with this registry.
     */
    protected void registerDefaultHandlers() {

        // class annotation handlers
        classHandlers.add(new ValangClassValidationAnnotationHandler());
        classHandlers.add(new ValidatorClassValidationAnnotationHandler());
        classHandlers.add(new ValidatorsClassValidationAnnotationHandler());

        // property annotation handlers
        propertyHandlers.add(new ValidValidationAnnotationHandler());
        propertyHandlers.add(new EmailValidationAnnotationHandler());
        propertyHandlers.add(new DateInTheFutureValidationAnnotationHandler());
        propertyHandlers.add(new DateInThePastValidationAnnotationHandler());
        if (LibraryUtils.JODA_TIME_IN_CLASSPATH) {
            propertyHandlers.add(new InstantInTheFutureValidationAnnotationHandler());
            propertyHandlers.add(new InstantInThePastValidationAnnotationHandler());
        }
        propertyHandlers.add(new LengthValidationAnnotationHandler());
        propertyHandlers.add(new NotBlankValidationAnnotationHandler());
        propertyHandlers.add(new NotEmptyValidationAnnotationHandler());
        propertyHandlers.add(new NotNullValidationAnnotationHandler());
        propertyHandlers.add(new RangeValidationAnnotationHandler());
        propertyHandlers.add(new RegExpValidationAnnotationHandler());
        propertyHandlers.add(new SizeValidationAnnotationHandler());
        propertyHandlers.add(new ValangPropertyValidationAnnotationHandler());
        if (LibraryUtils.HIBERNATE_VALIDATOR_IN_CLASSPATH) {
            propertyHandlers.add(new HibernatePropertyValidationAnnotationHandler());
        }
    }

}
