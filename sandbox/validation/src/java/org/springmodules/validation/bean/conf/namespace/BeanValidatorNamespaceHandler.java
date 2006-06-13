package org.springmodules.validation.bean.conf.namespace;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @author Uri Boness
 */
public class BeanValidatorNamespaceHandler extends NamespaceHandlerSupport {

    public void init() {
        registerBeanDefinitionParser("class", new ValidatorBeanDefinitionParser());
    }

}
