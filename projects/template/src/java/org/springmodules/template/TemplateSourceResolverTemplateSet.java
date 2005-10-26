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

package org.springmodules.template;

import java.util.*;

import org.apache.commons.logging.*;

/**
 * A TemplateSet implementation that uses a template source resolver and a template factory to create the tempaltes.
 *
 * @author Uri Boness
 */
public class TemplateSourceResolverTemplateSet implements TemplateSet {

    private final static Log log = LogFactory.getLog(TemplateSourceResolverTemplateSet.class);

    private TemplateSourceResolver templateSourceResolver;
    private TemplateFactory templateFactory;

    // caches the created templates.
    private Map templateByName;

    /**
     * JavaBean empty constructor.
     */
    public TemplateSourceResolverTemplateSet() {
        this(null, null);
    }

    /**
     * Constructs a new TemplateSourceResolverTemplateSet with the given template source resolver and template factory.
     *
     * @param templateSourceResolver The template source resolver that will be used to resolve the templates by names.
     * @param templateFactory The template factory that will be used to create the templates.
     */
    public TemplateSourceResolverTemplateSet(TemplateSourceResolver templateSourceResolver, TemplateFactory templateFactory) {
        templateByName = new HashMap();
        this.templateSourceResolver = templateSourceResolver;
        this.templateFactory = templateFactory;
    }

    /**
     * @see TemplateSet#getTemplate(String)
     */
    public Template getTemplate(String name) {
        Template template = (Template)templateByName.get(name);
        if (template == null) {
            template = createTemplate(name);
            templateByName.put(name, template);
        }
        return template;
    }

    /**
     * Creates a new template identified by the given name.
     *
     * @param name The name of the template to create. The internal template source resolver will resolve the given
     *        name to a source and from that source a new template will be created. Note that the template will be
     *        created only once and then cached for later retrieval.
     * @return The newly created template.
     */
    protected Template createTemplate(String name) {
        TemplateSource source = templateSourceResolver.resolveTemplateSource(name);
        if (source == null) {
            return null;
        }
        try {
            return templateFactory.createTemplate(source);
        } catch (TemplateCreationException tce) {
            if (log.isWarnEnabled()) {
                log.warn("Could not create Template '" + name + "' with source: " + source);
            }
            return null;
        }
    }

}
