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
import org.springframework.core.io.*;
import org.springmodules.template.sources.*;

/**
 * A FactoryBean implemenation that generates TemplateSets.
 * 
 * @author Uri Boness
 */
public abstract class AbstractTemplateSetFactoryBean implements FactoryBean, InitializingBean {

    private TemplateFactory templateFactory;

    private TemplateSource[] templateSources;

    private TemplateSourceResolver templateSourceResolver;

    // indicates whether the same template should alwasy be returned, that is, whether this
    // bena factory is a singlton or not.
    private boolean reuseTemplateSet = true;

    // the template returned by this bean factory in case the 'reuseTemplate' is set to true.
    private TemplateSet cachedTemplateSet;

    public AbstractTemplateSetFactoryBean() {
    }

    /**
     * Constructs a new TemplateSetFactoryBean with a list of template sources.
     *
     * @param templateSources A list of template sources from which the templates will be created.
     */
    public AbstractTemplateSetFactoryBean(TemplateSource[] templateSources) {
        this.templateSources = templateSources;
    }

    /**
     * Constructs a new TemplateSetFactoryBean with a template source resolver.
     *
     * @param templateSourceResolver The template source resolver to be used.
     */
    public AbstractTemplateSetFactoryBean(TemplateSourceResolver templateSourceResolver) {
        this.templateSourceResolver = templateSourceResolver;
    }
    
    /**
     * Constructs and returns a new template.
     *
     * @return A new template.
     * @throws Exception
     * @see org.springframework.beans.factory.FactoryBean#getObject() 
     */
    public Object getObject() throws Exception {
        if (reuseTemplateSet) {
            return getSameTemplateSet();
        }
        return createNewTemplateSet();
    }

    /**
     * @see org.springframework.beans.factory.FactoryBean#getObjectType() 
     */
    public Class getObjectType() {
        return (cachedTemplateSet != null)? cachedTemplateSet.getClass() : TemplateSet.class;
    }

    /**
     * By default this bean factory is a singleton factory. This can be changed by setting the 'reuseTemplate'
     * property to 'false'.
     *
     * @return whether the same template is always returns by the {@link #getObject()} method.
     */
    public boolean isSingleton() {
        return reuseTemplateSet;
    }

    /**
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        templateFactory = createTemplateFactory();
        if (templateFactory == null) {
            throw new BeanInitializationException("'templateFactory' property must be set");
        }

        if (templateSources == null && templateSourceResolver == null) {
            throw new BeanInitializationException("Either 'templateSources', or " +
                "'templateSourceResolver' property must be set");
        }

        if (reuseTemplateSet) {
            cachedTemplateSet = createNewTemplateSet();
        }
    }

    /**
     * Creates a new TemplateFactory that will be used to create the TemplateSets.
     * This method is called when the bean factory is initialized (i.e within the <code>afterPropertiesSet</code> method).
     *
     * @return The newly created template factory.
     */
    protected abstract TemplateFactory createTemplateFactory() throws Exception;

    /**
     * Sets the template sources needed to create the required template.
     *
     * @param templateSources The template source needed to create the required template.
     */
    public void setTemplateSources(TemplateSource[] templateSources) {
        this.templateSources = templateSources;
    }

    /**
     * Sets the resources that serve as the template sources for the required templates.
     *
     * @see #setTemplateSources(TemplateSource[])
     * @param templateResources The resources that serve as the template sources for the required templates.
     */
    public void setTemplateResources(Resource[] templateResources) {
        TemplateSource[] templateSources = new TemplateSource[templateResources.length];
        for (int i=0; i<templateResources.length; i++) {
            templateSources[i] = new ResourceTemplateSource(templateResources[i]);
        }
        this.templateSources = templateSources;
    }

    /**
     * Sets the template source resolver that will be used to resolve the template sources based on the template names.
     * 
     * @param templateSourceResolver The template source resolver to be used.
     */
    public void setTemplateSourceResolver(TemplateSourceResolver templateSourceResolver) {
        this.templateSourceResolver = templateSourceResolver;
    }

    /**
     * Sets whether the same template should be reused whenever {@link #getObject()}  is caled (singlton model).
     * 
     * @param reuseTemplateSet If <code>true</code>, the same template set will always be returned by {@link #getObject()}.
     */
    public void setReuseTemplateSet(boolean reuseTemplateSet) {
        this.reuseTemplateSet = reuseTemplateSet;
    }


    //============================================== Helper Methods ====================================================

    protected TemplateSet getSameTemplateSet() throws Exception {
        if (cachedTemplateSet == null) {
            cachedTemplateSet = createNewTemplateSet();
        }
        return cachedTemplateSet;
    }

    protected TemplateSet createNewTemplateSet() throws Exception {
        if (templateSources != null) {
           return templateFactory.createTemplateSet(templateSources);
        }
        return templateFactory.createTemplateSet(templateSourceResolver);
    }

}
