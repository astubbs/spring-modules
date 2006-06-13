package org.springmodules.validation.bean.conf.namespace;

import org.springmodules.validation.bean.conf.BeanValidationConfiguration;

/**
 * @author Uri Boness
 */
public class BeanValidationConfigurationWithClass {

    private Class beanClass;

    private BeanValidationConfiguration configuration;

    public BeanValidationConfigurationWithClass() {
    }

    public BeanValidationConfigurationWithClass(Class beanClass, BeanValidationConfiguration configuration) {
        this.beanClass = beanClass;
        this.configuration = configuration;
    }

    public Class getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class beanClass) {
        this.beanClass = beanClass;
    }

    public BeanValidationConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(BeanValidationConfiguration configuration) {
        this.configuration = configuration;
    }

}
