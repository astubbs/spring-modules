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

/**
 * A velocity ResourceLoader implementation that serves and uses itself as a template source registry
 * to find template sources by a names.
 *
 * @author Uri Boness
 */
public class TemplateSourceResourceLoader extends ResourceLoader {

    private final static Log log = LogFactory.getLog(TemplateSourceResourceLoader.class);

    private final static String DEFAULT_TEMPLATE_NAME = "_template_name_";

    // holds template source entries by template names.
    private Map entryByName;

    /**
     * Creates a new TemplateSourceResourceLoader.
     */
    public TemplateSourceResourceLoader() {
        entryByName = new HashMap();
    }

    /**
     * Adds the given template source to this loader (registry). The name of the template is the name
     * of the given source.
     *
     * @param source The template source to add to this loader.
     */
    public void addTemplateSource(TemplateSource source) {
        String name = (source.getName() != null)? source.getName() : DEFAULT_TEMPLATE_NAME;
        addTemplateSource(name, source);
    }

    /**
     * Adds the given template source to this loader (registry) and associates it with the given template name.
     *
     * @param name The associated template name.
     * @param source The template source to add to this loader.
     */
    public void addTemplateSource(String name, TemplateSource source) {
        TemplateSourceEntry entry = new TemplateSourceEntry();
        entry.templateSource = source;
        entry.lastModified = System.currentTimeMillis();
        entryByName.put(name, entry);
    }

    /**
     * A convenience method to add the given list of temlate source to this loader.
     *
     * @param sources The given list of template sources.
     */
    public void addTemplateSources(TemplateSource[] sources) {
        for (int i=0; i<sources.length; i++) {
            addTemplateSource(sources[i]);
        }
    }

    /**
     * @see ResourceLoader#init(org.apache.commons.collections.ExtendedProperties)
     */
    public void init(ExtendedProperties configuration) {
        // do nothing...
    }

    /**
     * @see ResourceLoader#getResourceStream(String)
     */
    public InputStream getResourceStream(String source) throws ResourceNotFoundException {
        TemplateSourceEntry entry = (TemplateSourceEntry)entryByName.get(source);
        if (entry == null) {
            throw new ResourceNotFoundException("Could not find template source with name \"" + source + "\"");
        }
        try {
            return entry.templateSource.getInputStream();
        } catch (IOException ioe) {
            log.error("Could not load template source", ioe);
            return null;
        }
    }

    /**
     * @see ResourceLoader#isSourceModified(org.apache.velocity.runtime.resource.Resource)
     */
    public boolean isSourceModified(Resource resource) {
        TemplateSourceEntry entry = (TemplateSourceEntry)entryByName.get(resource.getName());
        if (entry == null) {
            return true;
        }
        return entry.lastModified > resource.getLastModified();
    }

    /**
     * @see ResourceLoader#getLastModified(org.apache.velocity.runtime.resource.Resource)
     */
    public long getLastModified(Resource resource) {
        TemplateSourceEntry entry = (TemplateSourceEntry)entryByName.get(resource.getName());
        return entry.lastModified;
    }


    //============================================== Inner Classes ====================================================

    // a template source entry holds a template souce and the time it was last modified.
    private static class TemplateSourceEntry {
        public long lastModified;
        public TemplateSource templateSource;
    }

}
