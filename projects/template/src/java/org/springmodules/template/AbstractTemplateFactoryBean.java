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

import java.util.*;

import org.springframework.beans.factory.*;
import org.springframework.core.io.*;
import org.springmodules.template.sources.*;

/**
 * An abstract FactoryBean implemenation that generates Templates.
 *
 * @author Uri Boness
 */
public abstract class AbstractTemplateFactoryBean implements FactoryBean, InitializingBean {

    private TemplateFactory templateFactory;

    // either this template source is used for the template creation
    private TemplateSource templateSource;

    // or this combination of template sources or template source resolver with a template name.
    private TemplateSource[] templateSources;
    private TemplateSourceResolver templateSourceResolver;
    private String templateName;

    // indicates whether the same template should alwasy be returned, that is, whether this
    // bena factory is a singlton or not.
    private boolean reuseTemplate = true;

    // the template returned by this bean factory in case the 'reuseTemplate' is set to true.
    private Template cachedTemplate;

    public AbstractTemplateFactoryBean() {
    }

    /**
     * Constructs a new AbstractTemplateFactoryBean with a template source.
     *
     * @param templateSource The source of the create templates.
     */
    public AbstractTemplateFactoryBean(TemplateSource templateSource) {
        this.templateSource = templateSource;
    }

    /**
     * Constructs a new AbstractTemplateFactoryBean with a list of template sources and the name of the
     * template source to use when creating the required templates.
     *
     * @param templateSources A list of template sources needed to create the required template.
     * @param templateName The name of the template source for the required template.
     */
    public AbstractTemplateFactoryBean(TemplateSource[] templateSources, String templateName) {
        this.templateSources = templateSources;
        this.templateName = templateName;
    }

    /**
     * Constructs a new AbstractTemplateFactoryBean with a template source resolver and the name of the
     * template source to use when creating the required templates.
     *
     * @param templateSourceResolver The template source resolver to be used..
     * @param templateName The name of the template source for the required template.
     */
    public AbstractTemplateFactoryBean(TemplateSourceResolver templateSourceResolver, String templateName) {
        this.templateSourceResolver = templateSourceResolver;
        this.templateName = templateName;
    }

    /**
     * Constructs and returns a new template.
     *
     * @return A new template.
     * @throws Exception
     * @see org.springframework.beans.factory.FactoryBean#getObject()
     */
    public Object getObject() throws Exception {
        if (reuseTemplate) {
            return getSameTemplate();
        }
        return createNewTemplate();
    }

    /**
     * @see org.springframework.beans.factory.FactoryBean#getObjectType()
     */
    public Class getObjectType() {
        return (cachedTemplate != null)? cachedTemplate.getClass() : Template.class;
    }

    /**
     * By default this bean factory is a singleton factory. This can be changed by setting the 'reuseTemplate'
     * property to 'false'.
     *
     * @return whether the same template is always returns by the {@link #getObject()} method.
     */
    public boolean isSingleton() {
        return reuseTemplate;
    }

    /**
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        templateFactory = createTemplateFactory();
        if (templateFactory == null) {
            throw new BeanInitializationException("'templateFactory' property must be set");
        }

        if (templateSource == null && templateSources == null && templateSourceResolver == null) {
            throw new BeanInitializationException("Either 'templateSource', 'templateSources', or " +
                "'templateSourceResolver' property must be set");
        }

        if(templateSource == null && templateName == null) {
            throw new BeanInitializationException("When using 'templateSources' or 'templateSourceResolver', " +
                "property 'templateName' must be set");
        }

        if (reuseTemplate) {
            cachedTemplate = createNewTemplate();
        }
    }

    /**
     * Sets the template source of the required template.
     *
     * @param templateSource The template source of the required template.
     */
    public void setTemplateSource(TemplateSource templateSource) {
        this.templateSource = templateSource;
    }

    /**
     * Sets the resource that serves as the source of the required template.
     *
     * @see #setTemplateSource(org.springmodules.template.TemplateSource)
     * @param templateResource The resource that serves as the source of the required template.
     */
    public void setTemplateResource(Resource templateResource) {
        this.templateSource = new ResourceTemplateSource(templateResource);
    }

    /**
     * Sets the template sources needed to create the required template.
     * Note that for this to work the 'mainTemplateName' property must be set as well
     * by calling {@link #setTemplateName(String)}.<br/>
     * The 'templateSource' property is prioritized above this method. That is, when the
     * 'templateSource' property is set, it will serve as the source of the created template regardless
     * of whetehr 'templateSources' is set or not.
     *
     * @param templateSources The template source needed to create the required template.
     */
    public void setTemplateSources(TemplateSource[] templateSources) {
        this.templateSources = templateSources;
    }

    /**
     * Sets the resources that serve as the template sources for the required template.
     *
     * @see #setTemplateSources(org.springmodules.template.TemplateSource[])
     * @param templateResources The resources that serve as the template sources for the required template.
     */
    public void setTemplateResources(Resource[] templateResources) {
        TemplateSource[] templateSources = new TemplateSource[templateResources.length];
        for (int i=0; i<templateResources.length; i++) {
            templateSources[i] = new ResourceTemplateSource(templateResources[i]);
        }
        this.templateSources = templateSources;
    }

    /**
     * Sets the resources that serve as the template sources for the required template.
     *
     * @see #setTemplateSources(org.springmodules.template.TemplateSource[])
     * @param templateResourcesByName The resources that serve as the template sources associated with
     *        template logical names.
     */
    public void setTemplateResourcesMap(Map templateResourcesByName) {
        TemplateSource[] templateSources = new TemplateSource[templateResourcesByName.size()];
        int i=0;
        for (Iterator names = templateResourcesByName.keySet().iterator(); names.hasNext();) {
            String name = (String)names.next();
            Resource resource = (Resource)templateResourcesByName.get(name);
            templateSources[i] = new ResourceTemplateSource(name, resource);
        }
        this.templateSources = templateSources;
    }

    /**
     * Sets the template source resolver that will be used to resolve the template source based on the template naem.
     *
     * @param templateSourceResolver The template source resolver to be used.
     */
    public void setTemplateSourceResolver(TemplateSourceResolver templateSourceResolver) {
        this.templateSourceResolver = templateSourceResolver;
    }

    /**
     * Sets the name of the template source when using 'templateSources' or 'templateSourceResolver' to create the template.
     *
     * @see #setTemplateResources(org.springframework.core.io.Resource[])
     * @see #setTemplateSourceResolver(org.springmodules.template.TemplateSourceResolver)
     * @param templateName The name of the template source from which the template will be created.
     */
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    /**
     * Sets whether the same template should be reused whenever {@link #getObject()}  is caled (singlton model).
     *
     * @param reuseTemplate If <code>true</code>, the same template will always be returned by {@link #getObject()}.
     */
    public void setReuseTemplate(boolean reuseTemplate) {
        this.reuseTemplate = reuseTemplate;
    }

    /**
     * Creates the template factory that will be used by this factory bean to create templates.
     * This method is called after all the bean properties were set, so it is safe to use these properties
     * inside this method. Furthermore, this method can throw any exception which in trun will be thrown during
     * the bean factory initialization.
     *
     * @return The template factory that will be used by this factory bean to create templates.
     */
    protected abstract TemplateFactory createTemplateFactory() throws Exception;


    //============================================== Helper Methods ====================================================

    protected Template getSameTemplate() throws Exception {
        if (cachedTemplate == null) {
            cachedTemplate = createNewTemplate();
        }
        return cachedTemplate;
    }

    protected Template createNewTemplate() throws Exception {
        if (templateSource != null) {
            return templateFactory.createTemplate(templateSource);
        }
        TemplateSet set;
        if (templateSources != null) {
            set = templateFactory.createTemplateSet(templateSources);
        } else {
            set = templateFactory.createTemplateSet(templateSourceResolver);
        }
        Template template = set.getTemplate(templateName);
        if (template == null) {
            throw new TemplateException("Template '" + templateName + "' could not be found");
        }
        return template;
    }

}
