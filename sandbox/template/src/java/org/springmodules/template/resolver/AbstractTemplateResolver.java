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
import org.springmodules.template.Template;
import org.springmodules.template.TemplateEngine;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.context.ResourceLoaderAware;

/**
 * @author Uri Boness
 */
public abstract class AbstractTemplateResolver implements TemplateResolver, ResourceLoaderAware {

    protected final static String DEFAULT_ENCODING = "UTF-8";

    private TemplateEngine engine;

    private ResourceLoader resourceLoader;

    protected AbstractTemplateResolver() {
        this(null);
    }

    protected AbstractTemplateResolver(TemplateEngine engine) {
        this(engine, new DefaultResourceLoader());
    }

    protected AbstractTemplateResolver(TemplateEngine engine, ResourceLoader resourceLoader) {
        this.engine = engine;
        this.resourceLoader = resourceLoader;
    }

    public final Template resolve(String name) {
        return resolve(name, DEFAULT_ENCODING);
    }

    public Template resolve(String name, String encoding) {
        Resource resource = loadTemplateResource(name, encoding);
        return engine.createTemplate(resource, encoding);
    }

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
     * @see ResourceLoaderAware#setResourceLoader(ResourceLoader)
     */
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    protected ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

}
