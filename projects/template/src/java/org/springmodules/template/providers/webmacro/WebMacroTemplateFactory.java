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

package org.springmodules.template.providers.webmacro;

import java.util.*;
import java.io.*;

import org.springmodules.template.*;
import org.springmodules.template.Template;
import org.springmodules.template.resolvers.*;
import org.webmacro.*;
import org.webmacro.engine.*;
import org.apache.commons.logging.*;
import org.apache.commons.logging.Log;

/**
 * A WebMacro implementation of the TemlateFactory interface.
 *
 * @author Uri Boness
 */
public class WebMacroTemplateFactory implements TemplateFactory {

    private final static Log logger = LogFactory.getLog(WebMacroTemplateFactory.class);

    private Properties properties;

    /**
     * Default constructor (Javabean support).
     */
    public WebMacroTemplateFactory() {
    }

    /**
     * Creates a {@link Template} out of the given template source. The return template is implemented using a
     * WebMacro template.
     *
     * @see TemplateFactory#createTemplate(org.springmodules.template.TemplateSource)
     */
    public Template createTemplate(TemplateSource source) throws TemplateCreationException {
        WebMacro wm = createWebMacro(null);

        try {

            StreamTemplate webMacroTemplate = new StreamTemplate(wm.getBroker(), source.getInputStream());
            webMacroTemplate.parse();
            return new WebMacroTemplate(webMacroTemplate, new DefaultContextFactory(wm));

        } catch (IOException ioe) {
            logger.error("Could not read from template source '" + source + "'", ioe);
            throw new TemplateCreationException("Could not read from template source '" + source + "'", ioe);
        } catch (org.webmacro.TemplateException te) {
            logger.error("Could not parse template from template source '" + source + "'", te);
            throw new TemplateCreationException("Could not parse template from template source '" + source + "'", te);
        }
    }

    /**
     * Creates a {@link Template} out of the given template source resolver and the template name.
     * The returned template is implemented using a WebMacro template.
     *
     * @see TemplateFactory#createTemplate(org.springmodules.template.TemplateSourceResolver, String)
     */
    public Template createTemplate(TemplateSourceResolver templateSourceResolver, String templateName)
        throws TemplateCreationException {

        WebMacro wm = createWebMacro(new TemplateSourceResolverTemplateProvider(templateSourceResolver));

        try {

            org.webmacro.Template webMacroTemplate = wm.getTemplate(templateName);
            return new WebMacroTemplate(webMacroTemplate, new DefaultContextFactory(wm));

        } catch (ResourceException re) {
            logger.error("Could not fetch WebMacro template '" + templateName + "'", re);
            throw new TemplateCreationException("Could not fetch WebMacro template '" + templateName + "'", re);
        }
    }

    /**
     * Creates a template set out of the given template sources. The templates returned from the template set will be
     * implemented using a WebMacro template.
     *
     * @see TemplateFactory#createTemplateSet(org.springmodules.template.TemplateSource[])
     */
    public TemplateSet createTemplateSet(TemplateSource[] sources) throws TemplateCreationException {
        TemplateSourceResolver resolver = createTemplateSourceResolverFromTemplateSources(sources);
        return createTemplateSet(resolver);
    }

    /**
     * Creates a template set with the given template source resolver. The templates returned from the template set will be
     * implemented using a WebMacro template.
     *
     * @see TemplateFactory#createTemplateSet(org.springmodules.template.TemplateSourceResolver)
     */
    public TemplateSet createTemplateSet(TemplateSourceResolver templateSourceResolver) throws TemplateCreationException {
        WebMacro wm = createWebMacro(new TemplateSourceResolverTemplateProvider(templateSourceResolver));
        return new WebMacroTemplateSet(wm);
    }

    //================================================ Setter/Getter ===================================================

    /**
     * Sets the properties of WebMacro. Calling this method is an alternative configuration mechanism for the webmacro
     * engine. If the properties are not set here, WebMacro will try to load them from a properties file in the classpath.
     *
     * @param properties The properties configuration of WebMacro.
     */
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    //=============================================== Helper Methods ===================================================

    // creates a new web macro engine.
    protected WebMacro createWebMacro(TemplateSourceResolverTemplateProvider templateProvider) throws TemplateCreationException {
        try {

            Broker broker = (properties != null) ? Broker.getBroker(properties) : Broker.getBroker();
            if (templateProvider != null) {
                broker.addProvider(templateProvider, null);
            }

            return new WM(broker);

        } catch (InitException ie) {
            logger.error("Could not initialize WebMacro", ie);
            throw new TemplateCreationException("Could not initialize WebMacro", ie);
        }
    }

    // creates a template source resolver from a given template sources array.
    protected TemplateSourceResolver createTemplateSourceResolverFromTemplateSources(TemplateSource[] sources) {
        MapTemplateSourceResolver resolver = new MapTemplateSourceResolver(new HashMap(sources.length));
        for (int i=0; i<sources.length; i++) {
            resolver.addTemplateSource(sources[i].getName(), sources[i]);
        }
        return resolver;
    }

}
