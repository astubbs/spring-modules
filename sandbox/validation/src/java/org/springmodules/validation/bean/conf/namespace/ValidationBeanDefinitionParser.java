package org.springmodules.validation.bean.conf.namespace;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.ClassUtils;
import org.springmodules.validation.bean.conf.BeanValidationConfiguration;
import org.springmodules.validation.bean.conf.xml.DefaultXmlBeanValidationConfigurationLoader;
import org.w3c.dom.Element;

/**
 * @author Uri Boness
 */
public class ValidationBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    private static final String CLASS_ATTR = "class";

    protected Class getBeanClass(Element element) {
        return BeanValidationConfigurationWithClass.class;
    }

    protected void doParse(Element element, BeanDefinitionBuilder builder) {

        String className = element.getAttribute(CLASS_ATTR);

        Class beanClass;
        try {
            beanClass = ClassUtils.forName(className);
        } catch (ClassNotFoundException cnfe) {
            throw new RuntimeException(cnfe);
        }

        DefaultXmlBeanValidationConfigurationLoader loader = new DefaultXmlBeanValidationConfigurationLoader();
        BeanValidationConfiguration configuration = loader.createClassDefinition(beanClass, element);

        builder.addPropertyValue("beanClass", className);
        builder.addPropertyValue("configuration", configuration);
    }

}
