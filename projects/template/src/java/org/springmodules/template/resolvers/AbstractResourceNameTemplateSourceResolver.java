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

package org.springmodules.template.resolvers;

import org.springframework.core.io.*;
import org.springmodules.template.*;
import org.springmodules.template.sources.*;

/**
 * An abstract {@link TemplateSourceResolver} implementation that uses {@link Resource}s as
 * template sources which are resolved by their names. If the <i>prefix</i> and a <i>suffix</i> properties are set
 * and assuming the name to be resolved is <i>_name_</i>, then this resolver will look for a resource
 * named <i>prefix_name_suffix</i>.
 *
 * @author Uri Boness
 */
public abstract class AbstractResourceNameTemplateSourceResolver implements TemplateSourceResolver {

    private final static String EMPTY_STRING = "";

    // the prefix & suffix to be appended to the resolved names.
    private String prefix;
    private String suffix;

    /**
     * Constructs a new AbstractResourceNameTemplateSourceResolver with no prefix and suffix.
     */
    public AbstractResourceNameTemplateSourceResolver() {
        this(EMPTY_STRING, EMPTY_STRING);
    }

    /**
     * Constructs a new AbstractResourceNameTemplateSourceResolver with given prefix and suffix to be
     * prepended and appended to the resolved name respectively.
     *
     * @param prefix The prefix to prepend to the resolved name.
     * @param suffix The suffix to append to the resolved name.
     */
    public AbstractResourceNameTemplateSourceResolver(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    /**
     * @see TemplateSourceResolver#resolveTemplateSource(String)
     */
    public final TemplateSource resolveTemplateSource(String name) {
        Resource resource = getResource(prefix + name + suffix);
        return new ResourceTemplateSource(resource);
    }

    /**
     * Returns the prefix that will be prepended to the source name.
     *
     * @return The prefix that will be prepended to the source name.
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Sets the prefix that will be prepended to the source name.
     *
     * @param prefix The prefix that will be prepended to the source name.
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Returns the suffix that will be appended to the source name.
     *
     * @return The suffix that will be appended to the source name.
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * Sets the suffix that will be appended to the source name.
     *
     * @param suffix The suffix that will be appended to the source name.
     */
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    /**
     * Should be implemented by sub-classes.
     */
    protected abstract Resource getResource(String name);

}
