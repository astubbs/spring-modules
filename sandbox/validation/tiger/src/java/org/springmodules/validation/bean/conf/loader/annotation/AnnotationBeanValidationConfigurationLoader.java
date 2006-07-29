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

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springmodules.validation.bean.conf.BeanValidationConfiguration;
import org.springmodules.validation.bean.conf.DefaultBeanValidationConfiguration;
import org.springmodules.validation.bean.conf.MutableBeanValidationConfiguration;
import org.springmodules.validation.bean.conf.loader.BeanValidationConfigurationLoader;
import org.springmodules.validation.bean.conf.loader.annotation.handler.ClassValidationAnnotationHandler;
import org.springmodules.validation.bean.conf.loader.annotation.handler.PropertyValidationAnnotationHandler;

/**
 * A {@link org.springmodules.validation.bean.conf.loader.BeanValidationConfigurationLoader} implementation that creates validation configuration based on
 * validation rule extracted from class annoatations.
 *
 * @author Uri Boness
 */
public class AnnotationBeanValidationConfigurationLoader implements BeanValidationConfigurationLoader {

    private final static Log logger = LogFactory.getLog(AnnotationBeanValidationConfigurationLoader.class);

    private boolean checkValidatableAnnotation;

    private Map<Class, BeanValidationConfiguration> configurationByClass;

    private ValidationAnnotationHandlerRegistry handlerRegistry;

    /**
     * Constructs a new AnnotationBeanValidationConfigurationLoader. This loader supports all classes by default.
     */
    public AnnotationBeanValidationConfigurationLoader() {
        this(false);
    }

    /**
     * Constructs a new AnnotationBeanValidationConfigurationLoader. if <code>checkValidatableAnnotation</code> is set
     * to <code>true</code>, only classes annotated with the {@link @Validatable} annoation are supported.
     *
     * @param checkValidatableAnnotation Indicates whether this loader should only support {@link @Validatable}
     *        annotated classes.
     */
    public AnnotationBeanValidationConfigurationLoader(boolean checkValidatableAnnotation) {
        this.checkValidatableAnnotation = checkValidatableAnnotation;
        configurationByClass = new HashMap<Class, BeanValidationConfiguration>();
        handlerRegistry = new DefaultValidationAnnotationHandlerRegistry();
    }

    /**
     * If {@link #isCheckValidatableAnnotation()} return <code>true</code>, then this loader supports the given
     * class only if it has the {@link @Validatable} annotation. Otherwise, all classes are supported.
     *
     * @see BeanValidationConfigurationLoader#supports(Class)
     */
    public boolean supports(Class clazz) {
        return (checkValidatableAnnotation) ? clazz.isAnnotationPresent(Validatable.class) : true;
    }


    /**
     * Loads the validation configuration for the given class based on validation annotations. The resolved configuration
     * are cached after the first time they are being loaded.
     *
     * @see BeanValidationConfigurationLoader#loadConfiguration(Class)
     */
    public BeanValidationConfiguration loadConfiguration(Class clazz) {
        BeanValidationConfiguration configuration = configurationByClass.get(clazz);
        if (configuration == null) {
            configuration = createValidationConfiguration(clazz);
            configurationByClass.put(clazz, configuration);
        }
        return configuration;
    }


    //=============================================== Setter/Getter ====================================================

    /**
     * Sets whether this loader should only support classes that are marked with the {@link @Validatable} annotation.
     *
     * @param checkValidatableAnnotation determine whether {@link @Validatable} should be considered.
     */
    public void setCheckValidatableAnnotation(boolean checkValidatableAnnotation) {
        this.checkValidatableAnnotation = checkValidatableAnnotation;
    }

    /**
     * Returns whether this loader only supports classes that are marked with the {@link @Validatable} annotation.
     *
     * @return <code>true</code> if the {@link @Validatable} is considered, <code>false</code> otherwise.
     */
    public boolean isCheckValidatableAnnotation() {
        return checkValidatableAnnotation;
    }

    /**
     * Sets the validation annotation handler registry this loader will use to find the proper handlers for the
     * validation annotations.
     *
     * @param handlerRegistry The validation annotation registry to be used by this loader.
     */
    public void setHandlerRegistry(ValidationAnnotationHandlerRegistry handlerRegistry) {
        this.handlerRegistry = handlerRegistry;
    }

    //=============================================== Helper Methods ===================================================

    /**
     * Creates a new {@link BeanValidationConfiguration} for the given class based on the validation annotations it
     * has.
     *
     * @param clazz The class to which the validation configuration should be created.
     * @return The newly created {@link BeanValidationConfiguration}.
     */
    protected BeanValidationConfiguration createValidationConfiguration(Class clazz) {
        MutableBeanValidationConfiguration configuration = new DefaultBeanValidationConfiguration();
        handleClassAnnotations(clazz, configuration);
        handlePropertyAnnotations(clazz, configuration);
        return configuration;
    }

    /**
     * Handles all the class level annotation of the given class and manipulates the given validation configuration
     * accordingly.
     *
     * @param clazz The annotated class.
     * @param configuration The bean validation configuration to manipulate.
     */
    protected void handleClassAnnotations(Class clazz, MutableBeanValidationConfiguration configuration) {
        Annotation[] annotations = clazz.getAnnotations();
        for (Annotation annotation : annotations) {
            ClassValidationAnnotationHandler handler = handlerRegistry.findClassHanlder(annotation, clazz);
            if (handler == null) {
                logger.warn("No hanlder is defined for annotation '" + annotation.annotationType().getName() +
                    "'... Annotation will be ignored...");
            } else {
                handler.handleAnnotation(annotation, clazz, configuration);
            }
        }
    }

    /**
     * Handles all the property level annotations of the given class and manipulates the given validation configuration
     * accordingly. The property level annotations can either be placed on the <code>setter</code> methods of the
     * properties or on the appropriate class fields.
     *
     * @param clazz The annotated class.
     * @param configuration The bean validation configuration to mainpulate.
     */
    protected void handlePropertyAnnotations(Class clazz, MutableBeanValidationConfiguration configuration) {
        // extracting & handling all property annotation placed on property fields
        List<Field> fields = extractFieldFromClassHierarchy(clazz);
        for (Field field : fields) {
            String fieldName = field.getName();
            PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(clazz, fieldName);
            if (descriptor != null) {
                Annotation[] annotations = field.getAnnotations();
                handleProprtyAnnotations(annotations, clazz, descriptor, configuration);
            }
        }

        // extracting & handling all property annotations placed on property setters
        PropertyDescriptor[] descriptors = BeanUtils.getPropertyDescriptors(clazz);
        for (PropertyDescriptor descriptor : descriptors) {
            Method writeMethod = descriptor.getWriteMethod();
            if (writeMethod == null) {
                continue;
            }
            Annotation[] annotations = writeMethod.getAnnotations();
            handleProprtyAnnotations(annotations, clazz, descriptor, configuration);
        }
    }

    /**
     * Extracts all fields in the class hierarchy of the given class.
     *
     * @param clazz The given class.
     * @return All fields in the class hierarchy of the given class.
     */
    protected List<Field> extractFieldFromClassHierarchy(Class clazz) {
        List<Field> fields = new ArrayList<Field>();
        while (clazz != null) {
            CollectionUtils.addAll(fields, clazz.getDeclaredFields());
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    /**
     * Handles all the validation annotations of the given property.
     *
     * @param annotations The annotation to handle.
     * @param validatedClass The annotated class.
     * @param descriptor The property descriptor of the annotated property.
     * @param configuration The bean validation configuration to manipulate.
     */
    protected void handleProprtyAnnotations(Annotation[] annotations, Class validatedClass, PropertyDescriptor descriptor, MutableBeanValidationConfiguration configuration) {
        for (Annotation annotation : annotations) {
            PropertyValidationAnnotationHandler handler = handlerRegistry.findPropertyHanlder(annotation, validatedClass, descriptor);
            if (handler != null) {
                handler.handleAnnotation(annotation, validatedClass, descriptor, configuration);
            }
        }
    }

}
