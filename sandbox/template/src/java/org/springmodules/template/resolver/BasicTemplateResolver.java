/*
 * Copyright 2002-2007 the original author or authors.
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

package org.springmodules.template.resolver;

import org.springmodules.template.TemplateResolver;
import org.springmodules.template.TemplateEngine;
import org.springmodules.template.Template;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

/**
 * A basic implementation of {@link org.springmodules.template.TemplateResolver} which uses a {@link ResourceLoader} to
 * load resource by the template name. The template resources are then passed to a configured {@link TemplateEngine}
 * which in turn creates the appropriate template. Although this class can be used as is, it would be wise to check
 * out its sub-classes, specially those derived from {@link CachingTemplateResolver}.
 *
 * @author Uri Boness
 */
public class BasicTemplateResolver implements TemplateResolver, ResourceLoaderAware {

    /**
     * The default encoding that will be used (UTF-8)
     */
    protected final static String DEFAULT_ENCODING = "UTF-8";

    private TemplateEngine engine;

    private ResourceLoader resourceLoader;

    /**
     * Constructs a new BasicTemplateResolver with no template engine and the {@link DefaultResourceLoader} as the
     * resource loader.
     */
    protected BasicTemplateResolver() {
        this(null);
    }

    /**
     * Constructs a new BasicTemplateResolver with a given template engine and the {@link DefaultResourceLoader} as the
     * resource loader.
     *
     * @param engine The template engine that will be used to generate the appropriate templates.
     */
    protected BasicTemplateResolver(TemplateEngine engine) {
        this(engine, new DefaultResourceLoader());
    }

    /**
     * Constructs a new BasicTemplateResolver with given template engine and resource loader.
     *
     * @param engine The template engine that will be used to generate the appropriate template.
     * @param resourceLoader The resource loader that will load the template resources by their names.
     */
    protected BasicTemplateResolver(TemplateEngine engine, ResourceLoader resourceLoader) {
        this.engine = engine;
        this.resourceLoader = resourceLoader;
    }

    /**
     * Resolves the appropriate template based on the given template name. This method uses UTF-8 as the default
     * encoding.
     *
     * @param name The name of the template to be resolved.
     * @return The resolved template.
     */
    public final Template resolve(String name) {
        return resolve(name, DEFAULT_ENCODING);
    }

    /**
     * Resolves the template based on the given template name and encoding.
     *
     * @param name The name of the template to be resolved.
     * @param encoding The encoding of the resolved template.
     * @return The resolved template.
     */
    public Template resolve(String name, String encoding) {
        Resource resource = loadTemplateResource(name, encoding);
        return engine.createTemplate(resource, encoding);
    }

    /**
     * Loads the template resource based on the given name and encoding.
     *
     * @param name The name of the template.
     * @param encoding The encoding of the template.
     * @return The loaded template resource.
     */
    protected Resource loadTemplateResource(String name, String encoding) {
        return resourceLoader.getResource(name);
    }


    //============================================== Setter/Getter =====================================================

    /**
     * Sets the template engine to be used to load and process the resolved template.
     *
     * @param engine The template engine to be used to load and process the resolved template.
     */
    public void setEngine(TemplateEngine engine) {
        this.engine = engine;
    }

    /**
     * Returns the template engine that is used to load and process the resolved tempalatel
     *
     * @return The template engine that is used to load and process the resolved tempalatel
     */
    public TemplateEngine getEngine() {
        return engine;
    }

    /**
     * Sets the resource loader that will be used to load the template resources.
     *
     * @param resourceLoader The resource loader that will be used to load the template resources.
     * @see org.springframework.context.ResourceLoaderAware#setResourceLoader(org.springframework.core.io.ResourceLoader)
     */
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * Returns the resource loader used to load the template resources.
     *
     * @return The resource loader used to load the template resources.
     */
    protected ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

}
