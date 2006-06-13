package org.springmodules.validation.bean.conf.namespace;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.ClassUtils;
import org.springmodules.validation.bean.BeanValidator;
import org.springmodules.validation.bean.conf.xml.DefaultXmlBeanValidationConfigurationLoader;
import org.springmodules.validation.bean.conf.BeanValidationConfiguration;
import org.w3c.dom.Element;

/**
 * @author Uri Boness
 */
public class ValidatorBeanDefinitionParser extends AbstractBeanDefinitionParser {

    private final static String LOADER_NAME = "__bean_validation_conf_loader";
    private final static String VALIDATOR_BASE_NAME = "__bean_validator_base_";
    private static int validatorNameCounter = 0;

    private static final String NAME_ATTR = "name";

    protected BeanDefinition parseInternal(Element element, ParserContext parserContext) {

        BeanDefinitionRegistry registry = parserContext.getRegistry();

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
        BeanValidationConfiguration configuration = loader.createClassDefinition(beanClass, element);


        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(BeanValidationConfigurationWithClass.class);
        builder.addPropertyValue("beanClass", className);
        builder.addPropertyValue("configuration", configuration);
        registry.registerBeanDefinition(generateValidatorName(), builder.getBeanDefinition());

        builder = BeanDefinitionBuilder.rootBeanDefinition(BeanValidator.class);
        builder.addPropertyReference("configurationLoader", LOADER_NAME);
        return builder.getBeanDefinition();
    }

    private static String generateValidatorName() {
        return VALIDATOR_BASE_NAME + (++validatorNameCounter);
    }

}
