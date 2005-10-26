/*
 * Copyright 2002-2005 the original author or authors.
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

package org.springmodules.template;

import org.springframework.beans.*;
import org.springframework.beans.factory.*;

/**
 * A FactoryBean implemenation that generates Templates using a configured template factory.
 * 
 * @author Uri Boness
 */
public class TemplateSetFactoryBean extends AbstractTemplateSetFactoryBean implements BeanFactoryAware {

    // the template factory to be used.
    private String templateFactoryName;

    // the bean factory where this factory bean is configured.
    private BeanFactory beanFactory;

    /**
     * Empty Constructor (JavaBean support).
     */
    public TemplateSetFactoryBean() {
    }

    /**
     * Constructs a new TemplateSetFactoryBean with a template factory and a list of template sources.
     *
     * @param templateFactoryName The name of the template factory in the bean factory.
     * @param templateSources A list of template sources from which the templates will be created.
     */
    public TemplateSetFactoryBean(String templateFactoryName, TemplateSource[] templateSources) {
        super(templateSources);
        this.templateFactoryName = templateFactoryName;
    }

    /**
     * Constructs a new TemplateSetFactoryBean with a template factory and a template source resolver.
     *
     * @param templateFactoryName The name of the template factory in the bean factory.
     * @param templateSourceResolver The template source resolver to be used.
     */
    public TemplateSetFactoryBean(String templateFactoryName, TemplateSourceResolver templateSourceResolver) {
        super(templateSourceResolver);
        this.templateFactoryName = templateFactoryName;
    }

    /**
     * Sets the name of the tmeplate factory definition in the bean factory.
     *
     * @param templateFactoryName The name of the tmeplate factory definition in the bean factory.
     */
    public void setTemplateFactoryName(String templateFactoryName) {
        this.templateFactoryName = templateFactoryName;
    }

    /**
     * @see BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory)
     */
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    /**
     * Retrieving the template factory from the containing BeanFactory using the configured "templateFactoryName".
     *
     * @see BeanFactory#getBean(String)
     * @see org.springmodules.template.AbstractTemplateSetFactoryBean#createTemplateFactory()
     */
    public TemplateFactory createTemplateFactory() throws Exception {
        Object bean = beanFactory.getBean(templateFactoryName);
        if (!(bean instanceof TemplateFactory)) {
            throw new BeanInitializationException("bean '" + templateFactoryName + "' is not of a '" +
                TemplateFactory.class.getName() + "' type.");
        }
        return (TemplateFactory)bean;
    }

}
