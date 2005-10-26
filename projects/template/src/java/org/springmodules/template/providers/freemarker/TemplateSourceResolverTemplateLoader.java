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

import freemarker.cache.*;
import org.springmodules.template.*;
import org.springmodules.template.resolvers.*;

/**
 * A freemarker {@link TemplateLoader} imlementation that uses a TemplateSourceResolver to load templates.
 *
 * @author Uri Boness
 */
public class TemplateSourceResolverTemplateLoader implements TemplateLoader {

    // a cache to hold the loaded templates
    private Map entriesByName;

    // the template source resolver to be used.
    private TemplateSourceResolver templateSourceResolver;

    /**
     * Creates a new empty TemplateSourceResolverTemplateLoader which will not load any template by default.
     */
    public TemplateSourceResolverTemplateLoader() {
        this(NullTemplateSourceResolver.INSTANCE);
    }

    /**
     * Creates a new TemplateSourceResolverTemplateLoader with a given template source resolver to be used when
     * loading templates by name.
     *
     * @param templateSourceResolver The template source resolver to be used.
     */
    public TemplateSourceResolverTemplateLoader(TemplateSourceResolver templateSourceResolver) {
        entriesByName = new HashMap();
        this.templateSourceResolver = templateSourceResolver;
    }

    /**
     * This method can be used to manually add a template source to this loader (bypassing the template source resolver).
     * The given template source will be associate with its name.
     *
     * @param source The template source to be added.
     * @see org.springmodules.template.TemplateSource#getName()
     */
    public void addTemplateSource(TemplateSource source) {
        addTemplateSource(source.getName(), source);
    }

    /**
     * This method can be used to manually add a template source to this loader (bypassing the template source resolver).
     *
     * @param name The name to be associated with the given template source.
     * @param source The template source to be added.
     */
    public void addTemplateSource(String name, TemplateSource source) {
        storeTemplateSource(name, source);
    }

    /**
     * This method can be used to manually add a list of template sources.
     *
     * @param sources The list of template sources to be added.
     * @see #addTemplateSource(org.springmodules.template.TemplateSource)
     */
    public void addTemplateSources(TemplateSource[] sources) {
        for (int i=0; i<sources.length; i++) {
            addTemplateSource(sources[i]);
        }
    }

    /**
     * This method can be used to manually add a list of template sources.
     *
     * @param names The names to be associated with the given template sources.
     * @param sources The list of template sources to be added.
     * @see #addTemplateSource(String, org.springmodules.template.TemplateSource)
     */
    public void addTemplateSources(String[] names, TemplateSource[] sources) {
        if (names.length != sources.length) {
            throw new IllegalArgumentException("Given template source names count do not match the given sources count.");
        }
        for (int i=0; i<sources.length; i++) {
            addTemplateSource(names[i], sources[i]);
        }
    }

    /**
     * Sets the template source resolver to be used by this loader.
     *
     * @param templateSourceResolver The template source resolver to be used by this loader.
     */
    public void setTemplateSourceResolver(TemplateSourceResolver templateSourceResolver) {
        this.templateSourceResolver = templateSourceResolver;
    }

    /**
     * @see TemplateLoader#findTemplateSource(String)
     */
    public Object findTemplateSource(String name) throws IOException {
        TemplateSource source = fetchTemplateSource(name);
        if (source != null) {
            return source;
        }
        source = templateSourceResolver.resolveTemplateSource(name);
        if (source != null) {
            storeTemplateSource(name, source);
        }
        return source;
    }

    /**
     * @see TemplateLoader#getLastModified(Object)
     */
    public long getLastModified(Object templateSource) {
        return 0; //todo implement using entries
    }

    /**
     * @see TemplateLoader#getReader(Object, String)
     */
    public Reader getReader(Object templateSource, String encoding) throws IOException {
        return ((TemplateSource)templateSource).getReader();
    }

    /**
     * @see TemplateLoader#closeTemplateSource(Object)
     */
    public void closeTemplateSource(Object templateSource) throws IOException {
        // do nothing
    }


    //============================================== Helper Methods ====================================================

    /**
     * Stores the given template source in the internal cache, using the given name as the key.
     *
     * @param name The key for the stored template source.
     * @param source The template source to be stored in the internal cache.
     */
    protected void storeTemplateSource(String name, TemplateSource source) {
        TemplateSourceEntry entry = new TemplateSourceEntry();
        entry.templateSource = source;
        entry.timestamp = System.currentTimeMillis();
        entriesByName.put(name, entry);
    }

    /**
     * Retrieves a template source from the internal cache using the given key.
     *
     * @param name The name of the template source used as the key to retrieve it from the internal cache.
     * @return The cached template source, or <code>null</code> if non could be found.
     */
    protected TemplateSource fetchTemplateSource(String name) {
        TemplateSourceEntry entry = (TemplateSourceEntry)entriesByName.get(name);
        if (entry == null) {
            return null;
        }
        return entry.templateSource;
    }

    // represents a template source entry in the internal cache.
    private class TemplateSourceEntry {
        public TemplateSource templateSource;
        public long timestamp;
    }

}
