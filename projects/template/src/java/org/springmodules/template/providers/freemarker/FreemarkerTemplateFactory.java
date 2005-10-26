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

package org.springmodules.template.providers.freemarker;

import java.io.*;
import java.util.*;

import freemarker.core.*;
import freemarker.template.*;
import freemarker.template.TemplateException;
import org.springmodules.template.*;
import org.springmodules.template.Template;

/**
 * A Freemarker implementation of the TemlateFactory interface.
 *
 * @author Uri Boness
 */
public class FreemarkerTemplateFactory extends AbstractTemplateFactory {

    private final static String FREEMARKER_TEMPLATE_NAME = "freemarkerTemplate";

    private Properties settings;

    public FreemarkerTemplateFactory() {
        settings = new Properties();
    }

    /**
     * @see TemplateFactory#createTemplate(org.springmodules.template.TemplateSource)
     */
    public Template createTemplate(TemplateSource source) throws TemplateCreationException {
        Configuration configuration = createConfiguration();
        return createTemplate(source, configuration);
    }

    /**
     * @see TemplateFactory#createTemplateSet(org.springmodules.template.TemplateSourceResolver)
     */
    public Template createTemplate(TemplateSourceResolver templateSourceResolver, String templateName)
        throws TemplateCreationException {

        TemplateSourceResolverTemplateLoader loader = new TemplateSourceResolverTemplateLoader(templateSourceResolver);
        Configuration configuration = createConfiguration();
        configuration.setTemplateLoader(loader);
        return createTemplate(configuration, templateName);
    }

    /**
     * @see TemplateFactory#createTemplateSet(org.springmodules.template.TemplateSource[])
     */
    public TemplateSet createTemplateSet(TemplateSource[] sources) throws TemplateCreationException {
        TemplateSourceResolverTemplateLoader loader = new TemplateSourceResolverTemplateLoader();
        loader.addTemplateSources(sources);
        Configuration configuration = createConfiguration();
        configuration.setTemplateLoader(loader);
        SimpleTemplateSet templates = new SimpleTemplateSet();
        for (int i=0; i<sources.length; i++) {
            templates.addTemplate(sources[i].getName(), createTemplate(sources[i], configuration));
        }
        return templates;
    }

    /**
     * @see TemplateFactory#createTemplateSet(org.springmodules.template.TemplateSourceResolver)
     */
    public TemplateSet createTemplateSet(TemplateSourceResolver templateSourceResolver) throws TemplateCreationException {
        TemplateSourceResolverTemplateLoader loader = new TemplateSourceResolverTemplateLoader(templateSourceResolver);
        Configuration configuration = createConfiguration();
        configuration.setTemplateLoader(loader);
        return new FreemarkerTemplateSet(configuration);
    }

    /**
     * Defining specific Freemarker setting that will be used to generate
     * the Freemarker configurations.
     *
     * @param settings The freemarker settings.
     */
    public void setSettings(Properties settings) {
        this.settings = settings;
    }

    //========================================== Helper Methods ========================================================

    /**
     * creates a FreemarkerTemplate out of the given template source and freemarker configuration.
     *
     * @param source The source of the free marker template.
     * @param configuration The freemarker configuration.
     * @return The newly created FreemarkerTemplate.
     * @throws TemplateCreationException
     */
    protected Template createTemplate(TemplateSource source, Configuration configuration) throws TemplateCreationException {
        try {
            String name = (source.getName() != null)? source.getName() : FREEMARKER_TEMPLATE_NAME;
            return new FreemarkerTemplate(new freemarker.template.Template(name, source.getReader(), configuration));
        } catch (IOException ioe) {
            String message = "Could not create Freemarker template";
            log.error(message, ioe);
            throw new TemplateCreationException(message, ioe);
        }
    }

    /**
     * creates a FreemarkerTemplate based on the template that is retrieved from the freemarker configuration
     * by the given name.
     *
     * @param configuration The configuration from which the freemarker template will be retrieved.
     * @param name The name of the freemarker template to retrive from the configuration.
     * @return The newly created FreemarkerTemplate.
     * @throws TemplateCreationException
     */
    protected Template createTemplate(Configuration configuration, String name) throws TemplateCreationException {
        try {
            return new FreemarkerTemplate(configuration.getTemplate(name));
        } catch (FileNotFoundException fnfe) {
            throw new TemplateCreationException("Template named '" + name + "' could not be found", fnfe);
        } catch (ParseException pe) {
            throw new TemplateCreationException("Could not parse freemarker template. Bad syntax", pe);
        } catch (IOException ioe) {
            throw new TemplateCreationException("Could not read template source", ioe);
        }
    }

    /**
     * Creates a new freemarker configuration.
     *
     * @return The newly created freemarker configuration.
     * @throws TemplateConfigurationException
     */
    protected Configuration createConfiguration() throws TemplateConfigurationException {
        Configuration configuration = new Configuration();

        try {
            configuration.setSettings(settings);
        } catch (TemplateException te) {
            String message = "Could not configure Freemarker template configuration with settings";
            log.error(message, te);
            throw new TemplateConfigurationException(message, te);
        }

        return configuration;
    }

}
