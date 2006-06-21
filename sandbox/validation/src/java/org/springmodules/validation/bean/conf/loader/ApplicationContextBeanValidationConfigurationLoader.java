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
package org.springmodules.validation.bean.conf.loader;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springmodules.validation.bean.conf.BeanValidationConfiguration;
import org.springmodules.validation.bean.conf.BeanValidationConfigurationLoader;

/**
 * A {@link BeanValidationConfigurationLoader} implementation that loads the bean validation configurations from
 * an application context. This implementation looks for {@link org.springmodules.validation.bean.conf.loader.BeanValidationConfigurationWithClass}'s
 * that are registered in the application context and determines what configuration to load based on these beans.
 *
 * @author Uri Boness
 */
public class ApplicationContextBeanValidationConfigurationLoader
    implements BeanValidationConfigurationLoader, ApplicationContextAware {

    private ApplicationContext applicationContext;

    // caches the configuration bean names by the associated class, this is to prevent iteration over all
    // BeanValidationConfigurationWithClass types on each request, instead once we accessed a bean once, next time we
    // can access it directly by the cached name.
    private Map beanNameByClass;

    public ApplicationContextBeanValidationConfigurationLoader() {
        beanNameByClass = new HashMap();
    }

    /**
     * Finds and returns {@link org.springmodules.validation.bean.conf.loader.BeanValidationConfigurationWithClass}
     * instace in the application context that is configured with the given class. If not found,
     * returns <code>null</code>.
     *
     * @see BeanValidationConfigurationLoader#loadConfiguration(Class)
     */
    public BeanValidationConfiguration loadConfiguration(Class clazz) {

        if (beanNameByClass.containsKey(clazz)) {
            BeanValidationConfigurationWithClass configurationWithClass =
                (BeanValidationConfigurationWithClass) applicationContext.getBean((String)beanNameByClass.get(clazz));
            return configurationWithClass.getConfiguration();
        }

        Map beanByNames = applicationContext.getBeansOfType(BeanValidationConfigurationWithClass.class);
        for (Iterator iter = beanByNames.keySet().iterator(); iter.hasNext();) {
            String name = (String)iter.next();
            BeanValidationConfigurationWithClass configurationWithClass =
                (BeanValidationConfigurationWithClass)beanByNames.get(name);
            if (configurationWithClass.getBeanClass().equals(clazz)) { // todo: what about sub-classes?
                beanNameByClass.put(clazz, name);
                return configurationWithClass.getConfiguration();
            }
        }
        return null;
    }

    /**
     * Searches for a {@link BeanValidationConfigurationWithClass} instace in the application context that is configured
     * with the given class. Returns <code>true</code> if one is found and <code>false</code> otherwise.
     *
     * @see BeanValidationConfigurationLoader#supports(Class)
     */
    public boolean supports(Class clazz) {
        if (beanNameByClass.containsKey(clazz)) {
            return true;
        }
        Map beanByNames = applicationContext.getBeansOfType(BeanValidationConfigurationWithClass.class);
        for (Iterator iter = beanByNames.keySet().iterator(); iter.hasNext();) {
            String name = (String)iter.next();
            BeanValidationConfigurationWithClass configurationWithClass =
                (BeanValidationConfigurationWithClass)beanByNames.get(name);
            if (configurationWithClass.getBeanClass().equals(clazz)) { // todo: what about sub-classes?
                beanNameByClass.put(clazz, name);
                return true;
            }
        }
        return false;
    }

    /**
     * @see ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
     */
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
