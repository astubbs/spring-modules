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

import org.springframework.beans.factory.*;
import org.springframework.beans.*;

/**
 * A generic FactoryBean implemenation that generates Templates based on configured TemplateFactory.
 *
 * @author Uri Boness
 */
public class TemplateFactoryBean extends AbstractTemplateFactoryBean implements BeanFactoryAware {

    // the template factory that will be used to generated templates.
    private String templateFactoryName;

    // the bean factory this bean is defined in.
    private BeanFactory beanFactory;

    /**
     * Empty contructor (JavaBean support)
     */
    public TemplateFactoryBean() {
    }

    /**
     * Constructs a new TemplateFactoryBean with a template factory name and the template source.
     *
     * @param templateFactoryName The name of the template factory definition in the bean factory.
     * @param templateSource The source of the create templates.
     */
    public TemplateFactoryBean(String templateFactoryName, TemplateSource templateSource) {
        super(templateSource);
        this.templateFactoryName = templateFactoryName;
    }

    /**
     * Constructs a new TemplateFactoryBean with a template factory, a list of template sources and the name of the
     * template source to use when creating the required templates.
     *
     * @param templateFactoryName The template factory name in the bean factory.
     * @param templateSources A list of template sources needed to create the required template.
     * @param templateName The name of the template source for the required template.
     */
    public TemplateFactoryBean(String templateFactoryName, TemplateSource[] templateSources, String templateName) {
        super(templateSources, templateName);
        this.templateFactoryName = templateFactoryName;
    }

    /**
     * Constructs a new TemplateFactoryBean with a template factory, a template source resolver and the name of the
     * template source to use when creating the required templates.
     *
     * @param templateFactoryName The template factory name in the bean factory.
     * @param templateSourceResolver The template source resolver to be used..
     * @param templateName The name of the template source for the required template.
     */
    public TemplateFactoryBean(
        String templateFactoryName,
        TemplateSourceResolver templateSourceResolver,
        String templateName) {

        super(templateSourceResolver, templateName);
        this.templateFactoryName = templateFactoryName;
    }

    /**
     * Sets the name of the template factory definition in the bean factory.
     *
     * @param templateFactoryName The {@link TemplateFactory} that will be used to create the templates.
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
     * @see org.springmodules.template.AbstractTemplateFactoryBean#createTemplateFactory()
     */
    protected TemplateFactory createTemplateFactory() {
        Object bean = beanFactory.getBean(templateFactoryName);
        if (!(bean instanceof TemplateFactory)) {
            throw new BeanInitializationException("bean named '" + templateFactoryName + "' is not of a '" +
                TemplateFactory.class.getName() + "' type");
        }
        return (TemplateFactory)bean;
    }

}
