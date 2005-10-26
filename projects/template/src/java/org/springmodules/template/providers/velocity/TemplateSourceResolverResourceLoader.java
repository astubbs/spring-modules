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

package org.springmodules.template.providers.velocity;

import java.io.*;
import java.util.*;

import org.apache.commons.collections.*;
import org.apache.commons.logging.*;
import org.apache.velocity.exception.*;
import org.apache.velocity.runtime.resource.*;
import org.apache.velocity.runtime.resource.loader.*;
import org.springmodules.template.*;
import org.springmodules.template.resolvers.*;

/**
 * A velocity ResourceLoader implementation that uses a template source resolver to resolve template source which
 * will be used as the temlate resources for the velocity engine.
 *
 * @author Uri Boness
 */
public class TemplateSourceResolverResourceLoader extends ResourceLoader {

    private final static Log log = LogFactory.getLog(TemplateSourceResolverResourceLoader.class);

    // the template source resolver to use.
    private TemplateSourceResolver templateSourceResolver;

    // caching the template sources.
    private Map sourceByName;

    /**
     * Craetes a new TemplateSourceResolverResourceLoader which uses an "empty" (null) temlate source resolver.
     *
     * @see org.springmodules.template.resolvers.NullTemplateSourceResolver
     */
    public TemplateSourceResolverResourceLoader() {
        this(NullTemplateSourceResolver.INSTANCE);
    }

    /**
     * Creates a new TemplateSourceResolverResourceLoader with a given template source resolver.
     *
     * @param templateSourceResolver The given template source resolver.
     */
    public TemplateSourceResolverResourceLoader(TemplateSourceResolver templateSourceResolver) {
        sourceByName = new HashMap();
        this.templateSourceResolver = templateSourceResolver;
    }

    /**
     * Sets the template source resolver to be used by this resource loader.
     *
     * @param templateSourceResolver The template source resolver to be used by this resource loader.
     */
    public void setTemplateSourceResolver(TemplateSourceResolver templateSourceResolver) {
        this.templateSourceResolver = templateSourceResolver;
    }

    /**
     * @see ResourceLoader#init(org.apache.commons.collections.ExtendedProperties)
     */
    public void init(ExtendedProperties configuration) {
        // do nothing...
    }

    /**
     * Resolves the template source based on the given (template) name. and returns an input stream for
     * that source.
     *
     * @see ResourceLoader#getResourceStream(String) Thrown when no template source was found for the given name.
     */
    public InputStream getResourceStream(String source) throws ResourceNotFoundException {
        TemplateSource templateSource = getCachedTemplateSource(source);
        if (templateSource != null) {
            return getTemplateSourceInputStream(templateSource, source);
        }
        templateSource = templateSourceResolver.resolveTemplateSource(source);
        if (templateSource == null) {
            log.error("Could not resolve template source with name '" + source + "'");
        }
        cacheTemplateSource(source, templateSource);
        return getTemplateSourceInputStream(templateSource, source);
    }

    /**
     * It's impossible to know whether a template sourc was modified or not using using TemplateSourceResolver.
     * Therefore, this method always returns false.
     *
     * @see ResourceLoader#isSourceModified(org.apache.velocity.runtime.resource.Resource)
     */
    public boolean isSourceModified(Resource resource) {
        return false;
    }

    /**
     * @see ResourceLoader#getLastModified(org.apache.velocity.runtime.resource.Resource)
     */
    public long getLastModified(Resource resource) {
        return 0;
    }


    //============================================== Helper Methods ====================================================

    /**
     * Returns the input stream for the given template source.
     *
     * @param templateSource The given template source.
     * @param name The name of the template.
     * @return The input stream for the given template source.
     */
    protected InputStream getTemplateSourceInputStream(TemplateSource templateSource, String name) {
        try {
            return templateSource.getInputStream();
        } catch (IOException ioe) {
            log.error("Could not load velocity template from template source '" + name + "'");
            return null;
        }
    }

    /**
     * Returns the cached template source by the given name (key).
     *
     * @param name The name (key) of the template source.
     * @return The cached template source by the given name (key).
     */
    protected TemplateSource getCachedTemplateSource(String name) {
        return (TemplateSource)sourceByName.get(name);
    }

    /**
     * Caches the given template source by the given name (key).
     *
     * @param name The name by which the template source will be cached.
     * @param templateSource The template source to cache.
     */
    protected void cacheTemplateSource(String name, TemplateSource templateSource) {
        sourceByName.put(name, templateSource);
    }

}
