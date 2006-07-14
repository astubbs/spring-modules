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

package org.springmodules.validation.bean.conf.xml.handler;

import org.springframework.util.ClassUtils;
import org.springframework.validation.Validator;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.BeansException;
import org.springmodules.validation.bean.conf.MutableBeanValidationConfiguration;
import org.springmodules.validation.bean.conf.xml.ClassValidationElementHandler;
import org.springmodules.validation.bean.conf.xml.DefaultXmBeanValidationConfigurationlLoaderConstants;
import org.w3c.dom.Element;

/**
 *
 *
 * @author Uri Boness
 */
public class ValidatorValidationElementHandler implements ClassValidationElementHandler,
    DefaultXmBeanValidationConfigurationlLoaderConstants, ApplicationContextAware {

    private final static String ELEMENT_NAME = "validator";
    private final static String CLASS_ATTR = "class";

    private ApplicationContext applicationContext;

    /**
     * Determines whether this handler can handle the given element.
     *
     * @param element The element to be handled.
     * @return <code>true</code> if this handler can handle the given element, <code>false</code> otherwise.
     */
    public boolean supports(Element element, Class clazz) {
        return ELEMENT_NAME.equals(element.getLocalName()) && DEFAULT_NAMESPACE_URL.equals(element.getNamespaceURI());
    }

    /**
     * Handles the given element and and manipulates the given configuration appropriately.
     *
     * @param element       The element to be handled.
     * @param configuration The configuration to be manipulated.
     */
    public void handle(Element element, MutableBeanValidationConfiguration configuration) {
        if (element.hasAttribute(CLASS_ATTR)) {
            throw new XmlConditionConfigurationException("Attribute '" + CLASS_ATTR + "' is missing in the '" +
                ELEMENT_NAME + "' element");
        }
        String className = element.getAttribute(CLASS_ATTR);
        Class validatorClass = loadClass(className);
        Validator validator = createValidator(validatorClass);
        configuration.addCustomValidator(validator);
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    //=============================================== Helper Methods ===================================================

    /**
     * Loads the given validator class
     */
    protected Class loadClass(String className) {
        Class validatorClass;
        try {
            validatorClass = ClassUtils.forName(className);
        } catch (ClassNotFoundException cnfe) {
            throw new XmlConditionConfigurationException("Could not load class '" + className + "'", cnfe);
        }

        if (!Validator.class.isAssignableFrom(validatorClass)) {
            throw new XmlConditionConfigurationException("Class '" + className + "' is not of " +
                Validator.class.getName() + "' type");
        }
        return validatorClass;
    }

    /**
     * Loads the validator instance based on the given validator class.
     */
    protected Validator loadValidator(Class validatorClass) {
        Validator validator = loadValidatorFromApplicationContext(validatorClass);
        return (validator != null) ? validator : createValidator(validatorClass);
    }

    /**
     * Creates a new validator from the given validator class.
     */
    protected Validator createValidator(Class validatorClass) {
        try {
            return (Validator)validatorClass.newInstance();
        } catch (IllegalAccessException iae) {
            throw new XmlConditionConfigurationException("Could not instantiate validator class '" +
                validatorClass.getName() + "'", iae);
        } catch (InstantiationException ie) {
            throw new XmlConditionConfigurationException("Could not instantiate validator class '" +
                validatorClass.getName() + "'", ie);
        }
    }

    /**
     * Loads the validator instance of the given type from the application context. If the application context is not
     * set or no such validator is found, <code>null</code> is returned. If there's more then one bean defined for
     * the given validation class, there is no guarantee of which instance will be returned.
     */
    protected Validator loadValidatorFromApplicationContext(Class validatorClass) {
        if (applicationContext == null) {
            return null;
        }
        String[] names = applicationContext.getBeanNamesForType(validatorClass);
        if (names.length == 0) {
            return null;
        }
        return (Validator)applicationContext.getBean(names[0]);
    }

}
