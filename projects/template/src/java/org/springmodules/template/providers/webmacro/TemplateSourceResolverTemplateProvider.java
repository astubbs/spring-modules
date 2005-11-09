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

import java.io.*;
import java.util.*;

import org.springmodules.template.*;
import org.springmodules.template.resolvers.*;
import org.webmacro.*;
import org.webmacro.Template;
import org.webmacro.TemplateException;
import org.webmacro.engine.*;
import org.webmacro.util.*;

/**
 * A WebMacro template {@link Provider} implementation that uses a {@link TemplateSourceResolver} to fetch template
 * Sources from which WebMacro templates will be created.
 *
 * @author Uri Boness
 */
public class TemplateSourceResolverTemplateProvider implements Provider {

    private final static String PROVIDER_TYPE = "template";

    private Broker broker;

    private TemplateSourceResolver templateSourceResolver;

    // caching
    private Map templateCache;

    /**
     * Default constructor (javabean support). Construct a new template provider with a {@link NullTemplateSourceResolver}.
     */
    public TemplateSourceResolverTemplateProvider() {
        this(NullTemplateSourceResolver.INSTANCE);
    }

    /**
     * Constructs a new template provider with a given template source resolver.
     *
     * @param templateSourceResolver The template source resolver that will be used to fetch template sources.
     */
    public TemplateSourceResolverTemplateProvider(TemplateSourceResolver templateSourceResolver) {
        this.templateSourceResolver = templateSourceResolver;
        templateCache = new HashMap();
    }

    /**
     * @see Provider#init(org.webmacro.Broker, org.webmacro.util.Settings)
     */
    public void init(Broker broker, Settings config) throws InitException {
        this.broker = broker;
    }

    /**
     * Returns "template" as the provider type. The "template" type indicates in webmacro engine that all templates
     * should be provided by this provider.
     *
     * @return "template" as the provider type.
     * @see org.webmacro.Provider#getType()
     */
    public String getType() {
        return PROVIDER_TYPE;
    }

    /**
     * Clears the templates cache.
     *
     * @see org.webmacro.Provider#flush()
     */
    public void flush() {
        templateCache.clear();
    }

    /**
     * @see org.webmacro.Provider#destroy()
     */
    public void destroy() {
        templateCache.clear();
    }

    /**
     * Returns the webmacro {@link Template} that is associated with the given name. This is a cached implemenation,
     * meaning that after the first time the template is requested, it will be fetched and cached for later requests.
     *
     * @param name The name of the template.
     * @return The webmacro {@link Template} that is associated with the given name.
     * @throws ResourceException Thrown if for some reason the template could not be loaded/found.
     * @see Provider#get(String)
     */
    public final Object get(String name) throws ResourceException {
        Template template = getCachedTemplate(name);
        if (template != null) {
            return template;
        }
        template = internalLoad(name);
        cacheTemplate(name, template);
        return template;
    }

    /**
     * Sets the template source resolver that will be used by this provider to load the templates.
     *
     * @param templateSourceResolver The template source resolver that will be used by this provider.
     */
    public void setTemplateSourceResolver(TemplateSourceResolver templateSourceResolver) {
        this.templateSourceResolver = templateSourceResolver;
    }

    /**
     * The actual template loading (without caching).
     *
     * @param name The name of the template.
     * @return The newly loaded webmacro template.
     * @throws ResourceException Thrown if of any reason the template could not be loaded.
     */
    protected Template internalLoad(String name) throws ResourceException {
        TemplateSource templateSource = templateSourceResolver.resolveTemplateSource(name);
        InputStream in = getInputStreamFromSource(templateSource, name);
        Template template = createTemplate(in, name);
        parseTemplate(template, name);
        return template;
    }

    /**
     * Returns the input stream of the given template source.
     *
     * @param source The given template source.
     * @param name The name of the template.
     * @return The InputStream of the given template source.
     * @throws ResourceException Thrown when the input stream could not be created for the given template source.
     */
    protected InputStream getInputStreamFromSource(TemplateSource source, String name) throws ResourceException {
        try {

            return source.getInputStream();

        } catch (IOException ioe) {
            throw new ResourceException("Could not get input stream out of template source '" + name + "'", ioe);
        }
    }

    /**
     * Parses the template to make it ready for use.
     *
     * @param template The template to parse.
     * @param name The name of the template.
     * @throws ResourceException Thrown if the template could not be parsed.
     */
    protected void parseTemplate(Template template, String name) throws ResourceException {
        try {

            template.parse();

        } catch (IOException ioe) {
            throw new ResourceException("Could not parse template from template source '" + name + "'", ioe);
        } catch (TemplateException te) {
            throw new ResourceException("Could not parse template from template source '" + name + "'", te);
        }
    }

    /**
     * Creates a {@link StreamTemplate} out of the given InputStream.
     *
     * @param in The given InputStream.
     * @param name The name of the template.
     * @return The newly created StreamTemplate.
     * @throws ResourceException Thrown if the template could not be created.
     */
    protected Template createTemplate(InputStream in, String name) throws ResourceException {
        try {

            return new StreamTemplate(broker, in);

        } catch (IOException ioe) {
            throw new ResourceException("Could create template from template source '" + name + "'", ioe);
        }
    }

    protected Template getCachedTemplate(String name) {
        return (Template)templateCache.get(name);
    }

    protected void cacheTemplate(String name, Template template) {
        templateCache.put(name, template);
    }

}
