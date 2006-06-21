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

import org.springmodules.validation.bean.conf.BeanValidationConfiguration;

/**
 * Associates a {@link BeanValidationConfiguration} with a specific class.
 *
 * @author Uri Boness
 */
public class BeanValidationConfigurationWithClass {

    private Class beanClass;

    private BeanValidationConfiguration configuration;

    /**
     * Creates an empty BeanValidationConfigurationWithClass (javabean support).
     */
    public BeanValidationConfigurationWithClass() {
    }

    /**
     * Creates a new BeanValidationConfigurationWithClass with a given class and {@link BeanValidationConfiguration}.
     *
     * @param beanClass The given class.
     * @param configuration The given {@link BeanValidationConfiguration}.
     */
    public BeanValidationConfigurationWithClass(Class beanClass, BeanValidationConfiguration configuration) {
        this.beanClass = beanClass;
        this.configuration = configuration;
    }

    /**
     * Returns the class that is associated with the configured bean validation configuration.
     *
     * @return The class that is associated with the configured bean validation configuration.
     */
    public Class getBeanClass() {
        return beanClass;
    }

    /**
     * Sets the class to be associated with the configured bean validation configuration.
     *
     * @param beanClass The class to be associated with the configured bean validation configuration.
     */
    public void setBeanClass(Class beanClass) {
        this.beanClass = beanClass;
    }

    /**
     * Returns the bean validation configuration that is associated with the configured class.
     *
     * @return The bean validation configuration that is associated with the configured class.
     */
    public BeanValidationConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * Sets the bean validation configuration to be associated with the configured class.
     *
     * @param configuration The bean validation configuration to be associated with the configured class.
     */
    public void setConfiguration(BeanValidationConfiguration configuration) {
        this.configuration = configuration;
    }

}
