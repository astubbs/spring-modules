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

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import freemarker.cache.TemplateLoader;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springmodules.template.TemplateResolver;

/**
 * A {@link TemplateLoader} implementation that uses a {@link ResourceLoader resource loader} to load the
 * template resources.
 *
 * @author Uri Boness
 */
public class ResourceLoaderTemplateLoader implements TemplateLoader {

    private ResourceLoader resourceLoader;

    /**
     * Constructs a new ResourceTemplateLoader where {@link DefaultResourceLoader} is used as the resource loader.
     */
    public ResourceLoaderTemplateLoader() {
        this(new DefaultResourceLoader());
    }

    /**
     * Constructs a new ResourceTemplateLoader with a given resource loader to be used to load all template
     * resources.
     *
     * @param resourceLoader The resource loader to be used to load all template resources.
     */
    public ResourceLoaderTemplateLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * Returns the {@link Resource) loaded by the configured {@link TemplateResolver} based on the given resource name.
     *
     * @param name The name of the resource.
     * @return The loaded {@link Resource} based on the given resource name.
     * @throws IOException when the requested resource could not be loaded.
     * @see TemplateLoader#findTemplateSource(String)
     */
    public Object findTemplateSource(String name) throws IOException {
        return resourceLoader.getResource(name);
    }

    /**
     * If the given resource represents a physical file {@link java.io.File#lastModified()} is returned. Otherwise always
     * returns 0.
     *
     * @param object The given template {@link Resource}.
     * @return The last modified timestamp (in milli-seconds) of the given resource.
     * @see TemplateLoader#getLastModified(Object)
     */
    public long getLastModified(Object object) {
        Resource resource = (Resource)object;
        File file = getResourceFile(resource);
        return (file != null) ? file.lastModified() : 0;
    }

    public Reader getReader(Object templateSource, String encoding) throws IOException {
        Resource resource = (Resource)templateSource;
        return new InputStreamReader(resource.getInputStream(), encoding);
    }

    public void closeTemplateSource(Object templateSource) throws IOException {
        // do nothing
    }

    /**
     * Returns the internal resource loader that is used to load the freemarker templates.
     *
     * @return The internal resource loader that is used to load the freemarker templates.
     */
    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

    //============================================= Helper Methods =====================================================

    /**
     * Tries to resolve the physical file in the file system the given resource represents. If such file could not be
     * resolved or the given resource does not represent a physical file <code>null</code> is returned.
     *
     * @param resource The given resource.
     * @return The physical file represented by the given resource, <code>null</code> if no such file could be resolved.
     */
    protected File getResourceFile(Resource resource) {
        try {
            return resource.getFile();
        } catch (IOException ioe) {
            return null;
        }
    }

}
