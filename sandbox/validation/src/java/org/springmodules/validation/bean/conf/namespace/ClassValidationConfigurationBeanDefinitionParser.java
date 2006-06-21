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

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.ClassUtils;
import org.springmodules.validation.bean.BeanValidator;
import org.springmodules.validation.bean.conf.BeanValidationConfiguration;
import org.springmodules.validation.bean.conf.loader.ApplicationContextBeanValidationConfigurationLoader;
import org.springmodules.validation.bean.conf.loader.BeanValidationConfigurationWithClass;
import org.springmodules.validation.bean.conf.xml.DefaultXmlBeanValidationConfigurationLoader;
import org.w3c.dom.Element;

/**
 * A {@link org.springframework.beans.factory.xml.BeanDefinitionParser} that parses <code>class</code> elements as
 * described by the <code>org/springmodules/validation/bean/conf/xml/validation.xsd</code>. If an the <code>class</code>
 * element has an <code>id</code> property, then a new BeanValidator is created that can be identified by that <code>id</code>.
 * If the <code>id</code> attribute is not configured, the class definition can be used along with the
 * {@link ValidatorBeanDefinitionParser}.
 *
 * @author Uri Boness
 */
public class ClassValidationConfigurationBeanDefinitionParser extends AbstractBeanDefinitionParser {

    // the id of the {@link ApplicationContextBeanValidationConfigurationLoader} that is created to access the parsed
    // class validation configuration.
    final static String LOADER_NAME = "__bean_validation_conf_loader";

    private final static String CLASS_VALIDATION_CONFIGURATION_BASE_NAME = "__class_validation_configuration_base_name_";

    private static int classValidationConfigurationNameCounter = 0;

    private static final String NAME_ATTR = "name";

    /**
     * Creates the bean definition for the {@link BeanValidationConfigurationWithClass} that is created from the parsed
     * element. Also registered a new {@link org.springmodules.validation.bean.conf.loader.ApplicationContextBeanValidationConfigurationLoader} definition if none
     * already exist.
     *
     * @see AbstractBeanDefinitionParser#parseInternal(org.w3c.dom.Element, org.springframework.beans.factory.xml.ParserContext)
     */
    protected BeanDefinition parseInternal(Element element, ParserContext parserContext) {

        BeanDefinitionRegistry registry = parserContext.getRegistry();

        // if there is no loader in the app. context, then define one.
        if (!parserContext.getRegistry().containsBeanDefinition(LOADER_NAME)) {
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(ApplicationContextBeanValidationConfigurationLoader.class);
            registry.registerBeanDefinition(LOADER_NAME, builder.getBeanDefinition());
        }

        String className = element.getAttribute(NAME_ATTR);

        Class beanClass;
        try {
            beanClass = ClassUtils.forName(className);
        } catch (ClassNotFoundException cnfe) {
            throw new RuntimeException(cnfe);
        }

        DefaultXmlBeanValidationConfigurationLoader loader = new DefaultXmlBeanValidationConfigurationLoader();
        BeanValidationConfiguration configuration = loader.handleClassDefinition(beanClass, element);


        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(BeanValidationConfigurationWithClass.class);
        builder.addPropertyValue("beanClass", className);
        builder.addPropertyValue("configuration", configuration);

        // if the element has no 'id', then the configuration is the bean this handler will define.
        if (!element.hasAttribute(ID_ATTRIBUTE)) {
            return builder.getBeanDefinition();
        }

        // if the element has an 'id', then the configuration is defined, but this handler will actually also define
        // a bean validator with that 'id'.

        // registering the configuration
        registry.registerBeanDefinition(generateClassValidationConfigurationBeanName(), builder.getBeanDefinition());

        builder = BeanDefinitionBuilder.rootBeanDefinition(BeanValidator.class);
        builder.addPropertyReference("configurationLoader", LOADER_NAME);
        return builder.getBeanDefinition();
    }

    /**
     * If no <code>id</code> attribute is defined, a new unique id is generated for the class validation configuration.
     *
     * @see AbstractBeanDefinitionParser#extractId(org.w3c.dom.Element)
     */
    protected String extractId(Element element) {
        if (!element.hasAttribute(ID_ATTRIBUTE)) {
            return generateClassValidationConfigurationBeanName();
        }
        return super.extractId(element);
    }


    //=============================================== Helper Methods ===================================================

    // Generates a unique id for the the class validation configuration.\
    private static String generateClassValidationConfigurationBeanName() {
        return CLASS_VALIDATION_CONFIGURATION_BASE_NAME + (++classValidationConfigurationNameCounter);
    }

}
