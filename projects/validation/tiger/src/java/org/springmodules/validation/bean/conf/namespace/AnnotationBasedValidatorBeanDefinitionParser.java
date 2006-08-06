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

package org.springmodules.validation.bean.conf.namespace;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.ClassUtils;
import org.springmodules.validation.bean.BeanValidator;
import org.springmodules.validation.bean.conf.ValidationConfigurationException;
import org.springmodules.validation.bean.conf.loader.annotation.AnnotationBeanValidationConfigurationLoader;
import org.springmodules.validation.bean.conf.loader.annotation.DefaultValidationAnnotationHandlerRegistry;
import org.springmodules.validation.bean.conf.loader.annotation.handler.ClassValidationAnnotationHandler;
import org.springmodules.validation.bean.conf.loader.annotation.handler.PropertyValidationAnnotationHandler;
import org.springmodules.validation.util.xml.DomUtils;
import org.springmodules.validation.util.xml.SubElementsIterator;
import org.w3c.dom.Element;

/**
 *
 * @author Uri Boness
 */
public class AnnotationBasedValidatorBeanDefinitionParser extends AbstractBeanDefinitionParser implements ValidationBeansParserConstants {

    final static String ELEMENT_NAME = "annotation-based-validator";

    private final static String ERROR_CODE_CONVERTER_ATTR = "errorConverter";
    private final static String CLASS_ATTR = "class";

    private final static String ANNOTATION_HANDLERS_ELEMENT = "annotation-handlers";
    private final static String HANDLER_ELEMENT = "handler";

    private static final String HANDLER_REGISTRY_PREFIX = "__handler_registry_";
    private static final String CONFIGURATION_LOADER_PREFIX = "__configuration_loader_";

    protected BeanDefinition parseInternal(Element element, ParserContext parserContext) {

        String validatorId = extractId(element);

        String registryId = HANDLER_REGISTRY_PREFIX + validatorId;
        BeanDefinitionBuilder registryBuilder = BeanDefinitionBuilder.rootBeanDefinition(DefaultValidationAnnotationHandlerRegistry.class);
        parseHandlerElements(element, registryBuilder);
        parserContext.getRegistry().registerBeanDefinition(registryId, registryBuilder.getBeanDefinition());

        String loaderId = CONFIGURATION_LOADER_PREFIX + validatorId;
        BeanDefinitionBuilder loaderBuilder = BeanDefinitionBuilder.rootBeanDefinition(AnnotationBeanValidationConfigurationLoader.class);
        loaderBuilder.addPropertyReference("handlerRegistry", registryId);
        parserContext.getRegistry().registerBeanDefinition(loaderId, loaderBuilder.getBeanDefinition());

        BeanDefinitionBuilder validatorBuilder = BeanDefinitionBuilder.rootBeanDefinition(BeanValidator.class);
        if (element.hasAttribute(AnnotationBasedValidatorBeanDefinitionParser.ERROR_CODE_CONVERTER_ATTR)) {
            validatorBuilder.addPropertyReference("errorCodeConverter", element.getAttribute(AnnotationBasedValidatorBeanDefinitionParser.ERROR_CODE_CONVERTER_ATTR));
        }
        validatorBuilder.addPropertyReference("configurationLoader", loaderId);

        return validatorBuilder.getBeanDefinition();
    }

    /**
     * Returns the {@link org.springmodules.validation.bean.BeanValidator} class.
     *
     * @see org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser#doParse(org.w3c.dom.Element, org.springframework.beans.factory.support.BeanDefinitionBuilder)
     */
    protected Class getBeanClass(Element element) {
        return BeanValidator.class;
    }

    //=============================================== Helper Methods ===================================================

    protected void parseHandlerElements(Element element, BeanDefinitionBuilder registryBuilder) {
        Element functionsElement = DomUtils.getSingleSubElement(element, VALIDATION_BEANS_NAMESPACE, ANNOTATION_HANDLERS_ELEMENT);
        if (functionsElement == null) {
            return;
        }

        Iterator handlerElements = new SubElementsIterator(functionsElement, VALIDATION_BEANS_NAMESPACE, HANDLER_ELEMENT);
        List propertyHandlers = new ArrayList();
        List classHandlers = new ArrayList();
        while(handlerElements.hasNext()) {
            Element handlerElement = (Element)handlerElements.next();
            String className = handlerElement.getAttribute(AnnotationBasedValidatorBeanDefinitionParser.CLASS_ATTR);
            Object handler = loadAndInstantiate(className);
            if (PropertyValidationAnnotationHandler.class.isInstance(handler)) {
                propertyHandlers.add(handler);
            } else if (ClassValidationAnnotationHandler.class.isInstance(handler)) {
                classHandlers.add(handler);
            } else {
                throw new ValidationConfigurationException("class '" + className + "' is not a property hanlder nor a class handler");
            }
        }
        registryBuilder.addPropertyValue(
            "extraPropertyHandlers",
            propertyHandlers.toArray(new PropertyValidationAnnotationHandler[propertyHandlers.size()])
        );
        registryBuilder.addPropertyValue(
            "extraClassHandlers",
            classHandlers.toArray(new ClassValidationAnnotationHandler[classHandlers.size()])
        );
    }


    //=============================================== Helper Methods ===================================================

    /**
     * Loads and instantiates the given class.
     *
     * @param className The name of the given class.
     */
    protected Object loadAndInstantiate(String className) {
        Class clazz;
        try {
            clazz = ClassUtils.forName(className);
            return clazz.newInstance();
        } catch (ClassNotFoundException cnfe) {
            throw new ValidationConfigurationException("Could not load class '" + className + "'", cnfe);
        } catch (IllegalAccessException iae) {
            throw new ValidationConfigurationException("Could not instantiate class '" + className + "'", iae);
        } catch (InstantiationException ie) {
            throw new ValidationConfigurationException("Could not instantiate class '" + className + "'", ie);
        }
    }
}
