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

package org.springmodules.template.engine.freemarker;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;
import org.springmodules.template.Template;
import org.springmodules.template.TemplateCreationException;
import org.springmodules.template.engine.AbstractTemplateEngine;

/**
 * Represents the freemarker template engine.
 *
 * @author Uri Boness
 */
public class FreemarkerTemplateEngine extends AbstractTemplateEngine implements InitializingBean {

    private Properties settings;

    private Map sharedVariables;

    private ObjectWrapper objectWrapper;

    private Configuration configuration;

    private ResourceLoader resourceLoader;

    /**
     * Constructs a new FreemarkerTemplateEngine. The engine uses the default settings of freemarker engine and
     * {@link DefaultResourceLoader} as the resource loader that is used to load all template resources.
     */
    public FreemarkerTemplateEngine() {
        this(new DefaultResourceLoader());
    }

    /**
     * Constructs a new FreemarkerTemplateEngine with a given resource loader. The engine uses the default settings of
     * freemarker engine.
     *
     * @param resourceLoader The resource loader this engine will use to load all template resources.
     */
    public FreemarkerTemplateEngine(ResourceLoader resourceLoader) {
        this(resourceLoader, null, new HashMap());
    }

    /**
     * Constructs a new FreemarkerTemplateEngine with a given resource loader, settings, and shared variables.
     *
     * @param resourceLoader The resource loader this engine will use to load all template resources.
     * @param settings freemarker settings to configure the freemarker engine.
     * @param sharedVariables Variables that are shared between all template created/managed by this engine.
     */
    public FreemarkerTemplateEngine(ResourceLoader resourceLoader, Properties settings, Map sharedVariables) {
        this.resourceLoader = resourceLoader;
        this.sharedVariables = sharedVariables;
        this.settings = settings;
    }

    /**
     * @see org.springmodules.template.TemplateEngine#createTemplate(org.springframework.core.io.Resource, String)
     */
    public Template createTemplate(Resource resource, String encoding) {
        try {
            Reader reader = new InputStreamReader(resource.getInputStream());
            String name = resource.getFilename();
            return new FreemarkerTemplate(createFreemarkerTemplate(name, reader, configuration));
        } catch (IOException ioe) {
            throw new TemplateCreationException(
                "Could not create freemarker template from resource '" + resource.getDescription() + "'", ioe);
        }
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(resourceLoader, "Required properties 'resourceLoader' is not set");
        configuration = createConfiguration(resourceLoader, settings, sharedVariables, objectWrapper);
    }

    //================================================== Setter/Getter =================================================

    /**
     * Returns the extra settings set for this freemarker engine.
     *
     * @return The extra settings set for this freemarker engine.
     */
    public Properties getSettings() {
        return settings;
    }

    /**
     * Sets extra settings for this freemarker engine.
     *
     * @param settings The extra settings for this freemarker engine.
     * @see Configuration#setSettings(java.util.Properties)
     */
    public void setSettings(Properties settings) {
        this.settings = settings;
    }

    /**
     * Sets the {@link ResourceLoader resource loader} that this engine will use to load the templates.
     *
     * @param resourceLoader The resource loader that will be used by this engine to load the templates.
     */
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * Sets shared variables for the freemarker engine. Shared variable are managed by freemarker and are shared
     * among all templates.
     *
     * @param sharedVariables The variables to be shared by all templates.
     */
    public void setSharedVariables(Map sharedVariables) {
        this.sharedVariables = sharedVariables;
    }

    /**
     * Sets the object wrapper the freemarker engine will use to access the objects in the model.
     *
     * @param objectWrapper The object wrapper the freemarker engine will use to access the objects in the model.
     */
    public void setObjectWrapper(ObjectWrapper objectWrapper) {
        this.objectWrapper = objectWrapper;
    }


    //================================================== Helper Methods ================================================

    protected static freemarker.template.Template createFreemarkerTemplate(
            String name,
            Reader reader,
            Configuration configuration) throws IOException {

        return new freemarker.template.Template(name, reader, configuration);
    }

    /**
     * Creates a freemarker configuration with the given resource loader, freemarker settings, and shared variables.
     *
     * @param resourceLoader The resource loader that will be used to load all template resources.
     * @param settings The freemarker settings. Can be <code>null</code>
     * @param sharedVariables Variables shared by all templates created/managed by this engine.
     * @return The created configuration.
     * @throws TemplateException thrown when configuration construction fails.
     */
    protected static Configuration createConfiguration(
            ResourceLoader resourceLoader,
            Properties settings,
            Map sharedVariables,
            ObjectWrapper objectWrapper) throws TemplateException {

        ResourceLoaderTemplateLoader templateLoader = new ResourceLoaderTemplateLoader(resourceLoader);
        Configuration configuration = new Configuration();
        configuration.setTemplateLoader(templateLoader);

        for (Iterator vars = sharedVariables.entrySet().iterator(); vars.hasNext();) {
            Map.Entry var = (Map.Entry)vars.next();
            configuration.setSharedVariable((String)var.getKey(), var.getValue());
        }

        if (settings != null) {
            configuration.setSettings(settings);
        }

        if (objectWrapper != null) {
            configuration.setObjectWrapper(objectWrapper);
        }

        return configuration;
    }
}
