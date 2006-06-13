package org.springmodules.validation.bean.conf.namespace;

import java.util.Map;
import java.util.Iterator;

import org.springmodules.validation.bean.conf.BeanValidationConfigurationLoader;
import org.springmodules.validation.bean.conf.BeanValidationConfiguration;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.BeansException;

/**
 * @author Uri Boness
 */
public class ApplicationContextBeanValidationConfigurationLoader implements BeanValidationConfigurationLoader, ApplicationContextAware {

    private ApplicationContext applicationContext;
    
    public BeanValidationConfiguration loadConfiguration(Class clazz) {
        Map beanByNames = applicationContext.getBeansOfType(BeanValidationConfigurationWithClass.class);
        for (Iterator iter = beanByNames.values().iterator(); iter.hasNext();) {
            BeanValidationConfigurationWithClass configurationWithClass = (BeanValidationConfigurationWithClass)iter.next();
            if (configurationWithClass.getBeanClass().equals(clazz)) {
                return configurationWithClass.getConfiguration();
            }
        }
        return null;
    }

    public boolean supports(Class clazz) {
        Map beanByNames = applicationContext.getBeansOfType(BeanValidationConfigurationWithClass.class);
        for (Iterator iter = beanByNames.values().iterator(); iter.hasNext();) {
            BeanValidationConfigurationWithClass configurationWithClass = (BeanValidationConfigurationWithClass)iter.next();
            if (configurationWithClass.getClass().equals(clazz)) {
                return true;
            }
        }
        return false;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
